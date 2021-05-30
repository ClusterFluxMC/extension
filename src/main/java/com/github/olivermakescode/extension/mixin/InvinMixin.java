package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class InvinMixin {
    public int lastRegen;

    @Inject(method="tick()V", at=@At("RETURN"))
    public void invGamerule(CallbackInfo ci) {
        if (extension.invinFrames.getValue()) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.timeUntilRegen == 20) {
            this.lastRegen = 20;
            self.timeUntilRegen = 10;
        } else if (this.lastRegen > 10) {
            self.timeUntilRegen = 10;
            this.lastRegen--;
        } else if (this.lastRegen > 0)
            self.timeUntilRegen = this.lastRegen--;
        else self.timeUntilRegen = 0;
    }
}
