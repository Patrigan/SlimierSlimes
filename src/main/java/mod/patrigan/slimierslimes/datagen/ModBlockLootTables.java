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

    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
    private static final ILootCondition.IBuilder NO_SILK_TOUCH = SILK_TOUCH.inverted();
    @Override
    protected void addTables() {
        Arrays.stream(DyeColor.values()).forEach(this::registerBuildingBlocks);
        this.registerLootTable(ModBlocks.AMETHYST_CLUSTER.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.SMALL_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.MEDIUM_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(ModBlocks.LARGE_AMETHYST_BUD.get(), block -> blockNoDrop());
        this.registerLootTable(STONE_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(DyeColor.RED).getBlock().getId())));
        this.registerLootTable(NETHERRACK_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getBlock().getId())));
    }

    private ResourceLocation getLootTableIdOf(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }

    private LootTable.Builder dropOtherLootTable(ResourceLocation id) {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(TableLootEntry.builder(id)));
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
        return droppingWithSilkTouch(slimyBlock, withSurvivesExplosion(slimyBlock, ItemLootEntry.builder(cobblestone)))
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(NO_SILK_TOUCH).addEntry(ItemLootEntry.builder(ModItems.JELLY.get(dyeColor).get()).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F)))).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 1.0F, 1.5F, 2F, 2.5F)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().filter(block -> !block.equals(LIGHT_AIR)).map(RegistryObject::get).collect(Collectors.toList());
    }

    protected void registerBuildingBlockLootTable(BuildingBlockHelper blockHelper, Function<Block, LootTable.Builder> baseFactory) {
        this.registerLootTable(blockHelper.getBlock().get(), baseFactory);
        this.registerLootTable(blockHelper.getSlab().get(), droppingSlab(blockHelper.getSlab().get()));
        this.registerLootTable(blockHelper.getStairs().get(), dropping(blockHelper.getStairs().get()));
        this.registerLootTable(blockHelper.getButton().get(), dropping(blockHelper.getButton().get()));
        this.registerLootTable(blockHelper.getPressurePlate().get(), dropping(blockHelper.getPressurePlate().get()));
        this.registerLootTable(blockHelper.getWall().get(), dropping(blockHelper.getWall().get()));
    }

    protected void registerDroppingSelfBuildingBlockHelper(BuildingBlockHelper buildingBlockHelper){
        this.registerBuildingBlockLootTable(buildingBlockHelper, BlockLootTables::dropping);
    }
}