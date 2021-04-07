package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.init.ModProcessors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.LILY_PAD;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

public class LilyPadProcessor extends StructureProcessor {
    public static final Codec<LilyPadProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity)
            ).apply(builder, LilyPadProcessor::new));
    private static final long SEED = 837145L;
    private final float rarity;

    public LilyPadProcessor(float rarity) {
        this.rarity = rarity;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(blockInfo.pos, SEED);
        Random pieceRandom = settings.getRandom(piecePos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)).state.getFluidState().getType() == Fluids.WATER) {
                return new Template.BlockInfo(blockpos, LILY_PAD.defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.LILY_PADS;
    }
}