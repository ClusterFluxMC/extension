package com.github.olivermakescode.extension;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.*;

import java.util.HashMap;
import java.util.Map;

public class TpaCommand {
    public static HashMap<PlayerEntity, Integer> tpaReqs = new HashMap<>();
    public static HashMap<PlayerEntity, PlayerEntity> tpaReqPlayer = new HashMap<>();
    public static HashMap<PlayerEntity, Integer> tpaHereReqs = new HashMap<>();
    public static HashMap<PlayerEntity, PlayerEntity> tpaHereReqPlayer = new HashMap<>();
    public static BoolRuleHelper enabled;

    public static void register() {

        enabled = (BoolRuleHelper) GameruleHelper.register("tpaEnabled", false);

        ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> {
            if (!enabled.getValue()) return;

            for (Map.Entry<PlayerEntity, Integer> req : tpaReqs.entrySet()) {
                req.setValue(req.getValue()+1);
                if (req.getValue() == 600) {
                    tpaReqs.remove(req.getKey());
                    tpaReqPlayer.remove(req.getKey());
                }
            }

            for (Map.Entry<PlayerEntity, Integer> req : tpaHereReqs.entrySet()) {
                req.setValue(req.getValue()+1);
                if (req.getValue() == 600) {
                    tpaHereReqs.remove(req.getKey());
                    tpaHereReqPlayer.remove(req.getKey());
                }
            }
        });

        LiteralArgumentBuilder<ServerCommandSource> to = literal("tpa")
                .requires(source -> enabled.getValue())
                .then(argument("player", EntityArgumentType.player()).executes(TpaCommand::tpa));
        LiteralArgumentBuilder<ServerCommandSource> from = literal("tpahere")
                .requires(source -> enabled.getValue())
                .then(argument("player", EntityArgumentType.player()).executes(TpaCommand::tpaHere));

        LiteralArgumentBuilder<ServerCommandSource> accept = literal("tpaccept")
                .requires(source -> enabled.getValue())
                .executes(TpaCommand::tpAccept);

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(to);
            dispatcher.register(from);
            dispatcher.register(accept);
        });
    }

    public static int tpAccept(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity sender = ctx.getSource().getPlayer();
        if (tpaReqs.containsKey(sender)) {
            PlayerEntity target = tpaReqPlayer.get(sender);

            double x = sender.getX();
            double y = sender.getY();
            double z = sender.getZ();

            target.requestTeleport(x,y,z);

            tpaReqs.remove(sender);
            tpaReqPlayer.remove(sender);

            sender.sendMessage(Text.of("Teleport successful."),false);
            target.sendMessage(Text.of("Teleport successful."),false);

            return 1;
        }
        if (tpaHereReqs.containsKey(sender)) {
            PlayerEntity target = tpaHereReqPlayer.get(sender);

            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();

            sender.requestTeleport(x,y,z);

            tpaHereReqs.remove(sender);
            tpaHereReqPlayer.remove(sender);

            sender.sendMessage(Text.of("Teleport successful."),false);
            target.sendMessage(Text.of("Teleport successful."),false);

            return 1;
        }
        return -1;
    }

    public static int tpa(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity target = EntityArgumentType.getPlayer(ctx,"player");
        PlayerEntity sender = ctx.getSource().getPlayer();
        tpaReqs.put(target,0);
        tpaReqPlayer.put(target,sender);

        String senderName = NicknameCommand.nicks.getNick(sender);
        if (senderName == null)
            senderName = sender.getName().getString();

        String targetName = NicknameCommand.nicks.getNick(target);
        if (targetName == null)
            targetName = target.getName().getString();


        sender.sendMessage(Text.of("TPA request sent to "+targetName+". Request expires in 30 seconds."),false);
        target.sendMessage(Text.of("TPA request from "+senderName+". Use /tpaccept to accept. Request expires in 30 seconds."),false);

        return 0;
    }

    public static int tpaHere(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity target = EntityArgumentType.getPlayer(ctx,"player");
        PlayerEntity sender = ctx.getSource().getPlayer();
        tpaHereReqs.put(target,0);
        tpaHereReqPlayer.put(target,sender);

        String senderName = NicknameCommand.nicks.getNick(sender);
        if (senderName == null)
            senderName = sender.getName().getString();

        String targetName = NicknameCommand.nicks.getNick(target);
        if (targetName == null)
            targetName = target.getName().getString();


        sender.sendMessage(Text.of("TPAHere request sent to "+targetName+". Request expires in 30 seconds."),false);
        target.sendMessage(Text.of("TPAHere request from "+senderName+". Use /tpaccept to accept. Request expires in 30 seconds."),false);
        return 0;
    }
}
