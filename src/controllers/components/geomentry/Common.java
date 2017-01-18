package controllers.components.geomentry;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Created by HP PC on 1/18/2017.
 */
public class Common {
    public static Polygon convexHull(Polygon polygon){
        GeometryFactory factory =  polygon.getFactory();
        return factory.createPolygon(factory.createLinearRing(polygon.convexHull().getCoordinates()), null);
    }
}
