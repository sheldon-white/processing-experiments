package swhite;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import processing.core.PApplet;

import java.util.Random;

public abstract class DesktopGenerator extends PApplet {
    protected Random r = new Random();
    private String className;
    protected boolean doneDrawing = false;
    protected static PApplet context;
    private static String[] globalArgs;

    @Option(name = "-b", usage = "run in batch mode")
    private boolean batchMode;

    public DesktopGenerator() {
    }

    public DesktopGenerator(String name) {
        className = name;
    }

    public void cacheArgs(String[] args) {
        globalArgs = args;
    }

    public void run() {
        PApplet.main(className);
    }

    protected void processArgs() {
        try {
            CmdLineParser parser = new CmdLineParser(this);
            parser.parseArgument(globalArgs);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void settings() {
        processArgs();
        initializeSettings();
    }

    @Override
    public void draw() {
        context = this;
        drawDesktop();
        if (doneDrawing) {
            saveDesktop();

            print("Done!\n");
            if (batchMode) {
                System.exit(0);
            } else {
                noLoop();
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            saveDesktop();
        }
    }

    private void saveDesktop() {
        save(getName() + "." + System.currentTimeMillis() + ".png");
    }

    protected String getName() {
        return className;
    }

    protected abstract void initializeSettings();

    protected abstract void drawDesktop();
}

