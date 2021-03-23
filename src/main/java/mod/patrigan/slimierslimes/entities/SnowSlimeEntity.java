package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.projectile.SlimeSnowballEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SnowSlimeEntity extends AbstractSlimeEntity {

    public SnowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void land(){
        super.land();
        if (!this.level.isClientSide) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY());
            int k = MathHelper.floor(this.getZ());
            if (this.level.getBiome(new BlockPos(i, 0, k)).getTemperature(new BlockPos(i, j, k)) > 1.0F) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }
            fireSnowballs();

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = Blocks.SNOW.defaultBlockState();

            for(int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor(this.getY());
                k = MathHelper.floor(this.getZ() + (double) ((l / 2F % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos) && this.level.getBiome(blockpos).getTemperature(blockpos) < 0.8F && blockstate.canSurvive(this.level, blockpos)) {
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
            float rotationPitch = this.xRot - 45F;
            float rotationYaw = this.yRot + (360F/amount)*(x-1);
            float velocity = 0.3F + getSize() * 0.1F;
            snowballentity.shootFromRotation(this, rotationPitch, rotationYaw, 0.0F, velocity , 5.0F);
            this.level.addFreshEntity(snowballentity);
        }
    }

}