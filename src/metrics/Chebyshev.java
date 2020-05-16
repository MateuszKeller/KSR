package metrics;

import features.Feature;

import java.util.Map;

public class Chebyshev implements Metric{

    public double calcDistance(Map<String, Feature> features1, Map<String, Feature> features2)
    {
        double max = 0;

        for (Map.Entry<String, Feature> feature_1 : features1.entrySet())
        {
            Feature feature_2 = features2.get(feature_1.getKey());
            double diff = Math.abs(feature_1.getValue().compare(feature_2));
            if (diff > max)
                max = diff;

        }
        return max;
    }
}
