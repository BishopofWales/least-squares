package bishopofwales;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;


import no.uib.cipr.matrix.*;

public class Controller {

    public static final int LINE_INC = 20;
    public static final double LINE_SPEED = 1;
    public static final double POINT_RADIUS = 5;
    public LineCalculator lineCalculator;
    @FXML
    private Canvas graph;

    public void initialize(){
        //Init graphics
        
        drawGraphLines();

        //Init logic
        lineCalculator = new LineCalculator();
    }


    //Drawing functions

    public void drawGraphLines(){
        GraphicsContext gc = graph.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for(int i = 0; i < graph.getWidth()/LINE_INC; i++){
            gc.strokeLine(LINE_INC*i, 0, LINE_INC*i, graph.getHeight());
        }
        for(int i = 0; i < graph.getHeight()/LINE_INC; i++){
            gc.strokeLine(0,LINE_INC*i, graph.getWidth(), LINE_INC*i);
        }


    }
    public void drawPoint(Point point, GraphicsContext gc){
        gc.setFill(Color.RED);
        gc.fillOval(point.getX()-POINT_RADIUS, point.getY()-POINT_RADIUS, POINT_RADIUS*2, POINT_RADIUS*2);
    }
    public void drawPoints(){
        GraphicsContext gc = graph.getGraphicsContext2D();
        
        
        for(Point point : lineCalculator.points){
            drawPoint(point, gc);
        }
    }

    public void drawSlopeLine(double m, double b){
        
        final SimpleDoubleProperty x = new SimpleDoubleProperty();
        final SimpleDoubleProperty y = new SimpleDoubleProperty();

        
        if(b > graph.getHeight()){
            //b is below the screen
            double deltaY = b - graph.getHeight();
            double deltaX = deltaY/-m;
            x.set(deltaX);
            y.set(graph.getHeight());
        }
        else if (b < 0){
            //b is above the screen
            double deltaY = -b;
            double deltaX = deltaY/m;
            x.set(deltaX);
            y.set(0.0);
        }
        else{
            //b is on the screen
            x.set(0.0);
            y.set(b);
        }
        GraphicsContext gc = graph.getGraphicsContext2D();

        gc.clearRect(0,0,graph.getWidth(),graph.getHeight());
        drawGraphLines();
        drawPoints();
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Get the context and draw the line.
               
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(1);

                double newX = x.getValue() + LINE_SPEED;
                double newY = y.getValue() + LINE_SPEED * m;

                gc.strokeLine(x.getValue(), y.getValue(), newX, newY);

                //Animation stops itself when needed (so cool).
                if(newX > graph.getWidth() || newY > graph.getHeight() || newY < 0){
                    this.stop();
                }

                x.set(newX);
                y.set(newY);


            }
        };

        timer.start();
    }

    //Input handlers

    public void handleGraphClick(MouseEvent me){
        //Draw point on canvas
        GraphicsContext gc = graph.getGraphicsContext2D();
        drawPoint(new Point(me.getX(), me.getY()), gc);
        //Add point to line calculator
        lineCalculator.addPoint(me.getX(),me.getY());

    }


    public void centerButtonHandler(ActionEvent e) {
        Vector res = lineCalculator.calcLine();
        System.out.println("m " + res.get(1));
        System.out.println("b " + res.get(0));
        
        drawSlopeLine(res.get(1) , res.get(0));
    }

    public void clearButtonHandler(ActionEvent e){
        lineCalculator.clear();
        GraphicsContext gc = graph.getGraphicsContext2D();
        gc.clearRect(0,0,graph.getWidth(),graph.getHeight());
        drawGraphLines();
    }



}
