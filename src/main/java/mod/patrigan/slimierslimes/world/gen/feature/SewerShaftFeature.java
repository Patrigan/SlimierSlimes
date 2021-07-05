package mod.patrigan.slimierslimes.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

import static net.minecraft.block.Blocks.*;
import static net.minecraft.block.LadderBlock.FACING;

public class SewerShaftFeature extends Feature<NoFeatureConfig> {
    public SewerShaftFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos blockPos, NoFeatureConfig config) {
        Direction ladderDirection = getLadderDirection(world, blockPos.below());
        BlockPos ladderBlockPos = blockPos.relative(ladderDirection);
        int j = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, ladderBlockPos.getX(), ladderBlockPos.getZ());
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        world.setBlock(new BlockPos(ladderBlockPos.getX(), j+1, ladderBlockPos.getZ()), IRON_TRAPDOOR.defaultBlockState(), 2);
        while (j >= ladderBlockPos.getY()) {
            mutable.set(new BlockPos(ladderBlockPos.getX(), j, ladderBlockPos.getZ()));
            BlockState blockState = LADDER.defaultBlockState().setValue(FACING, ladderDirection.getOpposite());
            world.setBlock(mutable, blockState, 2);
            for(Direction direction : Direction.Plane.HORIZONTAL){
                mutable.move(direction);
                BlockState blockState1 = POLISHED_ANDESITE.defaultBlockState();
                world.setBlock(mutable, blockState1, 2);
                mutable.move(direction.getOpposite());
            }
            j--;
        }
        return true;
    }

    private Direction getLadderDirection(ISeedReader world, BlockPos blockPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for(Direction direction : Direction.Plane.HORIZONTAL){
            mutable.setWithOffset(blockPos, direction);
            BlockState blockState1 = world.getBlockState(mutable);
            if(blockState1.is(LADDER)){
                return direction;
            }
        }
        return Direction.NORTH;
    }
}