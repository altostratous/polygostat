package controllers.components.stat;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.random.RandomConvexPolygonGenerator;
import controllers.components.random.RandomPointGenerator;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

/**
 * Created by HP PC on 1/18/2017.
 */
public class TwoPolygonsSheet {
    private RandomConvexPolygonGenerator space;
    private Polygon firstPolygon, secondPolygon;
    public TwoPolygonsSheet(RandomConvexPolygonGenerator space){
        this.space = space;
    }
    public void next(){
        firstPolygon = space.nextPolygon();
        secondPolygon = space.nextPolygon();
    }
    public void drawToPanel(Pane pane){
        pane.getChildren().clear();
        javafx.scene.shape.Polygon polygon = new javafx.scene.shape.Polygon();
        for (Coordinate coordinate :
                firstPolygon.getCoordinates()) {
            polygon.getPoints().addAll(coordinate.x * pane.getWidth(), coordinate.y * pane.getHeight());
        }
        polygon.setStroke(Color.RED);
        polygon.setLayoutX(0);
        polygon.setLayoutY(0);
        polygon.setStrokeWidth(2);
        pane.getChildren().add(polygon);
    }
}
