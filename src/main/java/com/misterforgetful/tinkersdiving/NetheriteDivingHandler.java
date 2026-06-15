package com.misterforgetful.tinkersdiving;

import com.simibubi.create.AllTags.AllItemTags;

import com.simibubi.create.content.equipment.armor.DivingHelmetItem;

import com.misterforgetful.tinkersdiving.modifiers.TinkersDivingModifiers;


import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public final class NetheriteDivingHandler  {
	public static final String NETHERITE_DIVING_BITS_KEY = "CreateNetheriteDivingBits";
	public static final String FIRE_IMMUNE_KEY = "CreateFireImmune";


	/**
    * Returns true if the stack has any Tinkers' Construct fire-immune modifier (netherite, worldbound, probably if I ever publish this (I did) it'd be for use for others to mixin or otherwise for their own modifiers).
    */
    public static boolean isTinkersFireImmune(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        if (tool == null) return false;
        return tool.getModifierLevel(new ModifierId("tconstruct:netherite")) > 0
            || tool.getModifierLevel(new ModifierId("tconstruct:worldbound")) > 0;
    }

    public static boolean isNetheriteDivingHelmet(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        if (tool != null
            && tool.getModifierLevel(TinkersDivingModifiers.DIVING_HELMET_MODIFIER.get().getId()) > 0
            && isTinkersFireImmune(stack)) {
            return true;
        }
        return stack.getItem() instanceof DivingHelmetItem && isNetheriteArmor(stack);
    }
    
    public static boolean isNetheriteBacktank(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        return stack.is(AllItemTags.PRESSURIZED_AIR_SOURCES.tag) && isTinkersFireImmune(stack) && (tool != null && tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId()) > 0);
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        return isTinkersFireImmune(stack) || (stack.getItem() instanceof ArmorItem armorItem && armorItem.isFireResistant());
    }
}