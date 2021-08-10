package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public abstract class AlphaTnt {
    @Shadow @Final public static BooleanProperty UNSTABLE;

    @Shadow
    public static void primeTnt(World world, BlockPos pos) {

    }

    @Inject(method="onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",at=@At("HEAD"),cancellable=true)
    private void setUnstable(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (!world.isClient() && !player.isCreative() && extension.alphaTnt.getValue()) {
            primeTnt(world, pos);
            ci.cancel();
        }
    }
}
