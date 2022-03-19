package mod.patrigan.slimier_slimes.datagen;

import mod.patrigan.slimier_slimes.blocks.BuildingBlockHelper;
import mod.patrigan.slimier_slimes.init.ModBlocks;
import mod.patrigan.slimier_slimes.init.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static mod.patrigan.slimier_slimes.init.ModBlocks.*;
import static mod.patrigan.slimier_slimes.init.ModItems.SLIME_BALL;
import static net.minecraft.world.item.Items.COBBLESTONE;
import static net.minecraft.world.item.Items.NETHERRACK;

public class ModBlockLootTables extends BlockLoot {

    private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder NO_SILK_TOUCH = SILK_TOUCH.invert();

    @Override
    protected void addTables() {
        Arrays.stream(DyeColor.values()).forEach(this::registerBuildingBlocks);
        this.add(ModBlocks.AMETHYST_CLUSTER.get(), block -> noDrop());
        this.add(ModBlocks.SMALL_AMETHYST_BUD.get(), block -> noDrop());
        this.add(ModBlocks.MEDIUM_AMETHYST_BUD.get(), block -> noDrop());
        this.add(ModBlocks.LARGE_AMETHYST_BUD.get(), block -> noDrop());
        this.add(STONE_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(ModBlocks.SLIMY_COBBLESTONE_BLOCK.get(DyeColor.RED).getBlock().getId())));
        this.add(NETHERRACK_LAVA_SLIME_SPAWNER.get(), block -> dropOtherLootTable(getLootTableIdOf(SLIMY_NETHERRACK_BLOCK.get(DyeColor.RED).getBlock().getId())));
        GOO_LAYER_BLOCKS.forEach((dyeColor, blockRegistryObject) ->
                this.add(blockRegistryObject.get(), block -> createSingleItemTableWithSilkTouch(block, SLIME_BALL.get(dyeColor).get(), ConstantValue.exactly(1)))
        );
    }

    private ResourceLocation getLootTableIdOf(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }

    private LootTable.Builder dropOtherLootTable(ResourceLocation id) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootTableReference.lootTableReference(id)));
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
        return createSilkTouchDispatchTable(slimyBlock, applyExplosionCondition(slimyBlock, LootItem.lootTableItem(cobblestone)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NO_SILK_TOUCH).add(LootItem.lootTableItem(ModItems.JELLY.get(dyeColor).get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1.0F, 1.5F, 2F, 2.5F)));
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
        this.registerBuildingBlockLootTable(buildingBlockHelper, BlockLoot::createSingleItemTable);
    }
}