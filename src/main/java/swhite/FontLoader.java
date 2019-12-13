package swhite;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FontLoader {
    private Map<String, PFont> loadedFonts;
    private String[] fontNames;

    public FontLoader() {
        loadedFonts = new HashMap<>();
        fontNames = PFont.list();
    }

    public PFont getRandomFont(PApplet context, int size) {
        Random random = new Random();
        String fontName = fontNames[random.nextInt(fontNames.length - 1)];
        String key = size + "-" + fontName;
        if (!loadedFonts.containsKey(key)) {
            loadedFonts.put(key, context.createFont(fontName, size));
        }
        return loadedFonts.get(key);
    }
}
