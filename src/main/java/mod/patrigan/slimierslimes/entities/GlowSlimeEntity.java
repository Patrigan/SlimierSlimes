package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mod.patrigan.slimierslimes.init.ModBlocks.LIGHT_AIR;
import static net.minecraft.block.Blocks.CAVE_AIR;

public class GlowSlimeEntity extends AbstractSlimeEntity {

    @Override
    public void livingTick() {
        super.livingTick();
        updateLightLevel();
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.3F + 0.1F * (float) size));
    }

    private void updateLightLevel() {
        BlockPos position = getPosition();
        if(!dead && (world.getBlockState(position).getBlock().equals(CAVE_AIR)||world.getBlockState(position).getBlock().equals(Blocks.AIR))) {
            world.setBlockState(position, LIGHT_AIR.get().getDefaultState(), 3);
        }else if(!dead && (world.getBlockState(position.add(0, 1, 0)).getBlock().equals(CAVE_AIR)||world.getBlockState(position.add(0, 1, 0)).getBlock().equals(Blocks.AIR))) {
            world.setBlockState(position.add(0, 1, 0), LIGHT_AIR.get().getDefaultState(), 3);
        }
    }

    public GlowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new BlockParticleData(ParticleTypes.BLOCK, Blocks.GLOWSTONE.getDefaultState());
    }

    @Override
    public int getJumpDelay() {
        return super.getJumpDelay() * 10;
    }

    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() + 1.0F;
    }

}
