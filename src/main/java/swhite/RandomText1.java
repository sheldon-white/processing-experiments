package swhite;

import processing.core.PApplet;
import processing.core.PFont;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class RandomText1 extends PApplet {
    private static final String NAME = "RandomText1";
    private Random random = new Random();
    private Map<String, PFont> loadedFonts = new HashMap<>();
    private String[] fontNames = PFont.list();
    private List<String> dictionary;
    private Set<DisplayedWord> displayedWords = new HashSet<>();
    private FontLoader fontLoader = new FontLoader();

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
        dictionary = loadDictionary("/usr/share/dict/words");
    }

    @Override
    public void draw() {
        background(100, 255);
        int maxWords = 400;
        if (displayedWords.size() < maxWords) {
            displayedWords.add(newWord());
        }
        displayedWords = displayedWords.stream().filter(w -> w.alpha >= 0).map(DisplayedWord::draw).collect(Collectors.toSet());
    }

    private DisplayedWord newWord() {
        DisplayedWord displayedWord = new DisplayedWord();
        displayedWord.x = random.nextInt(width);
        displayedWord.y = random.nextInt(width);
        displayedWord.word = dictionary.get(random.nextInt(dictionary.size()));
        int fontSize = 10 + random.nextInt(100);
        displayedWord.font = fontLoader.getRandomFont(this, fontSize);
        displayedWord.color = color(56 + random.nextInt(200), 56 + random.nextInt(200), 56 + random.nextInt(200));
        displayedWord.theta = 2 * PI * random.nextFloat();
        displayedWord.alpha = 255;
        displayedWord.dAlpha = 1 + random.nextInt(10);
        return displayedWord;
    }

    private List<String> loadDictionary(String path) {
        List<String> words = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) words.add(line);
            br.close();
        } catch (Exception e) {
        }

        return words;
    }

    private class DisplayedWord {
        String word;
        PFont font;
        int color;
        int x;
        int y;
        float theta;
        int alpha;
        int dAlpha;

        DisplayedWord draw() {
            pushMatrix();
            translate(x, y);
            rotate(theta);
            textFont(font);
            fill(color, alpha);
            text(word, 0, 0);
            popMatrix();
            alpha -= dAlpha;
            return this;
        }
    }
}
