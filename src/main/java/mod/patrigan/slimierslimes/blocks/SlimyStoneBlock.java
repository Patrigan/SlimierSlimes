package mod.patrigan.slimierslimes.blocks;

import mod.patrigan.slimierslimes.entities.RockSlimeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.ROCK_SLIME;
import static mod.patrigan.slimierslimes.init.ModParticleTypes.DRIPPING_SLIME;

public class SlimyStoneBlock extends BreakableBlock {

    private static final float SPAWN_CHANCE = 0.4F;

    public SlimyStoneBlock() {
        super(Properties.create(Material.ROCK)
                .setRequiresTool()
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .slipperiness(0.9F)
                .sound(SoundType.SLIME)
                .setRequiresTool().hardnessAndResistance(3.0F, 1.0F)
                .notSolid()
        );
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
            RockSlimeEntity rockSlimeEntity = ROCK_SLIME.get().create(world);
            rockSlimeEntity.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(rockSlimeEntity);
            rockSlimeEntity.spawnExplosionParticle();
        }
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack) {
        super.spawnAdditionalDrops(state, worldIn, pos, stack);
        if (worldIn.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 && worldIn.rand.nextFloat() <= SPAWN_CHANCE) {
            this.spawnRockSlime(worldIn, pos);
        }

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        for(int i = 0; i < rand.nextInt(1) + 1; ++i) {
            this.addSlimeParticle(worldIn, pos, stateIn);
        }
    }

    @OnlyIn(Dist.CLIENT)
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

    @OnlyIn(Dist.CLIENT)
    private void addSlimeParticle(World world, BlockPos pos, VoxelShape shape, double y) {
        this.addSlimeParticle(world, (double)pos.getX() + shape.getStart(Direction.Axis.X), (double)pos.getX() + shape.getEnd(Direction.Axis.X), (double)pos.getZ() + shape.getStart(Direction.Axis.Z), (double)pos.getZ() + shape.getEnd(Direction.Axis.Z), y);
    }

    @OnlyIn(Dist.CLIENT)
    private void addSlimeParticle(World world, double x1, double x2, double z1, double z2, double y) {
        world.addParticle(DRIPPING_SLIME.get(), MathHelper.lerp(world.rand.nextDouble(), x1, x2), y, MathHelper.lerp(world.rand.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }
}
