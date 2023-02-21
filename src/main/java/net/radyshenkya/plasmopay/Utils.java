package net.radyshenkya.plasmopay;

import java.util.ArrayList;

import net.minecraft.text.Text;

public class Utils {
    public static String[] wrapStringByMaxLength(String str, int maxLength, boolean breakWords) {
        ArrayList<String> wrappedString = new ArrayList<String>();

        wrappedString.add("");

        int currentStringIndex = 0;
        int charsInLineCounter = 0;

        for (char chr : str.toCharArray()) {
            if (charsInLineCounter > maxLength && (chr == ' ' || breakWords)) {
                charsInLineCounter = 0;
                currentStringIndex += 1;
                wrappedString.add("");
            }

            charsInLineCounter += 1;

            wrappedString.set(currentStringIndex, wrappedString.get(currentStringIndex) + chr);
        }

        String[] result = new String[wrappedString.size()];
        result = wrappedString.toArray(result);
        return result;
    }

    public static Text[] wrapTextByMaxLength(Text text, int maxLength, boolean breakWords) {
        String[] wrapped = wrapStringByMaxLength(text.getString(), maxLength, breakWords);

        Text[] texts = new Text[wrapped.length];

        for (int i = 0; i < wrapped.length; i++) {
            System.out.println(wrapped[i]);
            texts[i] = Text.of(wrapped[i]);
        }

        return texts;
    }
}
