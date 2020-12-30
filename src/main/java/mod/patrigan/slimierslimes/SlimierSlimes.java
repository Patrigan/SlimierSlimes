package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.datagen.DataGenerators;
import mod.patrigan.slimierslimes.entities.*;
import mod.patrigan.slimierslimes.init.*;
import mod.patrigan.slimierslimes.world.gen.ModEntitySpawns;
import mod.patrigan.slimierslimes.world.gen.feature.ModFeatures;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
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

import static mod.patrigan.slimierslimes.init.ModBlocks.SLIMY_STONE_BLOCK;

@Mod(SlimierSlimes.MOD_ID)
public class SlimierSlimes {

    public static final String MOD_ID = "slimier-slimes";

    private static final Logger LOGGER = LogManager.getLogger();

    public SlimierSlimes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(DataGenerators::gatherData);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModParticleTypes.PARTICLES.register(modEventBus);

        modEventBus.addListener(ModEntitySpawns::initBaseWeights);

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.log(Level.INFO, "Slimier Slimes Loaded.");
    }

    public static final ItemGroup TAB = new ItemGroup("SlimierSlimesTab") {
        @Override
        public ItemStack createIcon(){
            return new ItemStack(ModItems.GREEN_JELLY.get());
        }
    };

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //Register Entities
            GlobalEntityTypeAttributes.put(ModEntityTypes.COMMON_SLIME.get(), CommonSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.PINK_SLIME.get(), PinkSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.ROCK_SLIME.get(), RockSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeEntity.getMutableAttributes().create());
            GlobalEntityTypeAttributes.put(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeEntity.getMutableAttributes().create());
            ModEntitySpawns.init();
            //Register Features
            ModFeatures.registerConfiguredFeatures();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(SLIMY_STONE_BLOCK.get(), RenderType.getTranslucent());
    }

}
