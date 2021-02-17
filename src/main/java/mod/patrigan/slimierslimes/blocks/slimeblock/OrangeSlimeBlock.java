package mod.patrigan.slimierslimes.blocks.slimeblock;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class OrangeSlimeBlock extends SlimeBlock implements IBlockColor, IItemColor {
    private static final double BOUNCE_MULTIPLIER = 1.0D;

    public OrangeSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void bounceEntity(Entity entity) {
        if(SlimierSlimes.SlimeConfig.allowSlimeBlockEffects.get()) {
            Vector3d vector3d = entity.getMotion();
            if (vector3d.y < 0.0D) {
                double d0 = (entity instanceof LivingEntity ? 1.0D : 0.8D)*BOUNCE_MULTIPLIER;
                double y1 = -vector3d.y* d0;
                double vH = Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z);
                double x1 = -vector3d.x / Math.max(vH, y1);
                double z1 = -vector3d.z / Math.max(vH, y1);
                entity.setMotion(x1, -vector3d.y * d0, z1);
            }
        }else{
            super.bounceEntity(entity);
        }
    }

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.ORANGE.getColorValue() : -1;
    }
}
