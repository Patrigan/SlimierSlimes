package mod.patrigan.slimierslimes.client.entity.render.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import mod.patrigan.slimierslimes.entities.ShroomSlimeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.util.math.vector.Vector3f;

public class ShroomSlimeMushroomLayer<T extends ShroomSlimeEntity> extends LayerRenderer<T, SlimeModel<T>> {

    private static final float ADJUSTMENT = 0.14F;

    public ShroomSlimeMushroomLayer(IEntityRenderer<T, SlimeModel<T>> p_i50931_1_) {
        super(p_i50931_1_);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!entity.isInvisible()) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            int i = LivingRenderer.getOverlayCoords(entity, 0.0F);
            if(entity.isTiny() && entity.getMushrooms().size() > 0) {
                tinyRendering(matrixStack, p_225628_2_, p_225628_3_, entity, blockrendererdispatcher, entity.getMushrooms().get(0), i);
            }else {
                if (entity.getMushrooms().size() > 0) {
                    matrixStack.pushPose();
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.translate(-0.5F + (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.5F + (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderBlock(entity.getMushrooms().get(0), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
                if (entity.getMushrooms().size() > 1){
                    matrixStack.pushPose();
                    //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F+entity.getRandom().nextInt(6)-3)); // spin it randomly
                    matrixStack.scale(-1.0F / entity.getSize(), -1.0F / entity.getSize(), 1.0F / entity.getSize()); // apply entity size and flip it
                    matrixStack.translate(-0.5F - (ADJUSTMENT * entity.getSize()), (-1F * entity.getSize()) - 0.05F, -0.5F - (ADJUSTMENT * entity.getSize())); // move it on top
                    blockrendererdispatcher.renderBlock(entity.getMushrooms().get(1), matrixStack, p_225628_2_, p_225628_3_, i, net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
                    matrixStack.popPose();
                }
            }
        }
    }

    private void tinyRendering(MatrixStack matrixStack, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T entity, BlockRendererDispatcher blockrendererdispatcher, BlockState blockstate, int i) {
        matrixStack.pushPose();
        //matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F)); // spin it randomly
        matrixStack.scale(-1.0F/ entity.getSize(), -1.0F/ entity.getSize(), 1.0F/ entity.getSize()); // apply entity size and flip it
        matrixStack.translate(-0.5F, (-1F* entity.getSize())-0.05F, -0.5F); // move it on top
        blockrendererdispatcher.renderBlock(blockstate, matrixStack, p_225628_2_, p_225628_3_, i,  net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }

}
