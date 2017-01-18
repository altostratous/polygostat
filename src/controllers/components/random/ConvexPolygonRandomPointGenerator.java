package controllers.components.random;

import com.vividsolutions.jts.geom.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP PC on 1/18/2017.
 */
public class ConvexPolygonRandomPointGenerator implements RandomPointGenerator {
    private Polygon polygon;
    private ArrayList<Polygon> triangles;
    private GeometryFactory geometryFactory;
    private WeighedRandomSelector triangleSelector;
    private HashMap<Polygon, TriangleRandomPointGenerator> triangleRandomPointGenerators;

    public ConvexPolygonRandomPointGenerator(Polygon polygon) {
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
        Coordinate[] coordinates = polygon.getCoordinates();
        ArrayList<Polygon> triangles = new ArrayList<>();
        Coordinate base = coordinates[0];
        for (int i = 1; i + 1 < coordinates.length; i++) {
            triangles.add(geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[]{base, coordinates[i], coordinates[i + 1], base}), null));
        }
        return triangles;
    }

    @Override
    public Coordinate nextCoordinate() {
        return triangleRandomPointGenerators.get(triangleSelector.selectNext()).nextCoordinate();
    }
}
