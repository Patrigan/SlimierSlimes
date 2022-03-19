package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.world.gen.structure.PillagerSlimeLab;
import mod.patrigan.slimier_slimes.world.gen.structure.Sewer;
import mod.patrigan.slimier_slimes.world.gen.structure.SlimeDungeon;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class ModConfiguredStructures {
    /**
     * Static instance of our structure so we can reference it and add it to biomes easily.
     */
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_PILLAGER_SLIME_LAB = ModStructures.PILLAGER_SLIME_LAB.get().configured(new JigsawConfiguration(
            // Dummy values for now. We will modify the pool at runtime since we cannot get json pool files here at mod init.
            // You can create and register your pools in code, pass in the code create pool here, and delete line 137 in RunDownHouseStructure
            () -> PlainVillagePools.START,

            // We will set size at runtime too as JigsawConfiguration will not handle sizes above 7.
            // If your size is below 7, you can set the size here and delete line 153 in RunDownHouseStructure
            0

            /*
             * The only reason we are using JigsawConfiguration here is because in RunDownHouseStructure's createPiecesGenerator method,
             * we are using JigsawPlacement.addPieces which requires JigsawConfiguration. However, if you create your own
             * JigsawPlacement.addPieces, you could reduce the amount of workarounds like above that you need and give yourself more
             * opportunities and control over your structures.
             *
             * An example of a custom JigsawPlacement.addPieces in action can be found here:
             * https://github.com/TelepathicGrunt/RepurposedStructures/blob/1.18/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/pieces/PieceLimitedJigsawManager.java
             */
    ));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_SLIME_DUNGEON = ModStructures.SLIME_DUNGEON.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START,0));
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_SEWER = ModStructures.SEWER.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START,0));

    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ PillagerSlimeLab.STRUCTURE_ID), CONFIGURED_PILLAGER_SLIME_LAB);
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ SlimeDungeon.STRUCTURE_ID), CONFIGURED_SLIME_DUNGEON);
        Registry.register(registry, new ResourceLocation(SlimierSlimes.MOD_ID, "configured_"+ Sewer.STRUCTURE_ID), CONFIGURED_SEWER);
    }
}