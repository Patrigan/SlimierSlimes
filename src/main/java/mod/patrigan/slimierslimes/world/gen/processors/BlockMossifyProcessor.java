package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
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
import static mod.patrigan.slimierslimes.init.ModProcessors.BLOCK_MOSSIFY;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class BlockMossifyProcessor extends StructureProcessor {
    public static final Codec<BlockMossifyProcessor> CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockMossifyProcessor::new, processor -> processor.mossiness).codec();
    private final float mossiness;

    public BlockMossifyProcessor(float mossiness) {
        this.mossiness = mossiness;
    }

    @Nullable
    public Template.BlockInfo processBlock(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings) {
        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation("mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock == null || newBlock.equals(AIR) ){
            newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, "mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        }
        if(newBlock != null && !newBlock.equals(AIR) && random.nextFloat() < mossiness){
            if (newBlock.is(BlockTags.STAIRS)) {
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.SLABS)) {
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.WALLS)) {
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else if (blockstate.getBlock().equals(Blocks.COBBLESTONE) || blockstate.getBlock().equals(Blocks.STONE_BRICKS)){
                blockstate1 = newBlock.defaultBlockState();
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return BLOCK_MOSSIFY;
    }
}