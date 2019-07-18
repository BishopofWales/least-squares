package bishopofwales;

import java.util.*;
import java.awt.geom.Point2D;

public class LightGrader extends Grader{
    private static int NUM_TRIALS = 6;
    private static double DIST_TO_TARGET = 10.0;
    private static double CIRCLE_RAD = 2.0;

    public LightGrader(Random rand) {
        super(rand);
    }

    // Assigns a grade to a lizard, based on performance of the task.
    void testLizard(Lizard lizard) {
        //LOL
        int cumScore = 0;
        //Multiple trials to smooth over the randomness somewhat.
        for(int i = 0; i < NUM_TRIALS; i++){
            double randAngle = rand.nextDouble()* Math.PI;
            Circle[] worldGeom = new Circle[1];
            Point2D circlePoint = new Point2D.Double( Math.cos(randAngle) * DIST_TO_TARGET, Math.cos(randAngle) * DIST_TO_TARGET);
            worldGeom[0] = new Circle(CIRCLE_RAD, circlePoint.getX(), circlePoint.getY());
            Point2D.Double result = lizard.simulateEnd(worldGeom, C.TIME_GIVEN);
            cumScore += DIST_TO_TARGET - result.distance(circlePoint);
        }
        
        lizard.setScore(cumScore);
    }
}