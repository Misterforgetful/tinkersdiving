package com.misterforgetful.tinkersdiving;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.EquipmentSlot;

import com.misterforgetful.tinkersdiving.modifiers.TinkersDivingModifiers;


@Mod.EventBusSubscriber(modid = TinkersDivingMod.MODID)
public class DivingEvents {
  public static void init() {}
  public static final EquipmentSlot SLOT = EquipmentSlot.FEET;
  
  @SubscribeEvent
  public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock evt) {
    Player p = evt.getEntity();
    ItemStack hand = p.getMainHandItem();
    if (hand.isEmpty() || !TinkersDivingUtils.hasModifier(hand, TinkersDivingModifiers.BACKTANK_MODIFIER.get())) return;

    BacktankPlacementHandler.tryPlaceBacktank(p, hand);

    evt.setCanceled(true);
    evt.setCancellationResult(InteractionResult.SUCCESS);
  } 
}