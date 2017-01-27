package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public class UnitSquareRandomNHullGenerator extends RandomNHullGenerator {
    public UnitSquareRandomNHullGenerator(int n) {
        super(n, new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(
                new Coordinate[] {
                        new Coordinate(.25, .25),
                        new Coordinate(.25, .75),
                        new Coordinate(.75, .75),
                        new Coordinate(.75, .25),
                        new Coordinate(.25, .25)
                }
        ), null));
        this.pointGenerator = new PolygonRandomPointGenerator(this.space);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0, 0, 1, 1);
    }
}