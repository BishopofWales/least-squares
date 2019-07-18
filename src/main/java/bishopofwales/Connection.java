package bishopofwales;

public class Connection{
    public static final double DEFAULT_WEIGHT = 10.0;

    int src;
    int dst;
    double weight;
    public Connection(int src, int dst, double weight){
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }
    public int getDst(){
        return dst;
    }
    public double getWeight(){
        return weight;
    }
}