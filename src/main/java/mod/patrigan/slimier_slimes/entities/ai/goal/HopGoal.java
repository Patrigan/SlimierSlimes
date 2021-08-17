package mod.patrigan.slimier_slimes.entities.ai.goal;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimier_slimes.entities.ai.controller.MoveHelperController;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HopGoal extends Goal {
    private final AbstractSlimeEntity slime;

    public HopGoal(AbstractSlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return !this.slime.isPassenger();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        ((MoveHelperController)this.slime.getMoveControl()).setSpeed(1.0D);
    }
}