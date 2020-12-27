package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.Random;

import static net.minecraft.block.Blocks.CAVE_AIR;

public class RockSlimeEntity extends AbstractSlimeEntity {

    public RockSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue((double)(size * 2));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.1F + 0.1F * (float) size));
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new BlockParticleData(ParticleTypes.BLOCK, Blocks.COBBLESTONE.getDefaultState());
    }

    @Override
    public int getJumpDelay() {
        return super.getJumpDelay() * 5;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    @Override
    protected void jump() {
        Vector3d vector3d = this.getMotion();
        this.setMotion(vector3d.x, this.getJumpUpwardsMotion(), vector3d.z);
        this.isAirBorne = true;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return super.getJumpUpwardsMotion() * 0.5F;
    }

    @Override
    public boolean canDamagePlayer() {
        return this.isServerWorld();
    }

    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() + 1.0F;
    }

    public static boolean spawnable(EntityType<? extends AbstractSlimeEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {

        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (canSpawnInSwamp(entityType, world, reason, pos, randomIn)) {
                return true;
            }
            return MonsterEntity.isValidLightLevel(world, pos, randomIn) && canSpawnOn(entityType, world, reason, pos, randomIn) && isCaveAir(world, pos);
        }

        return false;
    }

    private static boolean isCaveAir(IServerWorld world, BlockPos pos){
        return world.getBlockState(pos).getBlock().equals(CAVE_AIR);
    }

}
