package bishopofwales;

public class Circle {
    private double _radius;
    private double _xPos;
    private double _yPos;

    public Circle(double radius, double xPos, double yPos) {
        _radius = radius;
        _xPos = xPos;
        _yPos = yPos;
    }

    double getX() {
        return _xPos;
    }

    double getY() {
        return _yPos;
    }

    double getRadius() {
        return _radius;
    }
}