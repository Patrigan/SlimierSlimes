package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static mod.patrigan.slimierslimes.network.datasync.ModDataSerializers.BLOCK_STATE_LIST;
import static net.minecraft.block.Blocks.BROWN_MUSHROOM;
import static net.minecraft.block.Blocks.RED_MUSHROOM;

public class ShroomSlimeEntity extends AbstractSlimeEntity implements net.minecraftforge.common.IForgeShearable {
    private static final DataParameter<List<BlockState>> DATA_MUSHROOMS = EntityDataManager.defineId(ShroomSlimeEntity.class, BLOCK_STATE_LIST);

    private List<BlockState> mushrooms = new ArrayList<>();

    public ShroomSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData iLivingEntityData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.addMushroom(BROWN_MUSHROOM.defaultBlockState());
        if(!this.isTiny()){
            this.addMushroom(RED_MUSHROOM.defaultBlockState());
        }
        return iLivingEntityData;
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        if (itemstack.getItem() == Items.BOWL) {
            boolean flag = false;
            ItemStack itemstack1 = new ItemStack(Items.MUSHROOM_STEW);

            ItemStack itemstack2 = DrinkHelper.createFilledResult(itemstack, p_230254_1_, itemstack1, false);
            p_230254_1_.setItemInHand(p_230254_2_, itemstack2);
            SoundEvent soundevent;
            if (flag) {
                soundevent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            } else {
                soundevent = SoundEvents.MOOSHROOM_MILK;
            }

            this.playSound(soundevent, 1.0F, 1.0F);
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    public boolean isShearable(@javax.annotation.Nonnull ItemStack item, World world, BlockPos pos) {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public java.util.List<ItemStack> onSheared(PlayerEntity player, ItemStack item, World world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1.0F, 1.0F);
        if (!world.isClientSide()) {
            ((ServerWorld)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            java.util.List<ItemStack> items = new java.util.ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                items.add(new ItemStack(RED_MUSHROOM));
            }
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

    public List<BlockState> getMushrooms() {
        return mushrooms;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putIntArray("mushrooms", mushrooms.stream().map(Block::getId).mapToInt(x -> x).toArray());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        mushrooms = Collections.emptyList();
        if (compound.contains("mushrooms", 99)) {
            int[] intStream = compound.getIntArray("mushrooms");
            Arrays.stream(intStream).boxed().map(Block::stateById).forEach(this::addMushroom);
        }

    }

}