package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by HP PC on 1/30/2017.
 */
public class UnitCircleRandomNGonGenerator extends RandomNHullGenerator {
    public UnitCircleRandomNGonGenerator(int n) {
        super(n);

        pointGenerator = new CircleRandomPointGenerator(1, new Coordinate(0, 0));
    }
}
