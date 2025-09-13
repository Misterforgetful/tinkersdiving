package com.example.tinkersdiving.modifiers.abilities.armor;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;



public class BacktankModifier extends Modifier{
   public static final int AIR_PER_LEVEL = com.example.tinkersdiving.config.TinkersDivingConfig.COMMON.airPerLevel.get(); // 1200 air per additional level by default, I'm not sure why that's what I chose, 1 level = 20 minutes of underwater breathing though, so that's nice. Probably to reward players who go this route.
   public static final int BASE_AIR = com.example.tinkersdiving.config.TinkersDivingConfig.COMMON.defaultModifierCapacity.get(); // Base air for level 1 backtank modifier, also 20 minutes of underwater breathing by default. May change to 900 to match create when I publish.

   public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        // Only allow on chestplates, and only if not already present
        return slot == EquipmentSlot.CHEST && tool.getModifierLevel(this.getId()) < 6;
    }
}