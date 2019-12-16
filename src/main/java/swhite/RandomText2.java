package swhite;

import processing.core.PApplet;
import processing.core.PFont;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomText2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private Random random = new Random();
    private FontLoader fontLoader = new FontLoader();
    private Set<DisplayedKanji> displayedKanjis = new HashSet<>();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 3000;
        int outputHeight = 2000;
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
        int maxWords = 1000;
        int ctr = 60;
        while (--ctr > 0) {
            if (displayedKanjis.size() < maxWords) {
                displayedKanjis.add(newWord());
            }
        }
        displayedKanjis = displayedKanjis.stream().filter(w -> w.alpha >= 0).map(DisplayedKanji::draw).collect(Collectors.toSet());
    }

    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        }
    }

    private DisplayedKanji newWord() {
        // Common and uncommon kanji ( 4e00 - 9faf)
        int minChar = 0x4E00;
        int maxChar = 0x9faf;

        DisplayedKanji displayedKanji = new DisplayedKanji();
        displayedKanji.x = random.nextInt(width);
        displayedKanji.y = random.nextInt(width);
        displayedKanji.kanjiChar = (char) (minChar + random.nextInt(maxChar - minChar));
        int fontSize = 10 + random.nextInt(100);
        displayedKanji.font = fontLoader.getRandomFont(this, fontSize);
        displayedKanji.color = color(56 + random.nextInt(200), 56 + random.nextInt(200), 56 + random.nextInt(200));
        displayedKanji.theta = 2 * PI * random.nextFloat();
        displayedKanji.alpha = 255;
        displayedKanji.dAlpha = 1 + (2 * random.nextFloat());
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
        float dAlpha;

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
