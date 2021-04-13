package mod.patrigan.slimierslimes.item;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static mod.patrigan.slimierslimes.init.ModTags.Items.*;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SlimeArmorEvents {

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event)
    {
        LivingEntity entity = (LivingEntity) event.getEntity();
        DamageSource source = event.getSource();
        if(entity.getItemBySlot(EquipmentSlotType.CHEST).getItem().is(ARMOR_SLIME_CHESTPLATE)){
            if(source instanceof EntityDamageSource && !source.isProjectile()){
                Entity sourceEntity = ((EntityDamageSource) source).getEntity();
                int i = 2;
                if (sourceEntity instanceof LivingEntity) {
                    ((LivingEntity)sourceEntity).knockback((float)i * 0.5F, (double) MathHelper.sin(entity.yRot * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(entity.yRot * ((float)Math.PI / 180F))));
                } else {
                    sourceEntity.push((double)(-MathHelper.sin(entity.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(entity.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event){
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(entity.getItemBySlot(EquipmentSlotType.FEET).getItem().is(ARMOR_SLIME_BOOTS) && !entity.isSuppressingBounce()){
            Vector3d vector3d = entity.getDeltaMovement();
            if (vector3d.y < 0.0D) {
                double d0 =  1.0D;
                entity.setDeltaMovement(vector3d.x, -vector3d.y * d0, vector3d.z);
            }
            entity.fallDistance = 0.0F;
            event.setDamageMultiplier(0);
            //event.setCanceled(true);
        }
    }

    //Implementation as per Tinker's Construct
   /* @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity == null) {
            return;
        }
        if(entity.getItemBySlot(EquipmentSlotType.FEET).getItem().is(ARMOR_SLIME_BOOTS)) {
            // thing is wearing slime boots. let's get bouncyyyyy
            boolean isClientSide = entity.level.isClientSide;
            if (!entity.isCrouching() && event.getDistance() > 2) {
                event.setDamageMultiplier(0);
                entity.fallDistance = 0.0F;

                if (isClientSide) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, -1.00D, 1.0D));
                    //entity.isAirBorne = true;
                    entity.setOnGround(false);
                    double f = 0.91d + 0.04d;
                    // only slow down half as much when bouncing
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D/f, -1.00D, 1.0D/f));
                } else {
                    event.setCanceled(true); // we don't care about previous cancels, since we just bounceeeee
                }

                entity.playSound(SoundEvents.SLIME_SQUISH, 1f, 1f);
                //SlimeBounceHandler.addBounceHandler(entity, entity.getMotion().y);
            } else if (!isClientSide && entity.isCrouching()) {
                event.setDamageMultiplier(0.2f);
            }
        }
    }*/

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if(player.getItemBySlot(EquipmentSlotType.LEGS).getItem().is(ARMOR_SLIME_LEGGINGS)) {
            float f = player.getEyeHeight() - 0.11111111F;
            if (player.isInWater() && player.getFluidHeight(FluidTags.WATER) > (double) f) {
                setUnderwaterMovement(player);
            }
        }
        if(player.getItemBySlot(EquipmentSlotType.HEAD).getItem().is(ARMOR_SLIME_HELMET)){
            List<Entity> entities = player.level.getEntities(player, player.getBoundingBox());
            entities.forEach(entity -> {
                if (entity instanceof FallingBlockEntity && entity.getDeltaMovement().y < 0) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, -1.00D, 1.0D));
                }
            });
        }
    }

    private static void setUnderwaterMovement(PlayerEntity player) {
        Vector3d vector3d = player.getDeltaMovement();
        player.setDeltaMovement(vector3d.x * (double)0.99F, vector3d.y + (double)(vector3d.y < (double)0.06F ? 0.005F : 0.0F), vector3d.z * (double)0.99F);
    }
}
