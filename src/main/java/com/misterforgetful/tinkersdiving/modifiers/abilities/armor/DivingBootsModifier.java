package com.misterforgetful.tinkersdiving.modifiers.abilities.armor;


import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;

public class DivingBootsModifier extends NoLevelsModifier {
    public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        return slot == EquipmentSlot.FEET && tool.getModifierLevel(this.getId()) == 0;
    }
}