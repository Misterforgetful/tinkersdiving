package com.example.tinkersdiving;

import com.simibubi.create.AllTags.AllItemTags;

import com.simibubi.create.content.equipment.armor.DivingHelmetItem;

import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;


import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public final class NetheriteDivingHandler  {
	public static final String NETHERITE_DIVING_BITS_KEY = "CreateNetheriteDivingBits";
	public static final String FIRE_IMMUNE_KEY = "CreateFireImmune";

/*
  Same story as in DivingEvents, I was doing this all manually before I was advised to use mixin.
  That would have been a great suggestion if it werent for mixin's fickleness.
  Difference with this one is that there is still useable code.
*/


//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
//		EquipmentSlot slot = event.getSlot();
//		if (slot.getType() != EquipmentSlot.Type.ARMOR) {
//			return;
//		}
//
//		LivingEntity entity = event.getEntity();
//		ItemStack to = event.getTo();
//
//		if (slot == EquipmentSlot.HEAD) {
//			if (isNetheriteDivingHelmet(to)) {
//				setBit(entity, slot);
//			} else {
//				clearBit(entity, slot);
//			}
//		} else if (slot == EquipmentSlot.CHEST) {
//			if (isNetheriteBacktank(to) && BacktankModifier.hasAirRemaining(to)) {
//				setBit(entity, slot);
//			} else {
//				clearBit(entity, slot);
//			}
//		} else if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
//			if (isNetheriteArmor(to)) {
//				setBit(entity, slot);
//			} else {
//				clearBit(entity, slot);
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public static void onPlayerTick(PlayerTickEvent event) {
//		LivingEntity entity = event.player;
//		// Only run on server
//		if (entity.level().isClientSide) return;
//		// Check all armor slots
//		boolean fullSet = true;
//		for (EquipmentSlot slot : EquipmentSlot.values()) {
//			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
//				ItemStack stack = entity.getItemBySlot(slot);
//				if (!isNetheriteArmor(stack)) {
//					fullSet = false;
//					break;
//				}
//			}
//		}
//		setFireImmune(entity, fullSet);
//	}
//	
//	/**
//    * Returns true if the stack has any Tinkers' Construct fire-immune modifier (netherite, worldbound, probably if I ever publish this it'd be for use for others to mixin in their own modifiers).
//    */
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
//
    public static boolean isNetheriteBacktank(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        return stack.is(AllItemTags.PRESSURIZED_AIR_SOURCES.tag) && isTinkersFireImmune(stack) && (tool != null && tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId()) > 0);
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        return isTinkersFireImmune(stack) || (stack.getItem() instanceof ArmorItem armorItem && armorItem.isFireResistant());
    }




//	public static void setBit(LivingEntity entity, EquipmentSlot slot) {
//		CompoundTag nbt = entity.getPersistentData();
//		byte bits = nbt.getByte(NETHERITE_DIVING_BITS_KEY);
//		bits |= 1 << slot.getIndex();
//		nbt.putByte(NETHERITE_DIVING_BITS_KEY, bits);
//		// Debug: log bits and check if full set
//		boolean fullSet = (bits & 0b1111) == 0b1111;
//		nbt.putBoolean(FIRE_IMMUNE_KEY, fullSet);
//		if (fullSet) {
//			setFireImmune(entity, true);
//		}
//	}
//
//	public static void clearBit(LivingEntity entity, EquipmentSlot slot) {
//		CompoundTag nbt = entity.getPersistentData();
//		if (!nbt.contains(NETHERITE_DIVING_BITS_KEY)) {
//			return;
//		}
//		byte bits = nbt.getByte(NETHERITE_DIVING_BITS_KEY);
//		boolean prevFullSet = (bits & 0b1111) == 0b1111;
//		bits &= ~(1 << slot.getIndex());
//		nbt.putByte(NETHERITE_DIVING_BITS_KEY, bits);
//		nbt.putBoolean(FIRE_IMMUNE_KEY, false);
//		if (prevFullSet) {
//			setFireImmune(entity, false);
//		}
//	}
//
//	public static void setFireImmune(LivingEntity entity, boolean fireImmune) {
//		entity.getPersistentData().putBoolean(FIRE_IMMUNE_KEY, fireImmune);
//	}
}