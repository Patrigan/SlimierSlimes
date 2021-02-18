package mod.patrigan.slimierslimes.blocks.slimeblock;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import mod.patrigan.slimierslimes.util.ModBlockColor;
import mod.patrigan.slimierslimes.util.ModItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

import static java.lang.Boolean.TRUE;

public class LightGraySlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {
    private static final double BOUNCE_MULTIPLIER = 1.0D;

    public LightGraySlimeBlock(Properties properties) {
        super(properties);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    /*@Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.onLanded(worldIn, entityIn);
        } else {
            this.bounceEntity(worldIn, entityIn);
        }

    }

    public void bounceEntity(IBlockReader worldIn, Entity entity) {
        if(TRUE.equals(SlimierSlimes.SlimeConfig.allowSlimeBlockEffects.get())) {
            if(entity instanceof PlayerEntity){
                if(worldIn instanceof World && ((World) worldIn).isRemote){
                    propelEntity(entity);
                }
            }else{
                if(worldIn instanceof World && !((World) worldIn).isRemote){
                    propelEntity(entity);
                }
            }
        }else{
            super.bounceEntity(entity);
        }
    }

    private void propelEntity(Entity entity) {
        Vector3d vector3d = entity.getMotion();
        LOGGER.log(Level.INFO, "Start: " + vector3d.toString());
        if (vector3d.y < 0.0D) {
            double d0 = (entity instanceof LivingEntity ? 1.0D : 0.8D)*BOUNCE_MULTIPLIER;
            double y1 = -vector3d.y* d0;
            double vH = Math.sqrt(Entity.horizontalMag(vector3d));
            if (vH > 0.01D) {
                entity.setMotion( (vector3d.x  / vH * (y1+vH))*3, 0.07D,  (vector3d.z / vH * (y1+vH))*3);
            } else {
                Vector3d vector3d2 = entity.getLookVec();
                double vH2 = Math.sqrt(Entity.horizontalMag(vector3d2));
                entity.setMotion((vector3d2.x / vH2 * (y1+0.2D))*3, 0.07D, (vector3d2.z / vH2 * (y1+0.2D))*3);
            }
            //double v = Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z + vector3d.y*vector3d.y);
            //Vector3d normalized = vector3d.normalize();
            //if(vector3d.x == 0 && vector3d.z == 0){
            //    normalized = entity.getLookVec();
            //}
            //entity.setMotion(normalized.x * v, 0, normalized.z * v);
        }
        LOGGER.log(Level.INFO, "Adjusted: " + entity.getMotion().toString());
    }*/

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.LIGHT_GRAY.getColorValue() : -1;
    }
}
