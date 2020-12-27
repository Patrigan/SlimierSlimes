package mod.patrigan.slimierslimes.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import static mod.patrigan.slimierslimes.init.ModBlocks.AMETHYST_CLUSTER;

public class AmethystProjectileRenderer extends EntityRenderer<AmethystProjectileEntity> {

    //private static final ResourceLocation EVOKER_ILLAGER_FANGS = new ResourceLocation("textures/entity/illager/evoker_fangs.png");

    public AmethystProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(AmethystProjectileEntity entity) {
        return null;
    }

    @Override
    public void render(AmethystProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(AMETHYST_CLUSTER.get().getDefaultState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
