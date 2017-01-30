package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/30/2017.
 */
public class UnitSquareRandomNGonGenerator extends RandomNGonGenerator {
    public UnitSquareRandomNGonGenerator(int n) {
        super(n);

        pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                new Coordinate[]{
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(1, 1),
                        new Coordinate(1, 0),
                        new Coordinate(0, 0)
                }
        ), null));
    }

}
