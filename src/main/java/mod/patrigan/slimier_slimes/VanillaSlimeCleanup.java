package mod.patrigan.slimier_slimes;

import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import mod.patrigan.slimier_slimes.init.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mod.patrigan.slimier_slimes.SlimierSlimes.MAIN_CONFIG;
import static mod.patrigan.slimier_slimes.init.ModEntityTypes.COMMON_SLIME;
import static net.minecraft.world.entity.EntityType.SLIME;
import static net.minecraft.world.item.Items.SLIME_BALL;

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

    private static boolean hasSlimeBall(VillagerTrades.ItemListing trade){
        return (trade instanceof VillagerTrades.ItemsForEmeralds && ((VillagerTrades.ItemsForEmeralds) trade).itemStack.getItem().equals(SLIME_BALL)) ||
                (trade instanceof VillagerTrades.EmeraldForItems && ((VillagerTrades.EmeraldForItems) trade).item.equals(SLIME_BALL));
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if(Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get()) && !event.getWorld().isClientSide()){
            if(event.getEntity() instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity) event.getEntity();
                if (itemEntity.getItem().getItem().equals(SLIME_BALL)) {
                    itemEntity.setItem(new ItemStack(ModItems.SLIME_BALL.get(DyeColor.LIME).get(), itemEntity.getItem().getCount()));
                }
            }else if(event.getEntity() instanceof Slime) {
                Slime eventEntity = (Slime) event.getEntity();
                AbstractSlimeEntity slimeEntity = COMMON_SLIME.get().create(event.getWorld());
                if (eventEntity.isPersistenceRequired()) {
                    slimeEntity.setPersistenceRequired();
                }
                slimeEntity.setCustomName(eventEntity.getCustomName());
                slimeEntity.setNoAi(eventEntity.isNoAi());
                slimeEntity.setInvulnerable(eventEntity.isInvulnerable());
                slimeEntity.setSize(eventEntity.getSize(), true);
                slimeEntity.moveTo(eventEntity.getX(), eventEntity.getY(), eventEntity.getZ(), eventEntity.getYRot(), eventEntity.getXRot());
                event.getWorld().addFreshEntity(slimeEntity);
                eventEntity.remove(false);
            }
        }
    }

    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event) {
        if (Boolean.FALSE.equals(MAIN_CONFIG.allowVanillaSlime.get())) {
            List<MobSpawnSettings.SpawnerData> spawners = new ArrayList<>(event.getSpawns().getSpawner(SLIME.getCategory()));
            event.getSpawns().getSpawner(SLIME.getCategory()).clear();
            event.getSpawns().getSpawner(SLIME.getCategory()).addAll(spawners.stream().filter(spawner -> !spawner.type.equals(SLIME)).collect(Collectors.toList()));
        }
    }


}
