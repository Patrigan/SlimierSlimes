package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.controller.MoveHelperController;
import mod.patrigan.slimier_slimes.entities.ai.goal.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.Collection;

public class CreeperSlimeEntity extends AbstractSlimeEntity {
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(CreeperSlimeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(CreeperSlimeEntity.class, EntityDataSerializers.BOOLEAN);
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 30;
    private int explosionRadius = 3;

    public CreeperSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperSlimeSwellGoal(this));
        //this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        //this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AttackGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, -1);
        this.entityData.define(IGNITED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public boolean hasIgnited() {
        return this.entityData.get(IGNITED);
    }

    public void ignite() {
        this.entityData.set(IGNITED, true);
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperSlimeState(1);
            }

            int i = this.getCreeperSlimeState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.tick();
    }

    public int getCreeperSlimeState() {
        return this.entityData.get(STATE);
    }

    public void setCreeperSlimeState(int state) {
        this.entityData.set(STATE, state);
    }

    private void explode() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosionMode = Explosion.BlockInteraction.NONE;
            if(this.getSize() > 1 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)){
                explosionMode = Explosion.BlockInteraction.DESTROY;
            }
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius, explosionMode);
            this.setHealth(0F);
            this.remove(false);
            this.spawnLingeringCloud();
        }
    }

    public float getCreeperFlashIntensity(float partialTicks) {
        return Mth.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.explosionRadius = size;
    }

    protected void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

            for(MobEffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
            }

            this.level.addFreshEntity(areaeffectcloudentity);
        }

    }

}