package com.example.tinkersdiving.modifiers.abilities.armor;


import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class DivingBootsModifier extends Modifier {
    public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        return slot == EquipmentSlot.FEET && tool.getModifierLevel(this.getId()) == 0;
    }
}