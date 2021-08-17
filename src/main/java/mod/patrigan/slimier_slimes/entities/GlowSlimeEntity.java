package mod.patrigan.slimier_slimes.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static mod.patrigan.slimier_slimes.init.ModBlocks.LIGHT_AIR;

public class GlowSlimeEntity extends AbstractSlimeEntity {

    @Override
    public void aiStep() {
        super.aiStep();
        updateLightLevel();
    }

    private void updateLightLevel() {
        BlockPos position = blockPosition();
        if(!dead && level.getBlockState(position).isAir()) {
            level.setBlock(position, LIGHT_AIR.get().defaultBlockState(), 3);
        }else if(!dead && (level.getBlockState(position.offset(0, 1, 0)).isAir())) {
            level.setBlock(position.offset(0, 1, 0), LIGHT_AIR.get().defaultBlockState(), 3);
        }
    }

    public GlowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder getMutableAttributes() {
        return Monster.createMonsterAttributes();
    }

}
