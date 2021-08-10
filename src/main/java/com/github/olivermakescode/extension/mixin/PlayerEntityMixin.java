package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at=@At("HEAD"),method="attack(Lnet/minecraft/entity/Entity;)V")
    public void removeTnt(Entity target, CallbackInfo ci) {
        if (target instanceof TntEntity tnt && extension.alphaTnt.getValue()) {
            tnt.dropStack(Items.TNT.getDefaultStack());
            tnt.kill();
        }
    }
}
