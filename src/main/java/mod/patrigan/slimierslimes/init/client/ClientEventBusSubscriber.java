package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.client.entity.render.*;
import mod.patrigan.slimierslimes.entities.projectile.SlimeBallEntity;
import mod.patrigan.slimierslimes.init.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.SLIME_BALL_PROJECTILE;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.COMMON_SLIME.get(), CommonSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PINK_SLIME.get(), PinkSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CLOUD_SLIME.get(), CloudSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ROCK_SLIME.get(), RockSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LAVA_SLIME.get(), LavaSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.OBSIDIAN_SLIME.get(), ObsidianSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.AMETYST_PROJECTILE.get(), AmethystProjectileRenderer::new);
        SLIME_BALL_PROJECTILE.forEach((dyeColor, entityTypeRegistryObject) ->
                RenderingRegistry.registerEntityRenderingHandler(entityTypeRegistryObject.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()))
                );
    }
}
