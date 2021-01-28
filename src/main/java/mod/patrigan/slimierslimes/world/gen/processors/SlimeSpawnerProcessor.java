package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.Nullable;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.SPAWNER_ENTITY_TYPES;
import static mod.patrigan.slimierslimes.init.ModProcessors.SLIME_SPAWNER_RANDOM;
import static net.minecraft.block.Blocks.SPAWNER;

public class SlimeSpawnerProcessor extends StructureProcessor {
    public static final SlimeSpawnerProcessor INSTANCE = new SlimeSpawnerProcessor();
    public static final Codec<SlimeSpawnerProcessor> CODEC = Codec.unit(() ->INSTANCE);

    public SlimeSpawnerProcessor() {}

    @Nullable
    public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings) {
        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        if (blockstate.getBlock().equals(SPAWNER)) {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity) {
                AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic();
                EntityType<?> entitytype1 = SPAWNER_ENTITY_TYPES.get(random.nextInt(SPAWNER_ENTITY_TYPES.size()));
                abstractspawner.setEntityType(entitytype1);
                tileentity.markDirty();
            }

        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return SLIME_SPAWNER_RANDOM;
    }
}