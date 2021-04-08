package mod.patrigan.slimierslimes.init;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.structures.PillagerSlimeLab;
import mod.patrigan.slimierslimes.structures.Sewer;
import mod.patrigan.slimierslimes.structures.SlimeDungeon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ModConfiguredStructures {
    /**
     * Static instance of our structure so we can reference it and add it to biomes easily.
     */
    public static final StructureFeature<?, ?> CONFIGURED_PILLAGER_SLIME_LAB = ModStructures.PILLAGER_SLIME_LAB.get().configured(IFeatureConfig.NONE);
    public static final StructureFeature<?, ?> CONFIGURED_SLIME_DUNGEON = ModStructures.SLIME_DUNGEON.get().configured(IFeatureConfig.NONE);
    public static final StructureFeature<?, ?> CONFIGURED_SEWER = ModStructures.SEWER.get().configured(IFeatureConfig.NONE);

    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ PillagerSlimeLab.STRUCTURE_ID), CONFIGURED_PILLAGER_SLIME_LAB);
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ SlimeDungeon.STRUCTURE_ID), CONFIGURED_SLIME_DUNGEON);
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ Sewer.STRUCTURE_ID), CONFIGURED_SEWER);

        // Ok so, this part may be hard to grasp but basically, just add your structure to this to
        // prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
        // FlatGenerationSettings.STRUCTURES in it and you don't add your structure to it, the game
        // could crash later when you attempt to add the StructureSeparationSettings to the dimension.
        //
        // (It would also crash with superflat worldtype if you omit the below line
        //  and attempt to add the structure's StructureSeparationSettings to the world)
        //
        // Note: If you want your structure to spawn in superflat, remove the FlatChunkGenerator check
        // in StructureTutorialMain.addDimensionalSpacing and then create a superflat world, exit it,
        // and re-enter it and your structures will be spawning. I could not figure out why it needs
        // the restart but honestly, superflat is really buggy and shouldn't be your main focus in my opinion.
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.PILLAGER_SLIME_LAB.get(), CONFIGURED_PILLAGER_SLIME_LAB);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.SLIME_DUNGEON.get(), CONFIGURED_SLIME_DUNGEON);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(ModStructures.SEWER.get(), CONFIGURED_SEWER);
    }
}