package controllers.components;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import java.util.Random;

/**
 * Created by HP PC on 1/18/2017.
 */
public class TriangleRandomPointGenerator implements RandomPointGenerator{
    private Coordinate a, b, c;
    private Random random;
    public TriangleRandomPointGenerator(Polygon triangle) {
        random = new Random(System.currentTimeMillis());
        a = triangle.getCoordinates()[0];
        b = triangle.getCoordinates()[1];
        c = triangle.getCoordinates()[2];
    }

    @Override
    public Coordinate nextCoordinate() {
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double x = (1 - Math.sqrt(r1)) * a.x + (Math.sqrt(r1) * (1 - r2)) * b.x + (Math.sqrt(r1) * r2) * c.x;
        double y = (1 - Math.sqrt(r1)) * a.y + (Math.sqrt(r1) * (1 - r2)) * b.y + (Math.sqrt(r1) * r2) * c.y;
        return new Coordinate(x, y, 0);
    }
}
