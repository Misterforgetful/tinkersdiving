package com.example.tinkersdiving.modifiers.abilities.armor;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;

public class DivingHelmetModifier extends Modifier {

    public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD && tool.getModifierLevel(this.getId()) == 0;
    }
}
