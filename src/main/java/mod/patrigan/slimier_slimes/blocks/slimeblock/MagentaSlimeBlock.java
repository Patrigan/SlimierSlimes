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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockAndTintGetter;

import javax.annotation.Nullable;

import static java.lang.Boolean.TRUE;

public class MagentaSlimeBlock extends SlimeBlock implements ModBlockColor, ModItemColor {

    public MagentaSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void bounceUp(Entity entity) {
        if(TRUE.equals(SlimierSlimes.MAIN_CONFIG.allowSlimeBlockEffects.get())) {
            Vec3 vector3d = entity.getDeltaMovement();
            entity.setDeltaMovement(vector3d.x, 0, vector3d.z);
            effectEntity(entity);
        }else{
            super.bounceUp(entity);
        }
    }

    private void effectEntity(Entity entityIn) {
        if(entityIn instanceof LivingEntity){
            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
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
        return tintIndex == 0 ? DyeColor.MAGENTA.getMaterialColor().col : -1;
    }
}
