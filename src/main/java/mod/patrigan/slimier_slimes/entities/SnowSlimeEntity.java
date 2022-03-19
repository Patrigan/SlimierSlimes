package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.projectile.SlimeSnowballEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;

public class SnowSlimeEntity extends AbstractSlimeEntity {

    public SnowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void land(){
        super.land();
        if (!this.level.isClientSide) {
            int i = (int) Math.floor(this.getX());
            int j = (int) Math.floor(this.getY());
            int k = (int) Math.floor(this.getZ());
            if (this.level.getBiome(new BlockPos(i, j, k)).shouldSnowGolemBurn(new BlockPos(i, j, k))) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }
            fireSnowballs();

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = Blocks.SNOW.defaultBlockState();

            for(int l = 0; l < 4; ++l) {
                i = (int) Math.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = (int) Math.floor(this.getY());
                k = (int) Math.floor(this.getZ() + (double) ((l / 2F % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, blockstate);
                }
            }
        }
    }

    // On standard slime sizes: fires 1 / 4 / 8 snowballs
    public void fireSnowballs() {
        int amount = this.isTiny() ? 1 : (getSize() * 2);
        for(int x = 1; x <= amount; x++) {
            SlimeSnowballEntity snowballentity = new SlimeSnowballEntity(this.level, this);
            float rotationPitch = this.getXRot() - 45F;
            float rotationYaw = this.getYRot() + (360F/amount)*(x-1);
            float velocity = 0.3F + getSize() * 0.1F;
            snowballentity.shootFromRotation(this, rotationPitch, rotationYaw, 0.0F, velocity , 5.0F);
            this.level.addFreshEntity(snowballentity);
        }
    }

}