package mod.patrigan.slimier_slimes.world.gen.feature;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.init.ModConfiguredStructures;
import mod.patrigan.slimier_slimes.init.ModStructures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModStructureSpawns {

    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        if(types.contains(OVERWORLD)
                && !types.contains(OCEAN)
                && types.contains(DENSE)) {
            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PILLAGER_SLIME_LAB);
            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_SEWER);
        }
        if(types.contains(OVERWORLD)){
            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_SLIME_DUNGEON);
        }
    }

    /**
     * Will go into the world's chunkgenerator and manually add our structure spacing.
     * If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure.
     * (Don't forget to attempt to remove your structure too from
     *  the map if you are blacklisting that dimension! It might have
     *  your structure in it already.)
     *
     * Basically use this to make absolutely sure the chunkgenerator
     * can or cannot spawn your structure.
     */
    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel){
            ServerLevel serverWorld = (ServerLevel)event.getWorld();

            // Prevent spawning our structure in Vanilla's superflat world as
            // people seem to want their superflat worlds free of modded structures.
            // Also that vanilla superflat is really tricky and buggy to work with in my experience.
            if(serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverWorld.dimension().equals(Level.OVERWORLD)){
                return;
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.put(ModStructures.PILLAGER_SLIME_LAB.get(), StructureSettings.DEFAULTS.get(ModStructures.PILLAGER_SLIME_LAB.get()));
            tempMap.put(ModStructures.SLIME_DUNGEON.get(), StructureSettings.DEFAULTS.get(ModStructures.SLIME_DUNGEON.get()));
            tempMap.put(ModStructures.SEWER.get(), StructureSettings.DEFAULTS.get(ModStructures.SEWER.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

}