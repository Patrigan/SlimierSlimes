package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class ModTags {

    public static class Items {

        public static final ITag.INamedTag<Item> JELLY = tag("jelly");

        private static ITag.INamedTag<Item> tag(String id) {
            return ItemTags.makeWrapperTag(SlimierSlimes.MOD_ID + ":" + id);
        }
    }

}
