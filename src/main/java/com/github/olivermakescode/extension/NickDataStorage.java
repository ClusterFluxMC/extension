package com.github.olivermakescode.extension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

import java.util.HashMap;

public class NickDataStorage {
    public HashMap<String,String> nicks;

    public NickDataStorage() {
        nicks = new HashMap<String,String>();
    }

    public void addNick(PlayerEntity player, String name) {
        nicks.put(player.getUuidAsString(),name);
    }

    public void removeNick(PlayerEntity player) {
        nicks.remove(player.getUuidAsString());
    }

    public String getNick(PlayerEntity player) {
        return nicks.get(player.getUuidAsString());
    }

    public Text getNickAsDN(PlayerEntity player) {
        return Team.decorateName(player.getScoreboardTeam(), Text.of(getNick(player)));
    }
}
