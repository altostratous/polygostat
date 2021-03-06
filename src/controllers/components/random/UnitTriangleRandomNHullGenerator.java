package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public class UnitTriangleRandomNHullGenerator extends RandomNHullGenerator {
    public UnitTriangleRandomNHullGenerator(int n) {
        super(n);
        this.pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                new Coordinate[] {
                        new Coordinate(-0.5, 0),
                        new Coordinate(+0.5, 0),
                        new Coordinate(0, Math.signum(0.75)),
                        new Coordinate(-0.5, 0)
                }
        ), null));
    }

}
