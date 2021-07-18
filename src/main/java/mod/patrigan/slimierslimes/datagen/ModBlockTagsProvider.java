package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.block.material.Material;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraft.block.Blocks.BROWN_MUSHROOM;
import static net.minecraft.block.Blocks.RED_MUSHROOM;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModBlocks.BLOCK_HELPERS.forEach(this::addBlocksToTags);
        vanillaMushrooms();
    }

    private void vanillaMushrooms() {
        this.tag(ModTags.Blocks.MUSHROOMS).add(RED_MUSHROOM);
        this.tag(ModTags.Blocks.MUSHROOMS).add(BROWN_MUSHROOM);
    }

    private void addBlocksToTags(BuildingBlockHelper blockHelper) {
        this.tag(BlockTags.SLABS).add(blockHelper.getSlab().get());
        this.tag(BlockTags.STAIRS).add(blockHelper.getStairs().get());
        this.tag(BlockTags.BUTTONS).add(blockHelper.getButton().get());
        this.tag(BlockTags.PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        this.tag(BlockTags.WALLS).add(blockHelper.getWall().get());
        if(Material.STONE.equals(blockHelper.getBlock().get().defaultBlockState().getMaterial())) {
            this.tag(BlockTags.STONE_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        }else{
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        }
    }
}
