package mod.patrigan.slimier_slimes.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class WaterFlowFixPostPlacementProcessor implements PostPlacementProcessor {
    public static final WaterFlowFixPostPlacementProcessor WATER_FLOW_FIX = new WaterFlowFixPostPlacementProcessor();

    @Override
    public void afterPlace(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BlockPos.betweenClosedStream(AABB.of(boundingBox)).forEach(blockPos -> process(worldGenLevel, blockPos));
    }

    public void process(WorldGenLevel worldGenLevel, BlockPos blockPos) {
        BlockState blockState = worldGenLevel.getBlockState(blockPos);
        if (blockState.getFluidState().is(Fluids.WATER)) {
            worldGenLevel.scheduleTick(blockPos, Fluids.WATER, 5);
        }else if(blockState.getFluidState().is(Fluids.FLOWING_WATER)) {
            worldGenLevel.scheduleTick(blockPos, Fluids.FLOWING_WATER, 5);
        }

    }
}
