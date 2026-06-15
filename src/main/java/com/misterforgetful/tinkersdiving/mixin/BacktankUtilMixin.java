package com.misterforgetful.tinkersdiving.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.misterforgetful.tinkersdiving.TinkersDivingUtils;
import com.misterforgetful.tinkersdiving.modifiers.TinkersDivingModifiers;
import com.misterforgetful.tinkersdiving.modifiers.abilities.armor.BacktankModifier;
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