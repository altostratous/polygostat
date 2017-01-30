package controllers.components.stat;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;
import controllers.components.random.RandomNHullGenerator;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by HP PC on 1/18/2017.
 */
public class TwoPolygonsSheet {
    private RandomNHullGenerator space;
    private Polygon firstPolygon, secondPolygon, firstConvexHull, secondConvexHull, intersection;
    public TwoPolygonsSheet(RandomNHullGenerator space){
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
        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  pane.getChildren().clear();
                                  Common.drawPolygonToPanel(firstPolygon, pane, Color.DEEPSKYBLUE, Color.TRANSPARENT);
                                  Common.drawPolygonToPanel(secondPolygon, pane, Color.INDIANRED, Color.TRANSPARENT);
                                  Common.drawPolygonToPanel(intersection, pane, Color.DARKGREEN, Color.color(0.2, 0.8, 0.2, 0.3));
                              }
                          });
    }

    public double getOverLappingArea() {
        return intersection.getArea();
    }
}
