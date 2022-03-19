package mod.patrigan.slimier_slimes.datagen;

import mod.patrigan.slimier_slimes.blocks.BuildingBlockHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimier_slimes.blocks.GooLayerBlock.LAYERS;
import static mod.patrigan.slimier_slimes.init.ModBlocks.*;
import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BLOCK_HELPERS.forEach(this::registerBuildingBlockHelper
        );
        GOO_LAYER_BLOCKS.forEach((dyeColor, blockRegistryObject) ->
                registerLayeredBlock(blockRegistryObject, modBlockLoc("slime_block"))
        );
        registerLavaSlimeSpawnerBlock(STONE_LAVA_SLIME_SPAWNER, modLoc(SLIMY_STONE_BLOCK.get(DyeColor.RED).getId()));
        registerLavaSlimeSpawnerBlock(NETHERRACK_LAVA_SLIME_SPAWNER, modLoc(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getId()));
        registerCrossBlock(AMETHYST_CLUSTER);
        registerCrossBlock(SMALL_AMETHYST_BUD);
        registerCrossBlock(MEDIUM_AMETHYST_BUD);
        registerCrossBlock(LARGE_AMETHYST_BUD);
    }

    private void registerLayeredBlock(RegistryObject<Block> blockRegistryObject, ResourceLocation slime_block) {
        ResourceLocation id = blockRegistryObject.getId();
        VariantBlockStateBuilder variantBuilder = getVariantBuilder(blockRegistryObject.get());
        for(int height = 2; height <= 16; height += 2) {
            variantBuilder.partialState()
                    .with(LAYERS, height / 2)
                    .addModels(
                            new ConfiguredModel(
                                    models().withExistingParent(id.getPath()+"_height" + height, modBlockLoc("layered_block_height" + height))
                                        .texture("texture", modBlockLoc("slime_block"))
                                        .texture("particle", modBlockLoc("slime_block"))));
        }
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
        return modLoc(BLOCK_FOLDER + "/" + name);
    }

    private void registerBuildingBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockLoc(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), "block_block").texture("particle", texture).texture("texture", texture);
        simpleBlock(blockHelper.getBlock().get(), model);
        slabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getSlab().get().getRegistryName(), texture);
        stairsBlock((StairBlock)  blockHelper.getStairs().get(), texture);
        wallBlock((WallBlock) blockHelper.getWall().get(), texture);
        models().wallInventory(blockHelper.getWall().get().getRegistryName().getPath() + "_inventory", texture);
        buttonBlock(blockHelper);
        pressurePlateBlock(blockHelper);
    }

    private void buttonBlock(BuildingBlockHelper blockHelper) {
        getVariantBuilder(blockHelper.getButton().get()).forAllStates(state -> {
            ModelFile buttonModel = buttonModel(blockHelper, state.getValue(ButtonBlock.POWERED));
            AttachFace face = state.getValue(FACE);
            Direction dir = state.getValue(FACING);
            int xrot = 0;
            if (face == AttachFace.CEILING) {
                dir = dir.getOpposite();
                xrot = 180;
            } else if (face == AttachFace.WALL) {
                xrot = 90;
            }
            return ConfiguredModel.builder().modelFile(buttonModel)
                    .rotationY((int) dir.toYRot())
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
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> ConfiguredModel.builder().modelFile(pressurePlateModel(blockHelper, state.getValue(PressurePlateBlock.POWERED))).build());
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
        modStairsBlock((StairBlock)  blockHelper.getStairs().get(), texture, "colored_stairs");
        modWallBlock((WallBlock) blockHelper.getWall().get(), texture, "colored_wall");
        modButtonBlock(blockHelper, "colored_button", texture);
        modPressurePlateBlock(blockHelper, "colored_pressure_plate", texture);
    }

    private void registerSlimyBlock(BuildingBlockHelper blockHelper) {
        ResourceLocation texture = modBlockLoc(blockHelper.getBlockId());
        BlockModelBuilder model = models().withExistingParent(blockHelper.getId(), modLoc("block/slimy_block")).texture("texture", texture);
        simpleBlock(blockHelper.getBlock().get(), model);
        modSlabBlock((SlabBlock) blockHelper.getSlab().get(), blockHelper.getBlock().get().getRegistryName(), texture, "slimy_slab");
        modStairsBlock((StairBlock)  blockHelper.getStairs().get(), texture, "slimy_stairs");
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

    private void modStairsBlock(StairBlock block, ResourceLocation texture, String parent) {
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
            ModelFile buttonModel = modButtonModel(blockHelper, state.getValue(ButtonBlock.POWERED), parent, texture);
            AttachFace face = state.getValue(FACE);
            Direction dir = state.getValue(FACING);
            int xrot = 0;
            if (face == AttachFace.CEILING) {
                dir = dir.getOpposite();
                xrot = 180;
            } else if (face == AttachFace.WALL) {
                xrot = 270;
            }
            return ConfiguredModel.builder().modelFile(buttonModel)
                    .rotationY((int) dir.toYRot())
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
        getVariantBuilder(blockHelper.getPressurePlate().get()).forAllStates(state -> ConfiguredModel.builder().modelFile(modPressurePlateModel(blockHelper, state.getValue(PressurePlateBlock.POWERED), parent, texture)).build());
    }

    private ModelFile modPressurePlateModel(BuildingBlockHelper blockHelper, boolean powered, String parent, ResourceLocation texture) {
        String suffix = powered ? "_down" : "";
        String parentName = parent + suffix + (powered ? "" : "_up");
        return models().withExistingParent(blockHelper.getPressurePlate().get().getRegistryName().getPath() + suffix, modBlockLoc(parentName))
                .texture("texture", texture);
    }
}