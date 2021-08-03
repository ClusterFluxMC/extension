package com.github.olivermakescode.extension;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GameruleHelper {
    @Nullable
    public static MinecraftServer server;

    public static void start() {
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            server = minecraftServer;
            try {
                NicknameCommand.nicks.readFromNBT();
            } catch (IOException ignored) {}
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(minecraftServer -> {
            try {
                NicknameCommand.nicks.saveToNBT();
            } catch (IOException ignored) {}
            server = null;
        });
    }

    public static GameRuleInterface register(String name, boolean defaultValue) {
        return new BoolRuleHelper(name, defaultValue);
    }

    public static GameRuleInterface register(String name, int defaultValue) {
        return new IntRuleHelper(name, defaultValue);
    }

    public static GameRuleInterface register(String name, int defaultValue, int min) {
        return new IntRuleHelper(name, defaultValue, min);
    }

    public static GameRuleInterface register(String name, int defaultValue, int min, int max) {
        return new IntRuleHelper(name, defaultValue, min, max);
    }
}
