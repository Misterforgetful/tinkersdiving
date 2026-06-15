package com.misterforgetful.tinkersdiving;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.particle.AirParticleData;
import net.minecraft.network.chat.Component;

import java.util.List;


import org.joml.Math;

import com.misterforgetful.tinkersdiving.config.TinkersDivingConfig;
import com.misterforgetful.tinkersdiving.modifiers.TinkersDivingModifiers;

public class TinkerBacktankBlockEntity extends BacktankBlockEntity {
    private ItemStack chestplate = ItemStack.EMPTY;

    public TinkerBacktankBlockEntity(BlockPos pos, BlockState state) {
        super(TinkersDivingMod.TINKER_BACKTANK_BLOCK_ENTITY.get(), pos, state);
    }

    public static int getStoredAir(Level level, BlockPos pos) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof TinkerBacktankBlockEntity tb) {
            return tb.airLevel;
        }
        return 0;
    }

    public int getAir() {
        return airLevel;
    }

    public void setAir(int value) {
        airLevel = value;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack stack) {
        chestplate = stack;
    }

    public void tick() {
        super.tick();
        if (this.getSpeed() == 0.0) {
            return;
        }
        
        BlockState state = getBlockState();
        if (this.airLevelTimer > 0) {
            --this.airLevelTimer;
        } else {
            int max = getMaxAir();
            if (level != null && level.isClientSide) {
                Vec3 centerOf = VecHelper.getCenterOf(worldPosition);
                Vec3 v = VecHelper.offsetRandomly(centerOf, level.random, 0.65F);
                Vec3 m = centerOf.subtract(v);
                if (this.airLevel != max) {
                    level.addParticle(new AirParticleData(1.0F, 0.05F), v.x, v.y, v.z, m.x, m.y, m.z);
                }

            } else if (this.airLevel != max) {
                int prevComparatorLevel = this.getComparatorOutput();
                float abs = Math.abs(this.getSpeed());
                int increment = Mth.clamp(((int)abs - 100) / 20, 1, 5);
                this.airLevel = Math.min(max, this.airLevel + increment);
                if (level != null && this.getComparatorOutput() != prevComparatorLevel && !level.isClientSide()) {
                    level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
                }

                if (this.airLevel == max) {
                    this.sendData();
                }

                    this.airLevelTimer = Mth.clamp((int)(128.0F - abs / 5.0F) - 108, 0, 20);
                }
            }

        }



    public static int maxAir(ToolStack stack) {
        return 1200 * Math.max(getModifierLevel(stack),1);
    }



    public int getComparatorOutput() {
        int max = BacktankUtil.maxAir(this.chestplate);
        return ComparatorUtil.fractionToRedstoneLevel((double)((float)this.airLevel / (float)max));
    }


    public void addAir(int amount) {
        airLevel += amount;
        int maxAir = getMaxAir();
        if (airLevel > maxAir) airLevel = maxAir;
        updateChestplateNBT();
        setChanged();
    }



    public static int getMaxAir(ItemStack stack) {
        return TinkersDivingConfig.COMMON.defaultModifierCapacity.get() + (TinkersDivingConfig.COMMON.airPerLevel.get() * Math.max(getModifierLevel(stack), 0));
    }




    public int getMaxAir() {
        return getMaxAir(chestplate);
    }



    public static int getModifierLevel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 1;
        ToolStack tool = ToolStack.from(stack);
        int level = tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId());
        return level;
    }



    public static int getModifierLevel(ToolStack tool) {
        int level = tool.getModifierLevel(TinkersDivingModifiers.BACKTANK_MODIFIER.get().getId());
        return level;
    }


    public void updateChestplateNBT() {
        if (!chestplate.isEmpty()) {
            CompoundTag tag = chestplate.getOrCreateTag();
            tag.putInt("Air", airLevel);
            chestplate.setTag(tag);
        }
    }

    

    public void loadFromPlacedItem(ItemStack stack) {
        chestplate = stack.copy();
        CompoundTag tag = chestplate.getOrCreateTag();
        airLevel = tag.getInt("Air");
    }



    public ItemStack getDroppedChestplate() {
        if (chestplate.isEmpty()) return ItemStack.EMPTY;
        CompoundTag tag = chestplate.getOrCreateTag();
        tag.putInt("Air", airLevel);
        chestplate.setTag(tag);
        return chestplate.copy();
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }



    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Air", this.airLevel);
        compound.putInt("Timer", this.airLevelTimer);
        compound.put("Chestplate", this.chestplate.save(new CompoundTag()));
    }



    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        int prev = this.airLevel;
        this.airLevel = compound.getInt("Air");
        this.airLevelTimer = compound.getInt("Timer");
        chestplate = ItemStack.of(compound.getCompound("Chestplate"));
        if (prev != 0 && prev != this.airLevel && this.airLevel == this.getMaxAir() && clientPacket) {
            this.playFilledEffect();
        }
    }



      protected void playFilledEffect() {
        if (level != null) {
            AllSoundEvents.CONFIRM.playAt(level, worldPosition, 0.4F, 1.0F, true);
            Vec3 baseMotion = new Vec3(0.25, 0.1, 0.0);
            Vec3 baseVec = VecHelper.getCenterOf(worldPosition);
            for(int i = 0; i < 360; i += 10) {
                Vec3 m = VecHelper.rotate(baseMotion, (double)i, Axis.Y);
                Vec3 v = baseVec.add(m.normalize().scale(.25f));
                level.addParticle(ParticleTypes.SPIT, v.x, v.y, v.z, m.x, m.y, m.z);
            }
        }
    }



    @Override
    public Component getName() {
        return super.getName();
    }



      public int getAirLevel() {
      return this.airLevel;
   }



   public void setAirLevel(int airLevel) {
      this.airLevel = airLevel;
      this.sendData();
   }
}