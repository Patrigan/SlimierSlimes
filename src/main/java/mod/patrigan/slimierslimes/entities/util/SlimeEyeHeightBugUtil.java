package mod.patrigan.slimierslimes.entities.util;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SlimeEyeHeightBugUtil {

    @SubscribeEvent
    public static LivingDamageEvent handleLivingDamageEvent(LivingDamageEvent event){
        if(DamageSource.IN_WALL.equals(event.getSource()) && event.getEntity() instanceof AbstractSlimeEntity){
            AbstractSlimeEntity entity = (AbstractSlimeEntity) event.getEntity();
            if(entity.getSlimeSize() == 1){
                event.setCanceled(true);
            }
            return event;
        }else{
            return event;
        }
    }
}
