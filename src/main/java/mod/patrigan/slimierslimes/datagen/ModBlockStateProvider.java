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

import static mod.patrigan.slimierslimes.init.ModBlocks.*;
import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;
import static net.minecraft.block.HorizontalFaceBlock.FACE;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SlimierSlimes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BLOCK_HELPERS.forEach(this::registerBuildingBlockHelper
        );
        registerLavaSlimeSpawnerBlock(STONE_LAVA_SLIME_SPAWNER, modLoc(SLIMY_STONE_BLOCK.get(DyeColor.RED).getId()));
        registerLavaSlimeSpawnerBlock(NETHERRACK_LAVA_SLIME_SPAWNER, modLoc(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getId()));
        registerCrossBlock(AMETHYST_CLUSTER);
        registerCrossBlock(SMALL_AMETHYST_BUD);
        registerCrossBlock(MEDIUM_AMETHYST_BUD);
        registerCrossBlock(LARGE_AMETHYST_BUD);
    }

    private void registerBuildingBlockHelper(BuildingBlockHelper buildingBlockHelper){
        if(buildingBlockHelper.getDyeColor() == null){
            registerBuildingBlock(buildingBlockHelper);
        }else if(buildingBlockHelper.isSlimy()){
            registerSlimyBlock(buildingBlockHelper);
        }else{
            registerSlimeBlock(buildingBlockHelper);
        }
    }

    private void registerLavaSlimeSpawnerBlock(RegistryObject<Block> block, ResourceLocation parent) {
        String id = block.getId().getPath();
        BlockModelBuilder blockModel = models().withExistingParent(id, parent);
        getVariantBuilder(block.get())
                .partialState().setModels(new ConfiguredModel(blockModel));
    }

    private void registerCrossBlock(RegistryObject<Block> block) {
        String id = block.getId().getPath();
        ResourceLocation texture = modBlockLoc(id);
        BlockModelBuilder blockModel = models().withExistingParent(id, "block/cross").texture("cross", texture);
        getVariantBuilder(block.get())
                .partialState().setModels(new ConfiguredModel(blockModel));
    }

    public ResourceLocation modBlockLoc(String name) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + name);
    }

    private void registerBuildingBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockLoc(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), "block_block").texture("particle", texture).texture("texture", texture);
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

    private void registerSlimeBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockLoc(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), modBlockLoc("slime_block")).texture("particle", texture).texture("texture", texture);
        simpleBlock(blockHelper.getBlock().get(), model);
        modSlabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getBlock().get().getRegistryName(), texture, "colored_slab");
        modStairsBlock((StairsBlock)  blockHelper.getStairs().get(), texture, "colored_stairs");
        modWallBlock((WallBlock) blockHelper.getWall().get(), texture, "colored_wall");
        modButtonBlock(blockHelper, "colored_button", texture);
        modPressurePlateBlock(blockHelper, "colored_pressure_plate", texture);
    }

    private void registerSlimyBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockLoc(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), modLoc("block/slimy_block")).texture("texture", texture);
        simpleBlock(blockHelper.getBlock().get(), model);
        modSlabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getBlock().get().getRegistryName(), texture, "slimy_slab");
        modStairsBlock((StairsBlock)  blockHelper.getStairs().get(), texture, "slimy_stairs");
        modWallBlock((WallBlock) blockHelper.getWall().get(), texture, "slimy_wall");
        modButtonBlock(blockHelper, "slimy_button", texture);
        modPressurePlateBlock(blockHelper, "slimy_pressure_plate", texture);
    }

    private void modSlabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture, String parent) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder bottomModel = models().withExistingParent(name, modBlockLoc(parent)).texture("texture", texture);
        BlockModelBuilder topModel = models().withExistingParent(name + "_top", modBlockLoc(parent + "_top")).texture("texture", texture);
        slabBlock(block, bottomModel, topModel, models().getExistingFile(doubleslab));
    }

    private void modStairsBlock(StairsBlock block, ResourceLocation texture, String parent) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder standardModel = models().withExistingParent(name, modBlockLoc(parent)).texture("texture", texture);
        BlockModelBuilder innerModel = models().withExistingParent(name + "_inner", modBlockLoc(parent + "_inner")).texture("texture", texture);
        BlockModelBuilder outerModel = models().withExistingParent(name + "_outer", modBlockLoc(parent + "_outer")).texture("texture", texture);
        stairsBlock(block, standardModel, innerModel, outerModel);
    }

    private void modWallBlock(WallBlock block, ResourceLocation texture, String parent) {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder post = models().withExistingParent(name, modBlockLoc(parent + "_post")).texture("wall", texture);
        BlockModelBuilder side = models().withExistingParent(name + "_inner", modBlockLoc(parent + "_side")).texture("wall", texture);
        BlockModelBuilder sideTall = models().withExistingParent(name + "_outer", modBlockLoc(parent + "_side_tall")).texture("wall", texture);
        wallBlock(block, post, side, sideTall);
        models().withExistingParent(name + "_inventory", modBlockLoc(parent + "_inventory")).texture("wall", texture);
    }

    private void modButtonBlock(BuildingBlockHelper blockHelper, String parent, ResourceLocation texture) {
        getVariantBuilder(blockHelper.getButton().get()).forAllStates(state -> {
            ModelFile buttonModel = modButtonModel(blockHelper, state.get(AbstractButtonBlock.POWERED), parent, texture);
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
        modButtonInventory(blockHelper, parent);
    }

    private ModelFile modButtonModel(BuildingBlockHelper blockHelper, boolean powered, String parent, ResourceLocation texture) {
        String suffix = powered ? "_pressed" : "";
        return models().singleTexture(blockHelper.getButton().get().getRegistryName().getPath() + suffix, modBlockLoc(parent + suffix), "texture", texture);
    }

    private void modButtonInventory(BuildingBlockHelper blockHelper, String parent) {
        String suffix = "_inventory";
        models().singleTexture(blockHelper.getButton().get().getRegistryName().getPath() + suffix, modBlockLoc(parent + suffix), "texture", modBlockLoc(blockHelper.getBlockId()));
    }

    private void modPressurePlateBlock(BuildingBlockHelper blockHelper, String parent, ResourceLocation texture) {
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> ConfiguredModel.builder().modelFile(modPressurePlateModel(blockHelper, state.get(PressurePlateBlock.POWERED), parent, texture)).build());
    }

    private ModelFile modPressurePlateModel(BuildingBlockHelper blockHelper, boolean powered, String parent, ResourceLocation texture) {
        String suffix = powered ? "_down" : "";
        String parentName = parent + suffix + (powered ? "" : "_up");
        return models().withExistingParent(blockHelper.getPressurePlate().get().getRegistryName().getPath() + suffix, modBlockLoc(parentName))
                .texture("texture", texture);
    }
}