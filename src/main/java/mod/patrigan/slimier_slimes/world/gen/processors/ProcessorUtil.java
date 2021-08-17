package mod.patrigan.slimier_slimes.world.gen.processors;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;

import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ProcessorUtil {
    public static final String NBT_FINAL_STATE = "final_state";

    public static Random getRandom(BlockPos pos, long processorSeed) {
        return pos == null ? new Random(Util.getMillis() + processorSeed) : new Random(Mth.getSeed(pos) + processorSeed);
    }


    // Todo: add type checks on the objects
    // Todo: add checks to see if property actually exists.
    public static BlockState copyStairsState(BlockState state, Block newBlock) {
        return newBlock.defaultBlockState().setValue(StairBlock.FACING, state.getValue(StairBlock.FACING)).setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE)).setValue(StairBlock.HALF, state.getValue(StairBlock.HALF)).setValue(StairBlock.WATERLOGGED, state.getValue(StairBlock.WATERLOGGED));
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

    public static StructureTemplate.StructureBlockInfo getBlock(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    public static boolean isAir(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block == null || block.defaultBlockState().isAir();
        }else {
            return blockinfo == null || blockinfo.state.is(AIR) || blockinfo.state.is(CAVE_AIR);
        }
    }

    public static boolean isSolid(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.defaultBlockState().isAir() && !(block instanceof LiquidBlock);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof LiquidBlock);
        }
    }

    public static boolean isFaceFull(StructureTemplate.StructureBlockInfo blockinfo, Direction direction){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.defaultBlockState().isAir() && !(block instanceof LiquidBlock) &&
                    Block.isFaceFull(block.getShape(block.defaultBlockState(), null, blockinfo.pos, CollisionContext.empty()), direction);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof LiquidBlock) &&
            Block.isFaceFull(blockinfo.state.getBlock().getShape(blockinfo.state, null, blockinfo.pos, CollisionContext.empty()), direction);
        }
    }
}
