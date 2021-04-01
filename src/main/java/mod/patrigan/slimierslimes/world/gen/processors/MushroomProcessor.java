package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModProcessors.VINES;
import static mod.patrigan.slimierslimes.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

public class MushroomProcessor extends StructureProcessor {
    public static final Codec<mod.patrigan.slimierslimes.world.gen.processors.MushroomProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity)
            ).apply(builder, mod.patrigan.slimierslimes.world.gen.processors.MushroomProcessor::new));
    private static final long SEED = 3478985L;
    private final float rarity;

    public MushroomProcessor(float rarity) {
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
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                Block mushroom = MUSHROOMS.getRandomElement(pieceRandom);
                return new Template.BlockInfo(blockpos, mushroom.defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return VINES;
    }
}