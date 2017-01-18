package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public class UnitSquareRandomConvexPolygonGenerator implements RandomPolygonGenerator {
    private ConvexPolygonRandomPointGenerator pointGenerator;
    private int n;
    private GeometryFactory factory;
    public UnitSquareRandomConvexPolygonGenerator(int n) {
        this.n = n;
        factory = new GeometryFactory();
        this.pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
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
    public Polygon nextPolygon() {
        Coordinate[] coordinates = new Coordinate[n + 1];
        for (int i = 0; i < n; i++) {
            coordinates[i] = pointGenerator.nextCoordinate();
        }
        coordinates[n] = coordinates[0];
        Polygon polygon = factory.createPolygon(factory.createLinearRing(coordinates), null);
        return polygon;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0, 0, 1, 1);
    }
}
