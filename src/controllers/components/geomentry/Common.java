package controllers.components.geomentry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by HP PC on 1/18/2017.
 */
public class Common {
    public static Polygon convexHull(Polygon polygon){
//        if (polygon.getCoordinates().length <= 4)
//            return polygon;
        GeometryFactory factory =  polygon.getFactory();
        return factory.createPolygon(factory.createLinearRing(polygon.convexHull().getCoordinates()), null);
    }
    public static void drawPolygonToPanel(Polygon geometricPolygon, Pane pane, Paint strokeColor, Paint fillColor){
        javafx.scene.shape.Polygon polygon = new javafx.scene.shape.Polygon();
        for (Coordinate coordinate :
                geometricPolygon.getCoordinates()) {
            pane.getChildren().add(new Circle(coordinate.x * pane.getWidth(), coordinate.y * pane.getHeight(), 2, strokeColor));
        }
        Polygon convex = Common.convexHull(geometricPolygon);
        for (Coordinate coordinate :
                convex.getCoordinates()) {
            polygon.getPoints().addAll(coordinate.x * pane.getWidth(), coordinate.y * pane.getHeight());
        }
        polygon.setStroke(strokeColor);
        polygon.setFill(fillColor);
        polygon.setLayoutX(0);
        polygon.setLayoutY(0);
        polygon.setStrokeWidth(1.5);
        pane.getChildren().add(polygon);
    }
    public static int fixInSize(int index, int size){
        while (index < 0)
            index += size;
        return index % size;
    }
}
