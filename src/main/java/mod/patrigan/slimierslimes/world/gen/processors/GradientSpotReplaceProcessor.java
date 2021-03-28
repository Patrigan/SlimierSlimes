package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.util.OpenSimplex2F;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.List;

import static mod.patrigan.slimierslimes.init.ModProcessors.GRADIENT_SPOT_REPLACE;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class GradientSpotReplaceProcessor extends StructureProcessor {
    public static final Codec<GradientSpotReplaceProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    ResourceLocation.CODEC.listOf().fieldOf("gradientList").forGetter(data -> data.gradientList),
                    ResourceLocation.CODEC.fieldOf("toReplace").forGetter(data -> data.toReplace),
                    Codec.BOOL.optionalFieldOf("includeBuildingBlocks", false).forGetter(data -> data.includeBuildingBlocks)
            ).apply(builder, GradientSpotReplaceProcessor::new));
    /*public static final Codec<GradientSpotReplace> CODEC_SINGLE_ITEM = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    ResourceLocation.CODEC.fieldOf("toReplace").forGetter(data -> data.toReplace)
            ).apply(builder, GradientSpotReplace::new));*/
    private final float rarity;
    private final List<ResourceLocation> gradientList;
    private final ResourceLocation toReplace;
    private boolean includeBuildingBlocks;

    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public GradientSpotReplaceProcessor(float rarity, List<ResourceLocation> gradientList, ResourceLocation toReplace, boolean includeBuildingBlocks) {
        this.rarity = rarity;
        this.gradientList = gradientList;
        this.toReplace = toReplace;
        this.includeBuildingBlocks = includeBuildingBlocks;
    }

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
        BlockState blockstate1;
        setSeed(((ISeedReader) world).getSeed());

        BlockState blockstate = blockInfo.state;
        BlockPos blockPos = blockInfo.pos;

        ResourceLocation replacementBlock = getReplacementBlockResourceLocation(blockPos);

        Block newBlock = null;
        if(this.includeBuildingBlocks) {
            if(!matchesIncludingBuildingBlocks(blockstate)){
                return blockInfo;
            }
            if (blockstate.getBlock().is(BlockTags.STAIRS)) {
                newBlock = BLOCKS.getValue(new ResourceLocation(replacementBlock.getNamespace(), replacementBlock.getPath() + "_stairs"));
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (blockstate.getBlock().is(BlockTags.SLABS)) {
                newBlock = BLOCKS.getValue(new ResourceLocation(replacementBlock.getNamespace(), replacementBlock.getPath() + "_slab"));
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (blockstate.getBlock().is(BlockTags.WALLS)) {
                newBlock = BLOCKS.getValue(new ResourceLocation(replacementBlock.getNamespace(), replacementBlock.getPath() + "_wall"));
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else{
                blockstate1 = BLOCKS.getValue(replacementBlock).defaultBlockState();
            }
            return new Template.BlockInfo(blockPos, blockstate1, blockInfo.nbt);
        }else{
            if(!blockstate.getBlock().getRegistryName().getPath().equals(toReplace.getPath())){
                return blockInfo;
            }
            blockstate1 = BLOCKS.getValue(replacementBlock).defaultBlockState();
            return new Template.BlockInfo(blockPos, blockstate1, blockInfo.nbt);
        }
    }

    private boolean matchesIncludingBuildingBlocks(BlockState blockstate) {
        String path = blockstate.getBlock().getRegistryName().getPath();
        return path.equals(toReplace.getPath())
                || path.equals(toReplace.getPath() + "_stairs")
                || path.equals(toReplace.getPath() + "_slab")
                || path.equals(toReplace.getPath() + "_walls");
    }

    private ResourceLocation getReplacementBlockResourceLocation(BlockPos blockPos) {
        double noiseValue = (noiseGen.noise3_Classic(blockPos.getX() * 0.075D, blockPos.getY() * 0.075D, blockPos.getZ() * 0.075D));
        //double stepSize = (1D / (gradientList.size() + 1))*rarity;
        double stepSize = 0.1;
        for (ResourceLocation resourceLocation : gradientList) {
            int index = gradientList.indexOf(resourceLocation);
            double bound = (index + 1) * stepSize;
            if(noiseValue < bound && noiseValue > (bound * -1)) {
                return resourceLocation;
            }
        }
        return toReplace;
    }

    protected IStructureProcessorType<?> getType() {
        return GRADIENT_SPOT_REPLACE;
    }
}
