package model;

import metrics.Metric;
import metrics.Street;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void generateXLS(String[] labels, int k, Metric metric, double significance, int ratio, boolean[] features)
    {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        String[] metricName = metric.getClass().getName().split("\\.");
        String[] featuresNames = {"First_paragraph", "Last_paragraph", "First_20%", "Unique_50%", "Density", "Length", "S/A_Ratio", "K/L_Ratio", "First", "Most_Common"};

        Row row = sheet.createRow(1); //-
            Cell cell = row.createCell(1);
            cell.setCellValue("Settings:");

            cell = row.createCell(5);
            cell.setCellValue("Results:");

        row = sheet.createRow(2); //-
            cell = row.createCell(1);
            cell.setCellValue("K:");
            cell = row.createCell(2);
            cell.setCellValue(k);

            cell = row.createCell(5);
            cell.setCellValue("Correct:");
            cell = row.createCell(6);
            cell.setCellValue(trueAll);

        row = sheet.createRow(3); //-
            cell = row.createCell(1);
            cell.setCellValue("Metric:");
            cell = row.createCell(2);
            cell.setCellValue(metricName[1]);

            cell = row.createCell(5);
            cell.setCellValue("Incorrect:");
            cell = row.createCell(6);
            cell.setCellValue(falseAll);

        row = sheet.createRow(4); //-
            cell = row.createCell(1);
            cell.setCellValue("Signif:");
            cell = row.createCell(2);
            cell.setCellValue(significance);

            cell = row.createCell(5);
            cell.setCellValue("Accuracy:");
            cell = row.createCell(6);
            cell.setCellValue(accuracy);

        row = sheet.createRow(5); //-
            cell = row.createCell(1);
            cell.setCellValue("Ratio:");
            cell = row.createCell(2);
            cell.setCellValue(ratio);


        row = sheet.createRow(7); //-
            cell = row.createCell(1);
            cell.setCellValue("Features:");
            for (int i = 0; i < features.length; i++)
            {
                row = sheet.createRow(8+i);
                cell = row.createCell(1);
                cell.setCellValue(featuresNames[i]);
                cell = row.createCell(2);
                cell.setCellValue(features[i]);
            }

            row = sheet.getRow(7);
            cell = row.createCell(6);
            cell.setCellValue("Precision");
            cell = row.createCell(7);
            cell.setCellValue("Recall");

            for (int i = 0; i < labels.length; i++)
            {
                row = sheet.getRow(8+i);
                cell = row.createCell(5);
                cell.setCellValue(labels[i]);
                cell = row.createCell(6);
                cell.setCellValue(precision.get(labels[i]));
                cell = row.createCell(7);
                cell.setCellValue(recall.get(labels[i]));

            }

            row = sheet.getRow(7);
            cell = row.createCell(9);
            cell.setCellValue("Matrix");
            int j = 10;
            for (String label: labels)
            {
                cell = row.createCell(j);
                cell.setCellValue(label);
                j++;
            }

            int r = 8;

            for (String label: labels)
            {
                j = 9;
                row = sheet.getRow(r);
                cell = row.createCell(j);
                cell.setCellValue(label);
                j++;

                for (String l: labels)
                {
                    Integer value = confusionMatrix.get(label).get(l);
                    cell = row.createCell(j);
                    cell.setCellValue(value);
                    j++;
                }
                r++;
            }



        for (int i = 0; i < 12; i++)
            sheet.autoSizeColumn(i);



//        Row row = sheet.createRow(1);
//        Cell cell = row.createCell(2);
//        cell.setCellValue("Results");
//
//        row = sheet.getRow(1);
//        cell = row.createCell(5);
//        cell.setCellValue("Results2");

        try
        {

            String fileName = "Report_" + k + "_" + metricName[1] + "_" + ratio + "_" + dtf.format(now) + ".xlsx";
            FileOutputStream out = new FileOutputStream(fileName);
            workbook.write(out);
            out.close();

            workbook.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

}
