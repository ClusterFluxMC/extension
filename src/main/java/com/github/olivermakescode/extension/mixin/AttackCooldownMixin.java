package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class AttackCooldownMixin {
    @Inject(method= "resetLastAttackedTicks()V",at=@At("HEAD"),cancellable = true)
    private void attackCooldownGamerule(CallbackInfo ci) {
        if (!extension.attackCool.getValue() || !extension.itemCooldown.getValue())
            ci.cancel();

    }

    @Inject(method= "getAttackCooldownProgressPerTick()F",at=@At("HEAD"), cancellable = true)
    public void acg(CallbackInfoReturnable<Float> cir) {
        if (!extension.attackCool.getValue() || !extension.itemCooldown.getValue())
            cir.setReturnValue(20f);
    }
}
