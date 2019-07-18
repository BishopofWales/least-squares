package bishopofwales;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Lizard {

    public static final double LIN_SPD = 1.0; // m/s public
    public static final double ANG_SPD = Math.PI / 20.0; // rad/s
    public static final int EYE_NEUR = 50;
    public static final int RIGHT_NEUR = 51;
    public static final int LEFT_NEUR = 52;
    public static final int FORWARD_NEUR = 53;
    public static final int CLOCK_NEUR = 54;
    public static final int INHIB_NEUR = 55;

    private Brain _lizBrain;
    private double _xPos;
    private double _yPos;
    private double _rotation;
    private double _score;
    private boolean clockActive;
    // private Random _rand;
    private String _name;

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Lizard(Brain brain) {
        _name = "";
        _lizBrain = brain;
        _xPos = 0;
        _yPos = 0;
        _rotation = 0;
        clockActive = true;
    }

    private void proccessInput(Circle[] worldGeom) {
        // First we check if we see any geometry and stimulate the eye neuron if we do.
        for (Circle circle : worldGeom) {
            if (Raycast.circleRayCol(circle.getRadius(), circle.getX(), circle.getY(), _xPos, _yPos, _rotation)) {
                _lizBrain.triggerNeur(EYE_NEUR);
            }
        }
        // Next we check to see if the muscle neurons have fired and move the creature
        // accordingly.
        if (_lizBrain.readNeur(LEFT_NEUR) > Neuron.ACTION_POT) {
            _rotation -= ANG_SPD;
        }
        if (_lizBrain.readNeur(RIGHT_NEUR) > Neuron.ACTION_POT) {
            _rotation += ANG_SPD;
        }
        if (_lizBrain.readNeur(FORWARD_NEUR) > Neuron.ACTION_POT) {
            _xPos += Math.cos(_rotation) * LIN_SPD;
            _yPos += Math.sin(_rotation) * LIN_SPD;
        }
        //Clock neur gets released every time, if not inhibited
        if (_lizBrain.readNeur(INHIB_NEUR) > Neuron.ACTION_POT) {
            clockActive = !clockActive;
        }
        if (clockActive){
            _lizBrain.triggerNeur(CLOCK_NEUR);
        }
        _lizBrain.update();
       
    }

    public double getX() {
        return _xPos;
    }

    public double getY() {
        return _yPos;
    }

    public double getAng() {
        return _rotation;
    }

    public double getScore() {
        return _score;
    }

    public void setScore(double score) {
        _score = score;
    }

  
    public Point2D.Double simulateEnd(Circle[] worldGeom, int time){
        for(int i = 0; i < time; i++){
            proccessInput(worldGeom);
        }

        double savedX = getX();
        double savedY = getY();
        resetLizard();

        return new Point2D.Double(savedX, savedY);
    }

    public Point2D.Double[] simulateFull(Circle[] worldGeom, int time){
        ArrayList<Point2D.Double> positions = new ArrayList<Point2D.Double>();
        for(int i = 0; i < time; i++){
            positions.add(new Point2D.Double(getX(), getY()));
            proccessInput(worldGeom);
        }

        resetLizard();

        return positions.toArray(new Point2D.Double[positions.size()]);
    }

    //Resets position and zeroes out the neurons in the brain.
    public void resetLizard() {
        _xPos = 0;
        _yPos = 0;
        _rotation = 0;
        _lizBrain.reset();
        clockActive = true;
    }
    public Brain getBrain(){
        return _lizBrain;
    }
}
