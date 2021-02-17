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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PurpleSlimeBlock extends SlimeBlock implements IBlockColor, IItemColor {

    public PurpleSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void bounceEntity(Entity entity) {
        if(SlimierSlimes.SlimeConfig.allowSlimeBlockEffects.get()) {
            Vector3d vector3d = entity.getMotion();
            entity.setMotion(vector3d.x, 0, vector3d.z);
            effectEntity(entity);
        }else{
            super.bounceEntity(entity);
        }
    }

    private void effectEntity(Entity entityIn) {
        if(entityIn instanceof LivingEntity){
            ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20, 3));
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
        return tintIndex == 0 ? DyeColor.PURPLE.getColorValue() : -1;
    }
}
