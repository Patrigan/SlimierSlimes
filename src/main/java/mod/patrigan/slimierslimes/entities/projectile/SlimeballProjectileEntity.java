package mod.patrigan.slimierslimes.entities.projectile;

import mod.patrigan.slimierslimes.init.ModEntityTypes;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SlimeballProjectileEntity extends ProjectileItemEntity {
    public SlimeballProjectileEntity(EntityType<? extends SlimeballProjectileEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public SlimeballProjectileEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntityTypes.SLIMEBALL_PROJECTILE.get(), throwerIn, worldIn);
    }

    public SlimeballProjectileEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.SLIMEBALL_PROJECTILE.get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return ModItems.SLIME_BALL.get(DyeColor.LIME).get();
    }

    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return (IParticleData)(itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onEntityHit(EntityRayTraceResult entityRayTraceResult) {
        super.onEntityHit(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        int i = entity instanceof PlayerEntity ? 1 : 0;
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), (float)i);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}