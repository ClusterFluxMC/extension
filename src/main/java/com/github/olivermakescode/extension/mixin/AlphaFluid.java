package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FluidBlock.class)
public class AlphaFluid {
    @Shadow @Final private List<FluidState> statesByLevel;

    @Inject(method="getFluidState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;",at=@At("HEAD"),cancellable=true)
    private void getState(BlockState state, CallbackInfoReturnable<FluidState> cir) {
        if (extension.alphaFluid.getValue()) cir.setReturnValue(statesByLevel.get(0));
    }
}
