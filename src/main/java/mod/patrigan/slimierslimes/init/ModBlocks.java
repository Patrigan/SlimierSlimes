package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.blocks.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static mod.patrigan.slimierslimes.init.ModItems.registerBlockItem;

public class ModBlocks {
    private static final String SLAB_ID = "_slab";
    private static final String STAIRS_ID = "_stairs";
    private static final String BUTTON_ID = "_button";
    private static final String PLATE_ID = "_pressure_plate";
    private static final String WALL_ID = "_wall";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SlimierSlimes.MOD_ID);
    public static final List<String> BLOCK_IDS = new ArrayList<>();
    public static final List<BuildingBlockHelper> BLOCK_HELPERS = new ArrayList<>();

    public static final RegistryObject<Block> LIGHT_AIR = registerBlockWithoutItem("light_air",  LightAirBlock::new);
    public static final BuildingBlockHelper SLIMY_COBBLESTONE_BLOCK = registerBuildingBlock("slimy_cobblestone", SlimyStoneBlock::new);
    public static final BuildingBlockHelper SLIMY_STONE_BLOCK = registerBuildingBlock("slimy_stone", SlimyStoneBlock::new);
    public static final RegistryObject<Block> AMETHYST_CLUSTER = registerBlockWithoutItem("amethyst_cluster", AmethystClusterBlock::new);
    public static final RegistryObject<Block> SMALL_AMETHYST_BUD = registerBlockWithoutItem("small_amethyst_bud", SmallAmethystBudBlock::new);
    public static final RegistryObject<Block> MEDIUM_AMETHYST_BUD = registerBlockWithoutItem("medium_amethyst_bud", MediumAmethystBudBlock::new);
    public static final RegistryObject<Block> LARGE_AMETHYST_BUD = registerBlockWithoutItem("large_amethyst_bud", LargeAmethystBudBlock::new);

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

    private static BuildingBlockHelper registerBuildingBlock(String id, Supplier<Block> sup) {
        BuildingBlockHelper buildingBlockHelper = new BuildingBlockHelper(id,
                registerBlock(id, sup),
                registerBlock(id + SLAB_ID, () -> new SlabBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(id + STAIRS_ID, () -> new StairsBlock(() -> sup.get().getDefaultState(), AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).slipperiness(1F))),
                registerBlock(id + BUTTON_ID, () -> new StoneButtonBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F))),
                registerBlock(id + PLATE_ID, () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE).doesNotBlockMovement().hardnessAndResistance(0.5F).slipperiness(1F))),
                registerBlock(id + WALL_ID, () -> new WallBlock(AbstractBlock.Properties.from(sup.get()).harvestLevel(0).harvestTool(ToolType.PICKAXE))));
        BLOCK_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    public static void initRenderTypes(){
        initBuildingBlockRenderTypes(SLIMY_STONE_BLOCK, RenderType.getTranslucent());
        initBuildingBlockRenderTypes(SLIMY_COBBLESTONE_BLOCK, RenderType.getTranslucent());
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