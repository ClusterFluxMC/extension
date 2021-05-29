package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class InvinMixin {

    @Shadow public int timeUntilRegen;
    public int lastRegen;

    @Inject(method="tick()V", at=@At("RETURN"))
    public void invGamerule(CallbackInfo ci) {
        if (extension.invinFrames.getValue()) return;

        if (this.timeUntilRegen == 20) {
            this.lastRegen = 20;
            this.timeUntilRegen = 10;
        } else if (this.lastRegen > 10) {
            this.timeUntilRegen = 10;
            this.lastRegen--;
        } else if (this.lastRegen > 0)
            this.timeUntilRegen = --this.lastRegen;
    }
}
