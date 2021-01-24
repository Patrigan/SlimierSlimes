package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.entities.ai.goal.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.OBSIDIAN_SLIME;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.ROCK_SLIME;
import static net.minecraft.block.Blocks.CAVE_AIR;
import static net.minecraft.particles.ParticleTypes.LAVA;

public class LavaSlimeEntity extends AbstractSlimeEntity {

    public LavaSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected IParticleData getSquishParticle() {
        return LAVA;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new FaceLavaGoal(this));
        this.goalSelector.addGoal(4, new FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                livingEntity -> Math.abs(livingEntity.getPosY() - this.getPosY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void remove(boolean keepData) {
        if (!this.world.isRemote) {
            super.remove(keepData);
            BlockPos position = getPosition();
            if (this.getSlimeSize() > 1 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) && (world.getBlockState(position).getBlock().equals(CAVE_AIR)||world.getBlockState(position).getBlock().equals(Blocks.AIR))) {
                world.setBlockState(position, Blocks.LAVA.getDefaultState(), 3);
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

    private boolean isSurroundingBlockFlammable(IWorldReader worldIn, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (this.getCanBlockBurn(worldIn, pos.offset(direction))) {
                return true;
            }
        }

        return false;
    }

    private boolean getCanBlockBurn(IWorldReader worldIn, BlockPos pos) {
        return pos.getY() >= 0 && pos.getY() < 256 && !worldIn.isBlockLoaded(pos) ? false : worldIn.getBlockState(pos).getMaterial().isFlammable();
    }

    private void hardenInWater() {
        ObsidianSlimeEntity slimeentity = OBSIDIAN_SLIME.get().create(this.world);
        if (this.isNoDespawnRequired()) {
            slimeentity.enablePersistence();
        }

        slimeentity.setCustomName(this.getCustomName());
        slimeentity.setNoAI(this.isAIDisabled());
        slimeentity.setInvulnerable(this.isInvulnerable());
        slimeentity.setSlimeSize(this.getSlimeSize(), true);
        slimeentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        this.world.addEntity(slimeentity);
        this.remove(false);
    }

    @Override
    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    public static DyeColor getPrimaryColor(){
        return DyeColor.RED;
    }

    public static boolean spawnable(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && reason.equals(SpawnReason.SPAWNER) && world.getBlockState(pos).getBlock().equals(Blocks.LAVA)) {
            return true;
        }else{
            return AbstractSlimeEntity.spawnable(entityType, world, reason, pos, randomIn);
        }
    }
}