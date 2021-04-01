package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
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
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModProcessors.VINES;
import static mod.patrigan.slimierslimes.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.block.Blocks.*;
import static net.minecraft.block.VineBlock.PROPERTY_BY_DIRECTION;
import static net.minecraft.util.Direction.*;

public class VineProcessor extends StructureProcessor {
    public static final Codec<VineProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    Codec.BOOL.optionalFieldOf("attachToWall", true).forGetter(processor -> processor.attachToWall),
                    Codec.BOOL.optionalFieldOf("attachToCeiling", true).forGetter(processor -> processor.attachToCeiling)
            ).apply(builder, VineProcessor::new));
    private static final long SEED = 8514174L;
    private final float rarity;
    private final boolean attachToWall;
    private final boolean attachToCeiling;

    public VineProcessor(float rarity, boolean attachToWall, boolean attachToCeiling) {
        this.rarity = rarity;
        this.attachToWall = attachToWall;
        this.attachToCeiling = attachToCeiling;
    }

    @Nullable
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = getRandom(blockInfo.pos, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        List<Direction> possibleDirections = new ArrayList<>();
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(attachToWall){
                possibleDirections.addAll(Arrays.asList(NORTH, EAST, SOUTH, WEST));
            }
            if(attachToCeiling){
                possibleDirections.add(UP);
            }
            possibleDirections = possibleDirections.stream().filter(direction -> isDirectionPossible(pieceBlocks, rawBlockInfo.pos, direction)).collect(Collectors.toList());
        }
        if(possibleDirections.isEmpty()){
            return blockInfo;
        }else{
            Direction direction = possibleDirections.get(random.nextInt(possibleDirections.size()));
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            return new Template.BlockInfo(blockpos, VINE.defaultBlockState().setValue(property, true), blockInfo.nbt);
        }
    }

    private boolean isDirectionPossible(List<Template.BlockInfo> pieceBlocks, BlockPos pos, Direction direction){
        Template.BlockInfo tempBlock = getBlock(pieceBlocks, pos.relative(direction));
        return isFaceFull(tempBlock, direction.getOpposite());
    }

    protected IStructureProcessorType<?> getType() {
        return VINES;
    }
}
