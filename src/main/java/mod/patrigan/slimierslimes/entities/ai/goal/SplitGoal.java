package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.function.BooleanSupplier;

public class SplitGoal  extends Goal {
    AbstractSlimeEntity slime;
    BooleanSupplier predicate;

    public SplitGoal(AbstractSlimeEntity slimeIn, BooleanSupplier predicate) {
        this.slime = slimeIn;
        this.predicate = predicate;
    }

    @Override
    public boolean shouldExecute() {
        return predicate.getAsBoolean();
    }
    @Override
    public void startExecuting() {
        super.startExecuting();
        slime.setHealth(0F);
        slime.remove(false);
    }


}
