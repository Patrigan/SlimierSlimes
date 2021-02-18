package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;

import static mod.patrigan.slimierslimes.init.ModProcessors.AIR_RETAINER;
import static mod.patrigan.slimierslimes.init.ModProcessors.SLIME_SPAWNER_RANDOM;

public class AirRetainerProcessor extends StructureProcessor {
    public static final AirRetainerProcessor INSTANCE = new AirRetainerProcessor();
    public static final Codec<AirRetainerProcessor> CODEC = Codec.unit(() ->INSTANCE);

    public AirRetainerProcessor() {}

    @Nullable
    public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings) {
        if(world.isAirBlock(blockInfo.pos)){
            BlockState blockState = world.getBlockState(blockInfo.pos);
            return new Template.BlockInfo(blockInfo.pos, blockState, null);
        }else {
            return blockInfo;
        }
    }

    protected IStructureProcessorType<?> getType() {
        return AIR_RETAINER;
    }
}