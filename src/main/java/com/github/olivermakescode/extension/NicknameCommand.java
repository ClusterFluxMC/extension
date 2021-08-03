package com.github.olivermakescode.extension;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.*;

public class NicknameCommand {
    public static NickDataStorage nicks;

    public static void register() {
        nicks = new NickDataStorage();

        LiteralArgumentBuilder<ServerCommandSource> get = literal("get").executes(NicknameCommand::getName);
        LiteralArgumentBuilder<ServerCommandSource> set = literal("set").then(argument("nickname",StringArgumentType.string()).executes(NicknameCommand::setName));
        LiteralArgumentBuilder<ServerCommandSource> reset = literal("reset").executes(NicknameCommand::resetName);

        LiteralArgumentBuilder<ServerCommandSource> nick = literal("nick").then(get).then(set).then(reset).then(registerAdmin());
        LiteralArgumentBuilder<ServerCommandSource> nickname = literal("nickname").then(get).then(set).then(reset).then(registerAdmin());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(nick);
            dispatcher.register(nickname);
        });
    }

    public static LiteralArgumentBuilder<ServerCommandSource> registerAdmin() {
        LiteralArgumentBuilder<ServerCommandSource> get = literal("get").then(argument("player", EntityArgumentType.player()).executes(NicknameCommand::getNameAdmin));
        LiteralArgumentBuilder<ServerCommandSource> set = literal("set").then(argument("player", EntityArgumentType.player()).then(argument("nickname",StringArgumentType.string()).executes(NicknameCommand::setNameAdmin)));
        LiteralArgumentBuilder<ServerCommandSource> reset = literal("reset").then(argument("player", EntityArgumentType.player()).executes(NicknameCommand::resetNameAdmin));

        return literal("admin").requires(source -> source.hasPermissionLevel(4)).then(get).then(set).then(reset);
    }

    private static int getName(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().getEntity() instanceof PlayerEntity player) {
            ctx.getSource().sendFeedback(Text.of("Player has nickname "+nicks.getNick(player)), false);
            return 1;
        }
        ctx.getSource().sendError(Text.of("Unknown error occurred. Did you execute as an entity?"));
        return 1;
    }
    private static int setName(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().getEntity() instanceof PlayerEntity player) {
            String name = StringArgumentType.getString(ctx,"nickname");
            nicks.addNick(player,name);
            ctx.getSource().sendFeedback(Text.of("Changed nickname to "+name), false);
            return 1;
        }
        ctx.getSource().sendError(Text.of("Unknown error occurred. Did you execute as an entity?"));
        return 1;
    }
    private static int resetName(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().getEntity() instanceof PlayerEntity player) {
            nicks.removeNick(player);
            ctx.getSource().sendFeedback(Text.of("Reset nickname"), false);
            return 1;
        }
        ctx.getSource().sendError(Text.of("Unknown error occurred. Did you execute as an entity?"));
        return -1;
    }

    private static int getNameAdmin(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
       PlayerEntity target =  EntityArgumentType.getPlayer(ctx,"player");
       ctx.getSource().sendFeedback(Text.of("Player has nickname "+nicks.getNick(target)), false);
       return 1;
    }
    private static int setNameAdmin(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity target =  EntityArgumentType.getPlayer(ctx,"player");
        String name = StringArgumentType.getString(ctx,"nickname");
        nicks.addNick(target,name);
        ctx.getSource().sendFeedback(Text.of("Changed nickname to "+name), false);
        return 1;
    }
    private static int resetNameAdmin(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity target =  EntityArgumentType.getPlayer(ctx,"player");
        nicks.removeNick(target);
        ctx.getSource().sendFeedback(Text.of("Reset nickname"), false);
        return 1;
    }

}
