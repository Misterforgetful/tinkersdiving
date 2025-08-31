package com.example.tinkersdiving;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.EquipmentSlot;
import com.example.tinkersdiving.modifiers.TinkersDivingModifiers;


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

//At one point I was hardcoding the entire interaction system, until I was counseled to instead use mixin. 
//This remains for nostalgia purposes or something, I don't actually know. 



// Formerly had the same name as Create's event, changed to avoid conflicts 

//	@SubscribeEvent
//	public static void divingBootsMovementSystem(LivingTickEvent event) {
//		LivingEntity entity = event.getEntity();
//		if (!affects(entity))
//			return;
//
//		Vec3 motion = entity.getDeltaMovement();
//		boolean isJumping = entity.jumping;
//		entity.setOnGround(entity.onGround() || entity.verticalCollision);
//
//		if (isJumping && entity.onGround()) {
//			motion = motion.add(0, .5f, 0);
//			entity.setOnGround(false);
//		} else {
//			motion = motion.add(0, -0.05f, 0);
//		}
//
//		float multiplier = 1.3f;
//		if (motion.multiply(1, 0, 1)
//			.length() < 0.145f && (entity.zza > 0 || entity.xxa != 0) && !entity.isShiftKeyDown())
//			motion = motion.multiply(multiplier, 1, multiplier);
//		entity.setDeltaMovement(motion);
//	}

//Comment in place to aid in distinguishing Code blocks

//  protected static boolean affects(LivingEntity entity) {
//    if (!(!getWornItem(entity).isEmpty())) {
//      entity.getPersistentData()
//        .remove("HeavyBoots");
//      return false;
//    }
//    NBTHelper.putMarker(entity.getPersistentData(), "HeavyBoots");
//    if (!entity.isInWater() && !entity.isInLava())
//      return false;
//    if (entity.getPose() == net.minecraft.world.entity.Pose.SWIMMING)
//      return false;
//    if (entity instanceof Player playerEntity) {
//      if (playerEntity.getAbilities().flying)
//        return false;
//    }
//    return true;
//  }
  
  //Whadda ya think this is here for this time?

//  	public static Vec3 getMovementMultiplier(LivingEntity entity) {
//		double yMotion = entity.getDeltaMovement().y;
//		double vMultiplier = yMotion < 0 ? Math.max(0, 2.5 - Math.abs(yMotion) * 2) : 1;
//
//		if (!entity.onGround()) {
//			if (entity.jumping && entity.getPersistentData()
//				.contains("LavaGrounded")) {
//				boolean eyeInFluid = entity.isEyeInFluid(FluidTags.LAVA);
//				vMultiplier = yMotion == 0 ? 0 : (eyeInFluid ? 1 : 0.5) / yMotion;
//			} else if (yMotion > 0)
//				vMultiplier = 1.3;
//
//			entity.getPersistentData()
//				.remove("LavaGrounded");
//			return new Vec3(1.75, vMultiplier, 1.75);
//		}
//
//		entity.getPersistentData()
//			.putBoolean("LavaGrounded", true);
//		double hMultiplier = entity.isSprinting() ? 1.85 : 1.75;
//		return new Vec3(hMultiplier, vMultiplier, hMultiplier);
//	}

