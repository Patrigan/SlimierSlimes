package mod.patrigan.slimier_slimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimier_slimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimier_slimes.init.ModBlocks.GOO_LAYER_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModProcessors.GOO_BLOCKS;
import static mod.patrigan.slimier_slimes.util.ColorUtils.DYE_COLOR_CODEC;
import static mod.patrigan.slimier_slimes.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.slimier_slimes.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

public class GooProcessor extends StructureProcessor {

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
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos seedPos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        Random random = ProcessorUtil.getRandom(blockInfo.pos, SEED);
        Random pieceRandom = settings.getRandom(piecePos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<StructureTemplate.StructureBlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                Block gooBlock = null;
                if(dyeColor == null){
                    gooBlock = GOO_LAYER_BLOCKS.get(Arrays.asList(DyeColor.values()).get(pieceRandom.nextInt(DyeColor.values().length))).get();
                }else{
                    gooBlock = GOO_LAYER_BLOCKS.get(dyeColor).get();
                }
                BlockState blockState = gooBlock.defaultBlockState().setValue(LAYERS, pieceRandom.nextInt(maxHeight) + 1);
                return new StructureTemplate.StructureBlockInfo(blockpos, blockState, blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return GOO_BLOCKS;
    }
}