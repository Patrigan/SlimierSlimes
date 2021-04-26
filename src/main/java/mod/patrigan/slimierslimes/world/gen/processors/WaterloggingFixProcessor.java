package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import static mod.patrigan.slimierslimes.init.ModProcessors.WATERLOGGING_FIX_PROCESSOR;

public class WaterloggingFixProcessor extends StructureProcessor {

    public static final Codec<WaterloggingFixProcessor> CODEC = Codec.unit(WaterloggingFixProcessor::new);
    private WaterloggingFixProcessor() { }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {

        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        // ONLY RUN THIS IF STRUCTURE BLOCK IS A DRY WATERLOGGABLE BLOCK
        ChunkPos currentChunkPos = new ChunkPos(blockInfo.pos);
        if(blockInfo.state.getBlock() instanceof IWaterLoggable && !blockInfo.state.getValue(BlockStateProperties.WATERLOGGED)){
            IChunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            if(world.getFluidState(blockInfo.pos).is(FluidTags.WATER)){
                currentChunk.setBlockState(blockInfo.pos, Blocks.STONE.defaultBlockState(), false);
            }

            // Remove water in adjacent blocks across chunk boundaries and above/below as well
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfo.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = world.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }

                if (currentChunk.getFluidState(mutable).is(FluidTags.WATER)) {
                    currentChunk.setBlockState(mutable, Blocks.STONE.defaultBlockState(), false);
                }
            }
        }

        return blockInfo;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return WATERLOGGING_FIX_PROCESSOR;
    }
}