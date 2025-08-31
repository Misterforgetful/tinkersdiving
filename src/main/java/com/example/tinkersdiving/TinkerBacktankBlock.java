package com.example.tinkersdiving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;

import com.simibubi.create.AllShapes;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;


public class TinkerBacktankBlock extends HorizontalKineticBlock implements EntityBlock {
    public static final BooleanProperty SHAFT = BooleanProperty.create("shaft");
    public static final net.minecraft.world.level.block.state.properties.DirectionProperty FACING = net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

    public TinkerBacktankBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(2.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAFT, false));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SHAFT);
    }

    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    private static final VoxelShape SHAFT_SHAPE = com.simibubi.create.AllShapes.BACKTANK;

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.or(AllShapes.BACKTANK, SHAFT_SHAPE);
    }

    @Override
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, BlockGetter world, @Nonnull BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    public float getAdditionalAirCapacity(Level world, BlockPos pos) {
        return TinkerBacktankBlockEntity.getStoredAir(world, pos);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TinkerBacktankBlockEntity tank) {
                ItemStack stack = tank.getDroppedChestplate();
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
                }
                level.removeBlockEntity(pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public ItemStack getCloneItemStack(@Nonnull net.minecraft.world.level.BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof TinkerBacktankBlockEntity tank) {
            ItemStack chest = tank.getDroppedChestplate();
            if (!chest.isEmpty()) return chest.copy();
        }
        return super.getCloneItemStack(world, pos, state);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TinkerBacktankBlockEntity tank) {
            ItemStack chest = tank.getDroppedChestplate();
            if (!chest.isEmpty()) {
                if (player != null && !player.isCreative()) {
                    player.getInventory().placeItemBackInInventory(chest.copy());
                } else {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), chest.copy());
                }
            }
            level.removeBlockEntity(pos);
            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand,
        @Nonnull BlockHitResult hit) {
        if (player instanceof FakePlayer)
            return InteractionResult.PASS;
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;
        if (player.getMainHandItem()
            .getItem() instanceof BlockItem)
            return InteractionResult.PASS;
        if (!player.getItemBySlot(EquipmentSlot.CHEST)
            .isEmpty())
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .75f, 1);
            player.setItemSlot(EquipmentSlot.CHEST, getCloneItemStack(world, pos, state));
            world.removeBlockEntity(pos);
            world.destroyBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    public BlockState getStateForPlacement(@Nonnull UseOnContext context) {
        BlockState state = this.defaultBlockState()
            .setValue(SHAFT, true)
            .setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TinkerBacktankBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        if (type == TinkersDivingMod.TINKER_BACKTANK_BLOCK_ENTITY.get()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof TinkerBacktankBlockEntity tank) {
                    try {
                        float speed = 0f;
                        BlockEntity kinetic = lvl.getBlockEntity(pos);
                        if (kinetic != null && kinetic instanceof com.simibubi.create.content.kinetics.base.KineticBlockEntity kb) {
                            speed = kb.getSpeed();
                        }
                    } catch (Throwable ignored) {}
                    tank.tick();
                }
            };
        }
        return null;
    }
}