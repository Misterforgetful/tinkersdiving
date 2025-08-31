package com.example.tinkersdiving.client;
import net.minecraft.core.Direction;

import net.minecraft.world.level.block.state.BlockState;

import com.example.tinkersdiving.TinkerBacktankBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.equipment.armor.BacktankBlock;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.minecraft.client.renderer.RenderType;


public class TinkerBacktankRenderer extends KineticBlockEntityRenderer<TinkerBacktankBlockEntity> {
	public static final PartialModel TINKER_BACKTANK_SHAFT = AllPartialModels.COPPER_BACKTANK_SHAFT;
	public static final PartialModel TINKER_BACKTANK_COGS = AllPartialModels.COPPER_BACKTANK_COGS;
	public TinkerBacktankRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(TinkerBacktankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

		BlockState blockState = be.getBlockState();
		SuperByteBuffer cogs = CachedBuffers.partial(getCogsModel(blockState), blockState);
		cogs.center()
			.rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
			.uncenter()
			.translate(0, 6.5f / 16, 11f / 16)
			.rotate(AngleHelper.rad(be.getSpeed() / 4f * AnimationTickHolder.getRenderTime(be.getLevel()) % 360),
				Direction.EAST)
			.translate(0, -6.5f / 16, -11f / 16);
		cogs.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.cutout()));


		SuperByteBuffer shaft = CachedBuffers.partial(getShaftModel(blockState), blockState);
		shaft.center()
			.rotateYDegrees(180 + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING)))
			.rotate(AngleHelper.rad(be.getSpeed() / 4f * AnimationTickHolder.getRenderTime(be.getLevel()) % 360), Direction.UP)
			.uncenter();
		shaft.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.cutout()));
	}

	@Override
	protected SuperByteBuffer getRotatedModel(TinkerBacktankBlockEntity be, BlockState state) {
		return CachedBuffers.partial(getShaftModel(state), state);
	}

	public static PartialModel getCogsModel(BlockState state) {
		return TINKER_BACKTANK_COGS;
	}

	public static PartialModel getShaftModel(BlockState state) {
		return TINKER_BACKTANK_SHAFT;
	}
}
