package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvokerEntity.class)
public class WololoMixin {
    @Inject(at=@At("HEAD"),cancellable=true,method="setWololoTarget(Lnet/minecraft/entity/passive/SheepEntity;)V")
    private static void cancelWololo(SheepEntity sheep, CallbackInfo ci) {
        if (!extension.wololo.getValue()) ci.cancel();
    }
}
