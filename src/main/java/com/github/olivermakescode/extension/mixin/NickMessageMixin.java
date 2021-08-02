package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.NicknameCommand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(PlayerManager.class)
public class NickMessageMixin {

    @Redirect(
            method = "broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"
            )
    )
    public void redirect(ServerPlayerEntity serverPlayerEntity, Text message, MessageType type, UUID sender) {
        if (type == MessageType.CHAT) message = getText(message,serverPlayerEntity.server.getPlayerManager().getPlayer(sender));
        serverPlayerEntity.sendMessage(message,type,sender);
    }

    private static Text getText(Text old, PlayerEntity player) {
        if (NicknameCommand.nicks.getNick(player) == null || old == null)
            return old;
        if (old instanceof TranslatableText text)
            return new TranslatableText("chat.type.text", NicknameCommand.nicks.getNickAsDN(player), text.getArgs()[1]);
        return old;
    }

}
