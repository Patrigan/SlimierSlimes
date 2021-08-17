package mod.patrigan.slimier_slimes.datagen;

import net.minecraft.data.loot.ChestLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MOD_ID;
import static mod.patrigan.slimier_slimes.init.ModItems.*;
import static net.minecraft.tags.ItemTags.FLOWERS;
import static net.minecraft.tags.ItemTags.SAPLINGS;
import static net.minecraftforge.common.Tags.Items.SEEDS;

public class ModChestLootTables extends ChestLoot {

    private static final ResourceLocation SLIME_LOOT_TABLE = new ResourceLocation(MOD_ID, "chests/abstract_slime_table");

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        createAbstractSlimeLootTable(consumer);
        consumer.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(6.0F, 10.0F))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(40))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.BREAD).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                                .add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
        consumer.accept(new ResourceLocation(MOD_ID, "processors/saplings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(SAPLINGS).setWeight(1))));
        createBooksLootTable(consumer);
        createSewerLootTables(consumer);
    }

    private void createSewerLootTables(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/smithy"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(6.0F, 10.0F))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootTableReference.lootTableReference(new ResourceLocation("chests/village/village_weaponsmith")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/frozen_cistern"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(6.0F, 10.0F))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootTableReference.lootTableReference(new ResourceLocation("chests/village/village_snowy_house")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/storage"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(6.0F, 10.0F))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(50))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(25).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootTableReference.lootTableReference(new ResourceLocation("chests/simple_dungeon")).setWeight(25))
                        ));
        consumer.accept(new ResourceLocation(MOD_ID, "chests/sewer/greenhouse"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(7.0F, 11.0F))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(45))
                                .add(LootTableReference.lootTableReference(SLIME_LOOT_TABLE).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(TagEntry.expandTag(SAPLINGS).setWeight(15))
                                .add(TagEntry.expandTag(FLOWERS).setWeight(15))
                                .add(TagEntry.expandTag(SEEDS).setWeight(15))
                        ));
    }

    private void createAbstractSlimeLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        ResourceLocation jellies = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/jelly");
        ResourceLocation slimeballs = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/slimeball");
        ResourceLocation armors = new ResourceLocation(MOD_ID, "chests/abstract_slime_table/armor");

        LootPool.Builder jelliesBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        JELLY.forEach((dyeColor, jelly) -> jelliesBuilder.add(LootItem.lootTableItem(jelly.get()).setWeight(1)));
        consumer.accept(jellies,
                LootTable.lootTable().
                        withPool(jelliesBuilder));
        LootPool.Builder slimeBallBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        SLIME_BALL.forEach((dyeColor, slimeBall) -> slimeBallBuilder.add(LootItem.lootTableItem(slimeBall.get()).setWeight(1)));
        consumer.accept(slimeballs,
                LootTable.lootTable().
                        withPool(slimeBallBuilder));
        LootPool.Builder armorBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        SLIME_HELMET.forEach((dyeColor, armor) -> armorBuilder.add(LootItem.lootTableItem(armor.get()).setWeight(1)));
        SLIME_CHESTPLATE.forEach((dyeColor, armor) -> armorBuilder.add(LootItem.lootTableItem(armor.get()).setWeight(1)));
        SLIME_LEGGINGS.forEach((dyeColor, armor) -> armorBuilder.add(LootItem.lootTableItem(armor.get()).setWeight(1)));
        SLIME_BOOTS.forEach((dyeColor, armor) -> armorBuilder.add(LootItem.lootTableItem(armor.get()).setWeight(1)));
        consumer.accept(armors,
                LootTable.lootTable().
                        withPool(armorBuilder));
        consumer.accept(SLIME_LOOT_TABLE,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootTableReference.lootTableReference(jellies).setWeight(50).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
                                .add(LootTableReference.lootTableReference(slimeballs).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                                .add(LootTableReference.lootTableReference(armors).setWeight(20))
                        ));
    }

    private void createBooksLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putString("author", "Patrigan");
        compoundNBT.putString("title", "TestSuccess");
        ListTag listNBT = new ListTag();
        listNBT.add(StringTag.valueOf("This is a success"));
        compoundNBT.put("pages", listNBT);
        consumer.accept(new ResourceLocation(MOD_ID, "processors/books_test"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.WRITTEN_BOOK).setWeight(1).apply(SetNbtFunction.setTag(compoundNBT)))));
    }


}
