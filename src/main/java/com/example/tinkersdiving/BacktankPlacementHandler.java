package com.example.tinkersdiving;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;


public class BacktankPlacementHandler{

    public static InteractionResult tryPlaceBacktank(Player player, ItemStack hand) {
        if (player.getMainHandItem() != hand) {
            return InteractionResult.FAIL;
        }
        BlockHitResult hitResult = (BlockHitResult) player.pick(5.0D, 0.0F, false);
        BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());

        if (!player.level().getBlockState(pos).canBeReplaced()) {
            return InteractionResult.FAIL;
        }

        if (player.getBoundingBox().intersects(new AABB(pos))) {
            return InteractionResult.FAIL;
        }

        player.level().setBlockAndUpdate(pos, TinkersDivingMod.TINKER_BACKTANK_BLOCK.get().defaultBlockState().setValue(TinkerBacktankBlock.FACING, player.getDirection().getOpposite()));

        BlockEntity be = player.level().getBlockEntity(pos);
        if (be instanceof TinkerBacktankBlockEntity) {
            ((TinkerBacktankBlockEntity) be).loadFromPlacedItem(hand);
        }
        if (!player.isCreative()) {
            hand.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }   
}
