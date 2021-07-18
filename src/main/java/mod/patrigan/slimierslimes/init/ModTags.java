package mod.patrigan.slimierslimes.init;

import mod.patrigan.slimierslimes.SlimierSlimes;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

public class ModTags {

    public static class Items {

        public static final ITag.INamedTag<Item> JELLIES = tag("jellies");
        public static final ITag.INamedTag<Item> ARMOR_SLIME_CHESTPLATE = tag("armor/slime/chestplate");
        public static final ITag.INamedTag<Item> ARMOR_SLIME_HELMET = tag("armor/slime/helmet");
        public static final ITag.INamedTag<Item> ARMOR_SLIME_LEGGINGS = tag("armor/slime/leggings");
        public static final ITag.INamedTag<Item> ARMOR_SLIME_BOOTS = tag("armor/slime/boots");

        private static ITag.INamedTag<Item> tag(String id) {
            return ItemTags.bind(SlimierSlimes.MOD_ID + ":" + id);
        }
    }

    public static class EntityTypes {

        public static final ITag.INamedTag<EntityType<?>> SLIMES = forgeTag("slimes");

        private static ITag.INamedTag<EntityType<?>> forgeTag(String id) {
            return EntityTypeTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

    public static class Blocks {

        public static final ITag.INamedTag<Block> MUSHROOMS = forgeTag("mushrooms");

        private static ITag.INamedTag<Block> forgeTag(String id) {
            return BlockTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

}
