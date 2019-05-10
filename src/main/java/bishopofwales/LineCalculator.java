package bishopofwales;

import java.util.ArrayList;
import no.uib.cipr.matrix.*;

public class LineCalculator {
    ArrayList<Point> points;
    public LineCalculator(){
        points = new ArrayList<Point>();
    }
    public void addPoint(double x, double y){
        points.add(new Point(x,y));
    }
    public void clear(){
        points.clear();
    }
    public Vector calcLine(){

        //Read x values into A
        double[][] arrA = new double[points.size()][2];
        for(int i = 0; i < arrA.length; i++){
            arrA[i][0] = 1;
            arrA[i][1] = points.get(i).getX();
        }
        Matrix matrixA = new DenseMatrix(arrA);

        //Read y values into b
        double[] arrB = new double[points.size()];
        for(int i = 0; i < arrB.length; i++){
            arrB[i] = points.get(i).getY();
        }
        Vector vectorB = new DenseVector(arrB);

        //Store matrixA row and cols
        int aRows = matrixA.numRows();
        int aCols = matrixA.numColumns();
        //Compute transpose since we use it multiple times
        Matrix matrixATrans = matrixA.transpose(new DenseMatrix(aCols, aRows));

        //Compute answer
        Matrix lower = matrixATrans.mult(matrixA, new DenseMatrix(aCols, aCols));
        Vector upper = matrixATrans.mult(vectorB, new DenseVector(2));

        return lower.solve(upper, new DenseVector(2));
        
    }
}
class Point{
    private double _x;
    private double _y;

    public Point(double x, double y){
        _x = x;
        _y = y;
    }
    public double getX(){
        return _x;
    }
    public double getY(){
        return _y;
    }

}