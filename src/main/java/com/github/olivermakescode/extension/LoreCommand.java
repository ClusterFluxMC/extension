package com.github.olivermakescode.extension;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

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
        if (tag.contains("display"))
            display = tag.getCompound("display");
        String[] loreLines = MessageArgumentType.getMessage(ctx, "Text").getString().split("\\\\n");
        NbtList lore = FormattedStringParser.parseNbt(loreLines);
        display.put("Lore",lore);
        tag.put("display",display);
        item.setTag(tag);
        Text[] texts = FormattedStringParser.parse(loreLines);
        for (int i = 0; i < texts.length; i++) {
            if (i == 0)
                ctx.getSource().getPlayer().sendMessage(Text.of("Item's lore changed to: ").copy().append(texts[0]), false);
            else ctx.getSource().getPlayer().sendMessage(texts[i], false);
        }
        return 0;
    }
}
