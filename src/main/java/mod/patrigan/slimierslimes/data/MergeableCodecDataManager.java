/*

The MIT License (MIT)

Copyright (c) 2020 Joseph Bettendorff a.k.a. "Commoble"

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */

package mod.patrigan.slimierslimes.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Generic data loader for Codec-parsable data.
 * This works best if initialized during your mod's construction.
 * After creating the manager, subscribeAsSyncable can optionally be called on it to subscribe the manager
 * to the forge events necessary for syncing datapack data to clients.
 * @param <RAW> The type of the objects that the codec is parsing jsons as
 * @param <FINE> The type of the object we get after merging the parsed objects. Can be the same as RAW
 */
public class MergeableCodecDataManager<RAW, FINE> extends ReloadListener<Map<ResourceLocation, FINE>>
{
    protected static final String JSON_EXTENSION = ".json";
    protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();
    protected static final Gson STANDARD_GSON = new Gson();

    @Nonnull
    /** Mutable, non-null map containing whatever data was loaded last time server datapacks were loaded **/
    public Map<ResourceLocation, FINE> data = new HashMap<>();

    private final String folderName;
    private final Logger logger;
    private final Codec<RAW> codec;
    private final Function<List<RAW>, FINE> merger;
    private final Gson gson;
    private Optional<Runnable> syncOnReloadCallback = Optional.empty();

    /**
     * Initialize a data manager with the given folder name, codec, and merger
     * @param folderName The name of the folder to load data from,
     * e.g. "cheeses" would load data from "data/modid/cheeses" for all modids.
     * Can include subfolders, e.g. "cheeses/sharp"
     * @param logger A logger that will log parsing errors if they occur
     * @param codec A codec that will be used to parse jsons. See drullkus's codec primer for help on creating these:
     * https://gist.github.com/Drullkus/1bca3f2d7f048b1fe03be97c28f87910
     * @param merger A merging function that uses a list of java-objects-that-were-parsed-from-json to create a final object.
     * The list contains all successfully-parsed objects with the same ID from all mods and datapacks.
     * (for a json located at "data/modid/folderName/name.json", the object's ID is "modid:name")
     * As an example, consider vanilla's Tags: mods or datapacks can define tags with the same modid:name id,
     * and then all tag jsons defined with the same ID are merged additively into a single set of items, etc
     */
    public MergeableCodecDataManager(final String folderName, final Logger logger, Codec<RAW> codec, final Function<List<RAW>, FINE> merger)
    {
        this(folderName, logger, codec, merger, STANDARD_GSON);
    }


    /**
     * Initialize a data manager with the given folder name, codec, and merger, as well as a user-defined GSON instance.
     * @param folderName The name of the folder to load data from,
     * e.g. "cheeses" would load data from "data/modid/cheeses" for all modids.
     * Can include subfolders, e.g. "cheeses/sharp"
     * @param logger A logger that will log parsing errors if they occur
     * @param codec A codec that will be used to parse jsons. See drullkus's codec primer for help on creating these:
     * https://gist.github.com/Drullkus/1bca3f2d7f048b1fe03be97c28f87910
     * @param merger A merging function that uses a list of java-objects-that-were-parsed-from-json to create a final object.
     * The list contains all successfully-parsed objects with the same ID from all mods and datapacks.
     * (for a json located at "data/modid/folderName/name.json", the object's ID is "modid:name")
     * As an example, consider vanilla's Tags: mods or datapacks can define tags with the same modid:name id,
     * and then all tag jsons defined with the same ID are merged additively into a single set of items, etc
     * @param gson A GSON instance, allowing for user-defined deserializers. General not needed as the gson is only used to convert
     * raw json to a JsonElement, which the Codec then parses into a proper java object.
     */
    public MergeableCodecDataManager(final String folderName, final Logger logger, Codec<RAW> codec, final Function<List<RAW>, FINE> merger, final Gson gson)
    {
        this.folderName = folderName;
        this.logger = logger;
        this.codec = codec;
        this.merger = merger;
        this.gson = gson;
    }

