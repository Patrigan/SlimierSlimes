package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimierslimes.init.ModBlocks.BASE_SLIMY_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModProcessors.BLOCK_MOSSIFY;
import static mod.patrigan.slimierslimes.init.ModProcessors.BLOCK_SLIMIFY;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class BlockSlimifyProcessor extends StructureProcessor {
    public static final Codec<BlockSlimifyProcessor> CODEC = Codec.FLOAT.fieldOf("slimyness").xmap(BlockSlimifyProcessor::new, processor -> processor.slimyness).codec();
    private final float slimyness;
    //private final String dyeColor;

    public BlockSlimifyProcessor(float slimyness) {
        this.slimyness = slimyness;
        //this.dyeColor = dyeColor;
    }

    @Nullable
    public Template.BlockInfo func_230386_a_(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings) {
        /*DyeColor dyeColorObject;
        if(dyeColor.equals("RANDOM")){
            DyeColor
        }*/
        Random pieceRandom = settings.getRandom(piecePos);
        DyeColor dyeColor = DyeColor.byId(pieceRandom.nextInt(DyeColor.values().length));

        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, dyeColor.getTranslationKey() + "_slimy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock != null && !newBlock.equals(AIR) && random.nextFloat() < slimyness){
            if (newBlock.isIn(BlockTags.STAIRS)) {
                blockstate1 = this.getStairs(blockstate, newBlock);
            } else if (newBlock.isIn(BlockTags.SLABS)) {
                blockstate1 = this.getSlab(blockstate, newBlock);
            } else if (newBlock.isIn(BlockTags.WALLS)) {
                blockstate1 = this.getWall(blockstate, newBlock);
            }else if (BASE_SLIMY_BLOCKS.contains(blockstate.getBlock())){
                blockstate1 = this.getBlock(blockstate, newBlock);
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    private BlockState getBlock(BlockState blockstate, Block newBlock) {
        return newBlock.getDefaultState();
    }

    private BlockState getStairs(BlockState state, Block newBlock) {
        return newBlock.getDefaultState().with(StairsBlock.FACING, state.get(StairsBlock.FACING)).with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE)).with(StairsBlock.HALF, state.get(StairsBlock.HALF)).with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED));
    }

    private BlockState getSlab(BlockState blockstate, Block newBlock) {
        return newBlock.getDefaultState().with(SlabBlock.TYPE, blockstate.get(SlabBlock.TYPE));
    }

    private BlockState getWall(BlockState blockstate, Block newBlock) {
        return newBlock.getDefaultState()
                .with(WallBlock.UP, blockstate.get(WallBlock.UP))
                .with(WallBlock.WALL_HEIGHT_EAST, blockstate.get(WallBlock.WALL_HEIGHT_EAST))
                .with(WallBlock.WALL_HEIGHT_NORTH, blockstate.get(WallBlock.WALL_HEIGHT_NORTH))
                .with(WallBlock.WALL_HEIGHT_SOUTH, blockstate.get(WallBlock.WALL_HEIGHT_SOUTH))
                .with(WallBlock.WALL_HEIGHT_WEST, blockstate.get(WallBlock.WALL_HEIGHT_WEST))
                .with(WallBlock.WATERLOGGED, blockstate.get(WallBlock.WATERLOGGED));
    }

    protected IStructureProcessorType<?> getType() {
        return BLOCK_SLIMIFY;
    }
}