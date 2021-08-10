package com.github.olivermakescode.extension;

import com.google.common.collect.Sets;
import com.ibm.icu.impl.CollectionSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Pair;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeStorage {
    public HashMap<UUID, HashMap<String, Location>> homes;

    public HomeStorage() {
        homes = new HashMap<>();
    }

    public void addHome(PlayerEntity player, String name, BlockPos pos) {
        if (!homes.containsKey(player.getUuid()))
            homes.put(player.getUuid(),new HashMap<>());
        homes.get(player.getUuid()).put(name,new Location(player.getEntityWorld(),pos));
    }

    public Location getHome(PlayerEntity player, String name) {
        if (!homes.containsKey(player.getUuid())) return null;
        if (!homes.get(player.getUuid()).containsKey(name)) return null;
        return homes.get(player.getUuid()).get(name);
    }

    public void removeHome(PlayerEntity player, String name) {
        if (!homes.containsKey(player.getUuid())) return;
        if (!homes.get(player.getUuid()).containsKey(name)) return;
        homes.get(player.getUuid()).remove(name);
    }

    public Collection<String> getNames(PlayerEntity player) {
        if (!homes.containsKey(player.getUuid())) return null;
        Collection<String> out = Sets.newLinkedHashSet();
        for (Map.Entry<String,Location> location : homes.get(player.getUuid()).entrySet()) {
            out.add(location.getKey());
        }
        return out;
    }

    public boolean exists(PlayerEntity player, String name) {
        return getHome(player, name) != null;
    }

    public NbtCompound toNBT() {
        NbtCompound out = new NbtCompound();

        for (Map.Entry<UUID,HashMap<String,Location>> player : homes.entrySet()) {
            NbtCompound current = new NbtCompound();
            for (Map.Entry<String,Location> home : player.getValue().entrySet()) {
                current.put(home.getKey(),home.getValue().toNbt());
            }
            out.put(player.getKey().toString(),current);
        }

        return out;
    }

    public void fromNBT(NbtCompound nbt) {
        for (String uuid : nbt.getKeys()) {
            NbtCompound current = nbt.getCompound(uuid);
            HashMap<String,Location> homeMap = new HashMap<>();
            for (String name : current.getKeys()) {
                NbtCompound home = current.getCompound(name);
                BlockPos pos = BlockPos.fromLong(home.getLong("pos"));
                for (RegistryKey<World> key : GameruleHelper.server.getWorldRegistryKeys())
                    if (key.getValue().toString().equals(home.getString("id"))) homeMap.put(name,new Location(key,pos));
            }
            homes.put(UUID.fromString(uuid),homeMap);
        }
    }

    public void clear() {
        homes.clear();
    }

    public void save() throws IOException {
        NbtCompound nbt = toNBT();

        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "homes.v2.nbt");
        NbtIo.writeCompressed(nbt, nbtFile);

        clear();
    }

    public void load() throws IOException {
        Path savePath = GameruleHelper.server.getSavePath(WorldSavePath.ROOT);
        File nbtFile = new File(savePath.toAbsolutePath().toString(), "homes.v2.nbt");

        NbtCompound nbt = new NbtCompound();

        if (nbtFile.exists())
            nbt = NbtIo.readCompressed(nbtFile);

        fromNBT(nbt);
    }

    public static class Location {
        public RegistryKey<World> world;
        public BlockPos pos;

        public Location(World world, BlockPos pos) {
            this.world = world.getRegistryKey();
            this.pos = pos;
        }
        public Location(RegistryKey<World> world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Location location)) return false;
            return location.world.equals(world) && location.pos.equals(pos);
        }

        public NbtCompound toNbt() {
            NbtCompound out = new NbtCompound();
            out.putString("id",world.getValue().toString());
            out.putLong("pos",pos.asLong());
            return out;
        }

        public static Location of(World world, BlockPos pos) {
            return new Location(world,pos);
        }
        public static Location of(RegistryKey<World> world, BlockPos pos) {
            return new Location(world,pos);
        }
    }
}
