package mod.patrigan.slimier_slimes.client.entity.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.patrigan.slimier_slimes.entities.ShroomSlimeEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.SlimeModel;
import com.mojang.math.Vector3f;

public class ShroomSlimeMushroomLayer<T extends ShroomSlimeEntity> extends RenderLayer<T, SlimeModel<T>> {

    private static final float ADJUSTMENT = 0.14F;

    public ShroomSlimeMushroomLayer(RenderLayerParent<T, SlimeModel<T>> p_i50931_1_, EntityModelSet p_174537_) {
        super(p_i50931_1_);
    }

    public void render(PoseStack matrixStack, MultiBufferSource p_225628_2_, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!entity.isInvisible()) {
            BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
            if(entity.isTiny() && entity.getMushrooms().size() > 0) {
                tinyRendering(matrixStack, p_225628_2_, p_225628_3_, entity, blockrendererdispatcher, entity.getMushrooms().get(0), i);
            }else {
                if (entity.getMushrooms().size() > 0) {
                    matrixStack.pushPose();
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it
                    matrixStack.translate(-0.5F + (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.5F + (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderSingleBlock(entity.getMushrooms().get(0), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
                if (entity.getMushrooms().size() > 1){
                    matrixStack.pushPose();
                    //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it randomly
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(80.0F)); // spin it
                    matrixStack.translate(-0.5F - (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.5F - (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderSingleBlock(entity.getMushrooms().get(1), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
                if (entity.getMushrooms().size() > 2){
                    matrixStack.pushPose();
                    //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it randomly
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(121.0F)); // spin it
                    matrixStack.translate(-0.3F - (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.3F - (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderSingleBlock(entity.getMushrooms().get(1), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
                if (entity.getMushrooms().size() > 3){
                    matrixStack.pushPose();
                    //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it randomly
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.translate(-0.62F - (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.62F - (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderSingleBlock(entity.getMushrooms().get(1), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
            }
        }
    }

    private void tinyRendering(PoseStack matrixStack, MultiBufferSource p_225628_2_, int p_225628_3_, T entity, BlockRenderDispatcher blockrendererdispatcher, BlockState blockstate, int i) {
        matrixStack.pushPose();
        //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it randomly
        matrixStack.scale(-1.0F/ entity.getSize(), -1.0F/ entity.getSize(), 1.0F/ entity.getSize()); // apply entity size and flip it
        matrixStack.translate(-0.5F, (-1F* entity.getSize())-0.05F, -0.5F); // move it on top
        blockrendererdispatcher.renderSingleBlock(blockstate, matrixStack, p_225628_2_, p_225628_3_, i,  net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }

}
