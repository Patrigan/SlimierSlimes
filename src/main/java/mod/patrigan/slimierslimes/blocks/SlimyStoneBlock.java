package mod.patrigan.slimierslimes.blocks;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.util.ColorUtils;
import mod.patrigan.slimierslimes.util.ModBlockColor;
import mod.patrigan.slimierslimes.util.ModItemColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

import static mod.patrigan.slimierslimes.entities.AbstractSlimeEntity.SLIME_SIZE_KEY;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.ROCK_SLIME;
import static mod.patrigan.slimierslimes.init.ModParticleTypes.DRIPPING_SLIME;

public class SlimyStoneBlock extends BreakableBlock implements ModBlockColor, ModItemColor {
    private DyeColor dyeColor;

    private static final float SPAWN_CHANCE = 0.4F;
    public static final BooleanProperty CAN_SPAWN = BooleanProperty.create("can_spawn");

    public SlimyStoneBlock(DyeColor dyeColor) {
        super(Properties.create(Material.ROCK)
                .setRequiresTool()
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .slipperiness(0.7F)
                .sound(SoundType.SLIME)
                .setRequiresTool().hardnessAndResistance(3.0F, 4.0F)
                .notSolid()
        );
        this.setDefaultState(this.stateContainer.getBaseState().with(CAN_SPAWN, Boolean.TRUE));
        this.dyeColor = dyeColor;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSuppressingBounce()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.onLivingFall(fallDistance, 0.5F);
        }
    }

    private void spawnRockSlime(ServerWorld world, BlockPos pos) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {

            CompoundNBT compound = new CompoundNBT();
            compound.putInt(SLIME_SIZE_KEY, 1);
            AbstractSlimeEntity rockSlimeEntity = ROCK_SLIME.get().spawn(world, compound, null, null, pos, SpawnReason.TRIGGERED, false, false);
            if (rockSlimeEntity == null) {
                return;
            }
            rockSlimeEntity.setLocationAndAngles(rockSlimeEntity.getPosX(), rockSlimeEntity.getPosY(), rockSlimeEntity.getPosZ(), world.rand.nextFloat() * 360.0F, 0.0F);
            rockSlimeEntity.spawnExplosionParticle();
        }
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack) {
        super.spawnAdditionalDrops(state, worldIn, pos, stack);
        if (Boolean.TRUE.equals(state.get(CAN_SPAWN)) && worldIn.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 && worldIn.rand.nextFloat() <= SPAWN_CHANCE) {
            this.spawnRockSlime(worldIn, pos);
        }

    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        for(int i = 0; i < rand.nextInt(1) + 1; ++i) {
            this.addSlimeParticle(worldIn, pos, stateIn);
        }
    }

    private void addSlimeParticle(World world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && (world.rand.nextFloat() < 0.2F)) {
            VoxelShape voxelshape = state.getCollisionShape(world, pos);
            double d0 = voxelshape.getEnd(Direction.Axis.Y);
            if (d0 >= 1.0D && !state.isIn(BlockTags.IMPERMEABLE)) {
                double d1 = voxelshape.getStart(Direction.Axis.Y);
                if (d1 > 0.0D) {
                    this.addSlimeParticle(world, pos, voxelshape, (double)pos.getY() + d1 - 0.05D);
                } else {
                    BlockPos blockpos = pos.down();
                    BlockState blockstate = world.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(world, blockpos);
                    double d2 = voxelshape1.getEnd(Direction.Axis.Y);
                    if ((d2 < 1.0D || !blockstate.hasOpaqueCollisionShape(world, blockpos)) && blockstate.getFluidState().isEmpty()) {
                        this.addSlimeParticle(world, pos, voxelshape, (double)pos.getY() - 0.05D);
                    }
                }
            }

        }
    }

    private void addSlimeParticle(World world, BlockPos pos, VoxelShape shape, double y) {
        this.addSlimeParticle(world, (double)pos.getX() + shape.getStart(Direction.Axis.X), (double)pos.getX() + shape.getEnd(Direction.Axis.X), (double)pos.getZ() + shape.getStart(Direction.Axis.Z), (double)pos.getZ() + shape.getEnd(Direction.Axis.Z), y);
    }

    private void addSlimeParticle(World world, double x1, double x2, double z1, double z2, double y) {
        world.addParticle(DRIPPING_SLIME.get(dyeColor).get(), MathHelper.lerp(world.rand.nextDouble(), x1, x2), y, MathHelper.lerp(world.rand.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CAN_SPAWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(CAN_SPAWN, Boolean.FALSE);
    }

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
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
