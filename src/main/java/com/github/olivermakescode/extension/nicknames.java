package com.github.olivermakescode.extension;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.util.Arrays;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class nicknames {
    public static String[] username = {};
    public static String[] nickname = {};

    public static void addName(PlayerEntity user, String nick) {
        if (nick.equals("-reset")) nick = user.getEntityName();

        int exists = nickExists(user);
        if (exists >= 0) {
            nickname[exists] = nick;
            return;
        }
        username = Arrays.copyOf(username,username.length+1);
        username[username.length-1] = user.getUuidAsString();

        nickname = Arrays.copyOf(nickname,nickname.length+1);
        nickname[nickname.length-1] = nick;
    }

    private static int nickExists(PlayerEntity user) {
        for (int i = 0; i < nickname.length; i++) {
            if (username[i].equals(user.getUuidAsString()))
                return i;
        }
        return -1;
    }

    public static void load() throws IOException {
        String str = loadFile.load("nick.txt");
        if (str != null && !str.equals("") && !str.equals(" ") && !str.equals("\n")) {
            String[] file = str.split("\n");
            System.out.println(Arrays.toString(file));
            username = new String[file.length];
            nickname = new String[file.length];
            if (file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    String[] splitFile = file[i].split(":");
                    if (splitFile.length > 1) {
                        username[i] = splitFile[0];
                        nickname[i] = splitFile[1];
                    }
                    System.out.println(Arrays.toString(splitFile));
                }
            }
        }
    }

    public static void save() throws IOException {
        String[] intermediary = new String[username.length];
        String toSave = "";
        for (int i = 0; i < username.length; i++) {
            intermediary[i] = username[i] + ":" + nickname[i];
            toSave += intermediary[i] + "\n";
        }
        loadFile.save("nick.txt",toSave);
    }

    public static String getName(PlayerEntity user) {
        for (int i = 0; i < nickname.length; i++) {
            if (username[i].equals(user.getUuidAsString())) {
                if (nickname[i] != null)
                    return nickname[i];
                else return user.getGameProfile().getName();
            }
        }
        return user.getGameProfile().getName();
    }

    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            var ctx = argument("nickname", StringArgumentType.string()).executes(context -> {
                addName(context.getSource().getPlayer(),StringArgumentType.getString(context,"nickname"));
                return 1;
            });
            dispatcher.register(literal("nick").then(ctx));
            dispatcher.register(literal("nickname").then(ctx));
        });
    }
}
