package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.OBSIDIAN_SLIME;

public class LavaSlimeEntity extends AbstractSlimeEntity {

    public LavaSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceLavaGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void remove(boolean keepData) {
        if (!this.level.isClientSide) {
            super.remove(keepData);
            BlockPos position = blockPosition();
            if (this.isDeadOrDying() && this.getSize() > 1 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) && level.getBlockState(position).getBlock().isAir(level.getBlockState(position), level, position)) {
                level.setBlock(position, Blocks.LAVA.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public void tick() {
        if(this.isInWater()){
            hardenInWater();
        }
        /*if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK) && rand.nextFloat() < 0.01F) {
            int i = rand.nextInt(4);
            if (i > 0) {
                BlockPos blockpos = this.getPosition();

                for(int j = 0; j < i; ++j) {
                    blockpos = blockpos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
                    if (!world.isBlockPresent(blockpos)) {
                        return;
                    }

                    BlockState blockstate = world.getBlockState(blockpos);
                    if (blockstate.isAir()) {
                        if (this.isSurroundingBlockFlammable(world, blockpos)) {
                            world.setBlockState(blockpos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos, this.getPosition(), Blocks.FIRE.getDefaultState()));
                            return;
                        }
                    } else if (blockstate.getMaterial().blocksMovement()) {
                        return;
                    }
                }
            } else {
                for(int k = 0; k < 3; ++k) {
                    BlockPos blockpos1 = this.getPosition().add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
                    if (!world.isBlockPresent(blockpos1)) {
                        return;
                    }

                    if (world.isAirBlock(blockpos1.up()) && this.getCanBlockBurn(world, blockpos1)) {
                        world.setBlockState(blockpos1.up(), net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos1.up(), this.getPosition(), Blocks.FIRE.getDefaultState()));
                    }
                }
            }

        }*/
        super.tick();
    }


    private void hardenInWater() {
        AbstractSlimeEntity slimeentity = OBSIDIAN_SLIME.get().create(this.level);
        if (this.isPersistenceRequired()) {
            slimeentity.setPersistenceRequired();
        }

        slimeentity.setCustomName(this.getCustomName());
        slimeentity.setNoAi(this.isNoAi());
        slimeentity.setInvulnerable(this.isInvulnerable());
        slimeentity.setSize(this.getSize(), true);
        slimeentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        this.level.addFreshEntity(slimeentity);
        this.remove(false);
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        return worldIn.isUnobstructed(this);
    }

    public static boolean spawnable(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && reason.equals(SpawnReason.SPAWNER) && world.getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            return true;
        }else{
            return AbstractSlimeEntity.checkSlimeSpawnRules(entityType, world, reason, pos, randomIn);
        }
    }
}