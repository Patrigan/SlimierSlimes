package mod.patrigan.slimier_slimes.blocks;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimier_slimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModTags.Items.ARMOR_SLIME_BOOTS;
import static net.minecraft.world.level.block.Blocks.GRASS_BLOCK;

public class GooLayerBlock extends FallingBlock implements BonemealableBlock {
    public static final Material GOO = (new Material.Builder(MaterialColor.NONE)).noCollider().build();
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public GooLayerBlock(DyeColor color) {
        super(BlockBehaviour.Properties.of(GOO, color).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().randomTicks().noCollission().dynamicShape());
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, PathComputationType pathType) {
        switch(pathType) {
            case LAND:
                return blockState.getValue(LAYERS) < 5;
            case WATER:
                return false;
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext) {
            EntityCollisionContext entitycollisioncontext = (EntityCollisionContext) collisionContext;
            if (blockState.getValue(LAYERS) == 8 && entitycollisioncontext.getEntity() != null && entitycollisioncontext.getEntity() instanceof FallingBlockEntity) {
                return Shapes.block();
            }
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockReader, BlockPos blockPos, CollisionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public void entityInside(BlockState blockState, Level world, BlockPos blockPos, Entity entity) {
        if(entity instanceof FallingBlockEntity && entity.isAlive()){
            FallingBlockEntity fallingBlockEntity = (FallingBlockEntity) entity;
            if(fallingBlockEntity.getBlockState().is(blockState.getBlock()) && blockState.getValue(LAYERS) < 8){
                int totalLayers = blockState.getValue(LAYERS) + fallingBlockEntity.getBlockState().getValue(LAYERS);
                if(totalLayers > 8) {
                    world.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, 8));
                    world.setBlockAndUpdate(blockPos.above(), fallingBlockEntity.getBlockState().setValue(LAYERS, totalLayers - 8));
                }else{
                    world.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, totalLayers));
                }
                fallingBlockEntity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        if (entity instanceof LivingEntity && !(entity instanceof AbstractSlimeEntity) && !((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET).getItem().getTags().contains(ARMOR_SLIME_BOOTS.getName())) {
            double modifier = 1.0-(0.1D*blockState.getValue(LAYERS));
            Vec3 stuckSpeedMultiplier = new Vec3(modifier, 0.75D, modifier);
            if(entity.stuckSpeedMultiplier.equals(Vec3.ZERO) || entity.stuckSpeedMultiplier.lengthSqr() > stuckSpeedMultiplier.lengthSqr()) {
                entity.makeStuckInBlock(blockState, stuckSpeedMultiplier);
            }

        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader world, BlockPos blockPos) {
        BlockState blockstateBelow = world.getBlockState(blockPos.below());
        if(blockstateBelow.getBlock() == this){
            return true;
        }
        if (!blockstateBelow.is(Blocks.HONEY_BLOCK) && !blockstateBelow.is(Blocks.SOUL_SAND) && !blockstateBelow.isAir()) {
            return Block.isFaceFull(blockstateBelow.getCollisionShape(world, blockPos.below()), Direction.UP);
        } else {
            return true;
        }
    }

    @Override
    public void onLand(Level world, BlockPos blockPos, BlockState blockState, BlockState belowBlockState, FallingBlockEntity fallingBlockEntity) {
        if(belowBlockState.is(blockState.getBlock()) &&  belowBlockState.getValue(LAYERS) < 1){
            world.removeBlock(blockPos, true);
            world.setBlockAndUpdate(blockPos.below(), belowBlockState.setValue(LAYERS, belowBlockState.getValue(LAYERS) + 1));
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos blockPos, BlockPos neighbourBlockPos) {
        return !blockState.canSurvive(world, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, neighbourState, world, blockPos, neighbourBlockPos);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel world, BlockPos blockPos, Random random) {
        BlockState blockStateBelow = world.getBlockState(blockPos.below());
        if ((blockStateBelow.is(this) && blockStateBelow.getValue(LAYERS) < 8) || world.isEmptyBlock(blockPos.below()) || isFree(blockStateBelow) && blockPos.getY() >= 0) {
            FallingBlockEntity fallingblockentity = new FallingBlockEntity(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, world.getBlockState(blockPos));
            this.falling(fallingblockentity);
            world.addFreshEntity(fallingblockentity);
        }
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, Random random) {
        if(blockState.getValue(LAYERS) > 1) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            HashMap<BlockPos, BlockState> flowTargets = new HashMap<>();
            for(Direction direction : Direction.Plane.HORIZONTAL){
                mutable.setWithOffset(blockPos, direction);
                BlockState blockState1 = world.getBlockState(mutable);
                if(canFlowTo(blockState, blockState1, world, blockPos)){
                    flowTargets.put(mutable.immutable(), blockState1);
                }
            }
            if(flowTargets.size() <= 0){
                return;
            }
            BlockPos flowTargetPos = new ArrayList<>(flowTargets.keySet()).get(random.nextInt(flowTargets.size()));
            BlockState flowTarget = flowTargets.get(flowTargetPos);
            if(flowTarget.is(blockState.getBlock())){
                world.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, blockState.getValue(LAYERS) - 1));
                world.setBlockAndUpdate(flowTargetPos, flowTarget.setValue(LAYERS, flowTarget.getValue(LAYERS) + 1));
            }else{
                world.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, blockState.getValue(LAYERS) - 1));
                world.setBlockAndUpdate(flowTargetPos, blockState.getBlock().defaultBlockState());
            }
        }
    }

    private boolean canFlowTo(BlockState blockState, BlockState blockState1, ServerLevel world, BlockPos blockPos) {
        return (blockState1.is(blockState.getBlock()) && blockState.getValue(LAYERS) - blockState1.getValue(LAYERS) > 1)
                || blockState1.isAir();
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockItemUseContext) {
        int i = blockState.getValue(LAYERS);
        if (blockItemUseContext.getItemInHand().getItem() == this.asItem() && i < 8) {
            if (blockItemUseContext.replacingClickedOnBlock()) {
                return blockItemUseContext.getClickedFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockItemUseContext) {
        BlockState blockstate = blockItemUseContext.getLevel().getBlockState(blockItemUseContext.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(LAYERS);
            return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
        } else {
            return super.getStateForPlacement(blockItemUseContext);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LAYERS);
    }

    public static void spawnAtBlockPos(Level world, BlockPos blockPos, DyeColor dyeColor){
        BlockState blockState = world.getBlockState(blockPos);
        if(world.isEmptyBlock(blockPos)){
            world.setBlockAndUpdate(blockPos, GOO_LAYER_BLOCKS.get(dyeColor).get().defaultBlockState());
        }else{
            if(blockState.is(GOO_LAYER_BLOCKS.get(dyeColor).get()) && blockState.getValue(LAYERS) < 8){
                world.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, blockState.getValue(LAYERS) + 1));
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockReader, BlockPos blockPos, BlockState blockState, boolean p_176473_4_) {
        return blockReader.getBlockState(blockPos.below()).is(GRASS_BLOCK);
    }

    @Override
    public boolean isBonemealSuccess(Level p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverWorld, Random random, BlockPos blockPos, BlockState blockState) {
        int range = blockState.getValue(LAYERS)*2 +4;
        serverWorld.removeBlock(blockPos, true);

        AABB box = new AABB(blockPos).inflate(range);
        BlockPos.betweenClosedStream(box)
                .filter(blockPos1 -> shouldAttemptBonemeal(blockPos, blockPos1, range, random, serverWorld))
                .forEach(blockPos1 -> applyBonemeal(serverWorld, random, blockPos1));
    }

    private boolean shouldAttemptBonemeal(BlockPos blockPos, BlockPos blockPos1, int range, Random random, ServerLevel serverWorld) {
        double distSqr = Math.sqrt(blockPos.distSqr(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), false));
        if(random.nextFloat() < (range-distSqr)/range) {
            return serverWorld.getBlockState(blockPos1.below()).is(GRASS_BLOCK) && !serverWorld.getBlockState(blockPos1).isCollisionShapeFullBlock(serverWorld, blockPos1);
        }
        return false;
    }

    private void applyBonemeal(ServerLevel serverLevel, Random random, BlockPos blockpos1) {
        BlockState blockstateToCheck = Blocks.GRASS.defaultBlockState();
        BlockState blockstate2 = serverLevel.getBlockState(blockpos1);
        if (blockstate2.is(blockstateToCheck.getBlock()) && random.nextInt(10) == 0) {
            ((BonemealableBlock) blockstateToCheck.getBlock()).performBonemeal(serverLevel, random, blockpos1, blockstate2);
        }

        if (blockstate2.isAir()) {
            PlacedFeature placedfeature;
            if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = serverLevel.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) {
                    return;
                }

                placedfeature = ((RandomPatchConfiguration)list.get(0).config()).feature().get();
                placedfeature.place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, blockpos1);
            }
        }
    }
}