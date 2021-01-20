package mod.patrigan.slimierslimes.items;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JellyItem extends Item implements IItemColor {
    private DyeColor dyeColor;

    public JellyItem(Properties properties, DyeColor dyeColor) {
        super(properties);
        this.dyeColor = dyeColor;
    }

    @Override
    public int getColor(ItemStack itemStack, int tintIndex) {
        return dyeColor.getColorValue();
    }
}
