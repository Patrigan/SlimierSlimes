package mod.patrigan.slimier_slimes.entities.projectile;

import mod.patrigan.slimier_slimes.init.ModEntityTypes;
import mod.patrigan.slimier_slimes.init.ModTags;
import mod.patrigan.slimier_slimes.items.SlimeBallItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import static mod.patrigan.slimier_slimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimier_slimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModItems.SLIME_BALL;

public class SlimeBallEntity extends ThrowableItemProjectile {
    public SlimeBallEntity(EntityType<? extends SlimeBallEntity> entityType, Level world) {
        super(entityType, world);
    }

    public SlimeBallEntity(SlimeBallItem defaultItem, Level worldIn, LivingEntity throwerIn) {
        super(ModEntityTypes.SLIME_BALL_PROJECTILE.get(defaultItem.getDyeColor()).get(), throwerIn, worldIn);
    }

    public SlimeBallEntity(Level worldIn, double x, double y, double z) {
        super(ModEntityTypes.SLIME_BALL_PROJECTILE.get(DyeColor.LIME).get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return SLIME_BALL.get(DyeColor.LIME).get();
    }

    private ParticleOptions makeParticle() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity target = entityRayTraceResult.getEntity();
        int i = getDamage(target);
        target.hurt(DamageSource.thrown(this, this.getOwner()), (float)i);
        Entity ownerEntity = this.getOwner();
        if (ownerEntity == null || !(ownerEntity instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner())) {
            placeGooBlock(target.blockPosition());
        }
    }

    private int getDamage(Entity target) {
        if(this.getOwner() == null){
            return 1;
        }else if(target.getType().is(ModTags.EntityTypes.SLIMES)){
            return 0;
        }else if(target instanceof Player) {
            return 1;
        }else if(this.getOwner() instanceof Player){
            return 1;
        }
        return 0;
    }

    /**
     * Called when this Entity hits a block or entity.
     */
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (entity == null || !(entity instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner())) {
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

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}