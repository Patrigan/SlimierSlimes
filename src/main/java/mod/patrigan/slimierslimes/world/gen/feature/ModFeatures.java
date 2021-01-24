package mod.patrigan.slimierslimes.world.gen.feature;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFeatures {

    private static ConfiguredFeature<?,?> SLIMY_STONE_FEATURE;
    private static ConfiguredFeature<?,?> STONE_LAVA_SLIME_SPAWNER_FEATURES;
    private static ConfiguredFeature<?,?> NETHERRACK_LAVA_SLIME_SPAWNER_FEATURES;

    public static void registerConfiguredFeatures(){
        registerLavaSlimeSpawnerFeatures();
        SLIMY_STONE_FEATURE = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.SLIMY_STONE_BLOCK.get(DyeColor.GREEN).getBlock().get().getDefaultState(), 8)).range(128).square().func_242731_b(6);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "slimy_stone"), SLIMY_STONE_FEATURE);
    }

    private static void registerLavaSlimeSpawnerFeatures() {
        STONE_LAVA_SLIME_SPAWNER_FEATURES = new LavaSpawnerFeature(LavaSpawnerConfig.CODEC).withConfiguration(new LavaSpawnerConfig(new BlockMatchRuleTest(Blocks.STONE), ModBlocks.STONE_LAVA_SLIME_SPAWNER.get().getDefaultState(), 6)).range(128).square().func_242731_b(100);
        NETHERRACK_LAVA_SLIME_SPAWNER_FEATURES = new LavaSpawnerFeature(LavaSpawnerConfig.CODEC).withConfiguration(new LavaSpawnerConfig(new BlockMatchRuleTest(Blocks.NETHERRACK), ModBlocks.NETHERRACK_LAVA_SLIME_SPAWNER.get().getDefaultState(), 6)).range(128).square().func_242731_b(50);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> SLIMY_STONE_FEATURE);
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> STONE_LAVA_SLIME_SPAWNER_FEATURES);
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> NETHERRACK_LAVA_SLIME_SPAWNER_FEATURES);
    }


}
