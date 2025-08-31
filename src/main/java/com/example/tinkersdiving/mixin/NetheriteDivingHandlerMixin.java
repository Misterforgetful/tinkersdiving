package com.example.tinkersdiving.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = com.simibubi.create.content.equipment.armor.NetheriteDivingHandler.class)
class NetheriteDivingHandlerMixin {
    @Inject(method = "isNetheriteDivingHelmet", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tinkersdiving$overrideIsNetheriteDivingHelmet(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (com.example.tinkersdiving.NetheriteDivingHandler.isNetheriteDivingHelmet(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isNetheriteBacktank", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tinkersdiving$overrideIsNetheriteBacktank(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (com.example.tinkersdiving.NetheriteDivingHandler.isNetheriteBacktank(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isNetheriteArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tinkersdiving$overrideIsNetheriteArmor(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (com.example.tinkersdiving.NetheriteDivingHandler.isNetheriteArmor(stack)) {
            cir.setReturnValue(true);
        }
    }
}
