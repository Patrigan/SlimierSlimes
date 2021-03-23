package mod.patrigan.slimierslimes.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.entities.AbstractSlimeEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class PinkSlimeRenderer extends MobRenderer<AbstractSlimeEntity, SlimeModel<AbstractSlimeEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(SlimierSlimes.MOD_ID, "textures/entity/pink_slime.png");
    private static final float SIZEFACTOR = 0.3F;

    public PinkSlimeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SlimeModel<>(16), 0.7f);
        this.addLayer(new SlimeGelLayer<>(this));
    }

    @Override
    public void render(AbstractSlimeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.shadowRadius = 0.25F * (float)entityIn.getSize() * SIZEFACTOR;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(AbstractSlimeEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(f, f, f);
        matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSize() * SIZEFACTOR;
        float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSlimeEntity entity) {
        return TEXTURE;
    }
}
