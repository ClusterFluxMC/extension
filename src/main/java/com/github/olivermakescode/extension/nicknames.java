package com.github.olivermakescode.extension;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.io.IOException;
import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class nicknames {
    public static HashMap<String,String> nicks = new HashMap<String,String>();
    public static HashMap<String,Integer> colors = new HashMap<String,Integer>();

    public static void addName(Entity user, String nick) {
        if (nick.equals("-reset") || nick.equals("-clear")) {
            nicks.remove(user.getUuidAsString());
        }
        else {
            nicks.put(user.getUuidAsString(), nick);
        }
    }

    public static void load() throws IOException {
        nicks.clear();
        String str = loadFile.load("nick.txt");
        if (str != null && !str.equals("") && !str.equals(" ") && !str.equals("\n")) {
            String[] file = str.split("\n");
            if (file.length > 0) {
                for (String s : file) {
                    String[] splitFile = s.split(":");
                    if (splitFile.length > 1) {
                        nicks.put(splitFile[0], splitFile[1]);
                        if (splitFile.length > 2)
                            colors.put(splitFile[0],Integer.parseInt(splitFile[2]));
                    }
                }
            }
        }
    }

    public static void save() throws IOException {
        String toSave = "";
        for (String i: nicks.keySet())
            toSave += i + ":" + nicks.get(i) + ":" + colors.get(i).toString() + "\n";
        loadFile.save("nick.txt",toSave);
    }

    public static String getName(Entity user) {
        String uuid = user.getUuidAsString();
        if (nicks.containsKey(uuid))
            return nicks.get(uuid);
        return user.getEntityName();
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
