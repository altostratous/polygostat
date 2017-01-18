package controllers.components.random;

import com.vividsolutions.jts.geom.Polygon;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public interface RandomConvexPolygonGenerator {
    Polygon nextPolygon();
    Rectangle2D getBounds();
}
