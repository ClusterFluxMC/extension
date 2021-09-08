package com.github.olivermakescode.extension;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LoreCommand {
    public static void register() {
        LiteralArgumentBuilder<ServerCommandSource> setLore = literal("set").then(argument("Text", MessageArgumentType.message()).executes(LoreCommand::executeSet));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("lore").then(setLore));
        });
    }

    private static int executeSet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity player = ctx.getSource().getPlayer();
        ItemStack item = player.getMainHandStack();
        NbtCompound tag = item.getOrCreateTag();
        NbtCompound display = new NbtCompound();
        NbtList lore = new NbtList();
        if (tag.contains("display"))
            display = tag.getCompound("display");
        String[] loreLines = MessageArgumentType.getMessage(ctx, "Text").getString().split("\\\\n");
        for (String s : loreLines)
            lore.add(NbtString.of("[{\"text\":\""+s+"\"}]"));
        display.put("Lore",lore);
        tag.put("display",display);
        item.setTag(tag);
        return 0;
    }
}
