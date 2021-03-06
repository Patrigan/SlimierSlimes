package mod.patrigan.slimierslimes.entities.ai.goal;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public class FaceLavaGoal extends Goal {
    private final AbstractSlimeEntity slime;

    public FaceLavaGoal(AbstractSlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.hasEffect(Effects.LEVITATION)) && this.slime.getMoveControl() instanceof MoveHelperController) {
            return getClosestLavaBlocks().count() > 0;
        }
        return false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        float chosenDegrees;
        Optional<BlockPos> first = getClosestLavaBlocks().findFirst();
        if(first.isPresent()){
            double d0 = first.get().getX() - this.slime.getX();
            double d2 = first.get().getZ() - this.slime.getZ();
            chosenDegrees = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
        }else {
            chosenDegrees = (float) this.slime.getRandom().nextInt(360);
        }
        ((MoveHelperController) this.slime.getMoveControl()).setDirection(chosenDegrees, false);
    }

    private Stream<BlockPos> getClosestLavaBlocks(){
        return BlockPos.withinManhattanStream(this.slime.blockPosition(), 20, 2, 20).filter(blockPos -> this.slime.getCommandSenderWorld().getBlockState(blockPos).getBlock().equals(Blocks.LAVA));
    }
}