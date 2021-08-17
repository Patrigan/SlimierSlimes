package mod.patrigan.slimier_slimes.entities.ai.goal;

import mod.patrigan.slimier_slimes.entities.CamoSlimeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.effect.MobEffects;

public class HideGoal extends Goal {
    private final CamoSlimeEntity slime;

    public HideGoal(CamoSlimeEntity slime) {
        this.slime = slime;
    }

    @Override
    public boolean canUse() {
        return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.hasEffect(MobEffects.LEVITATION)) && slime.getEffect(MobEffects.INVISIBILITY) == null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        super.start();
        slime.hide();
    }

}
