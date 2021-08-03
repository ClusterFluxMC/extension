package com.github.olivermakescode.extension;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HomeStorage {
    public HashMap<String,HashMap<String,float[]>> homes;

    public HomeStorage() {
        homes = new HashMap<String,HashMap<String,float[]>>();
    }

    public void addHome(PlayerEntity player, String name, float x, float y, float z) {
        String uuid = player.getUuidAsString();
        float[] pos = {x, y, z};
        HashMap<String,float[]> newHomes;
        if (homes.containsKey(uuid))
            newHomes = homes.get(uuid);
        else newHomes = new HashMap<>();

        newHomes.put(name, pos);

        if (homes.containsKey(uuid))
            homes.replace(uuid,newHomes);
        else homes.put(uuid,newHomes);
    }

    public float[] getHome(PlayerEntity player, String name) {
        String uuid = player.getUuidAsString();
        if (!homes.containsKey(uuid))
            return null;
        HashMap<String,float[]> homeSet = homes.get(uuid);

        if (!homeSet.containsKey(name))
            return null;

        return homeSet.get(name);
    }

    public void removeHome(PlayerEntity player, String name) {
        String uuid = player.getUuidAsString();
        if (!homes.containsKey(uuid))
            return;

        HashMap<String,float[]> homeSet = homes.get(uuid);

        if (!homeSet.containsKey(name))
            return;

         homeSet.remove(name);
    }

    public Collection<String> getNames(PlayerEntity player) {
        HashMap<String,float[]> homeMap = homes.get(player.getUuidAsString());
        Collection<String> out = Sets.newLinkedHashSet();
        for (Map.Entry<String,float[]> home : homeMap.entrySet())
            out.add(home.getKey());
        return out;
    }

    public boolean exists(PlayerEntity player, String name) {
        return getHome(player, name) != null;
    }

    public NbtCompound toNBT() {
        NbtCompound nbt = new NbtCompound();
        for (Map.Entry<String,HashMap<String,float[]>> entry : homes.entrySet()) {
            String uuid = entry.getKey();
            HashMap<String,float[]> value = entry.getValue();

            NbtCompound homeSet = new NbtCompound();

            for (Map.Entry<String,float[]> home : value.entrySet()) {
                String name = home.getKey();
                float[] pos = home.getValue();

                NbtCompound nbtPos = new NbtCompound();
                nbtPos.putFloat("x",pos[0]);
                nbtPos.putFloat("y",pos[1]);
                nbtPos.putFloat("z",pos[2]);

                homeSet.put(name,nbtPos);
            }
            nbt.put(uuid, homeSet);
        }

        return nbt;
    }

    public void fromNBT(NbtCompound nbt) {
        for (String uuid : nbt.getKeys()) {
            NbtCompound homeSet = nbt.getCompound(uuid);
            HashMap<String,float[]> set = new HashMap<>();
            for (String name : homeSet.getKeys()) {
                NbtCompound nbtPos = homeSet.getCompound(name);
                float[] pos = {nbtPos.getFloat("x"),nbtPos.getFloat("y"),nbtPos.getFloat("z")};
                set.put(name,pos);
            }
            homes.put(uuid,set);
        }
    }

    public void clear() {
        homes.clear();
    }

    public void save() throws IOException {
        NbtCompound nbt = toNBT();

        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "homes.nbt");
        NbtIo.writeCompressed(nbt, nbtFile);

        clear();
    }

    public void load() throws IOException {
        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "homes.nbt");

        NbtCompound nbt = new NbtCompound();

        if (nbtFile.exists())
            nbt = NbtIo.readCompressed(nbtFile);

        fromNBT(nbt);
    }
}
