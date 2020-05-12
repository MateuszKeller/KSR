package features;

public class NumberFeature implements  Feature<Double> {

    private double value;

    NumberFeature(double value) { this.value = value; }

    public Double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public double compare(Feature<Double> feature) { return getValue() - feature.getValue(); }
}
