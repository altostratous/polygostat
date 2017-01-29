package controllers.components.geomentry;

import com.vividsolutions.jts.algorithm.distance.PointPairDistance;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * Created by HP PC on 1/18/2017.
 */
public class Common {
    public static Pane debugPain;
    public static Polygon convexHull(Polygon polygon){
//        if (polygon.getCoordinates().length <= 4)
//            return polygon;
        GeometryFactory factory =  polygon.getFactory();
        return factory.createPolygon(factory.createLinearRing(polygon.convexHull().getCoordinates()), null);
    }
    public static void drawPolygonToPanel(Polygon geometricPolygon, Pane pane, Paint strokeColor, Paint fillColor){
        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  javafx.scene.shape.Polygon polygon = new javafx.scene.shape.Polygon();
                                  pane.getChildren().clear();
                                  for (Coordinate coordinate :
                                          geometricPolygon.getCoordinates()) {
                                      pane.getChildren().add(new Circle((coordinate.x / 2 + 0.5) * pane.getWidth(), (coordinate.y / 2 + 0.5) * pane.getHeight(), 2, strokeColor));
                                  }
                                  // Polygon convex = Common.convexHull(geometricPolygon);
                                  for (Coordinate coordinate :
                                      // convex.getCoordinates()) {
                                          geometricPolygon.getCoordinates()) {
                                      polygon.getPoints().addAll((coordinate.x / 2 + 0.5) * pane.getWidth(), (coordinate.y / 2 + 0.5) * pane.getHeight());
                                  }
                                  polygon.setStroke(strokeColor);
                                  polygon.setFill(fillColor);
                                  polygon.setLayoutX(0);
                                  polygon.setLayoutY(0);
                                  polygon.setStrokeWidth(1.5);
                                  pane.getChildren().add(polygon);
                              }
                          });
    }

    public static Polygon addPointToConvexHull(Polygon polygon, Coordinate coordinate){
        Coordinate[] coordinates = new Coordinate[polygon.getCoordinates().length + 1];
        for (int i = 0; i < polygon.getCoordinates().length; i++) {
            coordinates[i] = polygon.getCoordinates()[i];
        }
        coordinates[coordinates.length - 2] = coordinate;
        coordinates[coordinates.length - 1] = coordinates[0];
        return Common.convexHull(
                polygon.getFactory().createPolygon(
                        polygon.getFactory().createLinearRing(coordinates),
                        null)
        );
    }

    public static int fixInSize(int index, int size){
        while (index < 0)
            index += size;
        return index % size;
    }

    public static Coordinate lineIntersection(Coordinate p0, Coordinate p1, Coordinate p2, Coordinate p3) {
        double m1, m2, b1, b2;
        if (p0.x == p1.x){
            if (p2.x == p3.x)
            {
                return null;
            }
            else
            {
                m2 = (p3.y - p2.y) / (p3.x - p2.y);
                b2 = p2.y - m2 * p2.x;
                return new Coordinate(p0.x, m2 * p0.x + b2);
            }
        }
        if (p3.x == p2.x){
            return Common.lineIntersection(p2, p3, p0, p1);
        }

        m2 = (p3.y - p2.y) / (p3.x - p2.y);
        b2 = p2.y - m2 * p2.x;

        m1 = (p1.y - p0.y) / (p1.x - p0.x);
        b1 = p0.y - m1 * p0.x;

        if (m1 == m2){
            return null;
        }

        double x = (b2 - b1) / (m1 - m2);
        return new Coordinate(x, x* m1 + b1);
    }

    public static Polygon convexPolygonalExclude(Polygon space, Polygon convex)
    {
        Coordinate[] spaceCoordinates = space.getCoordinates();
        Coordinate[] convexCoordinates = convex.getCoordinates();
        Coordinate[] finalCoordinates = new Coordinate[space.getCoordinates().length + convex.getCoordinates().length + 1];
        int minDistanceIndex = 0;
        double minDistance = convexCoordinates[0].distance(spaceCoordinates[0]);
        for (int i = 1; i < space.getCoordinates().length - 1; i++){
            double distance = convexCoordinates[i].distance(spaceCoordinates[0]);
            if (distance < minDistance){
                minDistance = distance;
                minDistanceIndex = i;
            }
        }
        for (int i = spaceCoordinates.length - 1; i >= 0; i--) {
            finalCoordinates[i] = spaceCoordinates[i];
        }
        for (int i = 0; i < convexCoordinates.length; i++) {
            finalCoordinates[i + spaceCoordinates.length] = convexCoordinates[minDistanceIndex];
            minDistanceIndex++;
            minDistanceIndex = Common.fixInSize(minDistanceIndex, convexCoordinates.length - 1);
        }
        finalCoordinates[finalCoordinates.length - 1] = spaceCoordinates[spaceCoordinates.length - 1];
        Polygon res = space.getFactory().createPolygon(space.getFactory().createLinearRing(finalCoordinates), null);
        //drawPolygonToPanel(res, debugPain, Color.BLACK, Color.TRANSPARENT);
        //drawPolygonToPanel(convex, debugPain, Color.GREEN, Color.TRANSPARENT);
        return res;
    }

    private static int pointCounter = 0;
    public static void debugPoint(Coordinate point, Paint color){
        Circle circle = new Circle(point.x * debugPain.getWidth(), point.y * debugPain.getHeight(), .5, color);
//        Label label = new Label(new Integer(pointCounter).toString());
//        label.setLayoutY(circle.getCenterY());
//        label.setLayoutX(circle.getCenterX());
        debugPain.getChildren().add(circle);
//        debugPain.getChildren().add(label);
        pointCounter++;
    }

    public static double randomBiLinear(Random random, double radius) {
        return radius * Math.sqrt(random.nextDouble());
    }
}
