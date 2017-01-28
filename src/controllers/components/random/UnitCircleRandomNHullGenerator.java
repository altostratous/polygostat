package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import javafx.geometry.Rectangle2D;

/**
 * Created by HP PC on 1/18/2017.
 */
public class UnitCircleRandomNHullGenerator extends RandomNGonGenerator {
    public UnitCircleRandomNHullGenerator(int n) {
        super(n);
        this.pointGenerator = new CircleRandomPointGenerator(1, new Coordinate(0,0));
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0, 0, 1, 1);
    }
}
