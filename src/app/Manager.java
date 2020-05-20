package app;

import features.Feature;
import features.NumberFeature;
import metrics.Chebyshev;
import metrics.Metric;
import metrics.Street;
import model.Article;
import operations.Classification;

import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification T = new Classification();
        T.fillRepository();
        T.load("keywords_0.5_60");
//        T.generateKeyWords();
        System.out.println("--- KeyWords: " + T.getKeywords().length + ": "+ Arrays.toString(T.getKeywords()));
//        T.save();


        int most = T.getRepository().findMostWordsAfterStoplist();
        int i = 15;

        Article objT = T.getRepository().getArticles()[i];
        Article m1 = T.getRepository().getArticles()[most];

        boolean[] whichFeatures = {true, true, true, true, true, true, true, true, true, true, };

        T.extractFeatures(whichFeatures);

        System.out.println("- Article " + i + ": " + objT);
//        System.out.println("- Article " + most + ": " + m1);

        T.normalize();

        System.out.println("- Article " + i + ": " + objT);
//        System.out.println("- Article " + most + ": " + m1);

        T.classify();




    }


}
