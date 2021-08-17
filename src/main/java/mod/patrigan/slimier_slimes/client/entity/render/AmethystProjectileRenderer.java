package mod.patrigan.slimier_slimes.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.patrigan.slimier_slimes.entities.projectile.AmethystProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import static mod.patrigan.slimier_slimes.init.ModBlocks.*;

public class AmethystProjectileRenderer extends EntityRenderer<AmethystProjectileEntity> {

    public AmethystProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AmethystProjectileEntity entity) {
        return null;
    }

    @Override
    public void render(AmethystProjectileEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float f = entityIn.getAnimationProgress(partialTicks);
        if (f != 0.0F) {
            matrixStackIn.pushPose();
            Block block = AMETHYST_CLUSTER.get();
            if(f <= 0.1F){
                block = SMALL_AMETHYST_BUD.get();
            }else if(f <= 0.2F){
                block = MEDIUM_AMETHYST_BUD.get();
            }else if(f <= 0.3F){
                block = LARGE_AMETHYST_BUD.get();
            }
            Minecraft.getInstance().getBlockRenderer()
                    .renderSingleBlock(block.defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
            matrixStackIn.popPose();
        }
    }
}