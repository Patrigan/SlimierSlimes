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
    public boolean canUse() {
        return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.hasEffect(Effects.LEVITATION)) && slime.getEffect(Effects.INVISIBILITY) == null;
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
