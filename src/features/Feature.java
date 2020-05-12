package features;

public interface Feature<T> {

    T getValue();
    double compare(Feature<T> feature);
}
