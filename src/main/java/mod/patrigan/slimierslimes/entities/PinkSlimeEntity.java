package mod.patrigan.slimierslimes.entities;

import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.world.World;

import static net.minecraft.particles.ParticleTypes.ITEM;

public class PinkSlimeEntity extends AbstractSlimeEntity {
    public PinkSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected IParticleData getSquishParticle() {
        return new ItemParticleData(ITEM, new ItemStack(ModItems.PINK_JELLY.get()));
    }
}
