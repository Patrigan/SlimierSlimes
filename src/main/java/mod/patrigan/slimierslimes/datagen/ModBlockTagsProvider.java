package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModItems;
import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.block.material.Material;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        ModBlocks.BLOCK_HELPERS.forEach(this::addBlocksToTags);
    }

    private void addBlocksToTags(BuildingBlockHelper blockHelper) {
        this.getOrCreateBuilder(BlockTags.SLABS).add(blockHelper.getSlab().get());
        this.getOrCreateBuilder(BlockTags.STAIRS).add(blockHelper.getStairs().get());
        this.getOrCreateBuilder(BlockTags.BUTTONS).add(blockHelper.getButton().get());
        this.getOrCreateBuilder(BlockTags.PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        this.getOrCreateBuilder(BlockTags.WALLS).add(blockHelper.getWall().get());
        if(Material.ROCK.equals(blockHelper.getBlock().get().getDefaultState().getMaterial())) {
            this.getOrCreateBuilder(BlockTags.STONE_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        }else{
            this.getOrCreateBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
        }
    }
}
