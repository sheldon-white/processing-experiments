package swhite.flocks;

import com.google.common.collect.Lists;
import processing.core.PApplet;
import processing.core.PFont;
import swhite.FontLoader;
import swhite.IntRect;
import swhite.ShapeUtils;
import swhite.Triangle;

import java.util.*;
import java.util.stream.Collectors;

public class Flock1 extends PApplet {
    private static final String NAME = "swhite.flocks.Flock1";
    private List<Flock> flocks = new ArrayList<>();
    private int minFlockCount = 10;
    private FontLoader fontLoader = new FontLoader();

    private List<ThingFactory> thingFactories = Lists.newArrayList(
            new CharacterFactory(33, 254), // printable ascii
            new CircleFactory(),
            new CharacterFactory(0x400, 0x4ff), // cyrillic
            new CharacterFactory(0x370, 0x3ff), // greek and coptic
            new CharacterFactory('A', 'Z'), // upper case
            new CharacterFactory('a', 'z'), // lower case
            new TriangleFactory(),
            new StarFactory(),
            new CharacterFactory(0x4E00, 0x9faf) // kanji
    );

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

    private class CharacterFactory implements ThingFactory {
        PFont font;
        int minCharacter, maxCharacter;

        CharacterFactory(int minCharacter, int maxCharacter) {
            this.minCharacter = minCharacter;
            this.maxCharacter = maxCharacter;
        }

        private class MovingCharacter extends MovingThing {
            Character character;

            MovingCharacter(int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
                super(x, y, w, h, fillColor, strokeColor, renderer);
                Random random = new Random();
                character = (char)(minCharacter + random.nextInt(maxCharacter - minCharacter + 1));
            }
        }

        private Renderer renderer = (context, thing) -> {
            MovingCharacter letter = (MovingCharacter) thing;
            context.textFont(font);
            context.fill(letter.getFillColor());
            IntRect bounds = letter.getBounds();
            context.text(letter.character, (float)bounds.x, (float)bounds.y);
        };

        public MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor) {
            if (font == null) {
                font = fontLoader.getRandomFont(context, (int)(h * 0.75));
            }
            return new MovingCharacter(x, y, w, h, fillColor, strokeColor, renderer);
        }

        public ThingFactory createNew() {
            return new CharacterFactory(minCharacter, maxCharacter);
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
        background(nextColor());

        flocks = flocks.stream().filter(f -> {
            f.advance();
            return !f.done();
        }).collect(Collectors.toList());
        if (flocks.size() < minFlockCount) {
            Flock flock = newFlock();
            flocks.add(flock);
        }
    }

    private int t = 0;
    private float dr, dg, db, r0, g0, b0;

    private int nextColor() {
        if (t == 0) {
            Random random = new Random();
            dr = 0.005f * random.nextFloat();
            dg = 0.005f * random.nextFloat();
            db = 0.005f * random.nextFloat();
            r0 = random.nextFloat();
            g0 = random.nextFloat();
            b0 = random.nextFloat();
        }
        colorMode(RGB);
        float minVal = 100;
        float maxVal = 220;
        float r = minVal + 0.5f * (maxVal - minVal) * (1 + sin(t * dr + r0));
        float g = minVal + 0.5f * (maxVal - minVal) * (1 + sin(t * dg + g0));
        float b = minVal + 0.5f * (maxVal - minVal) * (1 + sin(t * db + b0));
        //println(r, g, b);
        t++;
        return color(r, g, b);
    }

    private Flock newFlock() {
        Flock flock;
        Random random = new Random();
        ThingFactory thingFactory = thingFactories.get(random.nextInt(thingFactories.size()));
        ColorizerFactory colorizerFactory = colorizerFactories.get(random.nextInt(colorizerFactories.size()));
        flock = Flock.build(this, colorizerFactory.build(), thingFactory.createNew());
        return flock;
    }
}
