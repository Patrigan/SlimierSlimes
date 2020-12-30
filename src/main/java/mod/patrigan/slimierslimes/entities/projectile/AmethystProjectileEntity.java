package mod.patrigan.slimierslimes.entities.projectile;

import mod.patrigan.slimierslimes.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class AmethystProjectileEntity extends Entity {

    private static final float DAMAGE = 4.0F;

    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 22;
    private boolean clientSideAttackStarted;
    private LivingEntity caster;
    private UUID casterUuid;

    public AmethystProjectileEntity(EntityType<? extends AmethystProjectileEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public AmethystProjectileEntity(World worldIn, double x, double y, double z, float rotationYaw, int warmupDelayTicks, LivingEntity casterIn) {
        this(ModEntityTypes.AMETYST_PROJECTILE.get(), worldIn);
        this.warmupDelayTicks = warmupDelayTicks;
        this.setCaster(casterIn);
        this.rotationYaw = rotationYaw * (180F / (float)Math.PI);
        this.setPosition(x, y, z);
    }

    protected void registerData() {
        //No Data to register
    }

    public void setCaster(@Nullable LivingEntity livingEntity) {
        this.caster = livingEntity;
        this.casterUuid = livingEntity == null ? null : livingEntity.getUniqueID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(CompoundNBT compound) {
        this.warmupDelayTicks = compound.getInt("Warmup");
        if (compound.hasUniqueId("Owner")) {
            this.casterUuid = compound.getUniqueId("Owner");
        }

    }

    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            compound.putUniqueId("Owner", this.casterUuid);
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getPosX() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.getWidth() * 0.5D;
                        double d1 = this.getPosY() + 0.05D + this.rand.nextDouble();
                        double d2 = this.getPosZ() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.getWidth() * 0.5D;
                        double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
                        double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.world.addParticle(ParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.2D, 0.0D, 0.2D))) {
                    this.damage(livingentity);
                }
            }

            if (!this.sentSpikeEvent) {
                this.world.setEntityState(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.remove();
            }
        }

    }

    private void damage(LivingEntity target) {
        LivingEntity attacker = this.getCaster();
        if (target.isAlive() && !target.isInvulnerable() && target != attacker) {
            if (attacker == null) {
                target.attackEntityFrom(DamageSource.MAGIC, DAMAGE);
            } else {
                if (attacker.isOnSameTeam(target)) {
                    return;
                }

                target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, attacker), DAMAGE);
            }

        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, this.getSoundCategory(), 1.0F, this.rand.nextFloat() , false);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getAnimationProgress(float partialTicks) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - partialTicks) / 20.0F;
        }
    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}