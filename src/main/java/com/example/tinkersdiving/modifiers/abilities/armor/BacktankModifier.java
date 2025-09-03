package com.example.tinkersdiving.modifiers.abilities.armor;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;


import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllEnchantments;
import com.example.tinkersdiving.TinkersDivingUtils;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import java.util.List;

import org.joml.Math;

import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;;


public class BacktankModifier extends Modifier{
    public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        // Only allow on chestplates, and only if not already present
        return slot == EquipmentSlot.CHEST && tool.getModifierLevel(this.getId()) < 6;
    }

    /**
     * Gets the current air stored in the backtank item.
     */
//    public static float getAir(ToolStack tool) {
//        CompoundTag tag = tool.getOrCreateTag();
//        return Math.min(tag.getFloat("Air"), (float)maxAir(backtank));
//    }
    public static float getAir(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        return Math.min(tag.getFloat("Air"), (float)maxAir(item));
    }

    /**
     * Sets the air value in the backtank item.
     */

    /**
     * Maximum air based on modifier level (1-6 levels, 1800 per level)
     */
    public static int maxAir(ToolStack tool) {
        // Use the modifier id statically
        int level = tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId());
        return 1200 * Math.max(level, 1);
    }
    public static int maxAir(ItemStack Item) {
        // Use the modifier id statically
        ToolStack tool = ToolStack.from(Item);
        int level = tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId());
        return 1200 * Math.max(level, 1);
    }

    /**
     * Consumes air from the backtank item, returns true if successful.
     * 
     * I don't believe any of this is necessary, as Create's BacktankUtil already handles air consumption and warnings now that I'm using mixin.
     *
      public static void consumeAir(LivingEntity entity, ItemStack backtank, float i) {
      CompoundTag tag = backtank.getOrCreateTag();
      int maxAir = maxAir(backtank);
      float air = getAir(backtank);
      float newAir = Math.max(air - i, 0.0F);
      tag.putFloat("Air", Math.min(newAir, (float)maxAir));
      backtank.setTag(tag);
      if (entity instanceof ServerPlayer player) {
         sendWarning(player, air, newAir, (float)maxAir / 10.0F);
         sendWarning(player, air, newAir, 1.0F);
      }
   }
   	private static void sendWarning(ServerPlayer player, float air, float newAir, float threshold) {
		if (newAir > threshold)
			return;
		if (air <= threshold)
			return;

		boolean depleted = threshold == 1;
		MutableComponent component = CreateLang.translateDirect(depleted ? "backtank.depleted" : "backtank.low");

		AllSoundEvents.DENY.play(player.level(), null, player.blockPosition(), 1, 1.25f);
		AllSoundEvents.STEAM.play(player.level(), null, player.blockPosition(), .5f, .5f);

		player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 40, 10));
		player.connection.send(new ClientboundSetSubtitleTextPacket(
			Component.literal("\u26A0 ").withStyle(depleted ? ChatFormatting.RED : ChatFormatting.GOLD)
				.append(component.withStyle(ChatFormatting.GRAY))));
		player.connection.send(new ClientboundSetTitleTextPacket(CommonComponents.EMPTY));
	}


    public static boolean isBarVisible(ItemStack stack, int usesPerTank) {
      if (usesPerTank == 0) {
         return false;
      } else {
         Player player = (Player)DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> {
            return () -> {
               return Minecraft.getInstance().player;
            };
         });
         if (player == null) {
            return false;
         } else {
            List<ItemStack> backtanks = TinkersDivingUtils.getAllWithAir(player);
            return backtanks.isEmpty() ? stack.isDamaged() : true;
         }
      }
   }

   public static int getBarWidth(ItemStack stack, int usesPerTank) {
      if (usesPerTank == 0) {
         return 13;
      } else {
         Player player = (Player)DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> {
            return () -> {
               return Minecraft.getInstance().player;
            };
         });
         if (player == null) {
            return 13;
         } else {
            List<ItemStack> backtanks = TinkersDivingUtils.getAllWithAir(player);
            if (backtanks.isEmpty()) {
               return Math.round(13.0F - (float)stack.getDamageValue() / (float)stack.getMaxDamage() * 13.0F);
            } else if (backtanks.size() == 1) {
               return ((ItemStack)backtanks.get(0)).getItem().getBarWidth((ItemStack)backtanks.get(0));
            } else {
               int sumBarWidth = (Integer)backtanks.stream().map((backtank) -> {
                  return backtank.getItem().getBarWidth(backtank);
               }).reduce(0, Integer::sum);
               return Math.round((float)sumBarWidth / (float)backtanks.size());
            }
         }
      }
   }

   public static int getBarColor(ItemStack stack, int usesPerTank) {
      if (usesPerTank == 0) {
         return 0;
      } else {
         Player player = (Player)DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> {
            return () -> {
               return Minecraft.getInstance().player;
            };
         });
         if (player == null) {
            return 0;
         } else {
            List<ItemStack> backtanks = TinkersDivingUtils.getAllWithAir(player);
            return backtanks.isEmpty() ? Mth.hsvToRgb(Math.max(0.0F, 1.0F - (float)stack.getDamageValue() / (float)stack.getMaxDamage()) / 3.0F, 1.0F, 1.0F) : ((ItemStack)backtanks.get(0)).getItem().getBarColor((ItemStack)backtanks.get(0));
         }
      }
   }

   public static boolean hasAirRemaining(ItemStack to) {
      return getAir(to) > 0.0F;
   }
      */

   //@Override
   //public int getEnchantmentLevel(ItemStack tool, Enchantment enchantment) {
   //   if (enchantment == AllEnchantments.CAPACITY.get()) {
   //      return (int) Math.ceil((1200*tool.getModifierLevel(this.getId()))/900);
   //   }
   //   return tool.getEnchantmentLevel(enchantment);
   //}
   
   
   /*
    * Apparently this isn't correct, but in case the other system fails me I'm keeping it here.
    *
   
   @Override
   public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
      if (enchantment == AllEnchantments.CAPACITY.get()) {
         ToolStack tool = ToolStack.from(stack);
         return (int) Math.ceil((1200 * tool.getModifierLevel(this.getId())) / 900);
      }
      return 0;
   }

   @Override
   public int getEnchantmentLevel(ToolStack tool, Enchantment enchantment) {
      if (enchantment == (AllEnchantments.CAPACITY.get())) {
         return (int) Math.ceil((1200 * tool.getModifierLevel(this.getId())) / 900);
      }
      return 0;
   }
   */

}
