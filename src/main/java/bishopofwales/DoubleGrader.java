package bishopofwales;

import java.util.*;
import java.awt.geom.Point2D;

public class DoubleGrader extends Grader{

    private Random _rand;

    public DoubleGrader(Random rand) {
        super(rand);
    }

    // Assigns a grade to a lizard, based on performance of the task.
    void testLizard(Lizard lizard) {
        
        Circle[] worldGeom = new Circle[0];
        Point2D.Double result = lizard.simulateEnd(worldGeom, C.TIME_GIVEN);
        lizard.setScore(-1 * Raycast.dist(result.getX(), result.getY(),250, 0));
        //lizard.setScore(cum_score/C.NUM_ROUNDS);
        
        
        // System.out.println(
        // "Dist after: " + Raycast.dist(lizard.getX(), lizard.getY(),
        // worldGeom[0].getX(), worldGeom[0].getY()));
        // System.out.println("_________________");
    }
}