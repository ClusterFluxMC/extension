package com.github.olivermakescode.extension;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.*;
import static net.minecraft.command.CommandSource.suggestMatching;

public class HomeCommand {
    public static HomeStorage homes = new HomeStorage();
    public static BoolRuleHelper enabled;

    public static void register() {
        enabled = (BoolRuleHelper) GameruleHelper.register("homesEnabled", false);

        LiteralArgumentBuilder<ServerCommandSource> set = literal("sethome")
                .requires(source -> enabled.getValue())
                .then(
                        argument("home", StringArgumentType.string())
                        .executes(HomeCommand::setHome)
                );

        LiteralArgumentBuilder<ServerCommandSource> get = literal("home")
                .requires(source -> enabled.getValue())
                .then(
                        argument("home", StringArgumentType.string())
                        .suggests((c, b) -> suggestMatching(homes.getNames(c.getSource().getPlayer()),b))
                        .executes(HomeCommand::tpHome)
                );

        LiteralArgumentBuilder<ServerCommandSource> del = literal("delhome")
                .requires(source -> enabled.getValue())
                .then(
                        argument("home", StringArgumentType.string())
                                .suggests((c, b) -> suggestMatching(homes.getNames(c.getSource().getPlayer()),b))
                                .executes(HomeCommand::delHome)
                );

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(set);
            dispatcher.register(get);
            dispatcher.register(del);
        });
    }

    public static int tpHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String home = StringArgumentType.getString(ctx,"home");
        PlayerEntity player = ctx.getSource().getPlayer();
        float[] pos = homes.getHome(player,home);

        if (pos != null) {
            float x = pos[0];
            float y = pos[1];
            float z = pos[2];
            player.requestTeleport(x,y,z);
            ctx.getSource().sendFeedback(Text.of("Teleported to home \""+home+"\""), false);

            return 1;
        }

        ctx.getSource().sendError(Text.of("Home doesn't exist."));
        return -1;
    }

    public static int setHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String home = StringArgumentType.getString(ctx,"home");
        PlayerEntity player = ctx.getSource().getPlayer();
        float x = (float) player.getX();
        float y = (float) player.getY();
        float z = (float) player.getZ();

        homes.addHome(player,home,x,y,z);
        ctx.getSource().sendFeedback(Text.of("Home \""+home+"\" created at position "+(int)x+", "+(int)y+", "+(int)z), false);
        return 1;
    }

    public static int delHome(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        String home = StringArgumentType.getString(ctx,"home");
        PlayerEntity player = ctx.getSource().getPlayer();

        if (homes.exists(player, home)) {
            homes.removeHome(player,home);
            ctx.getSource().sendFeedback(Text.of("Home \""+home+"\" deleted."), false);
            return 1;
        }

        ctx.getSource().sendError(Text.of("Home doesn't exist."));
        return -1;
    }
}
