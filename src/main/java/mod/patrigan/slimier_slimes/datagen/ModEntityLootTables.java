package mod.patrigan.slimier_slimes.datagen;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.stream.Collectors;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.*;
import static mod.patrigan.slimier_slimes.init.ModItems.JELLY;
import static net.minecraft.world.item.DyeColor.*;
import static net.minecraft.world.item.Items.*;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class ModEntityLootTables extends EntityLoot {

    @Override
    protected void addTables() {
        this.add(COMMON_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(LIME)));
        this.add(PINK_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(PINK)).withPool(getJellyLootPool(PINK)));
        this.add(CLOUD_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(LIGHT_GRAY)));
        this.add(DIAMOND_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(CYAN)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(ROCK_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(BLUE)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(COBBLESTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(CRYSTAL_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(MAGENTA)).withPool(getJellyLootPool(LIGHT_BLUE)));
        this.add(GLOW_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(PURPLE)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(CREEPER_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(GREEN)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(SNOW_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(WHITE)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(SNOWBALL).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(CAMO_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(GREEN)).withPool(getJellyLootPool(GREEN)));
        this.add(LAVA_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(RED)).withPool(getJellyLootPool(RED)));
        this.add(OBSIDIAN_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(BLACK)).withPool(getJellyLootPool(PURPLE)).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
        this.add(BROWN_GOO_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(BROWN)));
        this.add(SHROOM_SLIME.get(), LootTable.lootTable().withPool(getJellyLootPool(RED)));
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private LootPool.Builder getJellyLootPool(DyeColor dyeColor){
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(JELLY.get(dyeColor).get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))));
    }
}
