package mod.patrigan.slimierslimes.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.patrigan.slimierslimes.entities.projectile.AmethystProjectileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import static mod.patrigan.slimierslimes.init.ModBlocks.*;

public class AmethystProjectileRenderer extends EntityRenderer<AmethystProjectileEntity> {


    public AmethystProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(AmethystProjectileEntity entity) {
        return null;
    }

    @Override
    public void render(AmethystProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = entityIn.getAnimationProgress(partialTicks);
        if (f != 0.0F) {
            matrixStackIn.push();
            Block block = AMETHYST_CLUSTER.get();
            if(f <= 0.1F){
                block = SMALL_AMETHYST_BUD.get();
            }else if(f <= 0.2F){
                block = MEDIUM_AMETHYST_BUD.get();
            }else if(f <= 0.3F){
                block = LARGE_AMETHYST_BUD.get();
            }
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(block.getDefaultState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
            matrixStackIn.pop();
        }
    }
}