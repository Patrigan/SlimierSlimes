package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModProcessors.AIR_RETAINER;

public class AirRetainerProcessor extends StructureProcessor {
    public static final Codec<AirRetainerProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.optionalFieldOf("rarity", 1F).forGetter(processor -> processor.rarity),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("to_replace", new ArrayList<>()).forGetter(data -> data.toReplace)
            ).apply(builder, AirRetainerProcessor::new));
    private static final long SEED = 478924L;

    private final float rarity;
    private final List<ResourceLocation> toReplace;

    public AirRetainerProcessor(float rarity, List<ResourceLocation> toReplace) {
        this.rarity = rarity;
        this.toReplace = toReplace;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(blockInfo.pos, SEED);
        if(world.isEmptyBlock(blockInfo.pos) && random.nextFloat() < rarity && (toReplace.isEmpty() || toReplace.contains(blockInfo.state.getBlock().getRegistryName()))){
            BlockState blockState = world.getBlockState(blockInfo.pos);
            return new Template.BlockInfo(blockInfo.pos, blockState, null);
        }else {
            return blockInfo;
        }
    }

    protected IStructureProcessorType<?> getType() {
        return AIR_RETAINER;
    }
}