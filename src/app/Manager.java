package app;

import features.Feature;
import features.NumberFeature;
import metrics.Chebyshev;
import metrics.Euclidean;
import metrics.Metric;
import metrics.Street;
import model.Article;
import operations.Classification;

import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification classification;
//        Metric[] metrics = {new Street(), new Chebyshev(), new Euclidean()};
        Metric[] metrics = {new Chebyshev()};
        boolean[] whichFeatures = {true, true, true, true, true, true, true, true, true, true};
//        int[] a = {1,2,3,4,5,6,7,10,13,15};
        int[] a = {1, 10};
        int[] percent = {30,50,60,70,90};

        for (Metric metric: metrics)
        {
            for (int i: a)
            {
                for (int p: percent)
                {
                    classification = new Classification(i, p, metric);
                    classification.load("keywords_0.5_" + p);
                    classification.fillRepository();
                    classification.extractFeatures(whichFeatures);
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
