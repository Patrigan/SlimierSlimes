package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;

import java.util.EnumSet;

public class FaceRandomGoal  extends Goal {
    private final AbstractSlimeEntity slime;
    private float chosenDegrees;
    private int nextRandomizeTime;

    public FaceRandomGoal(AbstractSlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(Effects.LEVITATION)) && this.slime.getMoveControl() instanceof MoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
            this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
        }

        ((MoveHelperController)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
    }
}