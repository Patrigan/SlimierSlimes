package mod.patrigan.slimier_slimes.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.patrigan.slimier_slimes.SlimierSlimes;
import mod.patrigan.slimier_slimes.entities.LavaSlimeEntity;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;


public class LavaSlimeRenderer extends MobRenderer<LavaSlimeEntity, SlimeModel<LavaSlimeEntity>> {

    protected static final ResourceLocation[] TEXTURES = new ResourceLocation[38];

    public LavaSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.7f);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
        this.initTextures();
    }

    private void initTextures() {
        for(int i = 0; i < 19; i++) {
            TEXTURES[i] = new ResourceLocation(SlimierSlimes.MOD_ID, "textures/entity/lava_slime_" + i + ".png");
        }
        for(int i = 0; i < 19; i++) {
            TEXTURES[19 + i] = new ResourceLocation(SlimierSlimes.MOD_ID, "textures/entity/lava_slime_" + (20 - i - 1) + ".png");
        }
    }

    @Override
    public void render(LavaSlimeEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.shadowRadius = 0.25F * (float)entityIn.getSize();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(LavaSlimeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(f, f, f);
        matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSize();
        float f2 = Mth.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public ResourceLocation getTextureLocation(LavaSlimeEntity entity) {
        final int changeInterval = 3;
        int textureNum = ((entity.tickCount + entity.getId()) / changeInterval) % TEXTURES.length;
        return TEXTURES[textureNum];
    }
}
