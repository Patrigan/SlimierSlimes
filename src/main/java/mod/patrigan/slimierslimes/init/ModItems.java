package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static mod.patrigan.slimierslimes.init.ModBlocks.AMETHYST_CLUSTER;
import static mod.patrigan.slimierslimes.init.ModBlocks.SLIMY_STONE_BLOCK;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SlimierSlimes.MOD_ID);
    public static final List<String> ITEM_IDS = new ArrayList<>();

    //Items
    public static final RegistryObject<Item> WHITE_JELLY = registerJelly(DyeColor.WHITE);
    public static final RegistryObject<Item> LIGHT_GRAY_JELLY = registerJelly(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Item> GRAY_JELLY = registerJelly(DyeColor.GRAY);
    public static final RegistryObject<Item> BLACK_JELLY = registerJelly(DyeColor.BLACK);
    public static final RegistryObject<Item> RED_JELLY = registerJelly(DyeColor.RED);
    public static final RegistryObject<Item> ORANGE_JELLY = registerJelly(DyeColor.ORANGE);
    public static final RegistryObject<Item> YELLOW_JELLY = registerJelly(DyeColor.YELLOW);
    public static final RegistryObject<Item> LIME_JELLY = registerJelly(DyeColor.LIME);
    public static final RegistryObject<Item> GREEN_JELLY = registerJelly(DyeColor.GREEN);
    public static final RegistryObject<Item> LIGHT_BLUE_JELLY = registerJelly(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Item> CYAN_JELLY = registerJelly(DyeColor.CYAN);
    public static final RegistryObject<Item> BLUE_JELLY = registerJelly(DyeColor.BLUE);
    public static final RegistryObject<Item> PURPLE_JELLY = registerJelly(DyeColor.PURPLE);
    public static final RegistryObject<Item> MAGENTA_JELLY = registerJelly(DyeColor.MAGENTA);
    public static final RegistryObject<Item> PINK_JELLY = registerJelly(DyeColor.PINK);
    public static final RegistryObject<Item> BROWN_JELLY = registerJelly(DyeColor.BROWN);

    //BlockItems
    public static final RegistryObject<Item> SLIMY_STONE_BLOCK_ITEM = ITEMS.register("slimy_stone",  () -> new BlockItem(SLIMY_STONE_BLOCK.get(), new Item.Properties().group(SlimierSlimes.TAB)));
    public static final RegistryObject<Item> AMETHYST_BLOCK_ITEM = ITEMS.register("amethyst_cluster",  () -> new BlockItem(AMETHYST_CLUSTER.get(), new Item.Properties().group(SlimierSlimes.TAB)));

    private static RegistryObject<Item> registerJelly(DyeColor dyeColorin){
        String itemId = dyeColorin.getTranslationKey() + "_jelly";
        ITEM_IDS.add(itemId);
        return ITEMS.register(itemId,  () -> new Item(new Item.Properties().group(SlimierSlimes.TAB)));
    }

}