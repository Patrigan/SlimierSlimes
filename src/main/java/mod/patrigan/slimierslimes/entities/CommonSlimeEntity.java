package mod.patrigan.slimierslimes.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class CommonSlimeEntity extends AbstractSlimeEntity {
    public CommonSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }
}