package mod.patrigan.slimierslimes.init.client;

import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.client.entity.render.*;
import mod.patrigan.slimierslimes.client.renderer.entity.layers.BipedTranslucentArmorLayer;
import mod.patrigan.slimierslimes.init.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

import static mod.patrigan.slimierslimes.init.ModEntityTypes.SLIME_BALL_PROJECTILE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = CLIENT)
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BROWN_GOO_SLIME.get(), GooSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.AMETYST_PROJECTILE.get(), AmethystProjectileRenderer::new);
        SLIME_BALL_PROJECTILE.forEach((dyeColor, entityTypeRegistryObject) ->
                RenderingRegistry.registerEntityRenderingHandler(entityTypeRegistryObject.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()))
                );

        addTranslucentArmorRenderer();
    }

    private static void addTranslucentArmorRenderer() {
        final Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        final PlayerRenderer defaultRenderer = skinMap.get("default");
        defaultRenderer.addLayer(new BipedTranslucentArmorLayer<>(defaultRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        final PlayerRenderer slimRenderer = skinMap.get("slim");
        slimRenderer.addLayer(new BipedTranslucentArmorLayer<>(slimRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));

//TODO: make mobs spawnable with the armors, Also clean this shit up.
/*        ZombieRenderer zombieRenderer = (ZombieRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(ZOMBIE);
        zombieRenderer.addLayer(new BipedTranslucentArmorLayer<>(zombieRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        GiantZombieRenderer giantZombieRenderer = (GiantZombieRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(GIANT);
        giantZombieRenderer.addLayer(new BipedTranslucentArmorLayer<>(giantZombieRenderer, new GiantModel(0.5F, true), new GiantModel(1.0F, true)));
        HuskRenderer huskRenderer = (HuskRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(HUSK);
        huskRenderer.addLayer(new BipedTranslucentArmorLayer<>(huskRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        DrownedRenderer drownedRenderer = (DrownedRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(DROWNED);
        drownedRenderer.addLayer(new BipedTranslucentArmorLayer<>(drownedRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        ArmorStandRenderer armorStandRenderer = (ArmorStandRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(ARMOR_STAND);
        armorStandRenderer.addLayer(new BipedTranslucentArmorLayer<>(armorStandRenderer, new ArmorStandArmorModel(0.5F), new ArmorStandArmorModel(1.0F)));
        PiglinRenderer piglinRenderer = (PiglinRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(PIGLIN);
        piglinRenderer.addLayer(new BipedTranslucentArmorLayer<>(piglinRenderer, new BipedModel<>(0.5F), new BipedModel<>(1.02F)));
        SkeletonRenderer skeletonRenderer = (SkeletonRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(SKELETON);
        skeletonRenderer.addLayer(new BipedTranslucentArmorLayer<>(skeletonRenderer, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        skeletonRenderer = (SkeletonRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(WITHER_SKELETON);
        skeletonRenderer.addLayer(new BipedTranslucentArmorLayer<>(skeletonRenderer, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        skeletonRenderer = (SkeletonRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(STRAY);
        skeletonRenderer.addLayer(new BipedTranslucentArmorLayer<>(skeletonRenderer, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        ZombieVillagerRenderer zombieVillagerRenderer = (ZombieVillagerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(ZOMBIE_VILLAGER);
        zombieVillagerRenderer.addLayer(new BipedTranslucentArmorLayer<>(zombieVillagerRenderer, new ZombieVillagerModel<>(0.5F, true), new ZombieVillagerModel<>(1.0F, true)));*/
    }
}
