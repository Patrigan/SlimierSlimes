package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particles.IParticleData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.ROCK_SLIME;
import static net.minecraft.block.Blocks.CAVE_AIR;
import static net.minecraft.particles.ParticleTypes.LAVA;

public class LavaSlimeEntity  extends AbstractSlimeEntity {

    public LavaSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected IParticleData getSquishParticle() {
        return LAVA;
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
        super.tick();
    }

    private void hardenInWater() {
        RockSlimeEntity slimeentity = ROCK_SLIME.get().create(this.world);
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
}