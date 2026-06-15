package com.misterforgetful.tinkersdiving.client;

import com.misterforgetful.tinkersdiving.TinkersDivingMod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;


@Mod.EventBusSubscriber(modid = TinkersDivingMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
            TinkersDivingMod.TINKER_BACKTANK_BLOCK_ENTITY.get(),
            TinkerBacktankRenderer::new
        );
    }
}
