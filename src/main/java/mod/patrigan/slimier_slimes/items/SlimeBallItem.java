package mod.patrigan.slimier_slimes.items;

import mod.patrigan.slimier_slimes.entities.projectile.SlimeBallEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

public class SlimeBallItem extends Item {
   DyeColor dyeColor;
   public SlimeBallItem(DyeColor dyeColor, Properties properties) {
      super(properties);
      this.dyeColor = dyeColor;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
      ItemStack itemstack = playerEntity.getItemInHand(hand);
      world.playSound((Player)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
      if (!world.isClientSide) {
         SlimeBallEntity slimeBallEntity = new SlimeBallEntity(this, world, playerEntity);
         slimeBallEntity.setItem(itemstack);
         slimeBallEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 1.5F, 1.0F);
         world.addFreshEntity(slimeBallEntity);
      }

      playerEntity.awardStat(Stats.ITEM_USED.get(this));
      if (!playerEntity.getAbilities().instabuild) {
         itemstack.shrink(1);
      }

      return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
   }

   public DyeColor getDyeColor() {
      return dyeColor;
   }
}
