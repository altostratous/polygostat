package controllers.components.random;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.*;
import controllers.components.geomentry.Common;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP PC on 1/18/2017.
 */
public class PolygonRandomPointGenerator implements RandomPointGenerator {
    private Polygon polygon;
    private ArrayList<Polygon> triangles;
    private GeometryFactory geometryFactory;
    private WeighedRandomSelector triangleSelector;
    private HashMap<Polygon, TriangleRandomPointGenerator> triangleRandomPointGenerators;

    public PolygonRandomPointGenerator(Polygon polygon) {
        geometryFactory = polygon.getFactory();
        this.polygon = polygon;
        triangles = explodeToTriangles(polygon);
        HashMap<Polygon, Double> areas = new HashMap<>();
        triangleRandomPointGenerators = new HashMap<>();
        for (Polygon triangle :
                triangles) {
            areas.put(triangle, triangle.getArea());
            triangleRandomPointGenerators.put(triangle, new TriangleRandomPointGenerator(triangle));
        }
        triangleSelector = new WeighedRandomSelector(areas);
    }

    private ArrayList<Polygon> explodeToTriangles(Polygon polygon) {
        Coordinate[] allCoordinates = polygon.getCoordinates();
        ArrayList<Polygon> result = new ArrayList<>();
        if (Common.convexHull(polygon).getCoordinates().length == allCoordinates.length){
            result.addAll(explodeConvexToTriangles(polygon));
            return result;
        }
        Coordinate[] coordinates = new Coordinate[allCoordinates.length - 1];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = allCoordinates[i];
        }
        int last;
        int next = 0;
        int current;
        for (current = 0; current < coordinates.length; current++){
            last = Common.fixInSize((current - 1), coordinates.length);
            next = Common.fixInSize((current + 1), coordinates.length);
            if (0 < Angle.angleBetweenOriented(coordinates[last], coordinates[current], coordinates[next])){
                break;
            }

        }
        int angleStart = next;
        next++;
        next = Common.fixInSize(next, coordinates.length);
        int angularNearestNeighbourIndex = next;
        double minAngle = Angle.angleBetweenOriented(coordinates[angleStart], coordinates[current], coordinates[next]);
        for (int i = 0; i < coordinates.length - 3; i++){
            next++;
            next = Common.fixInSize(next, coordinates.length);
            double angleBetweenOriented = Angle.angleBetweenOriented(coordinates[angleStart], coordinates[current], coordinates[next]);
            if (angleBetweenOriented < minAngle && angleBetweenOriented >= 0)
            {
                minAngle = angleBetweenOriented;
                angularNearestNeighbourIndex = next;
            }
        }
        Coordinate[] firstPolyCoords = new Coordinate[Common.fixInSize((angularNearestNeighbourIndex - current), coordinates.length) + 2];
        Coordinate[] secondPolyCoords = new Coordinate[Common.fixInSize((current - angularNearestNeighbourIndex), coordinates.length) + 2];
        for (int i = 0; i < firstPolyCoords.length - 1; i++){
            firstPolyCoords[i] = coordinates[Common.fixInSize((current + i), coordinates.length)];
        }
        for (int i = 0; i < secondPolyCoords.length - 1; i++){
            secondPolyCoords[i] = coordinates[Common.fixInSize((angularNearestNeighbourIndex + i), coordinates.length)];
        }
        firstPolyCoords[firstPolyCoords.length - 1] = firstPolyCoords[0];
        secondPolyCoords[secondPolyCoords.length - 1] = secondPolyCoords[0];
        result.addAll(explodeToTriangles(geometryFactory.createPolygon(geometryFactory.createLinearRing(firstPolyCoords), null)));
        result.addAll(explodeToTriangles(geometryFactory.createPolygon(geometryFactory.createLinearRing(secondPolyCoords), null)));
        return result;
    }

    private ArrayList<Polygon> explodeConvexToTriangles(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();
        ArrayList<Polygon> triangles = new ArrayList<>();
        Coordinate base = coordinates[0];
        for (int i = 1; i + 1 < coordinates.length - 1; i++) {
            triangles.add(geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[]{base, coordinates[i], coordinates[i + 1], base}), null));
        }
        return triangles;
    }

    @Override
    public Coordinate nextCoordinate() {
        return triangleRandomPointGenerators.get(triangleSelector.selectNext()).nextCoordinate();
    }

    public void drawToPanel(Pane mainPain) {
        for (Polygon triangle :
                triangles) {
            Common.drawPolygonToPanel(triangle, mainPain, Color.RED, Color.TRANSPARENT);
        }
    }
}
