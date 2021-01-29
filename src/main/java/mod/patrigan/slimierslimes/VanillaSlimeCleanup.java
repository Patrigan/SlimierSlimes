package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.SlimierSlimes.SlimeConfig;
import static net.minecraft.item.Items.SLIME_BALL;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaSlimeCleanup {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event)
    {
        if(Boolean.FALSE.equals(SlimeConfig.allowVanillaSlime.get())) {
            event.getTrades().forEach((key, trades) ->
                    event.getTrades().put(key, trades.stream().filter(VanillaSlimeCleanup::filterSlimeBall).collect(Collectors.toList())));
        }
    }

    private static boolean filterSlimeBall(VillagerTrades.ITrade trade){
        return trade instanceof VillagerTrades.ItemsForEmeraldsTrade && ! ((VillagerTrades.ItemsForEmeraldsTrade) trade).sellingItem.getItem().equals(SLIME_BALL);
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent entityJoinWorldEventIn) {
        if(Boolean.FALSE.equals(SlimeConfig.allowVanillaSlime.get())
            && entityJoinWorldEventIn.getEntity() instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entityJoinWorldEventIn.getEntity();
            if (itemEntity.getItem().getItem().equals(SLIME_BALL)) {
                itemEntity.setItem(new ItemStack(ModItems.SLIME_BALL.get(DyeColor.LIME).get(), itemEntity.getItem().getCount()));
            }
        }
    }
}
