package mod.patrigan.slimier_slimes.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.entities.CreeperSlimeEntity;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;


public class CreeperSlimeRenderer extends MobRenderer<CreeperSlimeEntity, SlimeModel<CreeperSlimeEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(SlimierSlimes.MOD_ID, "textures/entity/creeper_slime.png");

    public CreeperSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.7f);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public void render(CreeperSlimeEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.shadowRadius = 0.25F * (float)entityIn.getSize();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperSlimeEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    protected void scale(CreeperSlimeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(f, f, f);
        matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSize();
        float f2 = Mth.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
        preRenderCreeperFlashing(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    private void preRenderCreeperFlashing(CreeperSlimeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime){
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(f2, f3, f2);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperSlimeEntity entity) {
        return TEXTURE;
    }
}
