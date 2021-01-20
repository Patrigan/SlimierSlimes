package mod.patrigan.slimierslimes.datagen;

import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static mod.patrigan.slimierslimes.init.ModItems.*;
import static net.minecraft.item.DyeColor.*;
import static net.minecraft.item.Items.*;

public class ModEntityLootTables extends EntityLootTables {

    @Override
    protected void addTables() {
        this.registerLootTable(COMMON_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(GREEN)));
        this.registerLootTable(PINK_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(PINK)).addLootPool(getJellyLootPool(PINK)));
        this.registerLootTable(DIAMOND_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(CYAN)).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(DIAMOND).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(ROCK_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(BLUE)).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(COBBLESTONE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(CRYSTAL_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(MAGENTA)).addLootPool(getJellyLootPool(LIGHT_BLUE)));
        this.registerLootTable(GLOW_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(PURPLE)).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(GLOWSTONE_DUST).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(CREEPER_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(GREEN)).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(SNOW_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(WHITE)).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(SNOWBALL).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
        this.registerLootTable(CAMO_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(GREEN)).addLootPool(getJellyLootPool(GREEN)));
        this.registerLootTable(LAVA_SLIME.get(), LootTable.builder().addLootPool(getJellyLootPool(RED)).addLootPool(getJellyLootPool(RED)));
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootPool.Builder getJellyLootPool(DyeColor dyeColor){
        return LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(JELLY.get(dyeColor).get()).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))));
    }

    /*private LootPool.Builder getJellyLootPool(DyeColor dyeColor){
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("colorId", dyeColor.getId());
        return LootPool.builder().rolls(ConstantRange.of(1)).addEntry(
                ItemLootEntry.builder(JELLY.get())
                        .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
                        .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                        .acceptFunction(SetNBT.builder(compoundNBT)));
    }*/
}
