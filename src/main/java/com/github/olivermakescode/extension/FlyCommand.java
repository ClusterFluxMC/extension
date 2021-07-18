package com.github.olivermakescode.extension;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlyCommand{
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("fly").requires(source -> source.hasPermissionLevel(4)).executes(FlyCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        Entity entity = context.getSource().getEntity();
        if (entity instanceof PlayerEntity playerEntity && !playerEntity.isCreative()) {
            playerEntity.getAbilities().allowFlying = !playerEntity.getAbilities().allowFlying;
            if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity)
                serverPlayerEntity.sendAbilitiesUpdate();
        }
        return 0;
    }
}