//  @SubscribeEvent
//  public static void breatheWhenInhospitable(LivingBreatheEvent event) {
//    LivingEntity entity = event.getEntity();
//    if (!(entity instanceof Player player)) return;
//    if (!TinkersDivingUtils.hasModifier(player, TinkersDivingModifiers.DIVING_HELMET_MODIFIER.get())) return;
//    var level = entity.level();
//
//
//		if (level.isClientSide){
//			entity.getPersistentData().remove("VisualBacktankAir");
//    }
//
//
//    java.util.List<ItemStack> backtanks = TinkersDivingUtils.getAllWithAir(entity);
//    if (backtanks.isEmpty()) return;
//
//    boolean lavaDiving = entity.isInLava();
//    boolean fireImmune = entity.getPersistentData().getBoolean(com.example.tinkersdiving.NetheriteDivingHandler.FIRE_IMMUNE_KEY);
//
//    // Always set VisualBacktankAir on client if helmet is supplying air
//    float visualBacktankAir = 0f;
//    for (ItemStack stack : backtanks){
//      visualBacktankAir += BacktankModifier.getAir(stack);
//    }
//    if (level.isClientSide) {
//      entity.getPersistentData().putInt("VisualBacktankAir", Math.round(visualBacktankAir));
//    }
//
//    // Only consume air if the player cannot breathe (drowning or burning)
//    if ((lavaDiving && !fireImmune) || (event.canBreathe() && !lavaDiving)) {
//      // Not using air, just return
//      return;
//    }
//
//    // Consume air every second if needed
//    if (level.getGameTime() % 20 == 0) {
//      BacktankModifier.consumeAir(entity, backtanks.get(0), 1);
//    }
//
//    // Allow breathing and refilling air
//    event.setCanBreathe(true);
//    event.setCanRefillAir(true);
//  }
//  
//
//  public static ItemStack getWornItem(Entity entity) {
//    if (!(entity instanceof LivingEntity livingEntity)) {
//      return ItemStack.EMPTY;
//    }
//    ItemStack stack = livingEntity.getItemBySlot(SLOT);
//    if (!(TinkersDivingUtils.hasModifier(stack, TinkersDivingModifiers.DIVING_BOOTS_MODIFIER.get()))) {
//      return ItemStack.EMPTY;
//    }
//    return stack;
//  }

//  @SubscribeEvent
//  public static void onFireDamage(net.minecraftforge.event.entity.living.LivingHurtEvent event) {
//    LivingEntity entity = event.getEntity();
//    if (!(entity instanceof Player player)) return;
//    // Use Forge's fire damage tag check for 1.20.1+
//    if (!event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) return;
//    if (player.getPersistentData().getBoolean(com.example.tinkersdiving.NetheriteDivingHandler.FIRE_IMMUNE_KEY)) {
//      event.setCanceled(true);
//    }
//  }

  /**
   * Returns the worn diving helmet, prioritizing Tinkers' Construct helmets with the modifier,
   * then falling back to Create's helmet.
   */
//  public static ItemStack getAnyDivingHelmet(Entity entity) {
//      if (entity instanceof LivingEntity living) {
//          ItemStack head = living.getItemBySlot(EquipmentSlot.HEAD);
//          // Check for Tinkers' Construct diving helmet modifier
//          if (!head.isEmpty() && TinkersDivingUtils.hasModifier(head, TinkersDivingModifiers.DIVING_HELMET_MODIFIER.get())) {
//              return head;
//          }
//      }
//      // Fallback to Create's helmet
//      return com.simibubi.create.content.equipment.armor.DivingHelmetItem.getWornItem(entity);
//  }

//  @SubscribeEvent
//  public static void getFogDensity(ViewportEvent.RenderFog event) {
//        Camera camera = event.getCamera();
//        Level level = Minecraft.getInstance().level;
//        if (level == null) return;
//        BlockPos blockPos = camera.getBlockPosition();
//        FluidState fluidState = level.getFluidState(blockPos);
//        if (camera.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos))
//            return;
//
//        Fluid fluid = fluidState.getType();
//        Entity entity = camera.getEntity();
//
//        if (entity.isSpectator())
//            return;
//
//        ItemStack divingHelmet = getAnyDivingHelmet(entity);
//        if (!divingHelmet.isEmpty()) {
//            if (FluidHelper.isWater(fluid)) {
//                event.scaleFarPlaneDistance(6.25f);
//                event.setCanceled(true);
//                return;
//            } else if (FluidHelper.isLava(fluid) && NetheriteDivingHandler.isNetheriteDivingHelmet(divingHelmet)) {
//                event.setNearPlaneDistance(-4.0f);
//                event.setFarPlaneDistance(20.0f);
//                event.setCanceled(true);
//                return;
//            }
//        }
//    }
}