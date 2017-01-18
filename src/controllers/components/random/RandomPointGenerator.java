package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by HP PC on 1/18/2017.
 */
public interface RandomPointGenerator {
    Coordinate nextCoordinate();
}
