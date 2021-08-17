package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.entities.ai.goal.AttackGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FaceRandomGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.FloatGoal;
import mod.patrigan.slimier_slimes.entities.ai.goal.HopGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mod.patrigan.slimier_slimes.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.slimier_slimes.network.datasync.ModDataSerializers.BLOCK_STATE_LIST;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemUtils;

public class ShroomSlimeEntity extends AbstractSlimeEntity implements net.minecraftforge.common.IForgeShearable {
    private static final EntityDataAccessor<List<BlockState>> DATA_MUSHROOMS = SynchedEntityData.defineId(ShroomSlimeEntity.class, BLOCK_STATE_LIST);

    private List<BlockState> mushrooms = new ArrayList<>();

    public ShroomSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D && this.getMushrooms().isEmpty()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData iLivingEntityData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        for (int i = 0; i < this.getSize(); i++) {
            this.addMushroom(MUSHROOMS.getRandomElement(this.random).defaultBlockState());
        }
        return iLivingEntityData;
    }

    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        if (itemstack.getItem() == Items.BOWL) {
            boolean flag = this.random.nextFloat() <= SlimierSlimes.MAIN_CONFIG.shroomSlimeSuspiciousChance.get();
            ItemStack itemstack1;
            if(flag){
                itemstack1 = new ItemStack(Items.SUSPICIOUS_STEW);
                MobEffect effect = new ArrayList<>(ForgeRegistries.POTIONS.getValues()).get(this.random.nextInt(ForgeRegistries.POTIONS.getKeys().size()));
                SuspiciousStewItem.saveMobEffect(itemstack1, effect, this.random.nextInt(1500)+500);
            }else {
                itemstack1 = new ItemStack(Items.MUSHROOM_STEW);
            }

            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_230254_1_, itemstack1, false);
            p_230254_1_.setItemInHand(p_230254_2_, itemstack2);
            SoundEvent soundevent;
            if (flag) {
                soundevent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            } else {
                soundevent = SoundEvents.MOOSHROOM_MILK;
            }

            this.playSound(soundevent, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    public boolean isShearable(@javax.annotation.Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public java.util.List<ItemStack> onSheared(Player player, ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!world.isClientSide()) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            java.util.List<ItemStack> items = new java.util.ArrayList<>();
            this.getMushrooms().forEach(blockState -> items.add(new ItemStack(blockState.getBlock().asItem())));
            this.clearMushrooms();
            return items;
        }
        return java.util.Collections.emptyList();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MUSHROOMS, new ArrayList<BlockState>());
    }

    private void addMushroom(BlockState mushroom) {
        mushrooms.add(mushroom);
        this.entityData.set(DATA_MUSHROOMS, mushrooms);
    }

    private void clearMushrooms() {
        mushrooms.clear();
        this.entityData.set(DATA_MUSHROOMS, mushrooms);
    }

    public List<BlockState> getMushrooms() {
        return this.entityData.get(DATA_MUSHROOMS);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putIntArray("mushrooms", getMushrooms().stream().map(Block::getId).mapToInt(x -> x).toArray());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        mushrooms = new ArrayList<>();
        if (compound.contains("mushrooms")) {
            int[] intStream = compound.getIntArray("mushrooms");
            Arrays.stream(intStream).boxed().map(Block::stateById).forEach(this::addMushroom);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(damageSource, p_213333_2_, p_213333_3_);
        getMushrooms().forEach(blockState -> {
            if(this.random.nextFloat() <= 0.5f){
                this.spawnAtLocation(blockState.getBlock().asItem());
            }
        });
    }

    @Override
    public boolean isDealsDamage() {
        if(this.mushrooms.isEmpty()){
            return false;
        }else {
            return super.isDealsDamage();
        }
    }
}