package mod.patrigan.slimier_slimes.blocks.slimeblock;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.util.ModBlockColor;
import mod.patrigan.slimier_slimes.util.ModItemColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockAndTintGetter;

import javax.annotation.Nullable;

public class BlueSlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {
    private static final double BOUNCE_MULTIPLIER = 0.5D;

    public BlueSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void bounceUp(Entity entity) {
        if(SlimierSlimes.MAIN_CONFIG.allowSlimeBlockEffects.get()) {
            Vec3 vector3d = entity.getDeltaMovement();
            if (vector3d.y < 0.0D) {
                double d0 = (entity instanceof LivingEntity ? 1.0D : 0.8D)*BOUNCE_MULTIPLIER;
                double y1 = -vector3d.y* d0;
                double vH = Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z);
                double x1 = -vector3d.z / Math.max(vH, y1);
                double z1 = vector3d.x / Math.max(vH, y1);
                entity.setDeltaMovement(x1, y1, z1);
            }
        }else{
            super.bounceUp(entity);
        }
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int tintIndex) {
        return getColor(tintIndex);
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return getColor(tintIndex);
    }

    public int getColor(int tintIndex) {
        return tintIndex == 0 ? DyeColor.BLUE.getMaterialColor().col : -1;
    }
}
