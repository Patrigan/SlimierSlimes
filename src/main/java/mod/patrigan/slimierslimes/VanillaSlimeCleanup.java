package mod.patrigan.slimierslimes;

import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimierslimes.init.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mod.patrigan.slimierslimes.SlimierSlimes.MAIN_CONFIG;
import static mod.patrigan.slimierslimes.init.ModEntityTypes.*;
import static net.minecraft.entity.EntityType.SLIME;
import static net.minecraft.item.Items.SLIME_BALL;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaSlimeCleanup {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event)
    {
        if(Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get())) {
            event.getTrades().values().forEach(list->list.removeIf(VanillaSlimeCleanup::hasSlimeBall));
        }
    }

    @SubscribeEvent
    public static void onWandererTradesEvent(WandererTradesEvent event)
    {
        if(Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get())) {
            event.getGenericTrades().removeIf(VanillaSlimeCleanup::hasSlimeBall);
            event.getRareTrades().removeIf(VanillaSlimeCleanup::hasSlimeBall);
        }
    }

    private static boolean hasSlimeBall(VillagerTrades.ITrade trade){
        return (trade instanceof VillagerTrades.ItemsForEmeraldsTrade && ((VillagerTrades.ItemsForEmeraldsTrade) trade).itemStack.getItem().equals(SLIME_BALL)) ||
                (trade instanceof VillagerTrades.EmeraldForItemsTrade && ((VillagerTrades.EmeraldForItemsTrade) trade).item.equals(SLIME_BALL));
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if(Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get()) && !event.getWorld().isClientSide()){
            if(event.getEntity() instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity) event.getEntity();
                if (itemEntity.getItem().getItem().equals(SLIME_BALL)) {
                    itemEntity.setItem(new ItemStack(ModItems.SLIME_BALL.get(DyeColor.LIME).get(), itemEntity.getItem().getCount()));
                }
            }else if(event.getEntity() instanceof SlimeEntity) {
                SlimeEntity eventEntity = (SlimeEntity) event.getEntity();
                AbstractSlimeEntity slimeEntity = COMMON_SLIME.get().create(event.getWorld());
                if (eventEntity.isPersistenceRequired()) {
                    slimeEntity.setPersistenceRequired();
                }
                slimeEntity.setCustomName(eventEntity.getCustomName());
                slimeEntity.setNoAi(eventEntity.isNoAi());
                slimeEntity.setInvulnerable(eventEntity.isInvulnerable());
                slimeEntity.setSize(eventEntity.getSize(), true);
                slimeEntity.moveTo(eventEntity.getX(), eventEntity.getY(), eventEntity.getZ(), eventEntity.yRot, eventEntity.xRot);
                event.getWorld().addFreshEntity(slimeEntity);
                eventEntity.remove(false);
            }
        }
    }

    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event) {
        if (Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get())) {
            List<MobSpawnInfo.Spawners> spawners = new ArrayList<>(event.getSpawns().getSpawner(SLIME.getCategory()));
            event.getSpawns().getSpawner(SLIME.getCategory()).clear();
            event.getSpawns().getSpawner(SLIME.getCategory()).addAll(spawners.stream().filter(spawner -> !spawner.type.equals(SLIME)).collect(Collectors.toList()));
        }
    }


}
