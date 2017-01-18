package controllers.components;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Created by HP PC on 1/18/2017.
 */
public class TriangleRandomPointGenerator implements RandomPointGenerator{
    public TriangleRandomPointGenerator(Polygon triangle) {

    }

    @Override
    public Coordinate nextCoordinate() {
        return null;
    }
}
