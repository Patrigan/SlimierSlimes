package mod.patrigan.slimier_slimes.blocks;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimier_slimes.util.ColorUtils;
import mod.patrigan.slimier_slimes.util.ModBlockColor;
import mod.patrigan.slimier_slimes.util.ModItemColor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

import static mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity.SLIME_SIZE_KEY;
import static mod.patrigan.slimier_slimes.init.ModEntityTypes.ROCK_SLIME;
import static mod.patrigan.slimier_slimes.init.ModParticleTypes.DRIPPING_SLIME;

public class SlimyStoneBlock extends HalfTransparentBlock implements ModBlockColor, ModItemColor {
    private DyeColor dyeColor;

    private static final float SPAWN_CHANCE = 0.4F;
    public static final BooleanProperty CAN_SPAWN = BooleanProperty.create("can_spawn");

    public SlimyStoneBlock(DyeColor dyeColor) {
        super(Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .friction(0.7F)
                .sound(SoundType.SLIME_BLOCK)
                .requiresCorrectToolForDrops().strength(3.0F, 4.0F)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(CAN_SPAWN, Boolean.TRUE));
        this.dyeColor = dyeColor;
    }

    @Override
    public void fallOn(Level worldIn, BlockState pos, BlockPos pos1, Entity entityIn, float fallDistance) {
        if (entityIn.isSuppressingBounce()) {
            super.fallOn(worldIn, pos, pos1, entityIn, fallDistance);
        } else {
            entityIn.causeFallDamage(fallDistance, 0.5F, DamageSource.FALL);
        }
    }

    private void spawnRockSlime(ServerLevel world, BlockPos pos) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {

            CompoundTag compound = new CompoundTag();
            compound.putInt(SLIME_SIZE_KEY, 1);
            AbstractSlimeEntity rockSlimeEntity = ROCK_SLIME.get().spawn(world, compound, null, null, pos, MobSpawnType.TRIGGERED, false, false);
            if (rockSlimeEntity == null) {
                return;
            }
            rockSlimeEntity.moveTo(rockSlimeEntity.getX(), rockSlimeEntity.getY(), rockSlimeEntity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
            rockSlimeEntity.spawnAnim();
        }
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack) {
        super.spawnAfterBreak(state, worldIn, pos, stack);
        if (Boolean.TRUE.equals(state.getValue(CAN_SPAWN)) && worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 && worldIn.random.nextFloat() <= SPAWN_CHANCE) {
            this.spawnRockSlime(worldIn, pos);
        }

    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        for(int i = 0; i < rand.nextInt(1) + 1; ++i) {
            this.addSlimeParticle(worldIn, pos, stateIn);
        }
    }

    private void addSlimeParticle(Level world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && (world.random.nextFloat() < 0.2F)) {
            VoxelShape voxelshape = state.getBlockSupportShape(world, pos);
            double d0 = voxelshape.max(Direction.Axis.Y);
            if (d0 >= 1.0D && !state.is(BlockTags.IMPERMEABLE)) {
                double d1 = voxelshape.min(Direction.Axis.Y);
                if (d1 > 0.0D) {
                    this.addSlimeParticle(world, pos, voxelshape, (double)pos.getY() + d1 - 0.05D);
                } else {
                    BlockPos blockpos = pos.below();
                    BlockState blockstate = world.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getBlockSupportShape(world, blockpos);
                    double d2 = voxelshape1.max(Direction.Axis.Y);
                    if ((d2 < 1.0D || !blockstate.isCollisionShapeFullBlock(world, blockpos)) && blockstate.getFluidState().isEmpty()) {
                        this.addSlimeParticle(world, pos, voxelshape, (double)pos.getY() - 0.05D);
                    }
                }
            }

        }
    }

    private void addSlimeParticle(Level world, BlockPos pos, VoxelShape shape, double y) {
        this.addSlimeParticle(world, (double)pos.getX() + shape.min(Direction.Axis.X), (double)pos.getX() + shape.max(Direction.Axis.X), (double)pos.getZ() + shape.min(Direction.Axis.Z), (double)pos.getZ() + shape.max(Direction.Axis.Z), y);
    }

    private void addSlimeParticle(Level world, double x1, double x2, double z1, double z2, double y) {
        world.addParticle(DRIPPING_SLIME.get(dyeColor).get(), Mth.lerp(world.random.nextDouble(), x1, x2), y, Mth.lerp(world.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CAN_SPAWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CAN_SPAWN, Boolean.FALSE);
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? ColorUtils.getSlimyBlockColor(dyeColor) : -1;
    }
}
