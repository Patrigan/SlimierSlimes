package mod.patrigan.slimierslimes.datagen;

import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimierslimes.init.ModItems.*;
import static net.minecraft.tags.ItemTags.FLOWERS;
import static net.minecraft.tags.ItemTags.SAPLINGS;
import static net.minecraftforge.common.Tags.Items.SEEDS;

public class ModChestLootTables extends ChestLootTables {

    private static final ResourceLocation SLIME_LOOT_TABLE = new ResourceLocation(MOD_ID, "chests/abstract_slime_table");

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        createAbstractSlimeLootTable(consumer);
        consumer.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(6.0F, 10.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(40))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.BREAD).setWeight(25).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
                                .add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F))))));
        consumer.accept(new ResourceLocation(MOD_ID, "processors/saplings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(SAPLINGS).setWeight(1))));
        createBooksLootTable(consumer);
        createSewerLootTables(consumer);
    }

    private void createSewerLootTables(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/smithy"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(6.0F, 10.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TableLootEntry.lootTableReference(new ResourceLocation("chests/village/village_weaponsmith")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/frozen_cistern"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(6.0F, 10.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TableLootEntry.lootTableReference(new ResourceLocation("chests/village/village_snowy_house")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/storage"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(6.0F, 10.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TableLootEntry.lootTableReference(new ResourceLocation("chests/simple_dungeon")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/greenhouse"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(7.0F, 11.0F))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(45))
                                .add(TableLootEntry.lootTableReference(SLIME_LOOT_TABLE).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TagLootEntry.expandTag(SAPLINGS).setWeight(15))
                                .add(TagLootEntry.expandTag(FLOWERS).setWeight(15))
                                .add(TagLootEntry.expandTag(SEEDS).setWeight(15))
                        ));
    }

    private void createAbstractSlimeLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        ResourceLocation jellies = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/jelly");
        ResourceLocation slimeballs = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/slimeball");
        ResourceLocation armors = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/armor");

        LootPool.Builder jelliesBuilder = LootPool.lootPool().setRolls(ConstantRange.exactly(1));
        JELLY.forEach((dyeColor, jelly) -> jelliesBuilder.add(ItemLootEntry.lootTableItem(jelly.get()).setWeight(1)));
        consumer.accept(jellies,
                LootTable.lootTable().
                        withPool(jelliesBuilder));
        LootPool.Builder slimeBallBuilder = LootPool.lootPool().setRolls(ConstantRange.exactly(1));
        SLIME_BALL.forEach((dyeColor, slimeBall) -> slimeBallBuilder.add(ItemLootEntry.lootTableItem(slimeBall.get()).setWeight(1)));
        consumer.accept(slimeballs,
                LootTable.lootTable().
                        withPool(slimeBallBuilder));
        LootPool.Builder armorBuilder = LootPool.lootPool().setRolls(ConstantRange.exactly(1));
        SLIME_HELMET.forEach((dyeColor, armor) -> armorBuilder.add(ItemLootEntry.lootTableItem(armor.get()).setWeight(1)));
        SLIME_CHESTPLATE.forEach((dyeColor, armor) -> armorBuilder.add(ItemLootEntry.lootTableItem(armor.get()).setWeight(1)));
        SLIME_LEGGINGS.forEach((dyeColor, armor) -> armorBuilder.add(ItemLootEntry.lootTableItem(armor.get()).setWeight(1)));
        SLIME_BOOTS.forEach((dyeColor, armor) -> armorBuilder.add(ItemLootEntry.lootTableItem(armor.get()).setWeight(1)));
        consumer.accept(armors,
                LootTable.lootTable().
                        withPool(armorBuilder));
        consumer.accept(SLIME_LOOT_TABLE,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TableLootEntry.lootTableReference(jellies).setWeight(50).apply(SetCount.setCount(RandomValueRange.between(2.0F, 5.0F))))
                                .add(TableLootEntry.lootTableReference(slimeballs).setWeight(30).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
                                .add(TableLootEntry.lootTableReference(armors).setWeight(20))
                        ));
    }

    private void createBooksLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("author", "Patrigan");
        compoundNBT.putString("title", "TestSuccess");
        ListNBT listNBT = new ListNBT();
        listNBT.add(StringNBT.valueOf("This is a success"));
        compoundNBT.put("pages", listNBT);
        consumer.accept(new ResourceLocation(MOD_ID, "processors/books_test"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(ItemLootEntry.lootTableItem(Items.WRITTEN_BOOK).setWeight(1).apply(SetNBT.setTag(compoundNBT)))));
    }


}
