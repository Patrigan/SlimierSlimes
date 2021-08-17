package mod.patrigan.slimier_slimes.entities.ai.goal;

import mod.patrigan.slimier_slimes.entities.CreeperSlimeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CreeperSlimeSwellGoal extends Goal {
    private final CreeperSlimeEntity swellingCreeper;
    private LivingEntity creeperAttackTarget;

    public CreeperSlimeSwellGoal(CreeperSlimeEntity entitycreeperIn) {
        this.swellingCreeper = entitycreeperIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.swellingCreeper.getTarget();
        return this.swellingCreeper.getCreeperSlimeState() > 0 || livingentity != null && this.swellingCreeper.distanceToSqr(livingentity) < 9.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.swellingCreeper.getNavigation().stop();
        this.creeperAttackTarget = this.swellingCreeper.getTarget();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.creeperAttackTarget = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        if (this.creeperAttackTarget == null) {
            this.swellingCreeper.setCreeperSlimeState(-1);
        } else if (this.swellingCreeper.distanceToSqr(this.creeperAttackTarget) > 49.0D) {
            this.swellingCreeper.setCreeperSlimeState(-1);
        } else if (!this.swellingCreeper.getSensing().hasLineOfSight(this.creeperAttackTarget)) {
            this.swellingCreeper.setCreeperSlimeState(-1);
        } else {
            this.swellingCreeper.setCreeperSlimeState(1);
        }
    }
}
