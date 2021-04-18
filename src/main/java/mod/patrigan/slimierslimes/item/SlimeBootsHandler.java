package mod.patrigan.slimierslimes.item;



import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.IdentityHashMap;

public class SlimeBootsHandler {

    private static final IdentityHashMap<Entity, SlimeBootsHandler> bouncingEntities = new IdentityHashMap<>();

    public final LivingEntity livingEntity;
    private int timer;
    private boolean wasInAir;
    private double bounce;
    private int bounceTick;

    private double lastMovX;
    private double lastMovZ;

    public SlimeBootsHandler(LivingEntity livingEntity, double bounce) {
        this.livingEntity = livingEntity;
        this.timer = 0;
        this.wasInAir = false;
        this.bounce = bounce;

        if (bounce != 0) {
            this.bounceTick = livingEntity.tickCount;
        } else {
            this.bounceTick = 0;
        }
        bouncingEntities.put(livingEntity, this);
    }

    @SubscribeEvent
    public void playerTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player == this.livingEntity){
            bounce();
        }
    }

    @SubscribeEvent
    public void worldTickEnd(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !(livingEntity instanceof PlayerEntity)){
            bounce();
        }
    }

    private void bounce() {
        if(!livingEntity.isFallFlying()) {
            if (livingEntity.tickCount == this.bounceTick) {
                Vector3d vec3d = livingEntity.getDeltaMovement();
                livingEntity.setDeltaMovement(vec3d.x, this.bounce, vec3d.z);
                this.bounceTick = 0;
            }

            bounceMotion();

            endBounce();
        }
    }

    private void bounceMotion() {
        if (!this.livingEntity.isOnGround() && this.livingEntity.tickCount != this.bounceTick && (this.lastMovX != this.livingEntity.getDeltaMovement().x || this.lastMovZ != this.livingEntity.getDeltaMovement().z)){
            double f = 0.935d;
            Vector3d vec3d = this.livingEntity.getDeltaMovement();
            livingEntity.setDeltaMovement(vec3d.x / f, vec3d.y, vec3d.z / f);
            this.livingEntity.hasImpulse = true;
            this.lastMovX = this.livingEntity.getDeltaMovement().x;
            this.lastMovZ = this.livingEntity.getDeltaMovement().z;
        }
    }

    private void endBounce() {
        if (this.wasInAir && this.livingEntity.isOnGround()) {
            if (this.timer == 0) {
                this.timer = this.livingEntity.tickCount;
            } else if (this.livingEntity.tickCount - this.timer > 5) {
                MinecraftForge.EVENT_BUS.unregister(this);
                bouncingEntities.remove(this.livingEntity);
            }
        } else {
            this.timer = 0;
            this.wasInAir = true;
        }
    }


    public static void addSlimeBootsHandler(LivingEntity entity, double bounce) {
        SlimeBootsHandler handler = bouncingEntities.get(entity);
        if (handler == null) {
            // wasn't bouncing yet, register it
            MinecraftForge.EVENT_BUS.register(new SlimeBootsHandler(entity, bounce));
        } else if (bounce != 0) {
            // updated bounce if needed
            handler.bounce = bounce;
            handler.bounceTick = entity.tickCount;
        }
    }
}