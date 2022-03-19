package mod.patrigan.slimier_slimes.world.gen.feature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.init.ModConfiguredStructures;
import mod.patrigan.slimier_slimes.init.ModStructures;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModStructureSpawns {

//    @SubscribeEvent
//    public static void biomeLoading(final BiomeLoadingEvent event)
//    {
//        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
//
//        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);
//
//        if(types.contains(OVERWORLD)
//                && !types.contains(OCEAN)
//                && types.contains(DENSE)) {
//            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PILLAGER_SLIME_LAB);
//            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_SEWER);
//        }
//        if(types.contains(OVERWORLD)){
//            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_SLIME_DUNGEON);
//        }
//    }


    /**
     * Tells the chunkgenerator which biomes our structure can spawn in.
     * Will go into the world's chunkgenerator and manually add our structure spacing.
     * If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure.
     * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension!)
     * (It might have your structure in it already.)
     *
     * Basically use this to make absolutely sure the chunkgenerator can or cannot spawn your structure.
     */
    private static Method GETCODEC_METHOD;

    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel serverLevel){
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            // Skip superflat to prevent issues with it. Plus, users don't want structures clogging up their superflat worlds.
            if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();

            //////////// BIOME BASED STRUCTURE SPAWNING ////////////
            /*
             * NOTE: BiomeLoadingEvent from Forge API does not work with structures anymore.
             * Instead, we will use the below to add our structure to overworld biomes.
             * Remember, this is temporary until Forge API finds a better solution for adding structures to biomes.
             */

            // Create a mutable map we will use for easier adding to biomes
            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> structureToMultiMap = new HashMap<>();

            // Add the resourcekey of all biomes that this Configured Structure can spawn in.
            for(Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {

                Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biomeEntry.getKey());
                // Skip all ocean, end, nether, and none category biomes.
                // You can do checks for other traits that the biome has.
                Biome.BiomeCategory biomeCategory = biomeEntry.getValue().getBiomeCategory();
                if(biomeCategory != Biome.BiomeCategory.OCEAN && biomeCategory != Biome.BiomeCategory.THEEND && biomeCategory != Biome.BiomeCategory.NETHER && biomeCategory != Biome.BiomeCategory.NONE) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ModConfiguredStructures.CONFIGURED_SLIME_DUNGEON, biomeEntry.getKey());
                    if(types.contains(DENSE)) {
                        associateBiomeToConfiguredStructure(structureToMultiMap, ModConfiguredStructures.CONFIGURED_PILLAGER_SLIME_LAB, biomeEntry.getKey());
                        associateBiomeToConfiguredStructure(structureToMultiMap, ModConfiguredStructures.CONFIGURED_SEWER, biomeEntry.getKey());
                    }
                }
            }

            // Alternative way to add our structures to a fixed set of biomes by creating a set of biome resource keys.
            // To create a custom resource key that points to your own biome, do this:
            // ResourceKey.of(Registry.BIOME_REGISTRY, new ResourceLocation("modid", "custom_biome"))
//            ImmutableSet<ResourceKey<Biome>> overworldBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
//                    .add(Biomes.FOREST)
//                    .add(Biomes.MEADOW)
//                    .add(Biomes.PLAINS)
//                    .add(Biomes.SAVANNA)
//                    .add(Biomes.SNOWY_PLAINS)
//                    .add(Biomes.SWAMP)
//                    .add(Biomes.SUNFLOWER_PLAINS)
//                    .add(Biomes.TAIGA)
//                    .build();
//            overworldBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(structureToMultiMap, STConfiguredStructures.CONFIGURED_RUN_DOWN_HOUSE, biomeKey));

            // Grab the map that holds what ConfigureStructures a structure has and what biomes it can spawn in.
            // Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !structureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

            // Add our structures to the structure map/multimap and set the world to use this combined map/multimap.
            structureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

            // Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();


            //////////// DIMENSION BASED STRUCTURE SPAWNING (OPTIONAL) ////////////
            /*
             * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
             * They will handle your structure spacing for your if you add to BuiltinRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
             * This here is done with reflection as this tutorial is not about setting up and using Mixins.
             * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
             */
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                SlimierSlimes.LOGGER.error("Was unable to check if " + serverLevel.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            /*
             * Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also that vanilla superflat is really tricky and buggy to work with in my experience.
             */
            if(chunkGenerator instanceof FlatLevelSource &&
                    serverLevel.dimension().equals(Level.OVERWORLD)){
                return;
            }

            /*
             * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
             * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
             *
             * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as BuiltinRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
             * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
             * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
             */
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.put(ModStructures.PILLAGER_SLIME_LAB.get(), StructureSettings.DEFAULTS.get(ModStructures.PILLAGER_SLIME_LAB.get()));
            tempMap.put(ModStructures.SLIME_DUNGEON.get(), StructureSettings.DEFAULTS.get(ModStructures.SLIME_DUNGEON.get()));
            tempMap.put(ModStructures.SEWER.get(), StructureSettings.DEFAULTS.get(ModStructures.SEWER.get()));
            worldStructureConfig.structureConfig = tempMap;
        }
    }

    /**
     * Helper method that handles setting up the map to multimap relationship to help prevent issues.
     */
    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> STStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        STStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = STStructureToMultiMap.get(configuredStructureFeature.feature);
        if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            SlimierSlimes.LOGGER.error("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey
            );
        }
        else{
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }

}