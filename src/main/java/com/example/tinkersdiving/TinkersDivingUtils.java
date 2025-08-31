package com.example.tinkersdiving;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.example.tinkersdiving.modifiers.abilities.armor.BacktankModifier;
import com.simibubi.create.AllTags.AllItemTags;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class TinkersDivingUtils {
    private static final List<Function<LivingEntity, List<ItemStack>>> BACKTANK_SUPPLIERS = new ArrayList();
    static {
      addBacktankSupplier((entity) -> {
         List<ItemStack> stacks = new ArrayList();
         Iterator var2 = entity.getArmorSlots().iterator();

         while(var2.hasNext()) {
            ItemStack itemStack = (ItemStack)var2.next();
            if (AllItemTags.PRESSURIZED_AIR_SOURCES.matches(itemStack)) {
               stacks.add(itemStack);
            }
         }

         return stacks;
      });
    }
    public static boolean hasModifier(Player player, Modifier modifier) {
        for (var stack : player.getArmorSlots()) {
            ToolStack tool = ToolStack.from(stack);
            if (tool.getModifierLevel(modifier.getId()) > 0) {
                return true;
            }
        }
        return false;
    }

   public static void addBacktankSupplier(Function<LivingEntity, List<ItemStack>> supplier) {
      BACKTANK_SUPPLIERS.add(supplier);
   }

    public static boolean hasModifier(ToolStack tool, Modifier modifier) {
        return tool.getModifierLevel(modifier.getId()) > 0;
    }

    public static boolean hasModifier(ItemStack stack, Modifier modifier) {
        ToolStack tool = ToolStack.from(stack);
        return tool.getModifierLevel(modifier.getId()) > 0;
    }

    public static List<ItemStack> getAllWithAir(LivingEntity entity) {
      List<ItemStack> all = new ArrayList();
      Iterator var2 = BACKTANK_SUPPLIERS.iterator();

      while(var2.hasNext()) {
         Function<LivingEntity, List<ItemStack>> supplier = (Function)var2.next();
         List<ItemStack> result = (List)supplier.apply(entity);
         Iterator var5 = result.iterator();

         while(var5.hasNext()) {
            ItemStack stack = (ItemStack)var5.next();
            if (hasAirRemaining(stack)) {
               all.add(stack);
            }
         }
      }

      all.sort((a, b) -> {
         return Float.compare(BacktankModifier.getAir(a), BacktankModifier.getAir(b));
      });
      return all;
   }
   public static boolean hasAirRemaining(ItemStack stack) {
      return BacktankModifier.getAir(stack) > 0.0F;
   }
}