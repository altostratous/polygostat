package controllers.components.stat;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;
import controllers.components.random.RandomPolygonGenerator;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by HP PC on 1/18/2017.
 */
public class TwoPolygonsSheet {
    private RandomPolygonGenerator space;
    private Polygon firstPolygon, secondPolygon, firstConvexHull, secondConvexHull, intersection;
    public TwoPolygonsSheet(RandomPolygonGenerator space){
        this.space = space;
    }
    public void next(){
        firstPolygon = space.nextPolygon();
        secondPolygon = space.nextPolygon();
        firstConvexHull = Common.convexHull(firstPolygon);
        secondConvexHull = Common.convexHull(secondPolygon);
        GeometryFactory factory = firstPolygon.getFactory();
        intersection = factory.createPolygon(factory.createLinearRing(firstConvexHull.intersection(secondConvexHull).getCoordinates()), null);
    }
    public void drawToPanel(Pane pane){
        pane.getChildren().clear();
        drawPolygonToPanel(firstPolygon, pane, Color.DEEPSKYBLUE, Color.TRANSPARENT);
        drawPolygonToPanel(secondPolygon, pane, Color.INDIANRED, Color.TRANSPARENT);
        drawPolygonToPanel(intersection, pane, Color.DARKGREEN, Color.color(0.2, 0.8, 0.2, 0.3));
    }
    private void drawPolygonToPanel(Polygon geometricPolygon, Pane pane, Paint strokeColor, Paint fillColor){
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
}
