package com.example.tinkersdiving.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.example.tinkersdiving.TinkersDivingUtils;
import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;
import com.example.tinkersdiving.modifiers.abilities.armor.BacktankModifier;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = BacktankUtil.class, remap = false)
public class BacktankUtilMixin {
    @Inject(method = "maxAir", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tinkersdiving$modifyMaxAir(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (TinkersDivingUtils.hasModifier(stack, TinkersDivingModifiers.BACKTANK_MODIFIER.get())) {
            ToolStack tool = ToolStack.from(stack);
            cir.setReturnValue(BacktankModifier.BASE_AIR + ((tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.getId()))-1) * BacktankModifier.AIR_PER_LEVEL);
        }
    }
}