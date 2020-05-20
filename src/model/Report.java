package model;

import metrics.Metric;


import java.util.HashMap;
import java.util.Map;

/*

    TP – True Positive – liczba obserwacji poprawnie zaklasyfikowanych do klasy pozytywnej.
        Przykład: nasz model się nie pomylił i klienci, którzy zrezygnowali z oferty firmy zostali przypisani do klasy „nielojalni”
    TN – True Negative – liczba obserwacji poprawnie zaklasyfikowanych do klasy negatywnej.
        Przykład: nasz model się nie pomylił i klienci, którzy dalej korzystają z oferty firmy zostali przypisani do klasy „lojalni”
    FP – False Positive – liczba obserwacji zaklasyfikowanych do klasy pozytywnej podczas, gdy w rzeczywistości pochodzą z klasy negatywnej.
        Przykład: niestety nasz model nie jest idealny i popełnił błąd – klienci, którzy nadal korzystają z oferty firmy zostali przypisani do klasy „nielojalni”
    FN – False Negative – liczba obserwacji zaklasyfikowanych do klasy negatywnej podczas, gdy w rzeczywistości pochodzą z klasy pozytywnej.
        Przykład: niestety nasz model nie jest idealny i popełnił błąd – klienci, którzy zrezygnowali z oferty firmy zostali przypisani do klasy „lojalni”.

    ACCURACY = (TP + TN) / (TP + TN + FP + FN)
    PRECISION = TP / (TP + FP)
    RECALL = TP / (TP + FN)

 */

public class Report {

    private Map<String, Map<String, Integer>> confusionMatrix;
    private int trueAll;
    private int falseAll;

    private double accuracy;
    private Map<String, Double> precision;
    private Map<String, Double> recall;
    
    private String[] labels;

    public Report(String[] labels)
    {
        trueAll = 0;
        falseAll = 0;
        confusionMatrix = new HashMap<>();
        precision = new HashMap<>();
        recall = new HashMap<>();
        this.labels = labels;

        createMatrix();
    }

    private void createMatrix()
    {
        for (String label: labels)
        {
            confusionMatrix.put(label, new HashMap<>());

            for (String l: labels)
                confusionMatrix.get(label).put(l, 0);
        }
    }

    public void addToMatrix(String realLabel, String predictedLabel)
    {
        int value = confusionMatrix.get(realLabel).get(predictedLabel);
        confusionMatrix.get(realLabel).put(predictedLabel, value+1);
    }

    public void addTrue() { trueAll++; }
    public void addFalse() { falseAll++; }

    public void setStatistics()
    {
        accuracy = trueAll/ (double) (trueAll + falseAll);
        for (String label: labels)
        {
            precision.put(label, calcPrecision(label));
            recall.put(label, calcRecall(label));
        }
    }

    private double calcPrecision(String labelToCalc)
    {
        int tp = confusionMatrix.get(labelToCalc).get(labelToCalc);
        int fp = 0;
        if(tp == 0) return 0.d;
        
        for (String realLabel: labels)
            if(!realLabel.equals(labelToCalc))
                fp += confusionMatrix.get(realLabel).get(labelToCalc);

        return tp / (double) (tp + fp);
    }

    private double calcRecall(String labelToCalc)
    {
        int tp = confusionMatrix.get(labelToCalc).get(labelToCalc);
        int fn = 0;
        if(tp == 0) return 0.d;

        for (String predictedLabel: labels)
            if(!predictedLabel.equals(labelToCalc))
                fn += confusionMatrix.get(labelToCalc).values().stream().reduce(0, Integer::sum) - confusionMatrix.get(labelToCalc).get(labelToCalc);

        return tp / (double) (tp + fn);

    }

    public void generateXLS(String[] labels, int k, Metric metric, double significance, int ratio)
    {





    }


}
