package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.projectile.SlimeSnowballEntity;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static net.minecraft.particles.ParticleTypes.ITEM;

public class SnowSlimeEntity extends AbstractSlimeEntity {

    public SnowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new ItemParticleData(ITEM, new ItemStack(ModItems.JELLY.get(DyeColor.WHITE).get()));
    }

    @Override
    protected void land(){
        super.land();
        if (!this.world.isRemote) {
            int i = MathHelper.floor(this.getPosX());
            int j = MathHelper.floor(this.getPosY());
            int k = MathHelper.floor(this.getPosZ());
            if (this.world.getBiome(new BlockPos(i, 0, k)).getTemperature(new BlockPos(i, j, k)) > 1.0F) {
                this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }
            fireSnowballs();

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                return;
            }

            BlockState blockstate = Blocks.SNOW.getDefaultState();

            for(int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.getPosX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor(this.getPosY());
                k = MathHelper.floor(this.getPosZ() + (double) ((l / 2F % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                if (this.world.isAirBlock(blockpos) && this.world.getBiome(blockpos).getTemperature(blockpos) < 0.8F && blockstate.isValidPosition(this.world, blockpos)) {
                    this.world.setBlockState(blockpos, blockstate);
                }
            }
        }
    }

    // On standard slime sizes: fires 1 / 4 / 8 snowballs
    public void fireSnowballs() {
        int amount = this.isSmallSlime() ? 1 : (getSlimeSize() * 2);
        for(int x = 1; x <= amount; x++) {
            SlimeSnowballEntity snowballentity = new SlimeSnowballEntity(this.world, this);
            float rotationPitch = this.rotationPitch - 45F;
            float rotationYaw = this.rotationYaw + (360F/amount)*(x-1);
            float velocity = 0.3F + getSlimeSize() * 0.1F;
            snowballentity.func_234612_a_(this, rotationPitch, rotationYaw, 0.0F, velocity , 5.0F);
            this.world.addEntity(snowballentity);
        }
    }

}