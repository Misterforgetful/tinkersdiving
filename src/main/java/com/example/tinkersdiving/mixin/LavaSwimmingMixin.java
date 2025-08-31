package com.example.tinkersdiving.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;
import com.simibubi.create.content.equipment.armor.DivingBootsItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class LavaSwimmingMixin extends Entity {
	private LavaSwimmingMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

    @Inject(
        method = "travel(Lnet/minecraft/world/phys/Vec3;)V",
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z")),
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            shift = Shift.AFTER, 
            ordinal = 0
        )
        )
    private void tinkersdiving$onLavaTravel(Vec3 travelVector, CallbackInfo ci) {
        ItemStack bootsStack = DivingBootsItem.getWornItem(this);

        boolean hasTinkersBoots = com.example.tinkersdiving.TinkersDivingUtils.hasModifier(
            bootsStack,
            TinkersDivingModifiers.DIVING_BOOTS_MODIFIER.get()
        );
        boolean isFireResistant = com.example.tinkersdiving.NetheriteDivingHandler.isTinkersFireImmune(bootsStack);

        if (hasTinkersBoots && isFireResistant) {
            setDeltaMovement(getDeltaMovement().multiply(DivingBootsItem.getMovementMultiplier((LivingEntity) (Object) this)));
        }
    }
}