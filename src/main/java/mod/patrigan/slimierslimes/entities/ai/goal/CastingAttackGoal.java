package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class CastingAttackGoal extends Goal {
    private final AbstractSlimeEntity slimeEntity;
    private LivingEntity attackTarget;
    private int rangedAttackTime = -1;
    private int notSeeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;

    public CastingAttackGoal(AbstractSlimeEntity attacker, int maxAttackTime, float maxAttackDistanceIn) {
        this(attacker,  maxAttackTime, maxAttackTime, maxAttackDistanceIn);
    }

    public CastingAttackGoal(AbstractSlimeEntity attacker, int castingIntervalMin, int maxCastingTime, float maxAttackDistanceIn) {
        this.slimeEntity = attacker;
        this.attackIntervalMin = castingIntervalMin;
        this.maxRangedAttackTime = maxCastingTime;
        this.attackRadius = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.slimeEntity.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            double d0 = this.slimeEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
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
    public boolean canContinueToUse() {
        return this.canUse();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.attackTarget = null;
        this.notSeeTime = 0;
        this.rangedAttackTime = -1;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        double d0 = this.slimeEntity.distanceToSqr(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
        boolean flag = this.slimeEntity.getSensing().canSee(this.attackTarget);
        if (!flag) {
            ++this.notSeeTime;
        } else {
            this.notSeeTime = 0;
        }
        this.slimeEntity.lookAt(this.attackTarget, 10.0F, 10.0F);
        MoveHelperController moveHelper = (MoveHelperController) this.slimeEntity.getMoveControl();
        moveHelper.setDirection(this.slimeEntity.yRot, this.slimeEntity.isDealsDamage());
        moveHelper.setSpeed(0.1D);
        if (--this.rangedAttackTime == 0) {
            if (!flag) {
                return;
            }
            float f = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.rangedAttackTime < 0) {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
        }
    }

}

