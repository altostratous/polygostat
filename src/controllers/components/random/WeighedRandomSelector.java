package controllers.components.random;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.*;

/**
 * Created by HP PC on 1/18/2017.
 */
public class WeighedRandomSelector<T> {
    private ArrayList<Double> intervals;
    private Random random;
    private HashMap<Double, T> intervalSelectionMap;
    public WeighedRandomSelector(Map<T, Double> selections){
        random = new Random(System.currentTimeMillis());
        double totalWeight = 0;
        for (T key :
                selections.keySet()) {
            totalWeight += selections.get(key);
        }
        intervals = new ArrayList<>();
        intervalSelectionMap = new HashMap<>();
        double start = 0;
        for (T key :
                selections.keySet()) {
            intervals.add(start);
            intervalSelectionMap.put(start, key);
            start += selections.get(key)/totalWeight;
        }
    }
    public T selectNext(){
        return intervalSelectionMap.get(findInterval(random.nextDouble()));
    }

    private Double findInterval(double v) {
        return innerFindInterval(0, intervals.size(), v);
    }

    private Double innerFindInterval(int start, int end, double v) {
        if (start == end)
            throw new OutOfRangeException(v, 0d, 1d);
        int index = (start + end) / 2;
        if (intervals.get(index) <= v){
            if (index + 1 < intervals.size()){
                if (intervals.get(index+ 1) > v){
                    return intervals.get(index);
                } else {
                    return innerFindInterval(index + 1, end, v);
                }
            } else
            {
                return intervals.get(index);
            }
        }
        else {
            return innerFindInterval(start, index, v);
        }
    }
}
