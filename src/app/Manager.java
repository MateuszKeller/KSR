package app;

import features.Feature;
import features.NumberFeature;
import metrics.*;
import model.Article;
import operations.Classification;
import operations.NGrams;

import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification classification;
//        Metric[] metrics = {new Street(), new Chebyshev(), new Euclidean(), new Hamming()};
        Metric[] metrics = {new Hamming()};
        boolean[] whichFeatures1 = {true, true, true, true, true, true, true, true, false, false};
        boolean[] whichFeatures2 = {true, true, true, true, true, false, false, false, true, true};
        boolean[] whichFeatures3 = {false, false, false, false, true, true, true, true, true, true};
        boolean[] whichFeatures4 = {false, false, true, true, true, false, false, false, false, true};
        boolean[] all = {true, true, true, true, true, true, true, true, true, true};
//        int[] k = {1,2,3,4,5,6,7,10,13,15};
        int[] k = {2};
        int[] percent = {50}; //50, 60, 70, 90
        NGrams.setN(3);

        for (Metric metric: metrics)
        {
            for (int i: k)
            {
                for (int p: percent)
                {
//                    NGrams.setN(p);
                    classification = new Classification(i, p, metric);
                    classification.load("keywords_0.5_" + 50);
                    classification.fillRepository();
                    classification.extractFeatures(whichFeatures4);
                    classification.normalize();
                    classification.classify();

                    System.out.println("END " + p + " " + i);
                }


            }
        }

        /*
        Classification T = new Classification(1, 50, metric);
        T.load("keywords_0.5_50");
        T.fillRepository();

//        T.generateKeyWords();
        System.out.println("--- KeyWords: " + T.getKeywords().length + ": "+ Arrays.toString(T.getKeywords()));
//        T.save();


        Article objT = T.getRepository().getArticles()[i];
        Article m1 = T.getRepository().getArticles()[most];


        T.extractFeatures(whichFeatures);

//        System.out.println("- Article " + i + ": " + objT);
//        System.out.println("- Article " + most + ": " + m1);

        T.normalize();

//        System.out.println("- Article " + i + ": " + objT);
//        System.out.println("- Article " + most + ": " + m1);

        T.classify();

         */
        System.out.println("THE  END");






    }


}
