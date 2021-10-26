package com.github.olivermakescode.extension;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            Style style = Style.EMPTY;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                char currChar = chars[i];
                if (currChar == '\\') {
                    builder.append(chars[++i]);
                    continue;
                }
                if (currChar == '&') {
                    Formatting formatting = Formatting.byCode(chars[i + 1]);
                    if (formatting != null) {
                        current = current.append(builder.toString()).formatted(formatting);
                        continue;
                    }
                }
                builder.append(currChar);
            }
            text.add(current);
        }
        return text.toArray(Text[]::new);
    }
}
