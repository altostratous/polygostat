package controllers.components.random;

import com.vividsolutions.jts.geom.*;
import controllers.components.geomentry.Common;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Created by HP PC on 1/18/2017.
 */
public abstract class RandomNHullGenerator extends RandomPolygonGenerator {
    private int n;
    protected Polygon space;
    private RandomPolygonGenerator randomTriangleGenerator;

    class RandomTriangleGenerator extends RandomPolygonGenerator {
        public RandomTriangleGenerator(Polygon space) {
            super(3);
            this.pointGenerator = new PolygonRandomPointGenerator(space);
        }

        @Override
        public Rectangle2D getBounds() {
            return null;
        }
    }

    public RandomNHullGenerator(int n, Polygon space) {
        super(n);
        this.n = n;
        this.space = space;
        this.randomTriangleGenerator = new RandomTriangleGenerator(space);
    }

    @Override
    public Polygon nextPolygon() {
        Polygon convex = randomTriangleGenerator.nextPolygon();
         while (convex.getCoordinates().length <= n){
            convex = Common.addPointToConvexHull(
                    convex,
                    new PolygonRandomPointGenerator(Common.convexPolygonalExclude(space, convex)).nextCoordinate());
        }
        return convex;
    }

}
//
//    private Polygon findNgonConvexHull(Coordinate[][] coordinates) {
//        return innerFindNgonConvexHull(coordinates, 0, coordinates.length);
//    }
//
//    private Polygon innerFindNgonConvexHull(Coordinate[][] coordinates, int start, int end) {
//        if (start == end)
//            throw new InvalidStateException("This state is invalid.");
//        int index = (start + end) / 2;
//        Polygon convexHull = convexHullFromCoords(coordinates[index]);
//        int count = convexHull.getCoordinates().length - 1;
//        if (count == n){
//            return convexHull;
//        } else {
//            if (count > n){
//                return innerFindNgonConvexHull(coordinates, start, index);
//            } else {
//                return innerFindNgonConvexHull(coordinates, index + 1, end);
//            }
//        }
//    }
//
//    private Polygon convexHullFromCoords(Coordinate[] coordinates){
//        return Common.convexHull(factory.createPolygon(factory.createLinearRing(coordinates), null));
//    }

