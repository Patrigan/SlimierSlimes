package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class ObsidianSlimeEntity extends AbstractSlimeEntity {
    public ObsidianSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(Attributes.ARMOR).setBaseValue(size * 5);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((size + 1) * size * 5);
        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(size);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public int getJumpDelay() {
        return this.rand.nextInt(5) + 10;
    }
}
