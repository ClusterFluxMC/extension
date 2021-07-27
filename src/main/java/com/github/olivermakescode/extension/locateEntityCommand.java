package com.github.olivermakescode.extension;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class locateEntityCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("locateentity").requires(source -> source.hasPermissionLevel(4)).then(argument("selector", EntityArgumentType.entities()).executes(locateEntityCommand::run)));
        });
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "selector");

        Entity closestEntity = null;
        float prvDist = Float.MAX_VALUE;

        for (Entity entity : entities) {
            if (closestEntity != null) {
                float newDist = entity.distanceTo(context.getSource().getEntity());
                if (prvDist < newDist) {
                    prvDist = newDist;
                    closestEntity = entity;
                }
            } else {
                prvDist = entity.distanceTo(context.getSource().getEntity());
                closestEntity = entity;
            }
            System.out.println(prvDist);
        }

        System.out.println("Final: " + prvDist);

        if (closestEntity != null) {
            int[] pos = {
                    closestEntity.getBlockX(),
                    closestEntity.getBlockY(),
                    closestEntity.getBlockZ()
            };

            String feedback = "Closest entity with selector: "
                    + context.getInput().split(" ",2)[1]
                    + " found at pos: "
                    + pos[0] + ", "
                    + pos[1] + ", "
                    + pos[2];


            context.getSource().sendFeedback(Text.of(feedback), false);
            return 1;
        }
        context.getSource().sendError(Text.of("Unable to locate entity of selector: " + context.getInput().split(" ", 2)[1]));
        return 0;
    }
}
