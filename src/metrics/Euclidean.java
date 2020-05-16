package metrics;

import features.Feature;

import java.util.Map;

public class Euclidean implements Metric{
    
    public double calcDistance(Map<String, Feature> features1, Map<String, Feature> features2)
    {
        double sum = 0;
        for (Map.Entry<String, Feature> feature1 : features2.entrySet())
        {
            Feature feature2 = features2.get(feature1.getKey());
            sum += feature1.getValue().compare(feature2) * feature1.getValue().compare(feature2);
        }

        return Math.sqrt(sum);
    }
}
