package mod.patrigan.slimierslimes.items;

import mod.patrigan.slimierslimes.entities.projectile.SlimeBallEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SlimeBallItem extends Item {
   DyeColor dyeColor;
   public SlimeBallItem(DyeColor dyeColor, Properties properties) {
      super(properties);
      this.dyeColor = dyeColor;
   }

   @Override
   public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
      ItemStack itemstack = playerEntity.getItemInHand(hand);
      world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
      if (!world.isClientSide) {
         SlimeBallEntity slimeBallEntity = new SlimeBallEntity(this, world, playerEntity);
         slimeBallEntity.setItem(itemstack);
         slimeBallEntity.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 0.0F, 1.5F, 1.0F);
         world.addFreshEntity(slimeBallEntity);
      }

      playerEntity.awardStat(Stats.ITEM_USED.get(this));
      if (!playerEntity.abilities.instabuild) {
         itemstack.shrink(1);
      }

      return ActionResult.sidedSuccess(itemstack, world.isClientSide());
   }

   public DyeColor getDyeColor() {
      return dyeColor;
   }
}
