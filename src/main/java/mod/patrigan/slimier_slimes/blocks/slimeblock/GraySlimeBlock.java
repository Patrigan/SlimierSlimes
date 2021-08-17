package mod.patrigan.slimier_slimes.blocks.slimeblock;

import mod.patrigan.slimier_slimes.util.ModBlockColor;
import mod.patrigan.slimier_slimes.util.ModItemColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import javax.annotation.Nullable;

public class GraySlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {
    private static final double BOUNCE_MULTIPLIER = 1.0D;

    public GraySlimeBlock(Properties properties) {
        super(properties);
    }

    /*@Override
    public void bounceEntity(Entity entity) {
        if(SlimierSlimes.SlimeConfig.allowSlimeBlockEffects.get()) {
            Vector3d vector3d = entity.getMotion();
            if (vector3d.y < 0.0D) {
                double d0 = (entity instanceof LivingEntity ? 1.0D : 0.8D)*BOUNCE_MULTIPLIER;
                double vH = Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z);
                double x1 = -vector3d.x / d0;
                double z1 = -vector3d.z / d0;
                entity.setMotion(x1, 0.1, z1);
            }
        }else{
            super.bounceEntity(entity);
        }
    }*/

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.GRAY.getMaterialColor().col : -1;
    }
}
