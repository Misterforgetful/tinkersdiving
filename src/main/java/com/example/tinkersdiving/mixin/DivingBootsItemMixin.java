package com.example.tinkersdiving.mixin;

import com.example.tinkersdiving.TinkersDivingUtils;
import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;
import com.simibubi.create.content.equipment.armor.DivingBootsItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Diving Boots
@Mixin(value = DivingBootsItem.class)
public class DivingBootsItemMixin {
    @Inject(method = "getWornItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tinkersdiving$recognizeTinkersBoots(Entity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (entity instanceof LivingEntity living) {
            ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
            if (!boots.isEmpty() && TinkersDivingUtils.hasModifier(boots, TinkersDivingModifiers.DIVING_BOOTS_MODIFIER.get())) {
                cir.setReturnValue(boots);
            }
        }
    }
}
