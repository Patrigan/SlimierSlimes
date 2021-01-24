package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static net.minecraft.entity.merchant.villager.VillagerTrades.field_221240_b;
import static net.minecraft.item.Items.SLIME_BALL;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaSlimeCleanup {

    public static void WanderingTraderCleanup(){
        field_221240_b.forEach((key, trades) ->
                field_221240_b.put(key, Arrays.stream(trades).filter(VanillaSlimeCleanup::filterSlimeBall).toArray(VillagerTrades.ITrade[]::new)));
    }

    private static boolean filterSlimeBall(VillagerTrades.ITrade trade){
        return trade instanceof VillagerTrades.ItemsForEmeraldsTrade && ! ((VillagerTrades.ItemsForEmeraldsTrade) trade).sellingItem.getItem().equals(SLIME_BALL);
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent entityJoinWorldEventIn) {
        if(entityJoinWorldEventIn.getEntity() instanceof ItemEntity){
            ItemEntity itemEntity = (ItemEntity) entityJoinWorldEventIn.getEntity();
            if(itemEntity.getItem().getItem().equals(SLIME_BALL)){
                itemEntity.setItem(new ItemStack(ModItems.SLIME_BALL.get(DyeColor.LIME).get(), itemEntity.getItem().getCount()));
            }
        }
    }
}
