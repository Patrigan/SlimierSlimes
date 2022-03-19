package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.item.ModArmorMaterial;
import mod.patrigan.slimier_slimes.items.SlimeBallItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SlimierSlimes.MOD_ID);
    public static final List<String> ITEM_IDS = new ArrayList<>();

    //DyeColor Items
    public static final Map<DyeColor, RegistryObject<Item>> SLIME_CHESTPLATE = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_slime_chestplate", () -> new ArmorItem(ModArmorMaterial.SLIME, EquipmentSlot.CHEST, (new Item.Properties()).tab(SlimierSlimes.TAB))));
    public static final Map<DyeColor, RegistryObject<Item>> SLIME_BOOTS = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_slime_boots", () -> new ArmorItem(ModArmorMaterial.SLIME, EquipmentSlot.FEET, (new Item.Properties()).tab(SlimierSlimes.TAB))));
    public static final Map<DyeColor, RegistryObject<Item>> SLIME_LEGGINGS = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_slime_leggings", () -> new ArmorItem(ModArmorMaterial.SLIME, EquipmentSlot.LEGS, (new Item.Properties()).tab(SlimierSlimes.TAB))));
    public static final Map<DyeColor, RegistryObject<Item>> SLIME_HELMET = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_slime_helmet", () -> new ArmorItem(ModArmorMaterial.SLIME, EquipmentSlot.HEAD, (new Item.Properties()).tab(SlimierSlimes.TAB))));
    public static final Map<DyeColor, RegistryObject<Item>> JELLY = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_jelly", () -> new Item(new Item.Properties().tab(SlimierSlimes.TAB))));
    public static final Map<DyeColor, RegistryObject<Item>> SLIME_BALL = getDyeColorRegistryObjectHashMap(dyeColor -> registerColoredItem(dyeColor, "_slime_ball", () -> new SlimeBallItem(dyeColor, new Item.Properties().tab(SlimierSlimes.TAB))));

    private static Map<DyeColor, RegistryObject<Item>> getDyeColorRegistryObjectHashMap(Function<DyeColor, RegistryObject<Item>> registryFunc) {
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, registryFunc));
    }

    private static RegistryObject<Item> registerColoredItem(DyeColor dyeColorin, String itemId, Supplier<Item> itemSupplier){
        String coloredItemId = dyeColorin.toString() + itemId;
        ITEM_IDS.add(coloredItemId);
        return ITEMS.register(coloredItemId, itemSupplier);
    }

    public static RegistryObject<BlockItem> registerBlockItem(String id, RegistryObject<Block> block, Function<Supplier<Block>, BlockItem> itemCreatorFunction){
        return ITEMS.register(id,  () -> itemCreatorFunction.apply(block));
    }

}