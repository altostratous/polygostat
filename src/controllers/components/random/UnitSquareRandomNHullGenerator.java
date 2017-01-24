package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public class UnitSquareRandomNHullGenerator extends RandomNHullGenerator {
    public UnitSquareRandomNHullGenerator(int n) {
        super(n);
        this.pointGenerator = new PolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                new Coordinate[] {
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(1, 1),
                        new Coordinate(1, 0),
                        new Coordinate(0, 0)
                }
        ), null));
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0, 0, 1, 1);
    }
}
