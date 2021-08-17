package mod.patrigan.slimier_slimes.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimier_slimes.init.ModBlocks.BASE_SLIMY_BLOCKS;
import static mod.patrigan.slimier_slimes.init.ModProcessors.BLOCK_SLIMIFY;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class BlockSlimifyProcessor extends StructureProcessor {
    public static final Codec<BlockSlimifyProcessor> CODEC = Codec.FLOAT.fieldOf("slimyness").xmap(BlockSlimifyProcessor::new, processor -> processor.slimyness).codec();
    private final float slimyness;

    public BlockSlimifyProcessor(float slimyness) {
        this.slimyness = slimyness;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos seedPos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        Random pieceRandom = settings.getRandom(piecePos);
        DyeColor dyeColor = DyeColor.byId(pieceRandom.nextInt(DyeColor.values().length));

        Random random = settings.getRandom(blockInfo.pos);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, dyeColor + "_slimy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock != null && !newBlock.defaultBlockState().isAir() && random.nextFloat() < slimyness){
            if (newBlock.getTags().contains(BlockTags.STAIRS.getName())) {
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (newBlock.getTags().contains(BlockTags.SLABS.getName())) {
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (newBlock.getTags().contains(BlockTags.WALLS.getName())) {
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else if (BASE_SLIMY_BLOCKS.contains(blockstate.getBlock())){
                blockstate1 = newBlock.defaultBlockState();
            }
        }

        return blockstate1 != null ? new StructureTemplate.StructureBlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return BLOCK_SLIMIFY;
    }
}