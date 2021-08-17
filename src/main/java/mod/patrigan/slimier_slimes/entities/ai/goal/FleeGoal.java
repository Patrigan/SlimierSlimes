package mod.patrigan.slimier_slimes.entities.ai.goal;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimier_slimes.entities.ai.controller.MoveHelperController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

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
            return (!(livingentity instanceof Player) || !((Player) livingentity).getAbilities().invulnerable) && this.slime.getMoveControl() instanceof MoveHelperController
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
        } else if (livingentity instanceof Player && ((Player)livingentity).getAbilities().invulnerable) {
            return false;
        } else return fleeTimer >= 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.slime.faceAwayFromEntity(this.slime.getTarget(), 10.0F, 10.0F);
        ((MoveHelperController)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true);
        fleeTimer--;
        if(fleeTimer == 0){
            action.run();
        }
    }

    public int getFleeTimer() {
        return fleeTimer;
    }
}