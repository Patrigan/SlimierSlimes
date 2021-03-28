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
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.init.ModProcessors.CEILING_ATTACHMENT;
import static mod.patrigan.slimierslimes.init.ModProcessors.VINES;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.VINE;
import static net.minecraft.block.VineBlock.PROPERTY_BY_DIRECTION;
import static net.minecraft.util.Direction.*;

public class VineProcessor extends StructureProcessor {
    public static final Codec<VineProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    Codec.BOOL.optionalFieldOf("attachToWall", true).forGetter(processor -> processor.attachToWall),
                    Codec.BOOL.optionalFieldOf("attachToCeiling", true).forGetter(processor -> processor.attachToCeiling)
            ).apply(builder, VineProcessor::new));
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
        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        List<Direction> possibleDirections = new ArrayList<>();
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(attachToWall){
                possibleDirections.addAll(getWallDirections(pieceBlocks, rawBlockInfo.pos));
            }
            if(attachToCeiling){
                Template.BlockInfo tempBlock = getBlock(pieceBlocks, rawBlockInfo.pos.above());
                if(tempBlock != null && !tempBlock.state.is(AIR)){
                    possibleDirections.add(NORTH);
                }
            }
        }
        if(!possibleDirections.isEmpty()){
            Direction direction = possibleDirections.get(random.nextInt(possibleDirections.size()));
            blockstate1 = VINE.defaultBlockState();
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            blockstate1.setValue(property, true);
        }
        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    private List<Direction> getWallDirections(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        List<Direction> directions = new ArrayList<>();
        Template.BlockInfo tempBlock = getBlock(pieceBlocks, pos.north());
        if(tempBlock != null && !tempBlock.state.is(AIR)){
            directions.add(NORTH);
        }
        tempBlock = getBlock(pieceBlocks, pos.south());
        if(tempBlock != null && !tempBlock.state.is(AIR)){
            directions.add(SOUTH);
        }
        tempBlock = getBlock(pieceBlocks, pos.west());
        if(tempBlock != null && !tempBlock.state.is(AIR)){
            directions.add(WEST);
        }
        tempBlock = getBlock(pieceBlocks, pos.east());
        if(tempBlock != null && !tempBlock.state.is(AIR)){
            directions.add(EAST);
        }
        return directions;
    }

    private Template.BlockInfo getBlock(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    protected IStructureProcessorType<?> getType() {
        return VINES;
    }
}
