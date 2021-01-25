package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.datagen.DataGenerators;
import mod.patrigan.slimierslimes.init.*;
import mod.patrigan.slimierslimes.world.gen.ModEntitySpawns;
import mod.patrigan.slimierslimes.world.gen.feature.ModFeatures;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.registerAdditionalEntityInformation;

@Mod(SlimierSlimes.MOD_ID)
public class SlimierSlimes {

    public static final String MOD_ID = "slimier-slimes";

    public static final Logger LOGGER = LogManager.getLogger();

    public SlimierSlimes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(DataGenerators::gatherData);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModEntityTypes.SPAWN_EGGS.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModParticleTypes.PARTICLES.register(modEventBus);
        ModStructures.STRUCTURES.register(modEventBus);

        modEventBus.addListener(ModEntitySpawns::initBaseWeights);

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.log(Level.INFO, "Slimier Slimes Loaded.");
    }

    public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon(){
            return new ItemStack(ModItems.JELLY.get(DyeColor.LIME).get());
        }
    };

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //Register Features
            ModFeatures.registerConfiguredFeatures();
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            //Entity Spawning
            ModEntitySpawns.init();
            //Register Entities
            registerAdditionalEntityInformation();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModBlocks.initRenderTypes();
    }

}
