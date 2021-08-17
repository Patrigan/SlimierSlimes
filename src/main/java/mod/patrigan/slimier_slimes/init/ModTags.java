package mod.patrigan.slimier_slimes.init;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

public class ModTags {

    public static class Items {

        public static final Tag.Named<Item> JELLIES = tag("jellies");
        public static final Tag.Named<Item> ARMOR_SLIME_CHESTPLATE = tag("armor/slime/chestplate");
        public static final Tag.Named<Item> ARMOR_SLIME_HELMET = tag("armor/slime/helmet");
        public static final Tag.Named<Item> ARMOR_SLIME_LEGGINGS = tag("armor/slime/leggings");
        public static final Tag.Named<Item> ARMOR_SLIME_BOOTS = tag("armor/slime/boots");

        private static Tag.Named<Item> tag(String id) {
            return ItemTags.bind(SlimierSlimes.MOD_ID + ":" + id);
        }
    }

    public static class EntityTypes {

        public static final Tag.Named<EntityType<?>> SLIMES = forgeTag("slimes");

        private static Tag.Named<EntityType<?>> forgeTag(String id) {
            return EntityTypeTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

    public static class Blocks {

        public static final Tag.Named<Block> MUSHROOMS = forgeTag("mushrooms");

        private static Tag.Named<Block> forgeTag(String id) {
            return BlockTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

}
