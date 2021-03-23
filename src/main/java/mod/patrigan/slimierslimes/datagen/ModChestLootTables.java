package mod.patrigan.slimierslimes.datagen;

import mod.patrigan.slimierslimes.init.ModTags;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

import static mod.patrigan.slimierslimes.SlimierSlimes.MOD_ID;
import static net.minecraftforge.common.Tags.Items.SLIMEBALLS;

public class ModChestLootTables extends ChestLootTables {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(MOD_ID, "chests/pillager_slime_lab"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(5.0F, 8.0F))
                                .add(TagLootEntry.expandTag(ModTags.Items.JELLIES).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2.0F, 5.0F))))
                                .add(TagLootEntry.expandTag(SLIMEBALLS).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1.0F, 5.0F))))
                                .add(ItemLootEntry.lootTableItem(Items.BREAD).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
                                .add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F))))));
    }
}
