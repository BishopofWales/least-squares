package bishopofwales;

import java.util.*;

public class Neuron {

    public static final double ACTION_POT = 33.0;
    public static final double FALL_OFF = 1.0;

    private double polarityLevel;
    private long lastStimmed;
    private boolean addedtoRel;
    boolean isExcitatory;
    Connection[] connections;

    public Neuron(boolean isExcitatory, Connection[] connections) {
        polarityLevel = 0;
        lastStimmed = 0;
        this.connections = connections;
        addedtoRel = false;

        this.isExcitatory = isExcitatory;
    }

    public boolean getIsExcitatory(){
        return isExcitatory;
    }
    public boolean getAddedtoRel(){
        return addedtoRel;
    }
    public void setAddedToRel(boolean addedtoRel){
        this.addedtoRel = addedtoRel;
    }

    public void stimulate(long time, double amount) {
        // System.out.println("Stimmed " + this);
        // First we calculate the falloff since the last time the neuron was touched.
        polarityLevel = Math.max(0.0, polarityLevel - (time - lastStimmed) * FALL_OFF);
        // Next we add the pulse to the neuron
        polarityLevel += amount;
        /*
        
        */
        lastStimmed = time;
    }



    public double getPol() {
        return polarityLevel;
    }

    public Connection[] getCons() {
        return connections;
    }

    public void reset() {
        resetPol();
        lastStimmed = 0;
        addedtoRel = false;
    }
    public void resetPol(){
        polarityLevel = 0;
    }

}