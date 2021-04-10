package mod.patrigan.slimierslimes.entities.projectile;

import mod.patrigan.slimierslimes.init.ModEntityTypes;
import mod.patrigan.slimierslimes.init.ModTags;
import mod.patrigan.slimierslimes.items.SlimeBallItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import static mod.patrigan.slimierslimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimierslimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModItems.SLIME_BALL;

public class SlimeBallEntity extends ProjectileItemEntity {
    public SlimeBallEntity(EntityType<? extends SlimeBallEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public SlimeBallEntity(SlimeBallItem defaultItem, World worldIn, LivingEntity throwerIn) {
        super(ModEntityTypes.SLIME_BALL_PROJECTILE.get(defaultItem.getDyeColor()).get(), throwerIn, worldIn);
    }

    public SlimeBallEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.SLIME_BALL_PROJECTILE.get(DyeColor.LIME).get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return SLIME_BALL.get(DyeColor.LIME).get();
    }

    private IParticleData makeParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (IParticleData)(itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity target = entityRayTraceResult.getEntity();
        int i = getDamage(target);
        target.hurt(DamageSource.thrown(this, this.getOwner()), (float)i);
        Entity ownerEntity = this.getOwner();
        if (ownerEntity == null || !(ownerEntity instanceof MobEntity) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getEntity())) {
            placeGooBlock(target.blockPosition());
        }
    }

    private int getDamage(Entity target) {
        if(this.getOwner() == null){
            return 1;
        }else if(target.getType().is(ModTags.EntityTypes.SLIMES)){
            return 0;
        }else if(target instanceof PlayerEntity) {
            return 1;
        }else if(this.getOwner() instanceof PlayerEntity){
            return 1;
        }
        return 0;
    }

    /**
     * Called when this Entity hits a block or entity.
     */
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }
    }

    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof MobEntity) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getEntity())) {
                BlockPos blockpos = blockRayTraceResult.getBlockPos().relative(blockRayTraceResult.getDirection());
                placeGooBlock(blockpos);
            }

        }
    }

    private void placeGooBlock(BlockPos blockpos) {
        BlockState blockState = GOO_LAYER_BLOCKS.get(((SlimeBallItem) getItemRaw().getItem()).getDyeColor()).get().defaultBlockState();
        if (this.level.isEmptyBlock(blockpos)) {
            this.level.setBlockAndUpdate(blockpos, blockState);
        }else if(this.level.getBlockState(blockpos).is(blockState.getBlock())){
            BlockState gooLayerBlock = this.level.getBlockState(blockpos);
            if(gooLayerBlock.getValue(LAYERS) < 8){
                this.level.setBlockAndUpdate(blockpos, gooLayerBlock.setValue(LAYERS, gooLayerBlock.getValue(LAYERS)+1));
            }else{
                placeGooBlock(blockpos.above());
            }
        }
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}