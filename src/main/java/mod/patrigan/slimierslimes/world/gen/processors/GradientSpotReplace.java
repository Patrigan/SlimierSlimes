package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimierslimes.init.ModBlocks.BASE_SLIMY_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModProcessors.GRADIENT_SPOT_REPLACE;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class GradientSpotReplace extends StructureProcessor {
    public static final Codec<GradientSpotReplace> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    ResourceLocation.CODEC.listOf().fieldOf("gradientList").forGetter(data -> data.gradientList)
            ).apply(builder, GradientSpotReplace::new));
    private final float rarity;
    private final List<ResourceLocation> gradientList;

    public GradientSpotReplace(float rarity, List<ResourceLocation> gradientList) {
        this.rarity = rarity;
        this.gradientList = gradientList;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
        Random pieceRandom = settings.getRandom(piecePos);
        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate1 = null;

        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        ResourceLocation registryName = blockstate.getBlock().getRegistryName();
        if(registryName.equals(gradientList.get(0))) {
            Block newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, blockstate.getBlock().getRegistryName().getPath()));
            if (newBlock != null && !newBlock.equals(AIR) && random.nextFloat() < rarity) {
                if (newBlock.is(BlockTags.STAIRS)) {
                    blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
                } else if (newBlock.is(BlockTags.SLABS)) {
                    blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
                } else if (newBlock.is(BlockTags.WALLS)) {
                    blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
                } else if (BASE_SLIMY_BLOCKS.contains(blockstate.getBlock())) {
                    blockstate1 = newBlock.defaultBlockState();
                }
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return GRADIENT_SPOT_REPLACE;
    }
}
