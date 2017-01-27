package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public abstract class RandomPolygonGenerator {
    protected ConvexPolygonRandomPointGenerator pointGenerator;
    private int n;
    protected GeometryFactory factory;

    public void setN(int n) {
        this.n = n;
    }

    protected RandomPolygonGenerator(int n){
        factory = new GeometryFactory();
        this.n = n;
    }

    public Polygon nextPolygon() {
        Coordinate[] coordinates = new Coordinate[n + 1];
        for (int i = 0; i < n; i++) {
            coordinates[i] = pointGenerator.nextCoordinate();
        }
        coordinates[n] = coordinates[0];
        Polygon polygon = factory.createPolygon(factory.createLinearRing(coordinates), null);
        return polygon;
    }

    public abstract Rectangle2D getBounds();
}
