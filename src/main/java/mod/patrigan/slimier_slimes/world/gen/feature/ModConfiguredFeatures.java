package mod.patrigan.slimier_slimes.world.gen.feature;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.init.ModBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static mod.patrigan.slimier_slimes.init.ModFeatures.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModConfiguredFeatures {


    private static Map<DyeColor, ConfiguredFeature<?,?>> SLIMY_STONE_CONFIGURED_FEATURE = new EnumMap<>(DyeColor.class);
    private static Map<DyeColor, ConfiguredFeature<?,?>> SLIMY_NETHERRACK_CONFIGURED_FEATURE = new EnumMap<>(DyeColor.class);
    private static ConfiguredFeature<?,?> STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE;
    private static ConfiguredFeature<?,?> NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE;
    private static ConfiguredFeature<?,?> SEWER_SHAFT_CONFIGURED_FEATURE;
    private static Map<DyeColor, PlacedFeature> SLIMY_STONE_PLACED_FEATURE = new EnumMap<>(DyeColor.class);
    private static Map<DyeColor, PlacedFeature> SLIMY_NETHERRACK_PLACED_FEATURE = new EnumMap<>(DyeColor.class);
    private static PlacedFeature STONE_LAVA_SLIME_SPAWNER_PLACED_FEATURE;
    private static PlacedFeature NETHERRACK_LAVA_SLIME_SPAWNER_PLACED_FEATURE;
    private static PlacedFeature SEWER_SHAFT_PLACED_FEATURE;

    public static void registerConfiguredFeatures(){
        registerLavaSlimeSpawnerFeatures();
        Arrays.stream(DyeColor.values()).forEach(ModConfiguredFeatures::registerSlimyStone);
        Arrays.stream(DyeColor.values()).forEach(ModConfiguredFeatures::registerSlimyNetherrack);
        SEWER_SHAFT_CONFIGURED_FEATURE =  Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "sewer_shaft_configured_feature"), SEWER_SHAFT_FEATURE.get().configured(FeatureConfiguration.NONE));
        SEWER_SHAFT_PLACED_FEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "sewer_shaft_placed_feature"),
                SEWER_SHAFT_CONFIGURED_FEATURE.placed());

    }

    private static void registerSlimyStone(DyeColor dyeColor) {
        ConfiguredFeature<OreConfiguration, ?> configuredFeature = Feature.ORE.configured(new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor).getBlock().get().defaultBlockState(), 10, 0));
        SLIMY_STONE_CONFIGURED_FEATURE.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_stone_configured_feature"), configuredFeature));
        SLIMY_STONE_PLACED_FEATURE.put(dyeColor,
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_stone_placed_feature"),
                configuredFeature.placed(
                        CountPlacement.of(1), // How many attempts per chunk to spawn this feature.
                        InSquarePlacement.spread(), // Randomizes the x/z so it is in a random 0-15 spot in the chunk.
                        HeightRangePlacement.uniform( // Equal chance for any height in the following range:
                                VerticalAnchor.aboveBottom(64), // Bottom of spawn range starts 20 blocks above world bottom.
                                VerticalAnchor.belowTop(0)), // Top of the spawn range starts 50 blocks below world max height.
                        BiomeFilter.biome()) // Needed to allow the feature to spawn in biomes properly.
        ));
    }

    private static void registerSlimyNetherrack(DyeColor dyeColor) {
        ConfiguredFeature<OreConfiguration, ?> configuredFeature = Feature.ORE.configured(new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), ModBlocks.SLIMY_NETHERRACK_BLOCK.get(dyeColor).getBlock().get().defaultBlockState(), 10, 0));
        SLIMY_NETHERRACK_CONFIGURED_FEATURE.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_netherrack_configured_feature"), configuredFeature));
        SLIMY_NETHERRACK_PLACED_FEATURE.put(dyeColor,
                Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slimy_netherrack_placed_feature"),
                        configuredFeature.placed(
                                CountPlacement.of(1), // How many attempts per chunk to spawn this feature.
                                InSquarePlacement.spread(), // Randomizes the x/z so it is in a random 0-15 spot in the chunk.
                                HeightRangePlacement.uniform( // Equal chance for any height in the following range:
                                        VerticalAnchor.aboveBottom(0), // Bottom of spawn range starts 20 blocks above world bottom.
                                        VerticalAnchor.belowTop(0)), // Top of the spawn range starts 50 blocks below world max height.
                                BiomeFilter.biome()) // Needed to allow the feature to spawn in biomes properly.
                ));
    }

    private static void registerLavaSlimeSpawnerFeatures() {
        STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "stone_lava_slime_spawner_configured_feature"),
                LAVA_SLIME_SPAWNER_FEATURE.get().configured(new LavaSpawnerConfig(new BlockMatchTest(Blocks.STONE), ModBlocks.STONE_LAVA_SLIME_SPAWNER.get().defaultBlockState(), 6)));
        STONE_LAVA_SLIME_SPAWNER_PLACED_FEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "stone_lava_slime_spawner_placed_feature"),
                STONE_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE.placed(
                        CountPlacement.of(75), // How many attempts per chunk to spawn this feature.
                        InSquarePlacement.spread(), // Randomizes the x/z so it is in a random 0-15 spot in the chunk.
                        HeightRangePlacement.uniform( // Equal chance for any height in the following range:
                                VerticalAnchor.aboveBottom(0), // Bottom of spawn range starts 20 blocks above world bottom.
                                VerticalAnchor.belowTop(0)), // Top of the spawn range starts 50 blocks below world max height.
                        BiomeFilter.biome()) // Needed to allow the feature to spawn in biomes properly.
        );
        NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "netherrack_lava_slime_spawner_configured_feature"),
                LAVA_SLIME_SPAWNER_FEATURE.get().configured(new LavaSpawnerConfig(new BlockMatchTest(Blocks.STONE), ModBlocks.NETHERRACK_LAVA_SLIME_SPAWNER.get().defaultBlockState(), 6)));
        NETHERRACK_LAVA_SLIME_SPAWNER_PLACED_FEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SlimierSlimes.MOD_ID, "netherrack_lava_slime_spawner_placed_feature"),
                NETHERRACK_LAVA_SLIME_SPAWNER_CONFIGURED_FEATURE.placed(
                        CountPlacement.of(50), // How many attempts per chunk to spawn this feature.
                        InSquarePlacement.spread(), // Randomizes the x/z so it is in a random 0-15 spot in the chunk.
                        HeightRangePlacement.uniform( // Equal chance for any height in the following range:
                                VerticalAnchor.aboveBottom(0), // Bottom of spawn range starts 20 blocks above world bottom.
                                VerticalAnchor.belowTop(0)), // Top of the spawn range starts 50 blocks below world max height.
                        BiomeFilter.biome()) // Needed to allow the feature to spawn in biomes properly.
        );
    }

    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event)
    {
        Biome.BiomeCategory category = event.getCategory();
        if(category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NONE) {
            Arrays.stream(DyeColor.values()).forEach(dyeColor -> event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SLIMY_STONE_PLACED_FEATURE.get(dyeColor)));
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,  STONE_LAVA_SLIME_SPAWNER_PLACED_FEATURE);
        }
        if(category == Biome.BiomeCategory.NETHER) {
            Arrays.stream(DyeColor.values()).forEach(dyeColor -> event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SLIMY_NETHERRACK_PLACED_FEATURE.get(dyeColor)));
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NETHERRACK_LAVA_SLIME_SPAWNER_PLACED_FEATURE);
        }
    }
}
