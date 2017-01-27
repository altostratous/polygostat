package controllers.components.random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by HP PC on 1/24/2017.
 */
public class MultiPolygonalRandomPointGenerator implements RandomPointGenerator{
    private Collection<Polygon> polygons;
    private HashMap<Polygon, PolygonRandomPointGenerator> randomPointGeneratorHashMap;
    private WeighedRandomSelector<Polygon> selector;
    public MultiPolygonalRandomPointGenerator(Collection<Polygon> polygons){
        this.polygons = polygons;
        randomPointGeneratorHashMap = new HashMap<>();
        HashMap<Polygon, Double> weights = new HashMap<>();
        for (Polygon poly :
                polygons) {
            weights.put(poly, poly.getArea());
            randomPointGeneratorHashMap.put(poly, new PolygonRandomPointGenerator(poly));
        }
        selector= new WeighedRandomSelector<>(weights);

    }
    @Override
    public Coordinate nextCoordinate() {
        return randomPointGeneratorHashMap.get(selector.selectNext()).nextCoordinate();
    }
}
