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

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class ModTags {

    public static class Items {

        public static final ITag.INamedTag<Item> JELLIES = tag("jellies");

        private static ITag.INamedTag<Item> tag(String id) {
            return ItemTags.bind(SlimierSlimes.MOD_ID + ":" + id);
        }
    }

    public static class EntityTypes {

        public static final ITag.INamedTag<EntityType<?>> SLIMES = tag("slimes");

        private static ITag.INamedTag<EntityType<?>> tag(String id) {
            return EntityTypeTags.createOptional(new ResourceLocation(MOD_ID + ":" + id));
        }
    }

    public static class Blocks {

        public static final ITag.INamedTag<Block> MUSHROOMS = forgeTag("mushrooms");

        private static ITag.INamedTag<Block> forgeTag(String id) {
            return BlockTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

}
