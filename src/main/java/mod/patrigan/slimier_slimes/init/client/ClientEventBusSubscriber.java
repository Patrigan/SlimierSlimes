package mod.patrigan.slimier_slimes.init.client;

import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.client.entity.render.*;
import mod.patrigan.slimier_slimes.client.renderer.entity.layers.HumanoidTranslucentArmorLayer;
import mod.patrigan.slimier_slimes.init.ModEntityTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static mod.patrigan.slimier_slimes.init.ModEntityTypes.SLIME_BALL_PROJECTILE;
import static net.minecraft.client.model.geom.ModelLayers.*;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@Mod.EventBusSubscriber(modid = SlimierSlimes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.COMMON_SLIME.get(), AbstractSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.PINK_SLIME.get(), PinkSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CLOUD_SLIME.get(), CloudSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ROCK_SLIME.get(), RockSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CRYSTAL_SLIME.get(), CrystalSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GLOW_SLIME.get(), GlowSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CREEPER_SLIME.get(), CreeperSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SNOW_SLIME.get(), SnowSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CAMO_SLIME.get(), CamoSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DIAMOND_SLIME.get(), DiamondSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.LAVA_SLIME.get(), LavaSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.OBSIDIAN_SLIME.get(), ObsidianSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BROWN_GOO_SLIME.get(), GooSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SHROOM_SLIME.get(), ShroomSlimeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.AMETYST_PROJECTILE.get(), AmethystProjectileRenderer::new);
        SLIME_BALL_PROJECTILE.forEach((dyeColor, entityTypeRegistryObject) ->
                event.registerEntityRenderer(entityTypeRegistryObject.get(), ThrownItemRenderer::new)
                );
    }


    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        final PlayerRenderer defaultRenderer = event.getSkin("default");
        defaultRenderer.addLayer(new HumanoidTranslucentArmorLayer(defaultRenderer, new HumanoidModel<Player>(event.getEntityModels().bakeLayer(PLAYER_INNER_ARMOR)), new HumanoidModel<Player>(event.getEntityModels().bakeLayer(PLAYER_SLIM_OUTER_ARMOR))));
        final PlayerRenderer slimRenderer = event.getSkin("slim");
        slimRenderer.addLayer(new HumanoidTranslucentArmorLayer(slimRenderer, new HumanoidModel<Player>(event.getEntityModels().bakeLayer(PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<Player>(event.getEntityModels().bakeLayer(PLAYER_SLIM_OUTER_ARMOR))));

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
