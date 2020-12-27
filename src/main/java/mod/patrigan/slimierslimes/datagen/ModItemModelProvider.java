package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static mod.patrigan.slimierslimes.datagen.DataGenerators.DYE_ITEMS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        DYE_ITEMS.forEach(dyeItem -> {
            String dyeColor = ((DyeItem) dyeItem).getDyeColor().getTranslationKey();
            generated(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, dyeColor + "_jelly")).getRegistryName().getPath(), modLoc("item/jelly/"+dyeColor));
        });
        registerItemBlock("slimy_stone");
        registerItemBlock("amethyst_cluster");
    }

    private void registerItemBlock(String id) {
        getBuilder(ITEMS.getValue(new ResourceLocation(SlimierSlimes.MOD_ID, id)).getRegistryName().getPath())
                .parent(new ModelFile.UncheckedModelFile(mcLoc("slimier-slimes:block/"+id)));
    }

    private void generated(String path, ResourceLocation texture) {
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated"))).texture("layer0", texture);
    }
}
