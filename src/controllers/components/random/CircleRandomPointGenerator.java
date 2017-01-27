package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by HP PC on 1/18/2017.
 */
public class CircleRandomPointGenerator implements RandomPointGenerator {
    private double radius;
    private Coordinate center;
    private Random random;

    public CircleRandomPointGenerator(double radius, Coordinate center) {
        this.radius = radius;
        this.center = center;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public Coordinate nextCoordinate() {
        double r = Common.randomBiLinear(random, radius);
        double theta = random.nextDouble() * 2 * Math.PI;
        return new Coordinate(center.x + r * Math.cos(theta), center.y + r * Math.sin(theta));
    }
}
