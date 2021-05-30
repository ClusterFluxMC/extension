package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class AttackCooldownMixin {

    @Inject(method= "Lnet/minecraft/server/network/ServerPlayerEntity;tick()V",at=@At("HEAD"),cancellable = true)
    private void attackCooldownGamerule(CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        if (!extension.attackCool.getValue())
            self.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(40);
        else self.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(4);
        self.networkHandler.sendPacket(new EntityAttributesS2CPacket(self.getId(),self.getAttributes().getAttributesToSend()));
    }
}
