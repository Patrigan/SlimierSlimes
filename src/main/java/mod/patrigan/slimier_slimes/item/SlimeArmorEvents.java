package mod.patrigan.slimier_slimes.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimier_slimes.init.ModTags.Items.*;
import static net.minecraft.world.entity.EquipmentSlot.CHEST;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = FORGE)
public class SlimeArmorEvents {

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event)
    {
        LivingEntity entity = (LivingEntity) event.getEntity();
        DamageSource source = event.getSource();
        if(entity.getItemBySlot(CHEST).getItem().getTags().contains(ARMOR_SLIME_CHESTPLATE.getName())){
            if(source instanceof EntityDamageSource && !source.isProjectile()){
                Entity sourceEntity = ((EntityDamageSource) source).getEntity();
                int i = 2;
                if (sourceEntity instanceof LivingEntity) {
                    ((LivingEntity)sourceEntity).knockback((float)i * 0.5F, (double) Math.sin(entity.getYRot() * ((float)Math.PI / 180F)), (double)(-Math.cos(entity.getYRot() * ((float)Math.PI / 180F))));
                } else {
                    sourceEntity.push((double)(-Math.sin(entity.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(Math.cos(entity.getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity != null && entity.getItemBySlot(EquipmentSlot.FEET).getItem().getTags().contains(ARMOR_SLIME_BOOTS.getName())) {
            event.setDamageMultiplier(0);
            entity.fallDistance =  0.0F;
            if(!entity.isCrouching() && event.getDistance() > 2) {
                if (entity instanceof Player) {
                    if(entity.level.isClientSide()){
                        bounce(entity);
                    } else {
                        event.setCanceled(true);
                    }
                } else {
                    if(!entity.level.isClientSide()){
                        bounce(entity);
                    } else {
                        event.setCanceled(true);
                    }
                }
                entity.playSound(SoundEvents.SLIME_SQUISH, 1f, 1f);
                SlimeBootsHandler.addSlimeBootsHandler(entity, entity.getDeltaMovement().y);
            }
        }
    }

    private static void bounce(LivingEntity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y * -0.9, entity.getDeltaMovement().z);
        entity.hasImpulse = true;
        entity.setOnGround(false);
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        Player player = event.player;
        if(player.getItemBySlot(EquipmentSlot.LEGS).getItem().getTags().contains(ARMOR_SLIME_LEGGINGS.getName())) {
            float f = player.getEyeHeight() - 0.11111111F;
            if (player.isInWater() && player.getFluidHeight(FluidTags.WATER) > (double) f) {
                setUnderwaterMovement(player);
            }
        }
        if(player.getItemBySlot(EquipmentSlot.HEAD).getItem().getTags().contains(ARMOR_SLIME_HELMET.getName())){
            List<Entity> entities = player.level.getEntities(player, player.getBoundingBox());
            entities.forEach(entity -> {
                if (entity instanceof FallingBlockEntity && entity.getDeltaMovement().y < 0) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, -1.00D, 1.0D));
                }
            });
        }
    }

    private static void setUnderwaterMovement(Player player) {
        Vec3 vector3d = player.getDeltaMovement();
        player.setDeltaMovement(vector3d.x * (double)0.99F, vector3d.y + (double)(vector3d.y < (double)0.06F ? 0.005F : 0.0F), vector3d.z * (double)0.99F);
    }

//TODO:Find a better way to do this. Also needs support for mobs above.
/*    @SubscribeEvent
    public static void giveArmor(EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide() && event.getEntity() instanceof LivingEntity && ! (event.getEntity() instanceof PlayerEntity)) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            replaceArmorItem(event.getWorld(), entity, HEAD, ARMOR_SLIME_HELMET.getRandomElement(event.getWorld().getRandom()));
            replaceArmorItem(event.getWorld(), entity, CHEST, ARMOR_SLIME_CHESTPLATE.getRandomElement(event.getWorld().getRandom()));
            replaceArmorItem(event.getWorld(), entity, LEGS, ARMOR_SLIME_LEGGINGS.getRandomElement(event.getWorld().getRandom()));
            replaceArmorItem(event.getWorld(), entity, FEET, ARMOR_SLIME_BOOTS.getRandomElement(event.getWorld().getRandom()));
        }
    }
    private static void replaceArmorItem(World world, LivingEntity entity, EquipmentSlotType equipmentSlotType, Item item) {
        if (world.random.nextFloat() < 1F) {
            entity.setItemSlot(equipmentSlotType, new ItemStack(item));
        }
    }*/
}
