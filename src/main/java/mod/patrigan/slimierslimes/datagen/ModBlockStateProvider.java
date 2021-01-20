package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;
import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;
import static net.minecraft.block.HorizontalFaceBlock.FACE;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            registerSlimyBlock(SLIMY_STONE_BLOCK.get(dyeColor));
            registerSlimyBlock(SLIMY_COBBLESTONE_BLOCK.get(dyeColor));
        });
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

    private void registerBuildingBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockTexture(blockHelper.getBlockId());
        ResourceLocation slimeTexture = modBlockTexture("slimy_slime");
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), "block/block").texture("particle", texture).texture("outer", texture).texture("inner", texture).texture("slime", slimeTexture)
                .element().from(3F, 3F, 3F).to(12F,12F,12F).allFaces((d, f) -> f.texture("#inner").uvs(3, 3, 13, 13)).end()
                .element().from(0F, 0F, 0F).to(16F,16F,16F).allFaces((d, f) -> f.texture("#outer").uvs(0, 0, 16, 16)).end()
                .element().from(0F, 0F, 0F).to(16F,0F,16F).face(Direction.UP).texture("#outer").end().end()
                .element().from(0F, 16F, 0F).to(16F,16F,16F).face(Direction.DOWN).texture("#outer").end().end()
                .element().from(0F, 0F, 0F).to(16F,16F,0F).face(Direction.SOUTH).texture("#outer").end().end()
                .element().from(0F, 0F, 0F).to(0F,16F,16F).face(Direction.EAST).texture("#outer").end().end()
                .element().from(0F, 0F, 16F).to(16F,16F,16F).face(Direction.NORTH).texture("#outer").end().end()
                .element().from(16F, 0F, 0F).to(16F,16F,16F).face(Direction.WEST).texture("#outer").end().end();
        simpleBlock(blockHelper.getBlock().get(), model);
        slabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getSlab().get().getRegistryName(), texture);
        stairsBlock((StairsBlock)  blockHelper.getStairs().get(), texture);
        wallBlock((WallBlock) blockHelper.getWall().get(), texture);
        models().wallInventory(blockHelper.getWall().get().getRegistryName().getPath() + "_inventory", texture);
        buttonBlock(blockHelper);
        pressurePlateBlock(blockHelper);
    }

    private void buttonBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getButton().get()).forAllStates(state -> {
            ModelFile buttonModel = buttonModel(blockHelper, state.get(AbstractButtonBlock.POWERED));
            AttachFace face = state.get(FACE);
            Direction dir = state.get(HORIZONTAL_FACING);
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
        buttonInventory(blockHelper);
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
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> ConfiguredModel.builder().modelFile(pressurePlateModel(blockHelper, state.get(PressurePlateBlock.POWERED))).build());
    }

    private ModelFile pressurePlateModel(BuildingBlockHelper blockHelper, boolean powered) {
        String modelName = "pressure_plate" + (powered ? "_down" : "");
        String parentName = modelName + (powered ? "" : "_up");
        return models().withExistingParent(blockHelper.getBlockId() + "_" + modelName, mcLoc("block/" + parentName))
                .texture("texture", modLoc("block/" + blockHelper.getBlockId()));
    }

    private void registerSlimyBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockTexture(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), modLoc("block/slimy_block")).texture("outer", texture).texture("inner", texture);
        simpleBlock(blockHelper.getBlock().get(), model);
        slimySlabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getBlock().get().getRegistryName(), texture);
        slimyStairsBlock((StairsBlock)  blockHelper.getStairs().get(), texture);
        slimyWallBlock((WallBlock) blockHelper.getWall().get(), texture);
        slimyButtonBlock(blockHelper);
        slimyPressurePlateBlock(blockHelper);
    }

    private void slimySlabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder bottomModel = models().withExistingParent(name, modLoc("block/slimy_slab")).texture("primary", texture);
        BlockModelBuilder topModel = models().withExistingParent(name + "_top", modLoc("block/slimy_slab_top")).texture("primary", texture);
        slabBlock(block, bottomModel, topModel, models().getExistingFile(doubleslab));
    }

    private void slimyStairsBlock(StairsBlock block, ResourceLocation texture) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder standardModel = models().withExistingParent(name, modLoc("block/slimy_stairs")).texture("primary", texture);
        BlockModelBuilder innerModel = models().withExistingParent(name + "_inner", modLoc("block/slimy_stairs_inner")).texture("primary", texture);
        BlockModelBuilder outerModel = models().withExistingParent(name + "_outer", modLoc("block/slimy_stairs_outer")).texture("primary", texture);
        stairsBlock(block, standardModel, innerModel, outerModel);
    }

    private void slimyWallBlock(WallBlock block, ResourceLocation texture) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder post = models().withExistingParent(name, modLoc("block/slimy_wall_post")).texture("wall", texture);
        BlockModelBuilder side = models().withExistingParent(name + "_inner", modLoc("block/slimy_wall_side")).texture("wall", texture);
        BlockModelBuilder sideTall = models().withExistingParent(name + "_outer", modLoc("block/slimy_wall_side_tall")).texture("wall", texture);
        wallBlock(block, post, side, sideTall);
        models().withExistingParent(name + "_inventory", modLoc("block/slimy_wall_inventory")).texture("wall", texture);
    }

    private void slimyButtonBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getButton().get()).forAllStates(state -> {
            ModelFile buttonModel = slimyButtonModel(blockHelper, state.get(AbstractButtonBlock.POWERED));
            AttachFace face = state.get(FACE);
            Direction dir = state.get(HORIZONTAL_FACING);
            int xrot = 0;
            if (face == AttachFace.CEILING) {
                dir = dir.getOpposite();
                xrot = 180;
            } else if (face == AttachFace.WALL) {
                xrot = 270;
            }
            return ConfiguredModel.builder().modelFile(buttonModel)
                    .rotationY((int) dir.getHorizontalAngle())
                    .rotationX(xrot)
                    .uvLock(face == AttachFace.WALL).build();
        });
        slimyButtonInventory(blockHelper);
    }

    private ModelFile slimyButtonModel(BuildingBlockHelper blockHelper, boolean powered) {
        String suffix = "button" + (powered ? "_pressed" : "");
        return models().singleTexture(blockHelper.getButton().get().getRegistryName().getPath() + "_" + suffix, modLoc("block/slimy_" + suffix), "texture", modLoc("block/" + blockHelper.getBlockId()));
    }

    private void slimyButtonInventory(BuildingBlockHelper blockHelper) {
        String suffix = "button_inventory";
        models().singleTexture(blockHelper.getId() + "_" + suffix, modLoc("block/slimy_" + suffix), "texture", modLoc("block/" + blockHelper.getBlockId()));
    }

    private void slimyPressurePlateBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> ConfiguredModel.builder().modelFile(slimyPressurePlateModel(blockHelper, state.get(PressurePlateBlock.POWERED))).build());
    }

    private ModelFile slimyPressurePlateModel(BuildingBlockHelper blockHelper, boolean powered) {
        String modelName = "pressure_plate" + (powered ? "_down" : "");
        String parentName = "slimy_" + modelName + (powered ? "" : "_up");
        return models().withExistingParent(blockHelper.getId() + "_" + modelName, modLoc("block/" + parentName))
                .texture("texture", modLoc("block/" + blockHelper.getBlockId()));
    }
}