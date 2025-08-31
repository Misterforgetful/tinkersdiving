package com.example.tinkersdiving;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;


import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;
// Modifier classes (stubs)
import com.example.tinkersdiving.modifiers.abilities.armor.BacktankModifier;
import com.example.tinkersdiving.modifiers.abilities.armor.DivingHelmetModifier;
import com.example.tinkersdiving.modifiers.abilities.armor.DivingBootsModifier;

@Mod(TinkersDivingMod.MODID)
public class TinkersDivingMod {
    public static final String MODID = "tinkersdiving";
    public static final String VERSION = "0.2.1.1";

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);


    public static final StaticModifier<BacktankModifier> BACKTANK_MODIFIER =
        MODIFIERS.register("backtank", BacktankModifier::new);
    public static final StaticModifier<DivingHelmetModifier> DIVING_HELMET_MODIFIER =
        MODIFIERS.register("diving_helmet", DivingHelmetModifier::new);
    public static final StaticModifier<DivingBootsModifier> DIVING_BOOTS_MODIFIER =
        MODIFIERS.register("diving_boots", DivingBootsModifier::new);


    public static final RegistryObject<Block> TINKER_BACKTANK_BLOCK =
        BLOCKS.register("tinker_backtank", TinkerBacktankBlock::new);

    public static final RegistryObject<BlockEntityType<TinkerBacktankBlockEntity>> TINKER_BACKTANK_BLOCK_ENTITY =
        BLOCK_ENTITY.register("tinker_backtank", 
        () -> BlockEntityType.Builder
            .of(TinkerBacktankBlockEntity::new, TINKER_BACKTANK_BLOCK.get())
            .build(null));


    
    public static final String BACKTANK_MODIFIER_NAME = "Backtank";
    public static final String BACKTANK_MODIFIER_ID = MODID + ":" + BACKTANK_MODIFIER_NAME;
    public static final String BACKTANK_MODIFIER_DESCRIPTION = "Allows Tinker's Chestplates to store and manage air.";

    public static final String DIVING_BOOTS_MODIFIER_NAME = "Diving Boots";
    public static final String DIVING_BOOTS_MODIFIER_ID = MODID + ":" + DIVING_BOOTS_MODIFIER_NAME;
    public static final String DIVING_BOOTS_MODIFIER_DESCRIPTION = "Allows Tinker's Boot equivalent gear to function as Create Diving Boots.";

    public static final String DIVING_HELMET_MODIFIER_NAME = "Diving Helmet";
    public static final String DIVING_HELMET_MODIFIER_ID = MODID + ":" + DIVING_HELMET_MODIFIER_NAME;
    public static final String DIVING_HELMET_MODIFIER_DESCRIPTION = "Allows Tinker's Helmets to function as a Create Diving Helmet.";

    public TinkersDivingMod() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITY.register(FMLJavaModLoadingContext.get().getModEventBus());
        TinkersDivingModifiers.register(FMLJavaModLoadingContext.get().getModEventBus()); // Use static registration
        DivingEvents.init();
    }

    public static ResourceLocation asResource(String string) {
        return new ResourceLocation(MODID, string);
    }
}