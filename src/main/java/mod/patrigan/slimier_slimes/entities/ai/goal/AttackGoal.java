package mod.patrigan.slimier_slimes.entities.ai.goal;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimier_slimes.entities.ai.controller.MoveHelperController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

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
            return (!(livingentity instanceof Player) || !((Player) livingentity).getAbilities().invulnerable) && this.slime.getMoveControl() instanceof MoveHelperController;
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
        } else if (livingentity instanceof Player && ((Player)livingentity).getAbilities().invulnerable) {
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
        ((MoveHelperController)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
    }
}