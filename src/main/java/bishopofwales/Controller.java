package bishopofwales;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;


public class Controller {
    //Display vars
    private static final int LIZ_RAD = 5;
    private static final double LIZ_LINE_LEN = 8.0;
    
    public static final long FRAME_TIME = (long) Math.pow(10, 8);
    private Thread lizThread;
    private LizardRunner lizardRunner;
    //Lizard vars
    public static final int EVO_RUNS = 200;
    @FXML
    private Canvas graph;

    public void initialize() {

    }

    //Logic functions

    public void viewButtonHandler(ActionEvent e) {
        //If the thread is alive, the lizards are still being generated.
        if(!lizThread.isAlive()){
            Lizard[] lizards = lizardRunner.getLizards();
            System.out.println(lizards[0].getName());
            //Lizard copyOfLizard = new Lizard(lizards[0].getDNA());
            Lizard copyOfLizard = lizards[0];
            runView(new Circle[0], copyOfLizard.simulateFull(new Circle[0], C.TIME_GIVEN));
        }
    }

    //Drawing functions
    
    private void runView(final Circle[] worldGeom, Point2D.Double[] positions) {
        final GraphicsContext imageGraphics = graph.getGraphicsContext2D();
        new AnimationTimer() {
            //Keep track of real time to control rate of playback.
            long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > FRAME_TIME) {
                    renderFrame();
                    last = now;
                }
            }

            SimpleIntegerProperty time = new SimpleIntegerProperty();

            private void renderFrame() {
                if (time.get() >= positions.length) {
                    //Clean up after completing
                    this.stop();
                    return;
                }
                Point2D.Double currentPos = positions[time.get()];
                DrawingFunctions.clearCanvas(imageGraphics, graph);
                imageGraphics.setFill(Color.BLUE);
                imageGraphics.fillOval((int) currentPos.getX(), (int) currentPos.getY(), LIZ_RAD * 2, LIZ_RAD * 2);

                for (int i = 0; i < worldGeom.length; i++) {
                    imageGraphics.strokeOval((int) worldGeom[i].getX() - worldGeom[i].getRadius(),
                            (int) worldGeom[i].getY() - worldGeom[i].getRadius(), (int) worldGeom[i].getRadius() * 2,
                            (int) worldGeom[i].getRadius() * 2);
                }

                System.out.println("Time" + time);
                System.out.println("Lizard x: " + currentPos.getX());
                System.out.println("Lizard y: " + currentPos.getY());
                time.set(time.get() + 1);
            }
        }.start();
    }
    
  
    public void lizButtonHandler(ActionEvent event) {
        //Create seperate thread for generating the lizards
        lizardRunner = new LizardRunner(graph, EVO_RUNS);
        lizThread = new Thread(lizardRunner);
        lizThread.start();
    }
}

