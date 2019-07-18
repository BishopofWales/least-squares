package bishopofwales;

public class Raycast {
    //Calculate if a ray insersects a circle, where circ_x and circ_y are the center of the circle
    public static boolean circleRayCol(double radius, double circ_x, double circ_y, double rayStart_x,
            double rayStart_y, double rayAngle) {

        // Calculate the slope of the ray line.
        double raySlope = Math.sin(rayAngle) / Math.cos(rayAngle);
        // Calculate the origin of the ray line.
        double rayOrigin = getOrigin(rayStart_x, rayStart_y, raySlope);

        // Calculate normal of ray angle
        double normal = rayAngle + Math.PI / 2;
        // Calculate the slope of the normal line from the ray to the circle.
        double normalSlope = Math.sin(normal) / Math.cos(normal);
        // Calculate the y-origin of the normal line from the ray to the circle.
        double normalOrigin = getOrigin(circ_x, circ_y, normalSlope);

        // Find where the two lines collide
        double col_y = ylineCol(raySlope, normalSlope, rayOrigin, normalOrigin);
        double col_x = (col_y - normalOrigin) / normalSlope;

        // compare distance from origin to colision to the radius of the circle AND make
        // sure the collision occurs along the ray, not behind it.
        return Math.pow(col_x - circ_x, 2) + Math.pow(col_y - circ_y, 2) < Math.pow(radius, 2)
                && ((Math.cos(rayAngle) > 0) == (circ_x > rayStart_x));
    }

    private static double ylineCol(double m1, double m2, double b1, double b2) {
        return (m1 * b2 - m2 * b1) / (m1 - m2);
    }

    private static double getOrigin(double x, double y, double m) {
        return (y - m * x);
    }

    public static double distSq(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distSq(x1, y1, x2, y2));
    }
}