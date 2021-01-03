package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;

public class FleeGoal extends Goal {
    private final AbstractSlimeEntity slime;

    private int fleeTimer;
    private int baseFleeTime;
    private BooleanSupplier predicate;
    private Runnable action;

    public FleeGoal(AbstractSlimeEntity slimeIn, int fleeTime, BooleanSupplier predicate, Runnable action) {
        this.slime = slimeIn;
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
        this.baseFleeTime = fleeTime;
        this.action = action;
        this.predicate = predicate;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        LivingEntity livingentity = this.slime.getAttackTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            return (!(livingentity instanceof PlayerEntity) || !((PlayerEntity) livingentity).abilities.disableDamage) && this.slime.getMoveHelper() instanceof MoveHelperController
                    && predicate.getAsBoolean();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.fleeTimer = baseFleeTime;
        super.startExecuting();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        LivingEntity livingentity = this.slime.getAttackTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.disableDamage) {
            return false;
        } else return fleeTimer >= 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.slime.faceAwayFromEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
        ((MoveHelperController)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, true);
        fleeTimer--;
        if(fleeTimer == 0){
            action.run();
        }
    }

    public int getFleeTimer() {
        return fleeTimer;
    }
}