package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FloatGoal  extends Goal {
    private final AbstractSlimeEntity slime;

    public FloatGoal(AbstractSlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        slimeIn.getNavigator().setCanSwim(true);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveHelper() instanceof MoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        if (this.slime.getRNG().nextFloat() < 0.8F) {
            this.slime.getJumpController().setJumping();
        }

        ((MoveHelperController)this.slime.getMoveHelper()).setSpeed(1.2D);
    }
}