package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin {
    @Shadow protected abstract void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state);

    @Shadow public abstract int getLevel(FluidState state);

    @Shadow @Final public static IntProperty LEVEL;

    @Inject(method= "flow(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/fluid/FluidState;)V",at=@At("HEAD"),cancellable=true)
    public void tryFlowAlpha(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci) {
        if (fluidState.isEmpty()) return;
        if (extension.alphaSponges.getValue()) {
            Iterable<BlockPos> iterate = BlockPos.iterate(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
            for (BlockPos newPos : iterate) {
                if (world.getBlockState(newPos).getBlock() instanceof SpongeBlock) {
                    ci.cancel();
                    return;
                }
            }
        }
        if (extension.alphaFluid.getValue()) {
            if (state.getBlock() instanceof FluidFillable) {
                if (!fluidState.isStill())
                    fluidState = fluidState.with(LEVEL,0);
                ((FluidFillable)state.getBlock()).tryFillWithFluid(world, pos, state, fluidState);
            } else {
                if (!state.isAir())
                    beforeBreakingBlock(world, pos, state);
                world.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL);
            }
            ci.cancel();
        }
    }
}
