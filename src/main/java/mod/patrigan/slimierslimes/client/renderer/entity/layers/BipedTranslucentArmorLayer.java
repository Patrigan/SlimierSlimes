package mod.patrigan.slimierslimes.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mod.patrigan.slimierslimes.client.renderer.ModRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

import static mod.patrigan.slimierslimes.init.ModTags.Items.*;

@OnlyIn(Dist.CLIENT)
public class BipedTranslucentArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {
   private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
   private final A innerModel;
   private final A outerModel;

   public BipedTranslucentArmorLayer(IEntityRenderer<T, M> p_i50936_1_, A p_i50936_2_, A p_i50936_3_) {
      super(p_i50936_1_);
      this.innerModel = p_i50936_2_;
      this.outerModel = p_i50936_3_;
   }

   public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      this.renderArmorPiece(p_225628_1_, p_225628_2_, entity, EquipmentSlotType.CHEST, p_225628_3_, this.getArmorModel(EquipmentSlotType.CHEST));
      this.renderArmorPiece(p_225628_1_, p_225628_2_, entity, EquipmentSlotType.LEGS, p_225628_3_, this.getArmorModel(EquipmentSlotType.LEGS));
      this.renderArmorPiece(p_225628_1_, p_225628_2_, entity, EquipmentSlotType.FEET, p_225628_3_, this.getArmorModel(EquipmentSlotType.FEET));
      this.renderArmorPiece(p_225628_1_, p_225628_2_, entity, EquipmentSlotType.HEAD, p_225628_3_, this.getArmorModel(EquipmentSlotType.HEAD));
   }

   private void renderArmorPiece(MatrixStack p_241739_1_, IRenderTypeBuffer p_241739_2_, T p_241739_3_, EquipmentSlotType p_241739_4_, int p_241739_5_, A p_241739_6_) {
      ItemStack itemstack = p_241739_3_.getItemBySlot(p_241739_4_);
      if (itemstack.getItem() instanceof ArmorItem && isSlime(itemstack.getItem())) {
         ArmorItem armoritem = (ArmorItem)itemstack.getItem();
         if (armoritem.getSlot() == p_241739_4_) {
            p_241739_6_ = getArmorModelHook(p_241739_3_, itemstack, p_241739_4_, p_241739_6_);
            this.getParentModel().copyPropertiesTo(p_241739_6_);
            this.setPartVisibility(p_241739_6_, p_241739_4_);
            // boolean flag = this.usesInnerModel(p_241739_4_);
            boolean flag1 = itemstack.hasFoil();
            if(isSlime(itemstack.getItem())){
               int i = Minecraft.getInstance().getItemColors().getColor(itemstack, 0);
               renderColoredModel(p_241739_1_, p_241739_2_, p_241739_3_, p_241739_4_, p_241739_5_, p_241739_6_, itemstack, flag1, i);
            }else if (armoritem instanceof net.minecraft.item.IDyeableArmorItem) {
               int i = ((net.minecraft.item.IDyeableArmorItem)armoritem).getColor(itemstack);
               renderColoredModel(p_241739_1_, p_241739_2_, p_241739_3_, p_241739_4_, p_241739_5_, p_241739_6_, itemstack, flag1, i);
               this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, p_241739_6_, 1.0F, 1.0F, 1.0F, this.getArmorResource(p_241739_3_, itemstack, p_241739_4_, "overlay"));
            } else {
               this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, p_241739_6_, 1.0F, 1.0F, 1.0F, this.getArmorResource(p_241739_3_, itemstack, p_241739_4_, null));
            }

         }
      }
   }

   private void renderColoredModel(MatrixStack p_241739_1_, IRenderTypeBuffer p_241739_2_, T p_241739_3_, EquipmentSlotType p_241739_4_, int p_241739_5_, A p_241739_6_, ItemStack itemstack, boolean flag1, int i) {
      float f = (float)(i >> 16 & 255) / 255.0F;
      float f1 = (float)(i >> 8 & 255) / 255.0F;
      float f2 = (float)(i & 255) / 255.0F;
      this.renderModel(p_241739_1_, p_241739_2_, p_241739_5_, flag1, p_241739_6_, f, f1, f2, this.getArmorResource(p_241739_3_, itemstack, p_241739_4_, null));
   }

   private boolean isSlime(Item item) {
      return item.getItem().is(ARMOR_SLIME_BOOTS) || item.getItem().is(ARMOR_SLIME_LEGGINGS) || item.getItem().is(ARMOR_SLIME_CHESTPLATE) || item.getItem().is(ARMOR_SLIME_HELMET);
   }

   protected void setPartVisibility(A p_188359_1_, EquipmentSlotType p_188359_2_) {
      p_188359_1_.setAllVisible(false);
      switch(p_188359_2_) {
      case HEAD:
         p_188359_1_.head.visible = true;
         p_188359_1_.hat.visible = true;
         break;
      case CHEST:
         p_188359_1_.body.visible = true;
         p_188359_1_.rightArm.visible = true;
         p_188359_1_.leftArm.visible = true;
         break;
      case LEGS:
         p_188359_1_.body.visible = true;
         p_188359_1_.rightLeg.visible = true;
         p_188359_1_.leftLeg.visible = true;
         break;
      case FEET:
         p_188359_1_.rightLeg.visible = true;
         p_188359_1_.leftLeg.visible = true;
         break;
      default:
         break;
      }

   }

   private void renderModel(MatrixStack p_241738_1_, IRenderTypeBuffer p_241738_2_, int p_241738_3_, boolean p_241738_5_, A p_241738_6_, float p_241738_8_, float p_241738_9_, float p_241738_10_, ResourceLocation armorResource) {
      IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(p_241738_2_, ModRenderType.armorCutoutNoCullTranslucent(armorResource), false, p_241738_5_);
      p_241738_6_.renderToBuffer(p_241738_1_, ivertexbuilder, p_241738_3_, OverlayTexture.NO_OVERLAY, p_241738_8_, p_241738_9_, p_241738_10_, 1.0F);
   }

   private A getArmorModel(EquipmentSlotType p_241736_1_) {
      return (A)(this.usesInnerModel(p_241736_1_) ? this.innerModel : this.outerModel);
   }

   private boolean usesInnerModel(EquipmentSlotType p_188363_1_) {
      return p_188363_1_ == EquipmentSlotType.LEGS;
   }

   /*=================================== FORGE START =========================================*/

   /**
    * Hook to allow item-sensitive armor model. for LayerBipedArmor.
    */
   protected A getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlotType slot, A model) {
      return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
   }

   /**
    * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
    *
    * @param entity Entity wearing the armor
    * @param stack ItemStack for the armor
    * @param slot Slot ID that the item is in
    * @param type Subtype, can be null or "overlay"
    * @return ResourceLocation pointing at the armor's texture
    */
   public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
      ArmorItem item = (ArmorItem)stack.getItem();
      String texture = item.getMaterial().getName();
      String domain = "minecraft";
      int idx = texture.indexOf(':');
      if (idx != -1) {
         domain = texture.substring(0, idx);
         texture = texture.substring(idx + 1);
      }
      String s1 = String.format("%s:textures/models/armor/%s_layer_translucent_%d%s.png", domain, texture, (usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

      s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
      ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

      if (resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
      }

      return resourcelocation;
   }
   /*=================================== FORGE END ===========================================*/
}
