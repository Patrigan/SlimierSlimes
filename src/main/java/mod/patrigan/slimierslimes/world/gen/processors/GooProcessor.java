package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimierslimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModProcessors.GOO_BLOCKS;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

public class GooProcessor extends StructureProcessor {
    private static final Codec<DyeColor> DYE_COLOR_CODEC = Codec.STRING.flatComapMap(s -> DyeColor.byName(s, null), d -> DataResult.success(d.getName()));
    public static final Codec<GooProcessor> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                Codec.INT.optionalFieldOf("maxHeight", 1).forGetter(processor -> processor.maxHeight),
                DYE_COLOR_CODEC.fieldOf("dyeColor").forGetter(processor -> processor.dyeColor)
        ).apply(builder, GooProcessor::new));
    private static final long SEED = 3718417L;
    private final float rarity;
    private final int maxHeight;
    private final DyeColor dyeColor;

    public GooProcessor(float rarity, int maxHeight, DyeColor dyeColor) {
        this.rarity = rarity;
        this.maxHeight = maxHeight;
        this.dyeColor = dyeColor;
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
                Block gooBlock = null;
                if(dyeColor == null){
                    gooBlock = GOO_LAYER_BLOCKS.get(Arrays.asList(DyeColor.values()).get(pieceRandom.nextInt(DyeColor.values().length))).get();
                }else{
                    gooBlock = GOO_LAYER_BLOCKS.get(dyeColor).get();
                }
                BlockState blockState = gooBlock.defaultBlockState().setValue(LAYERS, pieceRandom.nextInt(maxHeight) + 1);
                return new Template.BlockInfo(blockpos, blockState, blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return GOO_BLOCKS;
    }
}