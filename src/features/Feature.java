package features;

public interface Feature<T> {

    T getValue();
//    void setValue();
    double compare(Feature<T> feature);
}
