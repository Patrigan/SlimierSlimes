package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class FleeGoal extends Goal {
    private final AbstractSlimeEntity slime;

    private int fleeTimer;
    private int baseFleeTime;
    private BooleanSupplier predicate;
    private Runnable action;

    public FleeGoal(AbstractSlimeEntity slimeIn, int fleeTime, BooleanSupplier predicate, Runnable action) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Flag.LOOK));
        this.baseFleeTime = fleeTime;
        this.action = action;
        this.predicate = predicate;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.slime.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            return (!(livingentity instanceof PlayerEntity) || !((PlayerEntity) livingentity).abilities.invulnerable) && this.slime.getMoveControl() instanceof MoveHelperController
                    && predicate.getAsBoolean();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.fleeTimer = baseFleeTime;
        super.start();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.slime.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.invulnerable) {
            return false;
        } else return fleeTimer >= 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.slime.faceAwayFromEntity(this.slime.getTarget(), 10.0F, 10.0F);
        ((MoveHelperController)this.slime.getMoveControl()).setDirection(this.slime.yRot, true);
        fleeTimer--;
        if(fleeTimer == 0){
            action.run();
        }
    }

    public int getFleeTimer() {
        return fleeTimer;
    }
}