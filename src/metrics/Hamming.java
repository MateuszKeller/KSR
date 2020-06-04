package metrics;

import features.Feature;

import java.util.Map;

public class Hamming implements Metric {

    public double calcDistance(Map<String, Feature> features1, Map<String, Feature> features2)
    {
        double diff = 0;

        for (Map.Entry<String, Feature> feature_1 : features1.entrySet())
        {
            Feature feature_2 = features2.get(feature_1.getKey());
            if (Math.abs(feature_1.getValue().compare(feature_2)) < 0.0000001)
                diff++;

        }
        return diff;
    }
}
