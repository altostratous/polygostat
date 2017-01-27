package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;

import java.util.ArrayList;

/**
 * Created by HP PC on 1/18/2017.
 */
public abstract class RandomNGonGenerator extends RandomPolygonGenerator {
    private int n;
    public RandomNGonGenerator(int n) {
        super(n);
        this.n = n;
    }

    @Override
    public Polygon nextPolygon() {
        Polygon convexHull = null;
        ArrayList<Coordinate> coordinateArrayList = new ArrayList<>();
        coordinateArrayList.add(pointGenerator.nextCoordinate());
        coordinateArrayList.add(pointGenerator.nextCoordinate());
        coordinateArrayList.add(coordinateArrayList.get(0));
        Coordinate[] coordinates;
        while (convexHull == null || convexHull.getCoordinates().length < n + 1) {
            coordinateArrayList.add(coordinateArrayList.get(coordinateArrayList.size() - 1));
            Coordinate newPoint = pointGenerator.nextCoordinate();
            if (convexHull != null)
                while (convexHull.contains(factory.createPoint( newPoint))){
                    newPoint = pointGenerator.nextCoordinate();
                }
            coordinateArrayList.set(coordinateArrayList.size() - 2, newPoint);
            coordinates = new Coordinate[coordinateArrayList.size()];
            coordinates = coordinateArrayList.toArray(coordinates);
            Polygon polygon = factory.createPolygon(factory.createLinearRing(coordinates), null);
            convexHull = Common.convexHull(polygon);
        }
        return convexHull;
    }
}
