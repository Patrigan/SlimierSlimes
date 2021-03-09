package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.ai.controller.MoveHelperController;
import mod.patrigan.slimierslimes.entities.ai.goal.AttackGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimierslimes.entities.ai.goal.HopGoal;
import mod.patrigan.slimierslimes.init.ModItems;
import mod.patrigan.slimierslimes.init.data.SlimeData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static mod.patrigan.slimierslimes.SlimierSlimes.SLIME_DATA;
import static mod.patrigan.slimierslimes.init.data.SquishParticleData.SquishParticleType.*;
import static net.minecraft.world.gen.Heightmap.Type.WORLD_SURFACE;

public class AbstractSlimeEntity extends MobEntity implements IMob {

    private static final float SWARM_CHANCE = 0.05F;

    private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(AbstractSlimeEntity.class, DataSerializers.VARINT);
    public static final String SLIME_SIZE_KEY = "Size";

    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public AbstractSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getPosY() - this.getPosY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SLIME_SIZE, 1);
    }

    //Slime Size is determined as follows: A random number is determined between 0 and 2. 1 is then bitshift equal to this number, resulting in either 1, 2 or 4.
    protected void setSlimeSize(int size, boolean resetHealth) {
        this.dataManager.set(SLIME_SIZE, size);
        this.recenterBoundingBox();
        this.recalculateSize();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) (size * size));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.2F + 0.1F * (float) size));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double) size);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }

        this.experienceValue = size;
    }

    public int getSlimeSize() {
        return this.dataManager.get(SLIME_SIZE);
    }


    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Size", this.getSlimeSize() - 1);
        compound.putBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        int i = compound.getInt(SLIME_SIZE_KEY);
        if (i < 0) {
            i = 0;
        }

        this.setSlimeSize(i + 1, false);
        super.readAdditional(compound);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isSmallSlime() {
        return this.getSlimeSize() <= 1;
    }

    protected IParticleData getSquishParticle() {
        SlimeData data = SLIME_DATA.getData(this.getType().getRegistryName());
        if(ITEM.equals(data.getSquishParticleData().getType())){
            return new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ForgeRegistries.ITEMS.getValue(data.getSquishParticleData().getResourceLocation())));
        }else if(BLOCK.equals(data.getSquishParticleData().getType())){
            return new BlockParticleData(ParticleTypes.BLOCK, ForgeRegistries.BLOCKS.getValue(data.getSquishParticleData().getResourceLocation()).getDefaultState());
        }else if(PARTICLE.equals(data.getSquishParticleData().getType())){
            return (BasicParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(data.getSquishParticleData().getResourceLocation());
        }
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected boolean isDespawnPeaceful() {
        return this.getSlimeSize() > 0;
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
        this.alterSquishAmount();
    }

    protected void land() {
        int i = this.getSlimeSize();

        if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
        for (int j = 0; j < i * 8; ++j) {
            float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
            float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
            float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
            this.world.addParticle(this.getSquishParticle(), this.getPosX() + (double) f2, this.getPosY(), this.getPosZ() + (double) f3, 0.0D, 0.0D, 0.0D);
        }

        this.playSound(this.getSquishSound(), this.getSoundVolume(), getSoundPitch() / 0.8F);
        this.squishAmount = -0.5F;
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    public int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }

    @Override
    public void recalculateSize() {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        super.recalculateSize();
        this.setPosition(d0, d1, d2);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SLIME_SIZE.equals(key)) {
            this.recalculateSize();
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }

    @Override
    public EntityType<? extends AbstractSlimeEntity> getType() {
        return (EntityType<? extends AbstractSlimeEntity>) super.getType();
    }

    @Override
    public void remove(boolean keepData) {
        int i = this.getSlimeSize();
        if (!this.world.isRemote && i > 1 && this.getShouldBeDead() && !this.removed) {
            ITextComponent itextcomponent = this.getCustomName();
            boolean flag = this.isAIDisabled();
            float f = (float) i / 4.0F;
            int j = i / 2;
            int k = 2 + this.rand.nextInt(3);

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2D) - 0.5F) * f;
                AbstractSlimeEntity slimeentity = this.getType().create(this.world);
                if (this.isNoDespawnRequired()) {
                    slimeentity.enablePersistence();
                }

                slimeentity.setCustomName(itextcomponent);
                slimeentity.setNoAI(flag);
                slimeentity.setInvulnerable(this.isInvulnerable());
                slimeentity.setSlimeSize(j, true);
                slimeentity.setLocationAndAngles(this.getPosX() + (double) f1, this.getPosY() + 0.5D, this.getPosZ() + (double) f2, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.addEntity(slimeentity);
            }
        }
        super.remove(keepData);
    }

    protected void spawnSwarm(){
        if (!this.world.isRemote) {
            ITextComponent itextcomponent = this.getCustomName();
            int i = 1;
            int k = 6 + this.rand.nextInt(5);
            float f = (float) i / 4.0F;

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2D) - 0.5F) * f;
                AbstractSlimeEntity slimeentity = this.getType().create(this.world);
                if (this.isNoDespawnRequired()) {
                    slimeentity.enablePersistence();
                }

                slimeentity.setCustomName(itextcomponent);
                slimeentity.setNoAI(this.isAIDisabled());
                slimeentity.setInvulnerable(this.isInvulnerable());
                slimeentity.setSlimeSize(i, true);
                slimeentity.setLocationAndAngles(this.getPosX() + (double) f1, this.getPosY() + 0.5D, this.getPosZ() + (double) f2, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.addEntity(slimeentity);
            }
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    @Override
    public void applyEntityCollision(Entity entityIn) {
        super.applyEntityCollision(entityIn);
        if (entityIn instanceof IronGolemEntity && this.canDamagePlayer()) {
            this.dealDamage((LivingEntity) entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn) {
        if (this.canDamagePlayer()) {
            this.dealDamage(entityIn);
        }
    }

    protected boolean dealDamage(LivingEntity entityIn) {
        if (this.isAlive()) {
            int i = this.getSlimeSize();
            if (this.getDistanceSq(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.canEntityBeSeen(entityIn) && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, getSoundPitch());
                this.applyEnchantments(this, entityIn);
                return true;
            }
        }
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.625F * sizeIn.height;
    }

    public boolean canDamagePlayer() {
        return this.isServerWorld();
    }

    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_HURT_SMALL : SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_DEATH_SMALL : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return this.isSmallSlime() ? this.getType().getLootTable() : LootTables.EMPTY;
    }

    public static boolean spawnable(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (canSpawnInSwamp(entityType, world, reason, pos, randomIn)) {
                return true;
            }
            boolean result = true;
            if(Boolean.TRUE.equals(SlimierSlimes.MAIN_CONFIG.maintainChunkSpawning.get())) {
                result = result && spawnInChunk(entityType, world, reason, pos, randomIn);
            }
            if(!Boolean.TRUE.equals(SLIME_DATA.getData(entityType.getRegistryName()).isSpawnOnSurface())){
                result = result && isBelowSurface(world, pos);
            }
            return result && MonsterEntity.isValidLightLevel(world, pos, randomIn) && canSpawnOn(entityType, world, reason, pos, randomIn);
        }
        return false;
    }

    private static boolean isBelowSurface(IServerWorld world, BlockPos pos) {
        return world.getHeight(WORLD_SURFACE, pos).getY() > pos.getY();
    }

    public static boolean spawnInChunk(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        if (!(world instanceof ISeedReader)) {
            return false;
        }

        ChunkPos chunkpos = new ChunkPos(pos);
        boolean flag = SharedSeedRandom.createSlimeChunkSpawningSeed(chunkpos.x, chunkpos.z, ((ISeedReader) world).getSeed(), 987234911L).nextInt(10) == 0;
        if (randomIn.nextInt(10) == 0 && flag && pos.getY() < 40) {
            return canSpawnOn(entityType, world, reason, pos, randomIn);
        }
        return false;
    }

    /**
        Allows spawning in the swamp at any depth
     */
    protected static boolean canSpawnInSwamp(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        return Objects.equals(world.func_242406_i(pos), Optional.of(Biomes.SWAMP)) // Must be Swamp
                && pos.getY() > 50 && pos.getY() < 70 // Must be between Y 50 and Y 70
                && randomIn.nextFloat() < 0.5F // 50% chance
                && randomIn.nextFloat() < world.getMoonFactor() // More chance during Full Moon, No chance during empty moon
                && world.getLight(pos) <= randomIn.nextInt(12) // Lightlevel needs to be lower than or, equal to 8.
                && canSpawnOn(entityType, world, reason, pos, randomIn); //Standard can spawn on.
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    public float getSoundVolume() {
        return 0.4F * (float) this.getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    public boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    @Override
    protected void jump() {
        Vector3d vector3d = this.getMotion();
        this.setMotion(vector3d.x, (double) this.getJumpUpwardsMotion(), vector3d.z);
        this.isAirBorne = true;
    }


    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int j = 0;
        boolean swarm = false;
        if(dataTag != null && dataTag.contains(SLIME_SIZE_KEY)) {
            j = dataTag.getInt(SLIME_SIZE_KEY);
        }else{
                int i = this.rand.nextInt(3);
                if(i < 2 && this.rand.nextFloat() < SWARM_CHANCE){
                    swarm = true;
                }else if (i < 2 && this.rand.nextFloat() < 0.5F * difficultyIn.getClampedAdditionalDifficulty()) {
                    ++i;
                }

                j = 1 << i;
        }
        this.setSlimeSize(j, true);
        ILivingEntityData iLivingEntityData = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if(swarm){
            spawnSwarm();
        }
        return iLivingEntityData;
    }

    @Override
    public float getSoundPitch() {
        float f = this.isSmallSlime() ? 1.4F : 0.8F;
        return ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * f;
    }

    public SoundEvent getJumpSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_JUMP_SMALL : SoundEvents.ENTITY_SLIME_JUMP;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return super.getSize(poseIn).scale(0.255F * (float) this.getSlimeSize());
    }

    /**
     * Called when the slime spawns particles on landing, see onUpdate.
     * Return true to prevent the spawning of the default particles.
     */
    protected boolean spawnCustomParticles() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    public void faceAwayFromEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
        double d0 = entityIn.getPosX() - this.getPosX();
        double d2 = entityIn.getPosZ() - this.getPosZ();
        double d1;
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn;
            d1 = livingentity.getPosYEye() - this.getPosYEye();
        } else {
            d1 = (entityIn.getBoundingBox().minY + entityIn.getBoundingBox().maxY) / 2.0D - this.getPosYEye();
        }

        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) + 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f1, maxPitchIncrease);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f, maxYawIncrease);
    }

    protected float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        SlimeData data = SLIME_DATA.getData(this.getType().getRegistryName());
        return data.getMaxInChunk();
    }

    public static DyeColor getPrimaryColor() {
        return DyeColor.LIME;
    }
}
