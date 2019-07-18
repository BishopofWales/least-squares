package bishopofwales;

import java.util.*;

public class Mutator {

    //Mutation constants
    public static final double CON_MUTATION_CHANCE = 0.04;
    public static final double WEIGHT_MUTATION_CHANCE = 0.01;
    public static final double WEIGHT_MUTATION_PROP = 0;//.2;

    private static double INITIAL_EXCIT_PROP = .7;
    private static double EXCIATORY_CHANGE_CHANCE = 0;//0.02;
    private static double EXCIATORY_CHANGE_PROP = .1;

    //Curve constants
    private static double RANK_FALL_OFF = .95;
    private static int INITIAL_CHILDREN = 60;

    private Random _rand;

    public Mutator(Random rand) {
        _rand = rand;
    }

    public Brain randomBrain() {

        Neuron[] randNeurons = new Neuron[C.NUM_NEUR];
        
        for(int i = 0; i < C.NUM_NEUR; i++){
            boolean isExcitatory = _rand.nextDouble() < INITIAL_EXCIT_PROP;
            Connection[] connections = new Connection[C.CONS]; 
            for(int k = 0; k < C.CONS; k++){
                connections[k] = new Connection(i, _rand.nextInt(C.NUM_NEUR), Connection.DEFAULT_WEIGHT);
            }
            randNeurons[i] = new Neuron(isExcitatory, connections);
        }
        return new Brain(randNeurons);
    }

    public void mutateClass(Lizard[] lizards) {
        Random rand = new Random();
        int[] distribution = generateDistribution(INITIAL_CHILDREN, RANK_FALL_OFF, C.CLASS_SIZE);
        
        int count = 0;
        int totalChildren = 0; 
        while(distribution[count] > 0){
            if(distribution[count] == 1){
                count++;
                continue;
            } 
            for(int i = 1; i < distribution[count]; i++){
                lizards[lizards.length-1-totalChildren] = copyAndMutate(lizards[count]);
                lizards[lizards.length-1-totalChildren].setName(lizards[count].getName()+"#");  
                totalChildren++;
            }
            count++;
            
        }

    }

    Lizard copyAndMutate(Lizard parentLizard) {
        Neuron[] parentNeurons = parentLizard.getBrain().getNeurons();
        Neuron[] childNeurons = new Neuron[parentNeurons.length];
        ArrayList<Double> conCounts = new ArrayList<Double>();
        for(int i = 0; i < parentNeurons.length; i++){
            //Copy connections from parent neuron into a new neuron
            ArrayList<Connection> newCons = new ArrayList<Connection>();
            for(int k = 0; k < parentNeurons[i].getCons().length; k++){
                //Mutate connection or copy.
                if (_rand.nextDouble() < CON_MUTATION_CHANCE) {
                    if(.5 < _rand.nextDouble()){
                        //Add a connection.
                        newCons.add(new Connection(i, _rand.nextInt(C.NUM_NEUR), Connection.DEFAULT_WEIGHT));
                    }
                    //Delete a connection. (AKA don't copy it.)
                    continue;
                }
            
                //Mutate connection weight or copy. 
                double weight =  parentNeurons[i].getCons()[k].getWeight();
                if(_rand.nextDouble() < WEIGHT_MUTATION_CHANCE){
                    weight *= 1 - (_rand.nextDouble() * 2 - 1)* WEIGHT_MUTATION_PROP;
                }
                newCons.add(new Connection(i, parentNeurons[i].getCons()[k].getDst(), weight));
                

            }
            boolean exciatory = parentNeurons[i].getIsExcitatory();
            if(_rand.nextDouble() < EXCIATORY_CHANGE_CHANCE){
                exciatory = !exciatory;
            }
            conCounts.add((double)newCons.size());
            childNeurons[i] = new Neuron(exciatory, newCons.toArray(new Connection[newCons.size()]));
        }
        //System.out.println("Average: " + Stats.findAverage(conCounts));
        //System.out.println("Median: " + Stats.findMedian(conCounts));
        return new Lizard(new Brain(childNeurons));
    }

    private int[] generateDistribution(int initialChildren, double rankFallOff, int totalChildren){
    
        int childrenRemaining = totalChildren;
        int[] distribution = new int[totalChildren];
        int childrenGiven = initialChildren;
        int count = 0;
        
        while(childrenGiven < childrenRemaining){
          childrenRemaining -= childrenGiven;
          distribution[count] = childrenGiven;
          childrenGiven = Math.max((int)(childrenGiven*rankFallOff), 2);
          count++;
        }
        //Once we exit the loop, give the remainder to this lizard.
        distribution[count] = childrenRemaining;
        
        return distribution;
      } 
}