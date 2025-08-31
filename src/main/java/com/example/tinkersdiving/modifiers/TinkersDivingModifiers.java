package com.example.tinkersdiving.modifiers;

import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.example.tinkersdiving.modifiers.abilities.armor.BacktankModifier;
import com.example.tinkersdiving.modifiers.abilities.armor.DivingBootsModifier;
import com.example.tinkersdiving.modifiers.abilities.armor.DivingHelmetModifier;
import net.minecraftforge.eventbus.api.IEventBus;

public class TinkersDivingModifiers {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create("tinkersdiving");

    public static final StaticModifier<BacktankModifier> BACKTANK_MODIFIER = MODIFIERS.register("backtank", BacktankModifier::new);
    public static final StaticModifier<DivingBootsModifier> DIVING_BOOTS_MODIFIER = MODIFIERS.register("diving_boots", DivingBootsModifier::new);
    public static final StaticModifier<DivingHelmetModifier> DIVING_HELMET_MODIFIER = MODIFIERS.register("diving_helmet", DivingHelmetModifier::new);

    public static void register(IEventBus bus) {
        MODIFIERS.register(bus);
    }
}
