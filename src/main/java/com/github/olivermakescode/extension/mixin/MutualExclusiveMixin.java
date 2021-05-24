package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.GameruleHelper;
import com.github.olivermakescode.extension.extension;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MutualExclusiveMixin {
    @Inject(method = "canCombine", at = @At("RETURN"), cancellable = true)
    public void mutual(CallbackInfoReturnable<Boolean> info) {
        if (GameruleHelper.server != null)
            if (extension.disableMutual.getValue())
                info.setReturnValue(true);
    }
}
