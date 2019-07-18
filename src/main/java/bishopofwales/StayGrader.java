package bishopofwales;

import java.util.*;
import java.awt.geom.Point2D;

public class StayGrader extends Grader{

    public StayGrader(Random rand) {
        super(rand);
    }

    // Assigns a grade to a lizard, based on performance of the task.
    void testLizard(Lizard lizard) {
        //LOL
        int cumScore = 0;
        Circle[] worldGeom = new Circle[0];
        Point2D.Double[] result = lizard.simulateFull(worldGeom, C.TIME_GIVEN);
        for(Point2D.Double point : result){
            if(point.getX() > 12 && point.getX() < 15){
                cumScore++;
            }
        }
        
        lizard.setScore(cumScore);
    }
}