package com.github.olivermakescode.extension;

import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FormattedStringParser {
    public static Text[] parse(@NotNull String str) {
        return parse(str.split("\\\\n"));
    }
    public static Text[] parse(@NotNull String[] arr) {
        List<Text> text = new ArrayList<>();
        for (String s : arr) {
            char[] chars = s.toCharArray();
            MutableText current = Text.of("").copy();
            List<Formatting> formatting = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                char currChar = chars[i];
                if (currChar == '\\') {
                    builder.append(chars[++i]);
                    continue;
                }
                if (currChar == '&') {
                    MutableText next = Text.of(builder.toString()).copy();
                    if (formatting.size() >= 1)
                        next = next.formatted(formatting.toArray(Formatting[]::new));
                    current = current.append(next);
                    builder = new StringBuilder();
                    if (chars[++i] == 'r')
                        formatting = new ArrayList<>();
                    else {
                        Formatting newFormat = Formatting.byCode(chars[i]);
                        if (newFormat != null)
                            formatting.add(newFormat);
                        else builder.append(chars[--i]);
                    }
                    continue;
                }
                builder.append(currChar);
            }
            MutableText next = Text.of(builder.toString()).copy();
            if (formatting.size() >= 1)
                next = next.formatted(formatting.toArray(Formatting[]::new));

            text.add(current.append(next));
        }
        return text.toArray(Text[]::new);
    }
    public static NbtList parseNbt(@NotNull String str) {
        return parseNbt(str.split("\\\\n"));
    }
    public static NbtList parseNbt(@NotNull String[] arr) {
        Text[] texts = parse(arr);
        NbtList list = new NbtList();
        for (Text t : texts)
            list.add(list.size(),NbtString.of(Text.Serializer.toJson(t)));
        return list;
    }
}
