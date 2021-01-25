package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.CamoSlimeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;

public class HideGoal extends Goal {
    private final CamoSlimeEntity slime;

    public HideGoal(CamoSlimeEntity slime) {
        this.slime = slime;
    }

    @Override
    public boolean shouldExecute() {
        return this.slime.getAttackTarget() == null && (this.slime.isOnGround() || this.slime.isPotionActive(Effects.LEVITATION)) && slime.getActivePotionEffect(Effects.INVISIBILITY) == null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        super.startExecuting();
        slime.hide();
    }

}
