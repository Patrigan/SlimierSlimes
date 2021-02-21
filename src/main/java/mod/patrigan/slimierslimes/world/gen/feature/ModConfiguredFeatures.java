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
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static mod.patrigan.slimierslimes.init.ModFeatures.LAVA_SLIME_SPAWNER_FEATURE;
import static mod.patrigan.slimierslimes.init.ModFeatures.SLIME_SPAWNER_FEATURE;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModConfiguredFeatures {


    private static Map<DyeColor, ConfiguredFeature<?,?>> SLIMY_STONE_CONFIGURED_FEATURE = new EnumMap<>(DyeColor.class);
    private static Map<DyeColor, ConfiguredFeature<?,?>> SLIMY_NETHERRACK_CONFIGURED_FEATURE = new EnumMap<>(DyeColor.class);
    private static ConfiguredFeature<?,?> STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE;
    private static ConfiguredFeature<?,?> NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE;
    private static ConfiguredFeature<?,?> SLIME_SPAWNER_CONFIGURED_FEATURE;

    public static void registerConfiguredFeatures(){
        registerLavaSlimeSpawnerFeatures();
        Arrays.stream(DyeColor.values()).forEach(ModConfiguredFeatures::registerSlimyStone);
        Arrays.stream(DyeColor.values()).forEach(ModConfiguredFeatures::registerSlimyNetherrack);
        SLIME_SPAWNER_CONFIGURED_FEATURE = SLIME_SPAWNER_FEATURE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "slime_spawner_configured_feature"), SLIME_SPAWNER_CONFIGURED_FEATURE);
    }

    private static void registerSlimyStone(DyeColor dyeColor) {
        SLIMY_STONE_CONFIGURED_FEATURE.put(dyeColor, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getBlock().get().getDefaultState(), 8)).range(128).square().func_242731_b(1));
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_stone_configured_feature"), SLIMY_STONE_CONFIGURED_FEATURE.get(dyeColor));
    }

    private static void registerSlimyNetherrack(DyeColor dyeColor) {
        SLIMY_NETHERRACK_CONFIGURED_FEATURE.put(dyeColor, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, ModBlocks.SLIMY_NETHERRACK_BLOCK.get(dyeColor).getBlock().get().getDefaultState(), 8)).range(128).square().func_242731_b(1));
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_stone_configured_feature"), SLIMY_NETHERRACK_CONFIGURED_FEATURE.get(dyeColor));
    }

    private static void registerLavaSlimeSpawnerFeatures() {
        STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE = LAVA_SLIME_SPAWNER_FEATURE.get().withConfiguration(new LavaSpawnerConfig(new BlockMatchRuleTest(Blocks.STONE), ModBlocks.STONE_LAVA_SLIME_SPAWNER.get().getDefaultState(), 6)).range(128).square().func_242731_b(100);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "stone_lava_slime_spawner_configured_feature"), STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE);
        NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE = LAVA_SLIME_SPAWNER_FEATURE.get().withConfiguration(new LavaSpawnerConfig(new BlockMatchRuleTest(Blocks.NETHERRACK), ModBlocks.NETHERRACK_LAVA_SLIME_SPAWNER.get().getDefaultState(), 6)).range(128).square().func_242731_b(20);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "netherrack_lava_slime_spawner_configured_feature"), NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE);
    }


    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> SLIMY_STONE_CONFIGURED_FEATURE.get(dyeColor)));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> SLIMY_NETHERRACK_CONFIGURED_FEATURE.get(dyeColor)));
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE);
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE);
    }
}
