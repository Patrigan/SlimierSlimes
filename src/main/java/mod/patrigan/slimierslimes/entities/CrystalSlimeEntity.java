package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.*;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import static net.minecraft.particles.ParticleTypes.ITEM;

public class CrystalSlimeEntity extends AbstractSlimeEntity {

    public CrystalSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CastingAttackGoal(this, 20, 20.0F));
        this.goalSelector.addGoal(3, new AttackGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getPosY() - this.getPosY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    //Slime Size is determined as follows: A random number is determined between 0 and 2. 1 is then bitshift equal to this number, resulting in either 1, 2 or 4.
    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(12D);
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new ItemParticleData(ITEM, new ItemStack(ModItems.JELLY.get(DyeColor.MAGENTA).get()));
    }

    @Override
    protected void land(){
        super.land();
        if (!this.world.isRemote) {
            fireCrystals();
        }
    }

    // On standard slime sizes: fires 1 / 1 / 4 snowballs
    public void hopCrystals() {
        double d0 = this.getPosY();
        double d1 = this.getPosY() + 1.0D;
        for(int i = 0; i < 5; ++i) {
            float f1 = (float)i * (float)Math.PI * 0.4F;
            this.spawnCrystals(this.getPosX() + (double) MathHelper.cos(f1) * 1.5D, this.getPosZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
        }
        if(!this.isSmallSlime()){
            for(int k = 0; k < 8; ++k) {
                float f2 = (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                this.spawnCrystals(this.getPosX() + (double)MathHelper.cos(f2) * 2.5D, this.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
            }
        }
    }

    public void fireCrystals() {
        LivingEntity target = this.getAttackTarget();
        if(target != null) {
            double d0 = Math.min(target.getPosY(), this.getPosY());
            double d1 = Math.max(target.getPosY(), this.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(target.getPosZ() - this.getPosZ(), target.getPosX() - this.getPosX());
            if (this.getDistanceSq(target) < 9.0D) {
                hopCrystals();
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    this.spawnCrystals(this.getPosX() + (double) MathHelper.cos(f) * d2, this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, l);
                }
            }
        }else {
            if (this.rand.nextFloat() <= 0.4F) {
                hopCrystals();
            }
        }
    }

    private void spawnCrystals(double posX, double posZ, double yDifference, double posY, float yaw, int warmupDelay) {
        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        boolean flag = false;
        double d0 = 0.0D;
        do {
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate = this.world.getBlockState(blockpos1);
            if (blockstate.isSolidSide(this.world, blockpos1, Direction.UP)) {
                if (!this.world.isAirBlock(blockpos)) {
                    BlockState blockstate1 = this.world.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }
            blockpos = blockpos.down();
        } while(blockpos.getY() >= MathHelper.floor(yDifference) - 1);

        if (flag) {
            this.world.addEntity(new AmethystProjectileEntity(this.world, posX, (double)blockpos.getY() + d0, posZ, yaw, warmupDelay, this));
        }
    }

}
