package bishopofwales;

import java.util.*;

public abstract class Grader {

    Random rand;

    public void gradeLizards(Lizard[] lizards) {

        for (Lizard lizard : lizards) {
            testLizard(lizard);
        }
        
        Arrays.sort(lizards, new LizGradComp());
    }

    public Grader(Random rand) {
        this.rand = rand;
    }

    // Assigns a grade to a lizard, based on performance of the task.
    abstract void testLizard(Lizard lizard);
    
    class LizGradComp implements Comparator<Lizard>{
        @Override
        public int compare(Lizard liz1, Lizard liz2){
            if(liz1.getScore() == liz2.getScore()) return 0;
            return liz1.getScore() > liz2.getScore() ? -1 : 1;
        }
    }

}