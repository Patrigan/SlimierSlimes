package mod.patrigan.slimier_slimes.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.patrigan.slimier_slimes.entities.AbstractSlimeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;


public class PinkSlimeRenderer extends AbstractSlimeRenderer {
    private static final float SIZEFACTOR = 0.3F;

    public PinkSlimeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void scale(AbstractSlimeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(f, f, f);
        matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSize() * SIZEFACTOR;
        float f2 = Mth.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }
}
