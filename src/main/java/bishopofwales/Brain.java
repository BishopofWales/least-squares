package bishopofwales;

import java.util.*;


public class Brain {

    

    private Queue<Neuron> relQueue;
    private long _time;
    private Neuron[] neurons;
    double[] exciteLevels;
    

    public Brain(Neuron[] neurons) {
        
        relQueue = new ArrayDeque<Neuron>();
        this.neurons = neurons;
        // Interpret dna into neuron connections.
        _time = 0;
    }

    public void triggerNeur(int neurIndex) {
        relQueue.add(neurons[neurIndex]);
        neurons[neurIndex].setAddedToRel(true);
    }

    public void stimulateNeuron(long time, int neurIndex, double amount){
        neurons[neurIndex].stimulate(time, amount);
        if (neurons[neurIndex].getPol() > Neuron.ACTION_POT && !neurons[neurIndex].getAddedtoRel()) {
            relQueue.add(neurons[neurIndex]);
            neurons[neurIndex].setAddedToRel(true);
        }
    }

    public void update() {
        // Takes all the neurons that went over their action potential the last run and
        // makes them fire.
        // Any neurons addded this cycle will fire in the next.
        
        int size = relQueue.size();
        for (int i = 0; i < size; i++) {
            Neuron releasingNeuron = relQueue.poll();
            releasingNeuron.setAddedToRel(false);
            for(Connection connection : releasingNeuron.getCons()){
                stimulateNeuron(_time, connection.getDst(), connection.getWeight()  * (releasingNeuron.isExcitatory ? 1.0 : -1.0));
            }
            releasingNeuron.resetPol();
        }
        _time++;
    }

    public double readNeur(int neurIndex) {
        return neurons[neurIndex].getPol();
    }

    public void reset() {
        for (Neuron neuron : neurons) {
            neuron.reset();
        }
        //System.out.println("Relque length:" + relQueue.size());
        //Empty the relqeue
        while(!relQueue.isEmpty()){
            relQueue.poll();
        }
        _time = 0;
    }
    public Neuron[] getNeurons(){
        return neurons;
    }

}