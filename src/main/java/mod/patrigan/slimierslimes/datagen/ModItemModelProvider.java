package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModBlocks.BLOCK_HELPERS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            generated(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_jelly")).getRegistryName().getPath(), modLoc("item/jelly"));
            generated(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_slime_ball")).getRegistryName().getPath(), modLoc("item/slime_ball"));
        });
        registerBlockItems();
        registerSpawnEggItems();
    }

    private void registerBlockItems() {
        BLOCK_HELPERS.forEach(this::registerBuildingBlockItems);
    }

    private void registerBuildingBlockItems(BuildingBlockHelper blockHelper) {
        blockItemModel(blockHelper.getBlock().get());
        blockItemModel(blockHelper.getStairs().get());
        blockItemModel(blockHelper.getSlab().get());
        blockInventoryModel(blockHelper.getWall().get());
        blockInventoryModel(blockHelper.getButton().get());
        blockItemModel(blockHelper.getPressurePlate().get());
    }

    private void blockItemModel(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name)));
    }

    private void blockInventoryModel(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name + "_inventory")));
    }

    private void registerSpawnEggItems() {
        ModEntityTypes.ENTITY_IDS.forEach(key ->
                getBuilder(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, key + "_spawn_egg")).getRegistryName().getPath())
                        .parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_spawn_egg"))));
    }

    private void generated(String path, ResourceLocation texture) {
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated"))).texture("layer0", texture);
    }
}
