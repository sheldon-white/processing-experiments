package swhite.flocks;

import com.google.common.collect.Lists;
import processing.core.PApplet;
import processing.core.PFont;
import swhite.IntRect;

import java.util.*;
import java.util.stream.Collectors;

public class Flock1 extends PApplet {
    private static final String NAME = "swhite.flocks.Flock1";
    private int outputWidth = 1500, outputHeight = 1000;
    private List<Flock> flocks = new ArrayList<>();
    private int minFlockCount = 10;
    private static Map<String, PFont> loadedFonts = new HashMap<>();
    private String[] fontNames = PFont.list();

    private List<ThingFactory> thingFactories = Lists.newArrayList(
            new CircleFactory(),
            new LetterFactory(),
            new KanjiFactory());
    private List<ColorizerFactory> colorizerFactories = Lists.newArrayList(
            new HueColorizerFactory(),
            new BrightnessColorizerFactory(),
            new SaturationColorizerFactory(),
            new RandomColorizerFactory());

    private class CircleFactory implements ThingFactory {
        private Renderer renderer = (context, thing) -> {
            context.stroke(thing.getStrokeColor());
            context.strokeWeight(1);
            context.fill(thing.getFillColor());
            IntRect bounds = thing.getBounds();
            context.ellipse(bounds.x, bounds.y, bounds.w, bounds.h);
        };

        public MovingThing build(int x, int y, int w, int h, int fillColor, int strokeColor) {
            return new MovingThing(x, y, w, h, fillColor, strokeColor, renderer);
        }
    }

    private class KanjiFactory implements ThingFactory {
        private class MovingKanji extends MovingThing {
            int minChar = 0x4E00;
            int maxChar = 0x9faf;
            Character character;
            PFont font;

            public MovingKanji(PFont font, int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
                super(x, y, w, h, fillColor, strokeColor, renderer);
                this.font = font;
                Random random = new Random();
                character = (char)(minChar + random.nextInt(maxChar - minChar));
            }
        }

        private Renderer renderer = (context, thing) -> {
            MovingKanji kanji = (MovingKanji) thing;
            context.textFont(kanji.font);
            context.fill(kanji.getFillColor());
            IntRect bounds = kanji.getBounds();
            context.text(kanji.character, (float)bounds.x, (float)bounds.y);
        };

        public MovingThing build(int x, int y, int w, int h, int fillColor, int strokeColor) {
            PFont font = getRandomFont((int)(h * 0.75));
            return new MovingKanji(font, x, y, w, h, fillColor, strokeColor, renderer);
        }
    }

    private class LetterFactory implements ThingFactory {
        PFont font;

        private class MovingLetter extends MovingThing {
            Character character;

            public MovingLetter(int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
                super(x, y, w, h, fillColor, strokeColor, renderer);
                Random random = new Random();
                Boolean upperCase = random.nextBoolean();
                char minChar = upperCase ? 'A' : 'a';
                character = (char)(minChar + random.nextInt(26));
            }
        }

        private Renderer renderer = (context, thing) -> {
            MovingLetter letter = (MovingLetter) thing;
            context.textFont(font);
            context.fill(letter.getFillColor());
            IntRect bounds = letter.getBounds();
            context.text(letter.character, (float)bounds.x, (float)bounds.y);
        };

        public MovingThing build(int x, int y, int w, int h, int fillColor, int strokeColor) {
            if (font == null) {
                font = getRandomFont(h);
            }
            return new MovingLetter(x, y, w, h, fillColor, strokeColor, renderer);
        }
    }

    private class HueColorizerFactory implements ColorizerFactory {
        public Colorizer build() {
            return new HueColorizer();
        }
    }

    private class BrightnessColorizerFactory implements ColorizerFactory {
        public Colorizer build() {
            return new BrightnessColorizer();
        }
    }

    private class SaturationColorizerFactory implements ColorizerFactory {
        public Colorizer build() {
            return new SaturationColorizer();
        }
    }

    private class RandomColorizerFactory implements ColorizerFactory {
        public Colorizer build() {
            return new RandomColorizer();
        }
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        for (int i = 0; i < minFlockCount; i++) {
            flocks.add(newFlock());
        }
    }

    @Override
    public void draw() {
        background(180);
        flocks = flocks.stream().filter(f -> {
            f.advance();
            return !f.done();
        }).collect(Collectors.toList());
        if (flocks.size() < minFlockCount) {
            Flock flock = newFlock();
            flocks.add(flock);
        }
    }

    public Flock newFlock() {
        Random random = new Random();
        ThingFactory thingFactory = thingFactories.get(random.nextInt(thingFactories.size()));
        ColorizerFactory colorizerFactory = colorizerFactories.get(random.nextInt(colorizerFactories.size()));
        return Flock.build(this, colorizerFactory.build(), thingFactory);
    }

    PFont getRandomFont(Integer size) {
        Random random = new Random();
        String fontName = fontNames[random.nextInt(fontNames.length - 1)];
        String key = size + "-" + fontName;
        if (!loadedFonts.containsKey(key)) {
            loadedFonts.put(key, createFont(fontName, size));
        }
        return loadedFonts.get(key);
    }


//    int minChar = 0x4E00;
//    int maxChar = 0x9faf;
//    Character kanjiChar = (char)(minChar + random.nextInt(maxChar - minChar));
//    text(kanjiChar, (float)(quad.x + xoffset), (float)(quad.y + yoffset));
}
