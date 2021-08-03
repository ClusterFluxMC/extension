package com.github.olivermakescode.extension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NickDataStorage {
    public HashMap<String,String> nicks;

    public NickDataStorage() {
        nicks = new HashMap<String,String>();
    }

    public void addNick(PlayerEntity player, String name) {
        if (nicks.containsKey(player.getUuidAsString())) {
            nicks.replace(player.getUuidAsString(),name);
            return;
        }
        nicks.put(player.getUuidAsString(),name);
    }

    public void removeNick(PlayerEntity player) {
        nicks.remove(player.getUuidAsString());
    }

    public String getNick(PlayerEntity player) {
        return nicks.get(player.getUuidAsString());
    }

    public Text getNickAsDN(PlayerEntity player, Style style) {
        return Team.decorateName(player.getScoreboardTeam(), Text.of(getNick(player))).setStyle(style);
    }

    public void saveToNBT() throws IOException {
        NbtCompound nbt = new NbtCompound();
        for (Map.Entry<String, String> entry : nicks.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            nbt.putString(key,value);
        }

        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "nick.nbt");
        NbtIo.writeCompressed(nbt, nbtFile);

        reset();
    }

    public void readFromNBT() throws IOException {
        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "nick.nbt");

        NbtCompound nbt = new NbtCompound();

        if (nbtFile.exists())
            nbt = NbtIo.readCompressed(nbtFile);
        for (String key : nbt.getKeys())
            nicks.put(key,nbt.getString(key));
    }

    public void reset() {
        nicks.clear();
    }
}
