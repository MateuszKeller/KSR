package features;

import operations.NGrams;

public class TextFeature implements Feature<String> {

    private String value;

    public TextFeature(String value) { this.value = value; }

    public String getValue() {
        return value;
    }
//    public void setValue(String value) { this.value = value;}

    public double compare(Feature<String> feature) {
        return NGrams.similarity(getValue(), feature.getValue());
    }
    public String toString() { return "value: " + value; }
}
