package com.example.tinkersdiving;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class TinkersDivingUtils {
    public static boolean hasModifier(ToolStack tool, Modifier modifier) {
        return tool.getModifierLevel(modifier.getId()) > 0;
    }

    public static boolean hasModifier(ItemStack stack, Modifier modifier) {
        ToolStack tool = ToolStack.from(stack);
        return tool.getModifierLevel(modifier.getId()) > 0;
    }
}