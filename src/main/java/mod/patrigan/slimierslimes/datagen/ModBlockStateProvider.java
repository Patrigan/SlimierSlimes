package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerSlimyStone();
        registerBlock(AMETHYST_CLUSTER);
        registerBlock(SMALL_AMETHYST_BUD);
        registerBlock(MEDIUM_AMETHYST_BUD);
        registerBlock(LARGE_AMETHYST_BUD);
    }

    private void registerSlimyStone() {
        ResourceLocation slimyStoneTexture = modBlockTexture(SLIMY_STONE_BLOCK.getId().getPath());
        BlockModelBuilder slimyStoneModel = models().withExistingParent("slimy_stone", "block/block").texture("outer", slimyStoneTexture).texture("inner", slimyStoneTexture)
                .element().from(3F, 3F, 3F).to(12F,12F,12F).allFaces((d, f) -> f.texture("#inner")).allFaces((d, f) -> f.uvs(3, 3, 13, 13)).end()
                .element().from(0F, 0F, 0F).to(16F,16F,16F).allFaces((d, f) -> f.texture("#outer")).allFaces((d, f) -> f.uvs(0, 0, 16, 16))
                    .faces((d, f) -> f.cullface(d)).end();
        getVariantBuilder(SLIMY_STONE_BLOCK.get())
                .partialState().setModels(new ConfiguredModel(slimyStoneModel));
    }

    private void registerBlock(RegistryObject<Block> block) {
        String id = block.getId().getPath();
        ResourceLocation texture = modBlockTexture(id);
        BlockModelBuilder blockModel = models().withExistingParent(id, "block/cross").texture("cross", texture);
        getVariantBuilder(block.get())
                .partialState().setModels(new ConfiguredModel(blockModel));
    }

    public ResourceLocation modBlockTexture(String name) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + name);
    }
}
