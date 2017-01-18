package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;
import org.apache.commons.math3.exception.OutOfRangeException;
import sun.plugin.dom.exception.InvalidStateException;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by HP PC on 1/18/2017.
 */
public abstract class RandomNHullGenerator extends RandomPolygonGenerator {
    private int n;
    public RandomNHullGenerator(int n) {
        super(n);
        this.n = n;
    }

    @Override
    public Polygon nextPolygon() {
        Coordinate[][] coordinates = new Coordinate[n * n * n][];
        Polygon convexHull = null;
        do {
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] = new Coordinate[i + 2];
                for (int j = 0; j < i; j++) {
                    coordinates[i][j] = coordinates[i - 1][j];
                }
                coordinates[i][i] = pointGenerator.nextCoordinate();
                coordinates[i][i + 1] = coordinates[i][0];
            }
            convexHull = convexHullFromCoords(coordinates[n * n * n - 1]);
        } while (convexHull.getCoordinates().length < n + 1);
        return findNgonConvexHull(coordinates);
    }

    private Polygon findNgonConvexHull(Coordinate[][] coordinates) {
        return innerFindNgonConvexHull(coordinates, 0, coordinates.length);
    }

    private Polygon innerFindNgonConvexHull(Coordinate[][] coordinates, int start, int end) {
        if (start == end)
            throw new InvalidStateException("This state is invalid.");
        int index = (start + end) / 2;
        Polygon convexHull = convexHullFromCoords(coordinates[index]);
        int count = convexHull.getCoordinates().length - 1;
        if (count == n){
            return convexHull;
        } else {
            if (count > n){
                return innerFindNgonConvexHull(coordinates, start, index);
            } else {
                return innerFindNgonConvexHull(coordinates, index + 1, end);
            }
        }
    }

    private Polygon convexHullFromCoords(Coordinate[] coordinates){
        return Common.convexHull(factory.createPolygon(factory.createLinearRing(coordinates), null));
    }
}