    /** Off-thread processing (can include reading files from hard drive) **/
    @Override
    protected Map<ResourceLocation, FINE> prepare(final IResourceManager resourceManager, final IProfiler profiler)
    {
        final Map<ResourceLocation, List<RAW>> map = Maps.newHashMap();

        for (ResourceLocation resourceLocation : resourceManager.listResources(this.folderName, MergeableCodecDataManager::isStringJsonFile))
        {
            final String namespace = resourceLocation.getNamespace();
            final String filePath = resourceLocation.getPath();
            final String dataPath = filePath.substring(this.folderName.length() + 1, filePath.length() - JSON_EXTENSION_LENGTH);

            // this is a json with identifier "somemodid:somedata"
            final ResourceLocation jsonIdentifier = new ResourceLocation(namespace, dataPath);
            // this is the list of all json objects with the given resource location (i.e. in multiple datapacks)
            final List<RAW> unmergedRaws = new ArrayList<>();
            // it's entirely possible that there are multiple jsons with this identifier,
            // we can query the resource manager for these
            try
            {
                for (IResource resource : resourceManager.getResources(resourceLocation))
                {
                    try // with resources
                            (
                                    final InputStream inputStream = resource.getInputStream();
                                    final Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                            )
                    {
                        // read the json file and save the parsed object for later
                        // this json element may return null
                        final JsonElement jsonElement = JSONUtils.fromJson(this.gson, reader, JsonElement.class);
                        this.codec.parse(JsonOps.INSTANCE, jsonElement)
                                // resultOrPartial either returns a non-empty optional or calls the consumer given
                                .resultOrPartial(MergeableCodecDataManager::throwJsonParseException)
                                .ifPresent(unmergedRaws::add);
                    }
                    catch(RuntimeException | IOException exception)
                    {
                        this.logger.error("Data loader for {} could not read data {} from file {} in data pack {}", this.folderName, jsonIdentifier, resourceLocation, resource.getSourceName(), exception);
                    }
                    finally
                    {
                        IOUtils.closeQuietly(resource);
                    }
                }
            }
            catch (IOException exception)
            {
                this.logger.error("Data loader for {} could not read data {} from file {}", this.folderName, jsonIdentifier, resourceLocation, exception);
            }


            map.put(jsonIdentifier, unmergedRaws);
        }

        return MergeableCodecDataManager.mapValues(map, this.merger::apply);
    }

    static boolean isStringJsonFile(final String filename)
    {
        return filename.endsWith(JSON_EXTENSION);
    }

    static void throwJsonParseException(final String codecParseFailure)
    {
        throw new JsonParseException(codecParseFailure);
    }

    static <Key, In, Out> Map<Key, Out> mapValues(final Map<Key,In> inputs, final Function<In, Out> mapper)
    {
        final Map<Key,Out> newMap = new HashMap<>();

        inputs.forEach((key, input) -> newMap.put(key, mapper.apply(input)));

        return newMap;
    }

    /** Main-thread processing, runs after prepare concludes **/
    @Override
    protected void apply(final Map<ResourceLocation, FINE> processedData, final IResourceManager resourceManager, final IProfiler profiler)
    {
        this.logger.info("Beginning loading of data for data loader: {}", this.folderName);
        // now that we're on the main thread, we can finalize the data
        this.data = processedData;
        this.logger.info("Data loader for {} loaded {} finalized objects", this.folderName, this.data.size());

        // hacky server test until we can find a better way to do this
        boolean isServer = true;
        try
        {
            LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        }
        catch(Exception e)
        {
            isServer = false;
        }
        if (isServer)
        {
            // if we're on the server and we are configured to send syncing packets, send syncing packets
            this.syncOnReloadCallback.ifPresent(Runnable::run);
        }
    }

    /**
     * This should be called at most once, in a mod constructor (FMLCommonSetupEvent *may* work as well)
     * Calling this method in static init may cause it to be called later than it should be.
     * Calling this method A) causes the data manager to send a data-syncing packet to all players when a server /reloads data,
     * and B) subscribes the data manager to the PlayerLoggedIn event to allow it to sync itself to players when they log in.
     * Be aware that the invoker must still manually subscribe the relevant packet type to the channel themselves.
     * @param <PACKET> the packet type that will be sent on the given channel
     * @param channel The networking channel of your mod
     * @param packetFactory  A packet constructor or factory method that converts the given map to a packet object to send on the given channel
     * @return this manager object
     */
    public <PACKET> MergeableCodecDataManager<RAW, FINE> subscribeAsSyncable(final SimpleChannel channel,
                                                                             final Function<Map<ResourceLocation, FINE>, PACKET> packetFactory)
    {
        MinecraftForge.EVENT_BUS.addListener(this.getLoginListener(channel, packetFactory));
        this.syncOnReloadCallback = Optional.of(() -> channel.send(PacketDistributor.ALL.noArg(), packetFactory.apply(this.data)));
        return this;
    }

    /** Generate an event listener function for the player-login-event **/
    private <PACKET> Consumer<PlayerEvent.PlayerLoggedInEvent> getLoginListener(final SimpleChannel channel,
                                                                                final Function<Map<ResourceLocation, FINE>, PACKET> packetFactory)
    {
        return event -> {
            PlayerEntity player = event.getPlayer();
            if (player instanceof ServerPlayerEntity)
            {
                channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packetFactory.apply(this.data));
            }
        };
    }
}