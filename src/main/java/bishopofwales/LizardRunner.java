package bishopofwales;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;

//input imports
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

class LizardRunner implements Runnable{
    private Canvas targetCanvas;
    private int numRuns;
    private Lizard[] lizards;
    public static final int HISTO_RANGE_START= 0;
    public static final int HISTO_RANGE_END = 200;
    public static final int HISTO_INCR = 1;
    public static final int BAR_WIDTH = 3;
    private static final Double ZOOM_SCALE = .1;
    private AffineTransform histoTrans;
    private double oldX;

    public LizardRunner(Canvas targetCanvas, int numRuns){
        histoTrans = new AffineTransform();
        this.targetCanvas = targetCanvas;
        this.numRuns = numRuns;
        targetCanvas.addEventHandler(ScrollEvent.SCROLL, 
                new EventHandler<ScrollEvent>() {
                    public void handle(ScrollEvent e){
                        if(lizards != null){
                            if(e.getDeltaY() > 0){
                                System.out.println("up");
                                histoTrans.scale(1+ZOOM_SCALE, 1+ZOOM_SCALE);
                            }
                            else{
                                System.out.println("down");
                                histoTrans.scale(1-ZOOM_SCALE, 1-ZOOM_SCALE);
                            }
                            drawHisto();
                        }
                    };
                });

        targetCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e){
                if(lizards != null){
                    histoTrans.translate(e.getX()-oldX, 0);
                    drawHisto();
                }
                oldX = e.getX();
            };
        });
        targetCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e){
                System.out.println("pressed");
                if(lizards != null){
                    oldX = e.getX();
                }
            };
        });
    }

    public void run(){

        Random rand = new Random();
        Mutator mut = new Mutator(rand);
        lizards = new Lizard[C.CLASS_SIZE];

        for (int i = 0; i < C.CLASS_SIZE; i++) {
            lizards[i] = new Lizard(mut.randomBrain());
            lizards[i].setName(Names.names[rand.nextInt(Names.names.length)]+Integer.toString(rand.nextInt(10000)));
        }

        Grader grader = new LightGrader(rand);
        Mutator mutator = new Mutator(rand);

        for (int i = 0; i < numRuns; i++) {
            grader.gradeLizards(lizards);

            drawHisto();
            drawLineGraph();
            //System.out.println("#################");
           
            System.out.println(i);
            System.out.println("_______________________");

            mutator.mutateClass(lizards);          
            
        }
        
    }


    private void drawLineGraph(){
        double[] grades = new double[lizards.length];
        //Tally values for histogram
        for (int k = lizards.length - 1; k >= 0; k--) {
            grades[k] = lizards[k].getScore();
        }
        double total = 0;
        for(int i = 0; i < grades.length/2;i++){
            total += grades[i];
        }
        double average = total/(grades.length/2);
        System.out.println("Average: " + average);

        System.out.println("Top 10:");
        for(int i = 0; i < 10; i++){
            System.out.println(lizards[i].getName() + " " + lizards[i].getScore());
        }
        //System.out.println("Offspring of #1:");
        double offspringTotal = 0;
        int offspringCount = 0;
        for(int i = 0; i < lizards.length; i++){
            
            if(lizards[i].getName().equals(lizards[0].getName()+"#")){
                //System.out.println(lizards[i].getName() + " " + lizards[i].getScore());
                offspringTotal += lizards[i].getScore();
                offspringCount++;
            }
            
        }
        System.out.println("Average of offspring of #1:" + (offspringTotal/offspringCount));
        
    }
    private void drawHisto(){
        double[] grades = new double[lizards.length];
        //Tally values for histogram
        for (int k = lizards.length - 1; k >= 0; k--) {
            grades[k] = lizards[k].getScore();
        }

        int[] histoValues = new int[(int)((grades[0] - grades[grades.length-1])/HISTO_INCR) + 1];
        double gradeOffset = grades[grades.length-1];
        for(int i = 0; i < grades.length; i++){
            histoValues[(int)((grades[i] - gradeOffset)/HISTO_INCR)]++;
        }

        final GraphicsContext imageGraphics = targetCanvas.getGraphicsContext2D();
        DrawingFunctions.clearCanvas(imageGraphics, targetCanvas);
        imageGraphics.setStroke(Color.BLACK);
        imageGraphics.setLineWidth(BAR_WIDTH * histoTrans.getScaleX());

        //Draw the values, using the transform to scale and translate the graph
        for(int i = 0; i < histoValues.length; i++){
            Point2D start = histoTrans.transform(new Point2D.Double(i+gradeOffset/HISTO_INCR,0.0), null);
            Point2D end = histoTrans.transform(new Point2D.Double(i+gradeOffset/HISTO_INCR, histoValues[i]),null);

            imageGraphics.strokeLine(start.getX(),start.getY(),end.getX(), end.getY());

        }
    }
    
    Lizard[] getLizards(){
        return lizards;
    }
    
}