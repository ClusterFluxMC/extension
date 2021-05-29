package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.nicknames;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@Mixin(ServerPlayerEntity.class)
public class NickMessageMixin {
    @ModifyVariable(method = "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", at = @At("HEAD"), ordinal=0)
    public Text modifyMessageNick(Text old, Text old2, MessageType type, UUID sender) {
        if (type != MessageType.CHAT) return old;
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        String nickname = nicknames.getName(self);
        if (nickname.equals(self.getName().getString())) return old;
        return new TranslatableText("chat.type.announcement", new LiteralText(nickname).setStyle(Style.EMPTY.withColor(0xffffff)), old);
    }
}