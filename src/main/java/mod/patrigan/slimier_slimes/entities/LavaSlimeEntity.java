package mod.patrigan.slimier_slimes.entities;

import mod.patrigan.slimier_slimes.entities.ai.goal.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import java.util.Random;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.OBSIDIAN_SLIME;
import static net.minecraft.world.entity.Entity.RemovalReason.KILLED;

public class LavaSlimeEntity extends AbstractSlimeEntity {

    public LavaSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceLavaGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (!this.level.isClientSide) {
            super.remove(removalReason);
            BlockPos position = blockPosition();
            if (removalReason == KILLED && this.isDeadOrDying() && this.getSize() > 1 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) && level.getBlockState(position).isAir()) {
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
        slimeentity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        this.level.addFreshEntity(slimeentity);
        this.discard();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this);
    }

    public static boolean spawnable(EntityType<? extends AbstractSlimeEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random randomIn) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && reason.equals(MobSpawnType.SPAWNER) && world.getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            return true;
        }else{
            return AbstractSlimeEntity.checkSlimeSpawnRules(entityType, world, reason, pos, randomIn);
        }
    }
}