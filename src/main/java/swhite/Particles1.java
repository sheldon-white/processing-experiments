package swhite;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Multiple Particle Systems
 * by Daniel Shiffman.
 *
 * Click the mouse to generate a burst of particles
 * at mouse position.
 *
 * Each burst is one instance of a particle system
 * with Particles and CrazyParticles (a subclass of Particle)
 * Note use of Inheritance and Polymorphism here.
 */

public class Particles1 extends PApplet {
    private static final String NAME = "Particles1";
    private ArrayList<ParticleSystem> systems;
    static Random random = new Random();
    private static Map<Integer, PFont> loadedFonts;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    public void settings() {
        size(1000, 800);
    }

    public void setup() {
        systems = new ArrayList<>();
        loadedFonts = loadFonts();
    }

    public void draw() {
        background(0);
        for (ParticleSystem ps : systems) {
            ps.run();
            ps.addParticle();
        }
        if (systems.isEmpty()) {
            fill(255);
            textAlign(CENTER);
            text("click mouse to add particle systems", width/2f, height/2f);
        }
    }

    public void mousePressed() {
        systems.add(new ParticleSystem(1, new PVector(mouseX, mouseY)));
    }

    private Map<Integer, PFont> loadFonts() {
        String[] fontNames = PFont.list();
        loadedFonts = new HashMap<>();
        int idx = 0;
        for (String fontName: fontNames) {
            for (int fontSize = 10; fontSize < 30; fontSize++) {
                loadedFonts.put(idx, createFont(fontName, fontSize));
                idx++;
            }
        }
        return loadedFonts;
    }

    class ParticleSystem {
        ArrayList<Particle> particles;    // An arraylist for all the particles
        PVector origin;                   // An origin point for where particles are birthed

        ParticleSystem(int num, PVector v) {
            particles = new ArrayList<>();   // Initialize the arraylist
            origin = v.copy();                        // Store the origin point
            for (int i = 0; i < num; i++) {
                particles.add(new Particle(origin));    // Add "num" amount of particles to the arraylist
            }
        }

        void run() {
            // Cycle through the ArrayList backwards, because we are deleting while iterating
            for (int i = particles.size()-1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.run();
                if (p.isDead()) {
                    particles.remove(i);
                }
            }
        }

        void addParticle() {
            int fontIdx = random.nextInt(loadedFonts.size());
            PFont font = loadedFonts.get(fontIdx);
            int color = color(56 + random.nextInt(200), 56 + random.nextInt(200), 56 + random.nextInt(200));
            int minChar = 0x4E00;
            int maxChar = 0x9faf;
            char c = (char)(minChar + random.nextInt(maxChar - minChar));
            //char c = (char)(33 + random.nextInt(94));
            Particle p = new CrazyParticle(origin, c, font, color);
            particles.add(p);
        }

//        void addParticle(Particle p) {
//            particles.add(p);
//        }
//
//        // A method to test if the particle system still has particles
//        boolean dead() {
//            return particles.isEmpty();
//        }
    }

// A simple Particle class
    class Particle {
        PVector position;
        PVector velocity;
        PVector acceleration;
        float lifespan;

        Particle(PVector l) {
            acceleration = new PVector(0f, 0.05f);
            velocity = new PVector(random(-1, 1), random(-2, 0));
            position = l.copy();
            lifespan = 255.0f;
        }

        void run() {
            update();
            display();
        }

        // Method to update position
        void update() {
            velocity.add(acceleration);
            position.add(velocity);
            lifespan -= 1.2;
        }

        // Method to display
        void display() {
        }

        // Is the particle still useful?
        boolean isDead() {
            return lifespan < 0.0;
        }
    }

    class CrazyParticle extends Particle {

        // Just adding one new variable to a CrazyParticle
        // It inherits all other fields from "Particle", and we don't have to retype them!
        float theta;
        PFont font;
        int color;
        char c;

        // The CrazyParticle constructor can call the parent class (super class) constructor
        CrazyParticle(PVector l, char c, PFont font, int color) {
            // "super" means do everything from the constructor in Particle
            super(l);
            // One more line of code to deal with the new variable, theta
            theta = 0.0f;
            this.font = font;
            this.color = color;
            this.c = c;
        }

        // Notice we don't have the method run() here; it is inherited from Particle

        // This update() method overrides the parent class update() method
        void update() {
            super.update();
            // Increment rotation based on horizontal velocity
            float theta_vel = (velocity.x * velocity.mag()) / 10.0f;
            theta += theta_vel;
        }

        // This display() method overrides the parent class display() method
        void display() {
            pushMatrix();
            translate(position.x, position.y);
            //rotate(theta);
            //stroke(255, lifespan);
            textFont(font);
            fill(color, lifespan);
            text(c, 0, 0);
            popMatrix();
        }
    }
}