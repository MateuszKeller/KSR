package metrics;

import features.Feature;

import java.util.Map;

public interface Metric {
    double calcDistance(Map<String, Feature> features1, Map<String, Feature> features2);
}
