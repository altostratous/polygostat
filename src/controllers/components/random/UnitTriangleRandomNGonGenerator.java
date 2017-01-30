package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by HP PC on 1/30/2017.
 */
public class UnitTriangleRandomNGonGenerator extends RandomNHullGenerator {
    public UnitTriangleRandomNGonGenerator(int n) {
        super(n);
        pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                new Coordinate[]{
                        new Coordinate(-0.5, 0),
                        new Coordinate(+0.5, 0),
                        new Coordinate(0, Math.signum(0.75)),
                        new Coordinate(-0.5, 0)
                }
        ), null));
    }
}
