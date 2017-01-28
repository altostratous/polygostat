package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public abstract class RandomNHullGenerator {
    protected RandomPointGenerator pointGenerator;
    private int n;
    protected GeometryFactory factory;

    public void setN(int n) {
        this.n = n;
    }

    protected RandomNHullGenerator(int n){
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
        return Common.convexHull(polygon);
    }

    public abstract Rectangle2D getBounds();
}
