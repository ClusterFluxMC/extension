package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.NicknameCommand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayNetworkHandler.class)
public class NickMessageMixin {
    @Shadow public ServerPlayerEntity player;

    @ModifyVariable(method= "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",at=@At("STORE"), ordinal = 0)
    public Text getNickT1(Text old) {
        return getText(old,this.player);
    }

    @ModifyVariable(method= "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",at=@At("STORE"), ordinal = 1)
    public Text getNickT2(Text old) {
        return getText(old,this.player);
    }

    private static Text getText(Text old, PlayerEntity player) {
        if (NicknameCommand.nicks.getNick(player) == null || old == null)
            return old;
        if (old instanceof TranslatableText text)
            return new TranslatableText("chat.type.text", NicknameCommand.nicks.getNickAsDN(player), text.getArgs()[1]);
        return old;
    }

}
