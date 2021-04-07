package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.items.SlimeBallItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
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