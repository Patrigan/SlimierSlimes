package mod.patrigan.slimierslimes.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mod.patrigan.slimierslimes.init.ModBlocks.LIGHT_AIR;
import static net.minecraft.block.Blocks.CAVE_AIR;

public class GlowSlimeEntity extends AbstractSlimeEntity {

    @Override
    public void aiStep() {
        super.aiStep();
        updateLightLevel();
    }

    private void updateLightLevel() {
        BlockPos position = blockPosition();
        if(!dead && (level.getBlockState(position).getBlock().equals(CAVE_AIR)||level.getBlockState(position).getBlock().equals(Blocks.AIR))) {
            level.setBlock(position, LIGHT_AIR.get().defaultBlockState(), 3);
        }else if(!dead && (level.getBlockState(position.offset(0, 1, 0)).getBlock().equals(CAVE_AIR)||level.getBlockState(position.offset(0, 1, 0)).getBlock().equals(Blocks.AIR))) {
            level.setBlock(position.offset(0, 1, 0), LIGHT_AIR.get().defaultBlockState(), 3);
        }
    }

    public GlowSlimeEntity(EntityType<? extends AbstractSlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getMutableAttributes() {
        return MonsterEntity.createMonsterAttributes();
    }

}
