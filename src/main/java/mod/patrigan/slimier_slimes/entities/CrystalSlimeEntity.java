package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.goal.*;
import mod.patrigan.slimier_slimes.entities.projectile.AmethystProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalSlimeEntity extends AbstractSlimeEntity {

    public CrystalSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CastingAttackGoal(this, 20, 20.0F));
        this.goalSelector.addGoal(3, new AttackGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    //Slime Size is determined as follows: A random number is determined between 0 and 2. 1 is then bitshift equal to this number, resulting in either 1, 2 or 4.
    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(12D);
    }

    @Override
    protected void land(){
        super.land();
        if (!this.level.isClientSide) {
            fireCrystals();
        }
    }

    // On standard slime sizes: fires 1 / 1 / 4 snowballs
    public void hopCrystals() {
        double d0 = this.getY();
        double d1 = this.getY() + 1.0D;
        for(int i = 0; i < 5; ++i) {
            float f1 = (float)i * (float)Math.PI * 0.4F;
            this.spawnCrystals(this.getX() + (double) Math.cos(f1) * 1.5D, this.getZ() + (double)Math.sin(f1) * 1.5D, d0, d1, f1, 0);
        }
        if(!this.isTiny()){
            for(int k = 0; k < 8; ++k) {
                float f2 = (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                this.spawnCrystals(this.getX() + (double)Math.cos(f2) * 2.5D, this.getZ() + (double)Math.sin(f2) * 2.5D, d0, d1, f2, 3);
            }
        }
    }

    public void fireCrystals() {
        LivingEntity target = this.getTarget();
        if(target != null) {
            double d0 = Math.min(target.getY(), this.getY());
            double d1 = Math.max(target.getY(), this.getY()) + 1.0D;
            float f = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (this.distanceToSqr(target) < 9.0D) {
                hopCrystals();
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    this.spawnCrystals(this.getX() + (double) Math.cos(f) * d2, this.getZ() + (double) Math.sin(f) * d2, d0, d1, f, l);
                }
            }
        }else {
            if (this.random.nextFloat() <= 0.4F) {
                hopCrystals();
            }
        }
    }

    private void spawnCrystals(double posX, double posZ, double yDifference, double posY, float yaw, int warmupDelay) {
        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        boolean flag = false;
        double d0 = 0.0D;
        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level, blockpos1, Direction.UP)) {
                if (!this.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getBlockSupportShape(this.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }
            blockpos = blockpos.below();
        } while(blockpos.getY() >= (int) Math.floor(yDifference) - 1);

        if (flag) {
            this.level.addFreshEntity(new AmethystProjectileEntity(this.level, posX, (double)blockpos.getY() + d0, posZ, yaw, warmupDelay, this));
        }
    }

}
