package mod.patrigan.slimier_slimes.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraft.world.level.block.LadderBlock.FACING;

public class SewerShaftFeature extends Feature<NoneFeatureConfiguration> {
    public SewerShaftFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        Direction ladderDirection = getLadderDirection(world, blockPos.below());
        BlockPos ladderBlockPos = blockPos.relative(ladderDirection);
        int j = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, ladderBlockPos.getX(), ladderBlockPos.getZ());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

    private Direction getLadderDirection(WorldGenLevel world, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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