package mod.patrigan.slimierslimes.world.gen.processors;

import net.minecraft.block.*;

public class ProcessorUtil {
    // Todo: add type checks on the objects
    // Todo: add checks to see if property actually exists.

    public static BlockState copyStairsState(BlockState state, Block newBlock) {
        return newBlock.defaultBlockState().setValue(StairsBlock.FACING, state.getValue(StairsBlock.FACING)).setValue(StairsBlock.SHAPE, state.getValue(StairsBlock.SHAPE)).setValue(StairsBlock.HALF, state.getValue(StairsBlock.HALF)).setValue(StairsBlock.WATERLOGGED, state.getValue(StairsBlock.WATERLOGGED));
    }

    public static BlockState copySlabState(BlockState blockstate, Block newBlock) {
        return newBlock.defaultBlockState().setValue(SlabBlock.TYPE, blockstate.getValue(SlabBlock.TYPE));
    }

    public static BlockState copyWallState(BlockState blockstate, Block newBlock) {
        return newBlock.defaultBlockState()
                .setValue(WallBlock.UP, blockstate.getValue(WallBlock.UP))
                .setValue(WallBlock.EAST_WALL, blockstate.getValue(WallBlock.EAST_WALL))
                .setValue(WallBlock.NORTH_WALL, blockstate.getValue(WallBlock.NORTH_WALL))
                .setValue(WallBlock.SOUTH_WALL, blockstate.getValue(WallBlock.SOUTH_WALL))
                .setValue(WallBlock.WEST_WALL, blockstate.getValue(WallBlock.WEST_WALL))
                .setValue(WallBlock.WATERLOGGED, blockstate.getValue(WallBlock.WATERLOGGED));
    }
}
