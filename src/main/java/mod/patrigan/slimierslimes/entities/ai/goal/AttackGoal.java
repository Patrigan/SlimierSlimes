package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class AttackGoal  extends Goal {
    private final AbstractSlimeEntity slime;
    private int growTiredTimer;

    public AttackGoal(AbstractSlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
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
            return (!(livingentity instanceof PlayerEntity) || !((PlayerEntity) livingentity).abilities.invulnerable) && this.slime.getMoveControl() instanceof MoveHelperController;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.growTiredTimer = 300;
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
        } else {
            return --this.growTiredTimer > 0;
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.slime.lookAt(this.slime.getTarget(), 10.0F, 10.0F);
        ((MoveHelperController)this.slime.getMoveControl()).setDirection(this.slime.yRot, this.slime.isDealsDamage());
    }
}