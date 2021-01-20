package mod.patrigan.slimierslimes.init;

import com.mojang.datafixers.types.Func;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.items.JellyItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SlimierSlimes.MOD_ID);
    public static final List<String> ITEM_IDS = new ArrayList<>();

    //DyeColor Items
    public static final Map<DyeColor, RegistryObject<Item>> JELLY = getDyeColorRegistryObjectHashMap(ModItems::registerJelly);

    private static Map<DyeColor, RegistryObject<Item>> getDyeColorRegistryObjectHashMap(Function<DyeColor, RegistryObject<Item>> registryFunc) {
        return Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, registryFunc));
    }

    private static RegistryObject<Item> registerJelly(DyeColor dyeColorin){
        String itemId = dyeColorin.getTranslationKey() + "_jelly";
        ITEM_IDS.add(itemId);
        return ITEMS.register(itemId,  () -> new JellyItem(new Item.Properties().group(SlimierSlimes.TAB), dyeColorin));
    }

    public static RegistryObject<BlockItem> registerBlockItem(String id, RegistryObject<Block> block, Function<Supplier<Block>, BlockItem> itemCreatorFunction){
        return ITEMS.register(id,  () -> itemCreatorFunction.apply(block));
    }

}