package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.*;
import mod.patrigan.slimierslimes.blocks.slimeblock.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModItems.registerBlockItem;

public class ModBlocks {
    public static final String SLAB_ID = "_slab";
    public static final String STAIRS_ID = "_stairs";
    public static final String BUTTON_ID = "_button";
    public static final String PLATE_ID = "_pressure_plate";
    public static final String WALL_ID = "_wall";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SlimierSlimes.MOD_ID);
    public static final List<String> BLOCK_IDS = new ArrayList<>();
    public static final List<BuildingBlockHelper> BLOCK_HELPERS = new ArrayList<>();
    public static final Map<DyeColor, BuildingBlockHelper> SLIME_BLOCK_HELPERS = new HashMap<>();

    //Slimy Blocks
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_COBBLESTONE_BLOCK = registerColoredBuildingBlock("slimy_cobblestone", SlimyStoneBlock::new, true);
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_STONE_BLOCK = registerColoredBuildingBlock("slimy_stone", SlimyStoneBlock::new, true);
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_NETHERRACK_BLOCK = registerColoredBuildingBlock("slimy_netherrack", SlimyStoneBlock::new, true);
    public static final List<Block> BASE_SLIMY_BLOCKS = Arrays.asList(Blocks.COBBLESTONE, Blocks.STONE, Blocks.NETHERRACK);

    //Slime Blocks
    public static final BuildingBlockHelper LIME_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.LIME, () -> new LimeSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper GREEN_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.GREEN, () -> new GreenSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper YELLOW_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.YELLOW, () -> new YellowSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper CYAN_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.CYAN, () -> new CyanSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_CYAN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper LIGHT_BLUE_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.LIGHT_BLUE, () -> new LightBlueSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_BLUE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper BLUE_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.BLUE, () -> new BlueSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_BLUE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper ORANGE_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.ORANGE, () -> new OrangeSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper RED_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.RED, () -> new RedSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper PINK_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.PINK, () -> new PinkSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper BROWN_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.BROWN, () -> new BrownSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper LIGHT_GRAY_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.LIGHT_GRAY, () -> new LightGraySlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper GRAY_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.GRAY, () -> new GraySlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper BLACK_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.BLACK, () -> new BlackSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper WHITE_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.WHITE, () -> new WhiteSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper PURPLE_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.PURPLE, () -> new PurpleSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);
    public static final BuildingBlockHelper MAGENTA_SLIME_BLOCK = registerSlimeBlockBuildingBlock("slime_block", DyeColor.MAGENTA, () -> new MagentaSlimeBlock(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), false, 0.6F, true);

    //Other colored blocks
    public static final Map<DyeColor, RegistryObject<Block>> GOO_LAYER_BLOCKS = registerColoredBlock("goo_layer_block", GooLayerBlock::new);

    //Utility Blocks
    public static final RegistryObject<Block> LIGHT_AIR = registerBlockWithoutItem("light_air",  LightAirBlock::new);
    public static final RegistryObject<Block> AMETHYST_CLUSTER = registerBlockWithoutItem("amethyst_cluster", AmethystClusterBlock::new);
    public static final RegistryObject<Block> SMALL_AMETHYST_BUD = registerBlockWithoutItem("small_amethyst_bud", SmallAmethystBudBlock::new);
    public static final RegistryObject<Block> MEDIUM_AMETHYST_BUD = registerBlockWithoutItem("medium_amethyst_bud", MediumAmethystBudBlock::new);
    public static final RegistryObject<Block> LARGE_AMETHYST_BUD = registerBlockWithoutItem("large_amethyst_bud", LargeAmethystBudBlock::new);
    public static final RegistryObject<Block> STONE_LAVA_SLIME_SPAWNER = registerBlockWithoutItem("stone_lava_slime_spawner_block", LavaSlimeSpawnerBlock::new);
    public static final RegistryObject<Block> NETHERRACK_LAVA_SLIME_SPAWNER = registerBlockWithoutItem("netherrack_lava_slime_spawner_block", LavaSlimeSpawnerBlock::new);

    private static RegistryObject<Block> registerBlock(String id, Supplier<Block> sup) {
        BLOCK_IDS.add(id);
        RegistryObject<Block> blockRegistryObject = BLOCKS.register(id, sup);
        registerBlockItem(id, blockRegistryObject, blockSupplier -> new BlockItem(blockSupplier.get(), new Item.Properties().tab(SlimierSlimes.TAB)));
        return blockRegistryObject;
    }

    private static RegistryObject<Block> registerBlockWithoutItem(String id, Supplier<Block> sup) {
        BLOCK_IDS.add(id);
        return BLOCKS.register(id, sup);
    }

    private static Map<DyeColor, RegistryObject<Block>> registerColoredBlock(String id, Function<DyeColor, Block> sup){
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> registerDyedBlock(id, dyeColor, () -> sup.apply(dyeColor))));
    }

    private static RegistryObject<Block> registerDyedBlock(String baseId, DyeColor dyeColor, Supplier<Block> sup) {
        String colorId = dyeColor + "_" + baseId;
        return registerBlock(colorId, sup);
    }

    private static Map<DyeColor, BuildingBlockHelper> registerColoredBuildingBlock(String id, Function<DyeColor, Block> sup, boolean slimy){
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> registerDyedBuildingBlock(id, dyeColor, () -> sup.apply(dyeColor), slimy, 0.6F, true)));
    }

    private static BuildingBlockHelper registerDyedBuildingBlock(String baseId, DyeColor dyeColor, Supplier<Block> sup, boolean slimy, float slipperiness, boolean translucent) {
        String colorId = dyeColor + "_" + baseId;
        BuildingBlockHelper buildingBlockHelper = new BuildingBlockHelper.Builder()
                .withBlockId(baseId).withDyeColor(dyeColor)
                .withBlock(registerBlock(colorId, sup))
                .withSlab(registerBlock(colorId + SLAB_ID, () -> new SlabBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).friction(slipperiness))))
                .withStairs(registerBlock(colorId + STAIRS_ID, () -> new StairsBlock(() -> sup.get().defaultBlockState(), AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).friction(slipperiness))))
                .withButton(registerBlock(colorId + BUTTON_ID, () -> new StoneButtonBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).noCollission().strength(0.5F))))
                .withPressurePlate(registerBlock(colorId + PLATE_ID, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).noCollission().strength(0.5F).friction(1F))))
                .withWall(registerBlock(colorId + WALL_ID, () -> new WallBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).friction(slipperiness))))
                .withSlimy(slimy)
                .withTranslucent(translucent)
                .createBuildingBlockHelper();
        BLOCK_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    private static BuildingBlockHelper registerSlimeBlockBuildingBlock(String baseId, DyeColor dyeColor, Supplier<Block> sup, boolean slimy, float slipperiness, boolean translucent) {
        BuildingBlockHelper helper = registerDyedBuildingBlock(baseId, dyeColor, sup, slimy, slipperiness, translucent);
        SLIME_BLOCK_HELPERS.put(dyeColor,helper);
        return helper;
    }

    private static BuildingBlockHelper registerBuildingBlock(String id, Supplier<Block> sup, boolean slimy, float slipperiness) {
        BuildingBlockHelper buildingBlockHelper = new BuildingBlockHelper.Builder()
                .withBlockId(id).withBlock(registerBlock(id, sup))
                .withSlab(registerBlock(id + SLAB_ID, () -> new SlabBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).friction(slipperiness))))
                .withStairs(registerBlock(id + STAIRS_ID, () -> new StairsBlock(() -> sup.get().defaultBlockState(), AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).friction(slipperiness))))
                .withButton(registerBlock(id + BUTTON_ID, () -> new StoneButtonBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).noCollission().strength(0.5F))))
                .withPressurePlate(registerBlock(id + PLATE_ID, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).noCollission().strength(0.5F).friction(slipperiness))))
                .withWall(registerBlock(id + WALL_ID, () -> new WallBlock(AbstractBlock.Properties.copy(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE))))
                .withSlimy(slimy).createBuildingBlockHelper();
        BLOCK_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    public static void initRenderTypes(){
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.isTranslucent()){
                initBuildingBlockRenderTypes(buildingBlockHelper, RenderType.translucent());
            }
        });
        GOO_LAYER_BLOCKS.forEach((dyeColor, blockRegistryObject) ->
                RenderTypeLookup.setRenderLayer(blockRegistryObject.get(), RenderType.translucent())
        );
        RenderTypeLookup.setRenderLayer(STONE_LAVA_SLIME_SPAWNER.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(NETHERRACK_LAVA_SLIME_SPAWNER.get(), RenderType.translucent());
    }

    private static void initBuildingBlockRenderTypes(BuildingBlockHelper blockHelper, RenderType renderType) {
        RenderTypeLookup.setRenderLayer(blockHelper.getBlock().get(), renderType);
        RenderTypeLookup.setRenderLayer(blockHelper.getSlab().get(), renderType);
        RenderTypeLookup.setRenderLayer(blockHelper.getStairs().get(), renderType);
        RenderTypeLookup.setRenderLayer(blockHelper.getButton().get(), renderType);
        RenderTypeLookup.setRenderLayer(blockHelper.getPressurePlate().get(), renderType);
        RenderTypeLookup.setRenderLayer(blockHelper.getWall().get(), renderType);
    }
}