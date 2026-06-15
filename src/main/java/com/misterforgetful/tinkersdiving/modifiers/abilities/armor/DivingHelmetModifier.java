package com.misterforgetful.tinkersdiving.modifiers.abilities.armor;

import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.entity.EquipmentSlot;


public class DivingHelmetModifier extends NoLevelsModifier {

    public boolean canApply(ToolStack tool, EquipmentSlot slot) {
        return (slot == EquipmentSlot.HEAD) && (tool.getModifierLevel(this.getId()) == 0);
    }
    
}
