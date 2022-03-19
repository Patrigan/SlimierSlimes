package mod.patrigan.slimier_slimes.datagen;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimier_slimes.init.ModBlocks;
import mod.patrigan.slimier_slimes.init.ModTags;
import net.minecraft.world.level.material.Material;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraft.world.level.block.Blocks.BROWN_MUSHROOM;
import static net.minecraft.world.level.block.Blocks.RED_MUSHROOM;


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
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getBlock().get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getSlab().get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getStairs().get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getWall().get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getButton().get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getPressurePlate().get());
        if(Material.STONE.equals(blockHelper.getBlock().get().defaultBlockState().getMaterial())) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getBlock().get());
            this.tag(BlockTags.STONE_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getSlab().get());
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getStairs().get());
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getWall().get());
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getButton().get());
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockHelper.getPressurePlate().get());
        }else{
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getBlock().get());
            this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockHelper.getBlock().get());
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(blockHelper.getPressurePlate().get());
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getSlab().get());
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getStairs().get());
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getWall().get());
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getButton().get());
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blockHelper.getPressurePlate().get());
        }
    }
}
