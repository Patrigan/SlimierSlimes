package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModProcessors.BLOCK_MOSSIFY;
import static mod.patrigan.slimierslimes.init.ModProcessors.CEILING_ATTACHMENT;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class CeilingAttachmentProcessor extends StructureProcessor {
    public static final Codec<CeilingAttachmentProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    ResourceLocation.CODEC.fieldOf("block").forGetter(data -> data.block),
                    Codec.BOOL.optionalFieldOf("needsWall", false).forGetter(processor -> processor.needsWall)
            ).apply(builder, CeilingAttachmentProcessor::new));

    private final float rarity;
    private final ResourceLocation block;
    private final boolean needsWall;

    public CeilingAttachmentProcessor(float rarity, ResourceLocation block, boolean needsWall) {
        this.rarity = rarity;
        this.block = block;
        this.needsWall = needsWall;
    }

    @Nullable
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(needsWall){
                blockstate1 = getIfNeedsWall(pieceBlocks, rawBlockInfo.pos);
            }else {
                blockstate1 = getIfAbove(pieceBlocks, rawBlockInfo.pos);
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    private BlockState getIfNeedsWall(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        List<Template.BlockInfo> neighbours = getSideNeighboursBlockInfo(pieceBlocks, pos, true);
        long count = neighbours.stream().filter(blockInfo -> blockInfo != null && !blockInfo.state.getBlock().equals(AIR)).count();
        if(count > 0){
            return getIfAbove(pieceBlocks, pos);
        }
        return null;
    }

    private List<Template.BlockInfo> getSideNeighboursBlockInfo(List<Template.BlockInfo> pieceBlocks, BlockPos pos, boolean diagonal) {
        List<Template.BlockInfo> neighbours = new ArrayList<>(Arrays.asList(getBlock(pieceBlocks, pos.north()), getBlock(pieceBlocks, pos.south()), getBlock(pieceBlocks, pos.west()), getBlock(pieceBlocks, pos.east())));
        if(diagonal){
            neighbours.addAll(Arrays.asList(
                    getBlock(pieceBlocks, pos.north().east()),
                    getBlock(pieceBlocks, pos.east().south()),
                    getBlock(pieceBlocks, pos.south().west()),
                    getBlock(pieceBlocks, pos.west().north())
            ));
        }
        return neighbours;
    }

    private BlockState getIfAbove(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        Template.BlockInfo above = getBlock(pieceBlocks, pos.above());
        if(above != null && !above.state.getBlock().equals(AIR)) {
            Block newBlock = BLOCKS.getValue(block);
            return newBlock.defaultBlockState();
        }
        return null;
    }

    private Template.BlockInfo getBlock(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    protected IStructureProcessorType<?> getType() {
        return CEILING_ATTACHMENT;
    }
}