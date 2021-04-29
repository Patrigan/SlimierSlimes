package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.init.ModProcessors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.Tags;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static mod.patrigan.slimierslimes.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.FLOWER_POT;
import static net.minecraft.tags.BlockTags.*;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

public class FlowerPotProcessor extends StructureProcessor {
    public static final Codec<FlowerPotProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("include_trees", true).forGetter(processor -> processor.includeTrees),
                    Codec.BOOL.optionalFieldOf("include_flowers", true).forGetter(processor -> processor.includeFlowers),
                    Codec.BOOL.optionalFieldOf("perPiece", false).forGetter(data -> data.perPiece),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("blacklist", emptyList()).forGetter(data -> data.blacklist)
            ).apply(builder, FlowerPotProcessor::new));
    private static final long SEED = 9348841L;
    private final boolean includeTrees;
    private final boolean includeFlowers;
    private final boolean perPiece;
    private final List<ResourceLocation> blacklist;

    public FlowerPotProcessor(boolean includeTrees, boolean includeFlowers, boolean perPiece, List<ResourceLocation> blacklist) {
        this.includeTrees = includeTrees;
        this.includeFlowers = includeFlowers;
        this.perPiece = perPiece;
        this.blacklist = blacklist;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(blockInfo.pos, SEED);
        Random pieceRandom = settings.getRandom(piecePos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(FLOWER_POT)){
            List<Block> flowerPotList = FLOWER_POTS.getValues().stream().filter(this::flowerPotFilter).collect(Collectors.toList());
            return new Template.BlockInfo(blockpos, flowerPotList.get(perPiece ? pieceRandom.nextInt(flowerPotList.size()) : random.nextInt(flowerPotList.size())).defaultBlockState(), blockInfo.nbt);
        }
        return blockInfo;
    }

    private boolean flowerPotFilter(Block block) {
        if(!(block instanceof FlowerPotBlock)){
            return false;
        }
        FlowerPotBlock flowerPotBlock = (FlowerPotBlock) block;
        Block content = flowerPotBlock.getContent();
        if(includeTrees && content.is(SAPLINGS) && !blacklist.contains(content.getRegistryName())){
            return true;
        }
        if(includeFlowers && content.is(FLOWERS) && !blacklist.contains(content.getRegistryName())){
            return true;
        }
        if(content.defaultBlockState().isAir() && !blacklist.contains(content.getRegistryName())){
            return true;
        }
        return false;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.FLOWER_POT;
    }
}