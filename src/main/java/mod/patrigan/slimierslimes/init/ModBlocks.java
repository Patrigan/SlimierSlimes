package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    //Slimy Blocks
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_COBBLESTONE_BLOCK = registerColoredBuildingBlock("slimy_cobblestone", SlimyStoneBlock::new, true);
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_STONE_BLOCK = registerColoredBuildingBlock("slimy_stone", SlimyStoneBlock::new, true);
    public static final Map<DyeColor, BuildingBlockHelper> SLIMY_NETHERRACK_BLOCK = registerColoredBuildingBlock("slimy_netherrack", SlimyStoneBlock::new, true);

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
        registerBlockItem(id, blockRegistryObject, blockSupplier -> new BlockItem(blockSupplier.get(), new Item.Properties().group(SlimierSlimes.TAB)));
        return blockRegistryObject;
    }

    private static RegistryObject<Block> registerBlockWithoutItem(String id, Supplier<Block> sup) {
        BLOCK_IDS.add(id);
        return BLOCKS.register(id, sup);
    }

    private static Map<DyeColor, BuildingBlockHelper> registerColoredBuildingBlock(String id, Function<DyeColor, Block> sup, boolean slimy){
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, dyeColor -> registerDyedBuildingBlock(id, dyeColor, () -> sup.apply(dyeColor), slimy)));
    }

    private static BuildingBlockHelper registerDyedBuildingBlock(String id, DyeColor dyeColor, Supplier<Block> sup, boolean slimy) {
        String colorId = dyeColor.getTranslationKey() + "_" + id;
        BuildingBlockHelper buildingBlockHelper = new BuildingBlockHelper(id,
                dyeColor,
                registerBlock(colorId, sup),
                registerBlock(colorId + SLAB_ID, () -> new SlabBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(colorId + STAIRS_ID, () -> new StairsBlock(() -> sup.get().getDefaultState(), AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(colorId + BUTTON_ID, () -> new StoneButtonBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F))),
                registerBlock(colorId + PLATE_ID, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F).slipperiness(1F))),
                registerBlock(colorId + WALL_ID, () -> new WallBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE))),
                slimy);
        BLOCK_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    private static BuildingBlockHelper registerBuildingBlock(String id, Supplier<Block> sup, boolean slimy) {
        BuildingBlockHelper buildingBlockHelper = new BuildingBlockHelper(id,
                registerBlock(id, sup),
                registerBlock(id + SLAB_ID, () -> new SlabBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(id + STAIRS_ID, () -> new StairsBlock(() -> sup.get().getDefaultState(), AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(id + BUTTON_ID, () -> new StoneButtonBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F))),
                registerBlock(id + PLATE_ID, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F).slipperiness(1F))),
                registerBlock(id + WALL_ID, () -> new WallBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE))),
                slimy);
        BLOCK_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    public static void initRenderTypes(){
        BLOCK_HELPERS.forEach(buildingBlockHelper -> {
            if(buildingBlockHelper.isSlimy()){
                initBuildingBlockRenderTypes(buildingBlockHelper, RenderType.getTranslucent());
            }
        });
        RenderTypeLookup.setRenderLayer(STONE_LAVA_SLIME_SPAWNER.get(), RenderType.getTranslucent());
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