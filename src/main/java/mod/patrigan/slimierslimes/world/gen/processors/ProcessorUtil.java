package mod.patrigan.slimierslimes.world.gen.processors;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;

import static net.minecraft.block.Blocks.*;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class ProcessorUtil {
    public static final String NBT_FINAL_STATE = "final_state";

    public static Random  getRandom(BlockPos pos, long processorSeed) {
        return pos == null ? new Random(Util.getMillis() + processorSeed) : new Random(MathHelper.getSeed(pos) + processorSeed);
    }


    // Todo: add type checks on the objects
    // Todo: add checks to see if property actually exists.
    public static BlockState copyStairsState(BlockState state, Block newBlock) {
        return newBlock.defaultBlockState().setValue(StairsBlock.FACING, state.getValue(StairsBlock.FACING)).setValue(StairsBlock.SHAPE, state.getValue(StairsBlock.SHAPE)).setValue(StairsBlock.HALF, state.getValue(StairsBlock.HALF)).setValue(StairsBlock.WATERLOGGED, state.getValue(StairsBlock.WATERLOGGED));
    }

    public static BlockState copySlabState(BlockState blockstate, Block newBlock) {
        return newBlock.defaultBlockState().setValue(SlabBlock.TYPE, blockstate.getValue(SlabBlock.TYPE)).setValue(SlabBlock.WATERLOGGED, blockstate.getValue(SlabBlock.WATERLOGGED));
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

    public static Template.BlockInfo getBlock(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    public static boolean isAir(Template.BlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block == null || block.is(AIR) || block.is(CAVE_AIR);
        }else {
            return blockinfo == null || blockinfo.state.is(AIR) || blockinfo.state.is(CAVE_AIR);
        }
    }

    public static boolean isSolid(Template.BlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.is(AIR) && !block.is(CAVE_AIR) && !(block instanceof FlowingFluidBlock);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof FlowingFluidBlock);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isFaceFull(Template.BlockInfo blockinfo, Direction direction){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.is(AIR) && !block.is(CAVE_AIR) && !(block instanceof FlowingFluidBlock) &&
                    Block.isFaceFull(block.getShape(block.defaultBlockState(), null, blockinfo.pos, ISelectionContext.empty()), direction);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof FlowingFluidBlock) &&
            Block.isFaceFull(blockinfo.state.getBlock().getShape(blockinfo.state, null, blockinfo.pos, ISelectionContext.empty()), direction);
        }
    }
}
