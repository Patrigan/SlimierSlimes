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
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        if (this.slime.getAttackTarget() == null && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.isPotionActive(Effects.LEVITATION)) && this.slime.getMoveHelper() instanceof MoveHelperController) {
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
            double d0 = first.get().getX() - this.slime.getPosX();
            double d2 = first.get().getZ() - this.slime.getPosZ();
            chosenDegrees = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
        }else {
            chosenDegrees = (float) this.slime.getRNG().nextInt(360);
        }
        ((MoveHelperController) this.slime.getMoveHelper()).setDirection(chosenDegrees, false);
    }

    private Stream<BlockPos> getClosestLavaBlocks(){
        return BlockPos.getProximitySortedBoxPositions(this.slime.getPosition(), 20, 2, 20).filter(blockPos -> this.slime.getEntityWorld().getBlockState(blockPos).getBlock().equals(Blocks.LAVA));
    }
}