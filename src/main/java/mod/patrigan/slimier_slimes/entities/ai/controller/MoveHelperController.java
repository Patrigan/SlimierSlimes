package mod.patrigan.slimier_slimes.entities.ai.controller;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class MoveHelperController extends MoveControl {
    private float yRot;
    private int jumpDelay;
    private final AbstractSlimeEntity slime;
    private boolean isAggressive;

    public MoveHelperController(AbstractSlimeEntity slimeIn) {
        super(slimeIn);
        this.slime = slimeIn;
        this.yRot = 180.0F * slimeIn.getYRot() / (float)Math.PI;
    }

    public void setDirection(float yRotIn, boolean aggressive) {
        this.yRot = yRotIn;
        this.isAggressive = aggressive;
    }

    public void setSpeed(double speedIn) {
        this.speedModifier = speedIn;
        this.operation = MoveControl.Operation.MOVE_TO;
    }

    @Override
    public void tick() {
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        if (this.operation != MoveControl.Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = MoveControl.Operation.WAIT;
            if (this.mob.isOnGround()) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.slime.getJumpControl().jump();
                    if (this.slime.doPlayJumpSound()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getVoicePitch());
                    }
                } else {
                    this.slime.xxa = 0.0F;
                    this.slime.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }

        }
    }
}
