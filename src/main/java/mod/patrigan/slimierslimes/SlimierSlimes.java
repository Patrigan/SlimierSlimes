package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.client.packet.SlimeDataSyncPacket;
import mod.patrigan.slimierslimes.datagen.DataGenerators;
import mod.patrigan.slimierslimes.init.*;
import mod.patrigan.slimierslimes.init.config.CommonConfigs.CommonConfigValues;
import mod.patrigan.slimierslimes.init.data.SlimeDatas;
import mod.patrigan.slimierslimes.network.datasync.ModDataSerializers;
import mod.patrigan.slimierslimes.util.ConfigHelper;
import mod.patrigan.slimierslimes.world.gen.ModEntitySpawns;
import mod.patrigan.slimierslimes.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mod.patrigan.slimierslimes.init.data.SlimeDatas.SLIME_DATA;

@Mod(SlimierSlimes.MOD_ID)
public class SlimierSlimes {

    public static final String MOD_ID = "slimier-slimes";

    public static final Logger LOGGER = LogManager.getLogger();

    public static CommonConfigValues MAIN_CONFIG = null;

    private static final String CHANNEL_PROTOCOL = "0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> CHANNEL_PROTOCOL,
            CHANNEL_PROTOCOL::equals,
            CHANNEL_PROTOCOL::equals);

    public SlimierSlimes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(DataGenerators::gatherData);
        MAIN_CONFIG = ConfigHelper.register(ModConfig.Type.SERVER, CommonConfigValues::new, "slimier_slimes-main.toml");

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModEntityTypes.SPAWN_EGGS.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModParticleTypes.PARTICLES.register(modEventBus);
        ModStructures.STRUCTURES.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModDataSerializers.DATA_SERIALIZERS.register(modEventBus);

        this.registerPackets();

        forgeBus.register(this);
        forgeBus.addListener(this::onAddReloadListeners);

        SLIME_DATA.subscribeAsSyncable(CHANNEL, SlimeDatas::toPacket);

        LOGGER.log(Level.INFO, "Slimier Slimes Loaded.");
    }

    public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(ModItems.JELLY.get(DyeColor.LIME).get());
        }
    };

    void registerPackets()
    {
        int id = 0;
        CHANNEL.registerMessage(id++, SlimeDataSyncPacket.class,
                SlimeDataSyncPacket::encode,
                SlimeDataSyncPacket::decode,
                SlimeDataSyncPacket::onPacketReceived);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //Register Features
            ModConfiguredFeatures.registerConfiguredFeatures();
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            //Entity Spawning
            ModEntitySpawns.init();
            ModProcessors.init();
        });
    }
    private void doClientStuff(final FMLClientSetupEvent event) {
        ModBlocks.initRenderTypes();
    }

    void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(SLIME_DATA);
    }

}
