package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.blocks.GooLayerBlock;
import mod.patrigan.slimier_slimes.entities.ai.controller.MoveHelperController;
import mod.patrigan.slimier_slimes.entities.ai.goal.AttackGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.HopGoal;
import mod.patrigan.slimier_slimes.init.data.SlimeData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimier_slimes.init.data.SlimeDatas.SLIME_DATA;
import static mod.patrigan.slimier_slimes.init.data.SquishParticleData.SquishParticleType.*;
import static net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE;
import static net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = FORGE)
public class AbstractSlimeEntity extends Mob implements Enemy {

    private static final float SWARM_CHANCE = 0.05F;

    private static final EntityDataAccessor<Integer> SLIME_SIZE = SynchedEntityData.defineId(AbstractSlimeEntity.class, EntityDataSerializers.INT);
    public static final String SLIME_SIZE_KEY = "Size";

    private final SlimeData data;

    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public AbstractSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
        data = SLIME_DATA.getData(this.getType().getRegistryName());
        this.moveControl = new MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLIME_SIZE, 1);
    }

    //Slime Size is determined as follows: A random number is determined between 0 and 2. 1 is then bitshift equal to this number, resulting in either 1, 2 or 4.
    public void setSize(int size, boolean resetHealth) {
        this.entityData.set(SLIME_SIZE, size);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(data.getMaxHealth(size, this.random));
        this.getAttribute(Attributes.ARMOR).setBaseValue(data.getArmor(size, this.random));
        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(data.getArmorToughness(size, this.random));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(data.getMovementSpeed(size, this.random));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(data.getAttackDamage(size, this.random));
        this.getAttribute(ENTITY_GRAVITY.get()).setBaseValue(data.getEntityGravity());
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }

        this.xpReward = data.getExperienceValue(size, this.random);
    }

    public int getSize() {
        return this.entityData.get(SLIME_SIZE);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Size", this.getSize() - 1);
        compound.putBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        int i = compound.getInt(SLIME_SIZE_KEY);
        if (i < 0) {
            i = 0;
        }

        this.setSize(i + 1, false);
        super.readAdditionalSaveData(compound);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isTiny() {
        return this.getSize() <= 1;
    }

    protected ParticleOptions getParticleType() {
        if(data != null) {
            if (ITEM.equals(data.getSquishParticleData().getType())) {
                return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ForgeRegistries.ITEMS.getValue(data.getSquishParticleData().getResourceLocation())));
            } else if (BLOCK.equals(data.getSquishParticleData().getType())) {
                return new BlockParticleOption(ParticleTypes.BLOCK, ForgeRegistries.BLOCKS.getValue(data.getSquishParticleData().getResourceLocation()).defaultBlockState());
            } else if (PARTICLE.equals(data.getSquishParticleData().getType())) {
                return (SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(data.getSquishParticleData().getResourceLocation());
            }
        }
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getSize() > 0;
    }

    @Override
    public void tick() {
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            land();
        } else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }

    protected void land() {
        if (!this.level.isClientSide) {
            int i = this.getSize();

            if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
                this.level.addParticle(this.getParticleType(), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), getVoicePitch() / 0.8F);
            this.squishAmount = -0.5F;
        }
    }

    protected void decreaseSquish() {
        this.squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    public int getJumpDelay() {
        return (int) data.getJumpDelay(this.getSize(), this.random);
    }

    @Override
    protected float getJumpPower() {
        return super.getJumpPower() * data.getJumpHeightMultiplier(this.getSize(), this.random);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SLIME_SIZE.equals(key)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityType<? extends AbstractSlimeEntity> getType() {
        return (EntityType<? extends AbstractSlimeEntity>) super.getType();
    }

    @Override
    public void remove(boolean keepData) {
        int i = this.getSize();
        if (!this.level.isClientSide && i > 1 && this.isDeadOrDying() && !this.isRemoved()) {
            Component itextcomponent = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float) i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2D) - 0.5F) * f;
                AbstractSlimeEntity slimeentity = this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    slimeentity.setPersistenceRequired();
                }

                slimeentity.setCustomName(itextcomponent);
                slimeentity.setNoAi(flag);
                slimeentity.setInvulnerable(this.isInvulnerable());
                slimeentity.setSize(j, true);
                slimeentity.moveTo(this.getX() + (double) f1, this.getY() + 0.5D, this.getZ() + (double) f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(slimeentity);
            }
        }
        super.remove(keepData);
    }

    protected void spawnSwarm(){
        if (!this.level.isClientSide) {
            Component itextcomponent = this.getCustomName();
            int i = 1;
            int k = 6 + this.random.nextInt(5);
            float f = (float) i / 4.0F;

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2D) - 0.5F) * f;
                AbstractSlimeEntity slimeentity = this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    slimeentity.setPersistenceRequired();
                }

                slimeentity.setCustomName(itextcomponent);
                slimeentity.setNoAi(this.isNoAi());
                slimeentity.setInvulnerable(this.isInvulnerable());
                slimeentity.setSize(i, true);
                slimeentity.moveTo(this.getX() + (double) f1, this.getY() + 0.5D, this.getZ() + (double) f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(slimeentity);
            }
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    @Override
    public void push(Entity entityIn) {
        super.push(entityIn);
        if (entityIn instanceof IronGolem && this.isDealsDamage()) {
            this.dealDamage((LivingEntity) entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void playerTouch(Player entityIn) {
        if (this.isDealsDamage()) {
            this.dealDamage(entityIn);
        }
    }

    protected boolean dealDamage(LivingEntity entityIn) {
        if (this.isAlive()) {
            int i = this.getSize();
            if (this.distanceToSqr(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.hasLineOfSight(entityIn) && entityIn.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, getVoicePitch());
                this.doEnchantDamageEffects(this, entityIn);
                return true;
            }
        }
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.625F * sizeIn.height;
    }

    public boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isTiny() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isTiny() ? this.getType().getDefaultLootTable() : BuiltInLootTables.EMPTY;
    }

    public static boolean checkSlimeSpawnRules(EntityType<? extends AbstractSlimeEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random randomIn) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (canSpawnInSwamp(entityType, world, reason, pos, randomIn)) {
                return true;
            }
            boolean result = true;
            if(Boolean.TRUE.equals(SlimierSlimes.MAIN_CONFIG.maintainChunkSpawning.get())) {
                result = spawnInChunk(entityType, world, reason, pos, randomIn);
            }
            if(!Boolean.TRUE.equals(SLIME_DATA.getData(entityType.getRegistryName()).isSpawnOnSurface())){
                result = result && isBelowSurface(world, pos);
            }
            return result && Monster.isDarkEnoughToSpawn(world, pos, randomIn) && checkMobSpawnRules(entityType, world, reason, pos, randomIn);
        }
        return false;
    }

    private static boolean isBelowSurface(ServerLevelAccessor world, BlockPos pos) {
        return world.getHeightmapPos(WORLD_SURFACE, pos).getY() > pos.getY();
    }

    public static boolean spawnInChunk(EntityType<? extends AbstractSlimeEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random randomIn) {
        if (!(world instanceof WorldGenLevel)) {
            return false;
        }

        ChunkPos chunkpos = new ChunkPos(pos);
        boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel) world).getSeed(), 987234911L).nextInt(10) == 0;
        if (randomIn.nextInt(10) == 0 && flag && pos.getY() < 40) {
            return checkMobSpawnRules(entityType, world, reason, pos, randomIn);
        }
        return false;
    }

    /**
        Allows spawning in the swamp at any depth
     */
    protected static boolean canSpawnInSwamp(EntityType<? extends AbstractSlimeEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return Objects.equals(world.getBiomeName(pos), Optional.of(Biomes.SWAMP)) // Must be Swamp
                && pos.getY() > 50 && pos.getY() < 70 // Must be between Y 50 and Y 70
                && randomIn.nextFloat() < 0.5F // 50% chance
                && randomIn.nextFloat() < world.getMoonBrightness() // More chance during Full Moon, No chance during empty moon
                && world.getMaxLocalRawBrightness(pos) <= randomIn.nextInt(12) // Lightlevel needs to be lower than or, equal to 8.
                && checkMobSpawnRules(entityType, world, reason, pos, randomIn); //Standard can spawn on.
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    public float getSoundVolume() {
        return 0.4F * (float) this.getSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    public boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    @Override
    protected void jumpFromGround() {
        float f = this.getJumpPower();
        if (this.hasEffect(MobEffects.JUMP)) {
            f += 0.1F * (float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1);
        }
        Vec3 vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x, f, vector3d.z);
        this.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }


    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        int j = 0;
        boolean swarm = false;
        if(dataTag != null && dataTag.contains(SLIME_SIZE_KEY)) {
            j = dataTag.getInt(SLIME_SIZE_KEY);
        }else{
                int i = this.random.nextInt(3);
                if(i < 2 && this.random.nextFloat() < SWARM_CHANCE){
                    swarm = true;
                }else if (i < 2 && this.random.nextFloat() < 0.5F * difficultyIn.getSpecialMultiplier()) {
                    ++i;
                }

                j = 1 << i;
        }
        this.setSize(j, true);
        SpawnGroupData iLivingEntityData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if(swarm){
            spawnSwarm();
        }
        return iLivingEntityData;
    }

    @Override
    public float getVoicePitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    public SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return super.getDimensions(poseIn).scale(0.255F * (float) this.getSize());
    }

    /**
     * Called when the slime spawns particles on landing, see onUpdate.
     * Return true to prevent the spawning of the default particles.
     */
    protected boolean spawnCustomParticles() {
        return false;
    }

    public static AttributeSupplier.Builder getMutableAttributes() {
        return Monster.createMonsterAttributes();
    }

    public void faceAwayFromEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
        double d0 = entityIn.getX() - this.getX();
        double d2 = entityIn.getZ() - this.getZ();
        double d1;
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn;
            d1 = livingentity.getEyeY() - this.getEyeY();
        } else {
            d1 = (entityIn.getBoundingBox().minY + entityIn.getBoundingBox().maxY) / 2.0D - this.getEyeY();
        }

        double d3 = Math.sqrt((float)(d0 * d0 + d2 * d2));
        float f = (float)(Math.atan2(d2, d0) * (double)(180F / (float)Math.PI)) + 90.0F;
        float f1 = (float)(-(Math.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
        this.setXRot(this.updateRotation(this.getXRot(), f1, maxPitchIncrease));
        this.setYRot(this.updateRotation(this.getYRot(), f, maxYawIncrease));
    }

    protected float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return data.getMaxInChunk();
    }

    public DyeColor getDyeColor(){
        return data.getDyeColor();
    }

    @SubscribeEvent
    public static void preventFallDamage(LivingFallEvent event) {
        if(event.getEntity() instanceof AbstractSlimeEntity) {
            event.setDamageMultiplier(0.0f);
            AbstractSlimeEntity entity = (AbstractSlimeEntity) event.getEntity();
            if(!event.getEntity().level.isClientSide() && event.getDistance() > (14 / (10 * entity.getAttributeValue(ENTITY_GRAVITY.get())))){
                entity.setHealth(0);
                if(entity.isTiny()) {
                    GooLayerBlock.spawnAtBlockPos(entity.level, entity.blockPosition(), entity.getDyeColor());
                }
                event.getEntity().remove(false);
            }
        }
    }
}
