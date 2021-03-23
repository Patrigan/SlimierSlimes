package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimierslimes.init.ModBlocks;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;
import static net.minecraft.item.Items.COBBLESTONE;
import static net.minecraft.item.Items.NETHERRACK;

public class ModBlockLootTables extends BlockLootTables {

    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
    private static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.invert();
    @Override
    protected void addTables() {
        Arrays.stream(DyeColor.values()).forEach(this::registerBuildingBlocks);
        this.add(ModBlocks.AMETHYST_CLUSTER.get(), block -> noDrop());
        this.add(ModBlocks.SMALL_AMETHYST_BUD.get(), block -> noDrop());
        this.add(ModBlocks.MEDIUM_AMETHYST_BUD.get(), block -> noDrop());
        this.add(ModBlocks.LARGE_AMETHYST_BUD.get(), block -> noDrop());
        this.add(STONE_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(DyeColor.RED).getBlock().getId())));
        this.add(NETHERRACK_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getBlock().getId())));
    }

    private ResourceLocation getLootTableIdOf(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }

    private LootTable.Builder dropOtherLootTable(ResourceLocation id) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(TableLootEntry.lootTableReference(id)));
    }

    private void registerBuildingBlocks(DyeColor dyeColor){
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(dyeColor), slimyStoneBlock ->
                getSlimyStoneBuilder(dyeColor, slimyStoneBlock, COBBLESTONE));
        this.registerBuildingBlockLootTable(ModBlocks.SLIMY_STONE_BLOCK.get(dyeColor), slimyStoneBlock ->
                getSlimyStoneBuilder(dyeColor, slimyStoneBlock, COBBLESTONE));
        this.registerBuildingBlockLootTable(SLIMY_NETHERRACK_BLOCK.get(dyeColor), slimyStoneBlock ->
                getSlimyStoneBuilder(dyeColor, slimyStoneBlock, NETHERRACK));
        SLIME_BLOCK_HELPERS.values().forEach(this::registerDroppingSelfBuildingBlockHelper);
    }

    private LootTable.Builder getSlimyStoneBuilder(DyeColor dyeColor, Block slimyBlock, Item cobblestone) {
        return createSilkTouchDispatchTable(slimyBlock, applyExplosionCondition(slimyBlock, ItemLootEntry.lootTableItem(cobblestone)))
                .withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).when(NO_SILK_TOUCH).add(ItemLootEntry.lootTableItem(ModItems.JELLY.get(dyeColor).get()).apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))).when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1.0F, 1.5F, 2F, 2.5F)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().filter(block -> !block.equals(LIGHT_AIR)).map(RegistryObject::get).collect(Collectors.toList());
    }

    protected void registerBuildingBlockLootTable(BuildingBlockHelper blockHelper, Function<Block, LootTable.Builder> baseFactory) {
        this.add(blockHelper.getBlock().get(), baseFactory);
        this.add(blockHelper.getSlab().get(), createSlabItemTable(blockHelper.getSlab().get()));
        this.add(blockHelper.getStairs().get(), createSingleItemTable(blockHelper.getStairs().get()));
        this.add(blockHelper.getButton().get(), createSingleItemTable(blockHelper.getButton().get()));
        this.add(blockHelper.getPressurePlate().get(), createSingleItemTable(blockHelper.getPressurePlate().get()));
        this.add(blockHelper.getWall().get(), createSingleItemTable(blockHelper.getWall().get()));
    }

    protected void registerDroppingSelfBuildingBlockHelper(BuildingBlockHelper buildingBlockHelper){
        this.registerBuildingBlockLootTable(buildingBlockHelper, BlockLootTables::createSingleItemTable);
    }
}