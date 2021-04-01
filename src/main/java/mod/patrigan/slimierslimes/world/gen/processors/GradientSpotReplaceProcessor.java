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
                    ResourceLocation.CODEC.fieldOf("toReplace").forGetter(data -> data.toReplace)
            ).apply(builder, GradientSpotReplaceProcessor::new));
    private final float rarity;
    private final List<ResourceLocation> gradientList;
    private final ResourceLocation toReplace;

    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public GradientSpotReplaceProcessor(float rarity, List<ResourceLocation> gradientList, ResourceLocation toReplace) {
        this.rarity = rarity;
        this.gradientList = gradientList;
        this.toReplace = toReplace;
    }

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
        setSeed(((ISeedReader) world).getSeed());

        BlockState blockstate = blockInfo.state;
        BlockPos blockPos = blockInfo.pos;
        if(!blockstate.getBlock().getRegistryName().getPath().equals(toReplace.getPath())){
            return blockInfo;
        }

        ResourceLocation newBlockResourceLocation = getReplacementBlockResourceLocation(blockPos);
        Block newBlock = BLOCKS.getValue(newBlockResourceLocation);
        if(newBlock == null){
            return blockInfo;
        }

        if (blockstate.getBlock().is(BlockTags.STAIRS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copyStairsState(blockstate, newBlock), blockInfo.nbt);
        } else if (blockstate.getBlock().is(BlockTags.SLABS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copySlabState(blockstate, newBlock), blockInfo.nbt);
        } else if (blockstate.getBlock().is(BlockTags.WALLS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copyWallState(blockstate, newBlock), blockInfo.nbt);
        }else{
            return new Template.BlockInfo(blockPos, newBlock.defaultBlockState(), blockInfo.nbt);
        }
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
