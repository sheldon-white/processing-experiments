package swhite.flocks;

import com.google.common.collect.Lists;
import processing.core.PApplet;
import processing.core.PFont;
import swhite.IntRect;
import swhite.ShapeUtils;
import swhite.Triangle;

import java.util.*;
import java.util.stream.Collectors;

public class Flock1 extends PApplet {
    private static final String NAME = "swhite.flocks.Flock1";
    private List<Flock> flocks = new ArrayList<>();
    private int minFlockCount = 10;

    private List<ThingFactory> thingFactories = Lists.newArrayList(
            new CircleFactory(),
            new LetterFactory(),
            new TriangleFactory(),
            new StarFactory(),
            new KanjiFactory());
    private List<ColorizerFactory> colorizerFactories = Lists.newArrayList(
            new HueColorizerFactory(),
            new BrightnessColorizerFactory(),
            new SaturationColorizerFactory(),
            new RandomColorizerFactory());

    private static class CircleFactory implements ThingFactory {
        private Renderer renderer = (context, thing) -> {
            context.stroke(thing.getStrokeColor());
            context.strokeWeight(1);
            context.fill(thing.getFillColor());
            IntRect bounds = thing.getBounds();
            float size = bounds.w * 0.75f;
            context.ellipse(bounds.x, bounds.y, size, size);
        };

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            return new MovingThing(x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new CircleFactory();
        }
    }

    private class TriangleFactory implements ThingFactory {
        Triangle triangle;

        private class MovingTriangle extends MovingThing {
            Triangle triangle;

            MovingTriangle(Triangle triangle, int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
                super(x, y, w, h, fillColor, strokeColor, renderer);
                this.triangle = triangle;
            }
        }

        private Renderer renderer = (context, thing) -> {
            context.stroke(thing.getStrokeColor());
            context.strokeWeight(1);
            context.fill(thing.getFillColor());
            IntRect bounds = thing.getBounds();
            Triangle t = triangle.transform(bounds.x, bounds.y, 1);
            t.draw(context);
        };

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            if (triangle == null) {
                triangle = ShapeUtils.randomTriangle(0, 0, w, h);
            }
            return new MovingTriangle(triangle, x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new TriangleFactory();
        }
    }

    private class StarFactory implements ThingFactory {
        private class MovingStar extends MovingThing {
            int numPoints;
            float dTheta;
            float innerRadius, outerRadius;

            MovingStar(int numPoints, float dTheta, float innerRadius, float outerRadius, int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
                super(x, y, w, h, fillColor, strokeColor, renderer);
                this.numPoints = numPoints;
                this.dTheta = dTheta;
                this.innerRadius = innerRadius;
                this.outerRadius = outerRadius;
            }
        }

        private Renderer renderer = (context, thing) -> {
            MovingStar movingStar = (MovingStar) thing;
            context.stroke(thing.getStrokeColor());
            context.strokeWeight(1);
            context.fill(thing.getFillColor());
            IntRect bounds = thing.getBounds();
            float centerX = bounds.x + bounds.w / 2f;
            float centerY = bounds.y + bounds.h / 2f;
            float theta = movingStar.dTheta * (bounds.x + bounds.y);
            star(centerX, centerY, movingStar.innerRadius, movingStar.outerRadius, movingStar.numPoints, theta);
        };

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            Random random = new Random();
            int numPoints = 3 + random.nextInt(6);
            float theta = random.nextFloat() * 0.05f;
            float outerRadius = (int)(Math.min(w, h) * 0.5);
            float innerRadius = (int)(outerRadius * (0.1f + random.nextFloat() * 0.4));
            return new MovingStar(numPoints, theta, innerRadius, outerRadius, x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new StarFactory();
        }

        void star(float x, float y, float radius1, float radius2, int npoints, float theta) {
            float angle = TWO_PI / npoints;
            float halfAngle = angle / 2.0f;
            beginShape();
            for (float a = 0; a < TWO_PI; a += angle) {
                float sx = x + cos(a + theta) * radius2;
                float sy = y + sin(a + theta) * radius2;
                vertex(sx, sy);
                sx = x + cos(a + halfAngle + theta) * radius1;
                sy = y + sin(a + halfAngle + theta) * radius1;
                vertex(sx, sy);
            }
            endShape(CLOSE);
        }
    }

    private class KanjiFactory implements ThingFactory {
        private class MovingKanji extends MovingThing {
            int minChar = 0x4E00;
            int maxChar = 0x9faf;
            Character character;
            PFont font;

            MovingKanji(PFont font, int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
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

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            PFont font = getRandomFont(context, (int)(h * 0.75));
            return new MovingKanji(font, x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new KanjiFactory();
        }
    }

    private class LetterFactory implements ThingFactory {
        PFont font;

        LetterFactory() {
        }

        private class MovingLetter extends MovingThing {
            Character character;

            MovingLetter(int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
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

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            if (font == null) {
                font = getRandomFont(context, (int)(h * 0.75));
            }
            return new MovingLetter(x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new LetterFactory();
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
        int outputWidth = 1500;
        int outputHeight = 1000;
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

    private Flock newFlock() {
        Flock flock;
        Random random = new Random();
        ThingFactory thingFactory = thingFactories.get(random.nextInt(thingFactories.size()));
        ColorizerFactory colorizerFactory = colorizerFactories.get(random.nextInt(colorizerFactories.size()));
        flock = Flock.build(this, colorizerFactory.build(), thingFactory.createNew());
        return flock;
    }

    private Map<String, PFont> loadedFonts = new HashMap<>();
    private String[] fontNames = PFont.list();

    private PFont getRandomFont(PApplet context, int size) {
        Random random = new Random();
        String fontName = fontNames[random.nextInt(fontNames.length - 1)];
        String key = size + "-" + fontName;
        if (!loadedFonts.containsKey(key)) {
            loadedFonts.put(key, context.createFont(fontName, size));
        }
        return loadedFonts.get(key);
    }
}
