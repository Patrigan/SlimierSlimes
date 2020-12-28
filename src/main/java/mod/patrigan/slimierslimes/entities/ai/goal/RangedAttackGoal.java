package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class RangedAttackGoal extends Goal {
    private final AbstractSlimeEntity slimeEntity;
    private final IRangedAttackMob rangedAttackEntityHost;
    private LivingEntity attackTarget;
    private int rangedAttackTime = -1;
    private int notSeeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;

    public RangedAttackGoal(IRangedAttackMob attacker, int maxAttackTime, float maxAttackDistanceIn) {
        this(attacker,  maxAttackTime, maxAttackTime, maxAttackDistanceIn);
    }

    public RangedAttackGoal(IRangedAttackMob attacker, int attackIntervalMin, int maxAttackTime, float maxAttackDistanceIn) {
        if (!(attacker instanceof LivingEntity)) {
            throw new IllegalArgumentException("Ranged AttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackEntityHost = attacker;
            this.slimeEntity = (AbstractSlimeEntity) attacker;
            this.attackIntervalMin = attackIntervalMin;
            this.maxRangedAttackTime = maxAttackTime;
            this.attackRadius = maxAttackDistanceIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        LivingEntity livingentity = this.slimeEntity.getAttackTarget();
        if (livingentity != null && livingentity.isAlive()) {
            double d0 = this.slimeEntity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            if(d0 <= (double)this.maxAttackDistance && this.notSeeTime < 5) {
                this.attackTarget = livingentity;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.notSeeTime = 0;
        this.rangedAttackTime = -1;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        double d0 = this.slimeEntity.getDistanceSq(this.attackTarget.getPosX(), this.attackTarget.getPosY(), this.attackTarget.getPosZ());
        boolean flag = this.slimeEntity.getEntitySenses().canSee(this.attackTarget);
        if (!flag) {
            ++this.notSeeTime;
        } else {
            this.notSeeTime = 0;
        }
        this.slimeEntity.faceEntity(this.attackTarget, 10.0F, 10.0F);
        MoveHelperController moveHelper = (MoveHelperController) this.slimeEntity.getMoveHelper();
        moveHelper.setDirection(this.slimeEntity.rotationYaw, this.slimeEntity.canDamagePlayer());
        if(!isFacingTarget()){
            return;
        }
        if (--this.rangedAttackTime == 0) {
            if (!flag) {
                return;
            }

            float f = MathHelper.sqrt(d0) / this.attackRadius;
            float distanceFactor = MathHelper.clamp(f, 0.1F, 1.0F);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, distanceFactor);
            this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.rangedAttackTime < 0) {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
        }

    }

    private boolean isFacingTarget() {
        double d0 = this.attackTarget.getPosX() - this.slimeEntity.getPosX();
        double d2 = this.attackTarget.getPosZ() - this.slimeEntity.getPosZ();
        float f = MathHelper.wrapDegrees((float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
        if (f<0F){
            f = f+360F;
        }
        return this.slimeEntity.renderYawOffset -5F < f && f < this.slimeEntity.renderYawOffset + 5F;
    }

}

