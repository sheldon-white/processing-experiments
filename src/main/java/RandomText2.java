import processing.core.PApplet;
import processing.core.PFont;

import java.util.*;
import java.util.stream.Collectors;

public class RandomText2 extends PApplet {
    private static final String NAME = "RandomText2";
    private Random random = new Random();
    private Map<String, PFont> loadedFonts = new HashMap<>();
    private String[] fontNames = PFont.list();
    private List<String> dictionary;
    private Set<DisplayedKanji> displayedKanjis = new HashSet<>();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 1600;
        int outputHeight = 900;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        background(100, 255);
        int maxWords = 400;
        if (displayedKanjis.size() < maxWords) {
            displayedKanjis.add(newWord());
        }
        displayedKanjis = displayedKanjis.stream().filter(w -> w.alpha >= 0).map(DisplayedKanji::draw).collect(Collectors.toSet());
    }

    PFont getRandomFont(Integer size) {
        String fontName = fontNames[random.nextInt(fontNames.length - 1)];
        String key = size + "-" + fontName;
        if (!loadedFonts.containsKey(key)) {
            loadedFonts.put(key, createFont(fontName, size));
        }
        return loadedFonts.get(key);
    }

    DisplayedKanji newWord() {
        // Common and uncommon kanji ( 4e00 - 9faf)
        int minChar = 0x4E00;
        int maxChar = 0x9faf;

        DisplayedKanji displayedKanji = new DisplayedKanji();
        displayedKanji.x = random.nextInt(width);
        displayedKanji.y = random.nextInt(width);
        displayedKanji.kanjiChar = (char)(minChar + random.nextInt(maxChar - minChar));
        int fontSize = 10 + random.nextInt(100);
        displayedKanji.font = getRandomFont(fontSize);
        displayedKanji.color = color(56 + random.nextInt(200), 56 + random.nextInt(200), 56 + random.nextInt(200));
        displayedKanji.theta = 2 * PI * random.nextFloat();
        displayedKanji.alpha = 255;
        displayedKanji.dAlpha = 1 + random.nextInt(10);
        return displayedKanji;
    }

    private class DisplayedKanji {
        Character kanjiChar;
        PFont font;
        int color;
        int x;
        int y;
        float theta;
        int alpha;
        int dAlpha;
        private Random random = new Random();

        DisplayedKanji draw() {
            pushMatrix();
            translate(x, y);
            rotate(theta);
            textFont(font);
            fill(color, alpha);
            text(kanjiChar, 0, 0);
            popMatrix();
            alpha -= dAlpha;
            return this;
        }
    }
}
