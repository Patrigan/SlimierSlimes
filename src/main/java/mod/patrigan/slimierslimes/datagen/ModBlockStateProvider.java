package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerSlimyStone(SLIMY_STONE_BLOCK);
        registerSlimyStone(SLIMY_COBBLESTONE_BLOCK);
        registerBlock(AMETHYST_CLUSTER);
        registerBlock(SMALL_AMETHYST_BUD);
        registerBlock(MEDIUM_AMETHYST_BUD);
        registerBlock(LARGE_AMETHYST_BUD);
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

    private void registerSlimyStone(BuildingBlockHelper blockHelper) {
        String path = blockHelper.getBlock().get().getRegistryName().getPath();
        ResourceLocation texture = modBlockTexture(path);
        BlockModelBuilder model = models().withExistingParent(path, "block/block").texture("particle", texture).texture("outer", texture).texture("inner", texture)
                .element().from(3F, 3F, 3F).to(12F,12F,12F).allFaces((d, f) -> f.texture("#inner")).allFaces((d, f) -> f.uvs(3, 3, 13, 13)).end()
                .element().from(0F, 0F, 0F).to(16F,16F,16F).allFaces((d, f) -> f.texture("#outer")).allFaces((d, f) -> f.uvs(0, 0, 16, 16))
                /*.faces((d, f) -> f.cullface(d))*/.end()
                .element().from(0F, 0F, 0F).to(16F,0F,16F).face(Direction.UP).texture("#outer").end().end()
                .element().from(0F, 16F, 0F).to(16F,16F,16F).face(Direction.DOWN).texture("#outer").end().end()
                .element().from(0F, 0F, 0F).to(16F,16F,0F).face(Direction.SOUTH).texture("#outer").end().end()
                .element().from(0F, 0F, 0F).to(0F,16F,16F).face(Direction.EAST).texture("#outer").end().end()
                .element().from(0F, 0F, 16F).to(16F,16F,16F).face(Direction.NORTH).texture("#outer").end().end()
                .element().from(16F, 0F, 0F).to(16F,16F,16F).face(Direction.WEST).texture("#outer").end().end();
        getVariantBuilder(blockHelper.getBlock().get())
                .partialState().setModels(new ConfiguredModel(model));
        slabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getSlab().get().getRegistryName(), texture);
        stairsBlock((StairsBlock)  blockHelper.getStairs().get(), texture);
        wallBlock((WallBlock) blockHelper.getWall().get(), texture);
        models().wallInventory(blockHelper.getWall().get().getRegistryName().getPath() + "_inventory", texture);
        buttonBlock(blockHelper);
        buttonInventory(blockHelper);
        pressurePlateBlock(blockHelper);
    }

    private void buttonBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getButton().get()).forAllStates(state -> {
            ModelFile buttonModel = buttonModel(blockHelper, state.get(AbstractButtonBlock.POWERED));
            AttachFace face = state.get(AbstractButtonBlock.FACE);
            Direction dir = state.get(AbstractButtonBlock.HORIZONTAL_FACING);
            int xrot = 0;
            if (face == AttachFace.CEILING) {
                dir = dir.getOpposite();
                xrot = 180;
            } else if (face == AttachFace.WALL) {
                xrot = 90;
            }
            return ConfiguredModel.builder().modelFile(buttonModel)
                    .rotationY((int) dir.getHorizontalAngle())
                    .rotationX(xrot)
                    .uvLock(face == AttachFace.WALL).build();
        });
    }

    private ModelFile buttonModel(BuildingBlockHelper blockId, boolean powered) {
        String suffix = "button" + (powered ? "_pressed" : "");
        return models().singleTexture(blockId.getButton().get().getRegistryName().getPath() + "_" + suffix, mcLoc("block/" + suffix), "texture", modLoc("block/" + blockId.getBlockId()));
    }

    private void buttonInventory(BuildingBlockHelper blockHelper) {
        String suffix = "button_inventory";
        models().singleTexture(blockHelper.getBlockId() + "_" + suffix, mcLoc("block/" + suffix), "texture", modLoc("block/" + blockHelper.getBlockId()));
    }

    private void pressurePlateBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> {
            return ConfiguredModel.builder().modelFile(pressurePlateModel(blockHelper, state.get(PressurePlateBlock.POWERED))).build();
        });
    }

    private ModelFile pressurePlateModel(BuildingBlockHelper blockHelper, boolean powered) {
        String modelName = "pressure_plate" + (powered ? "_down" : "");
        String parentName = modelName + (powered ? "" : "_up");
        return models().withExistingParent(blockHelper.getBlockId() + "_" + modelName, mcLoc("block/" + parentName))
                .texture("texture", modLoc("block/" + blockHelper.getBlockId()));
    }
}