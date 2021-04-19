package mod.patrigan.slimierslimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimierslimes.init.ModBlocks.BASE_SLIMY_BLOCKS;
import static mod.patrigan.slimierslimes.init.ModProcessors.BLOCK_SLIMIFY;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class BlockSlimifyProcessor extends StructureProcessor {
    public static final Codec<BlockSlimifyProcessor> CODEC = Codec.FLOAT.fieldOf("slimyness").xmap(BlockSlimifyProcessor::new, processor -> processor.slimyness).codec();
    private final float slimyness;

    public BlockSlimifyProcessor(float slimyness) {
        this.slimyness = slimyness;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random pieceRandom = settings.getRandom(piecePos);
        DyeColor dyeColor = DyeColor.byId(pieceRandom.nextInt(DyeColor.values().length));

        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, dyeColor + "_slimy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock != null && !newBlock.equals(AIR) && random.nextFloat() < slimyness){
            if (newBlock.is(BlockTags.STAIRS)) {
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.SLABS)) {
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.WALLS)) {
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else if (BASE_SLIMY_BLOCKS.contains(blockstate.getBlock())){
                blockstate1 = newBlock.defaultBlockState();
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return BLOCK_SLIMIFY;
    }
}