package com.example.tinkersdiving.mixin;

import com.example.tinkersdiving.TinkersDivingUtils;
import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.example.tinkersdiving.NetheriteDivingHandler;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetItemMixin {
	@Inject(method = "getWornItem", at = @At("HEAD"), cancellable = true, remap = false)
	private static void tinkersdiving$recognizeTinkersHelmet(Entity entity, CallbackInfoReturnable<ItemStack> cir) {
		if (entity instanceof LivingEntity living) {
			ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
			if (!helmet.isEmpty() && TinkersDivingUtils.hasModifier(helmet, TinkersDivingModifiers.DIVING_HELMET_MODIFIER.get())) {
				cir.setReturnValue(helmet);
			}
		}
	}

	@ModifyExpressionValue(
		method = "breatheUnderwater",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/Item;isFireResistant()Z",
			remap = true
		),
		remap = false
	)
	private static boolean tinkersdiving$modifyHelmetFireResistant(boolean original, Item item, ItemStack stack) {
		if (NetheriteDivingHandler.isNetheriteArmor(stack)) {
			return true;
		}
		return original;
	}

	@ModifyArg(
		method = "breatheUnderwater",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"
		),
		index = 0
	)
	private static Predicate<ItemStack> tinkersdiving$modifyBacktankPredicate(Predicate<ItemStack> original) {
		return NetheriteDivingHandler::isNetheriteArmor;
	}
}
