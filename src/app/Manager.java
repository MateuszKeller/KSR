package app;

import features.Feature;
import features.NumberFeature;
import metrics.Chebyshev;
import model.Article;
import operations.Classification;

import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification T = new Classification();
        T.fillRepository();
        T.load("keywords_0.5_90");
//        T.generateKeyWords();

//        T.save();


        int most = T.getRepository().findMostWordsAfterStoplist();

        int i = 15;

        Article objT = T.getRepository().getArticles()[i];
        Article l1 = T.getRepository().getLearningArticles()[i];
        Article m1 = T.getRepository().getLearningArticles()[most];
        System.out.println("- Article " + i + ": " + objT);
        System.out.println("--- KeyWords: " + T.getKeywords().length + ": "+ Arrays.toString(T.getKeywords()));

        boolean[] whichFeatures = {true, true, true, true, true, true, true, true, true, true, };

        T.extractFeatures(whichFeatures);

        System.out.println("- Article " + i + ": " + objT);
//        System.out.println("- Article " + i + ": " + l1);
        System.out.println("- Article " + most + ": " + m1);

        T.normalize();

        System.out.println("- Article " + i + ": " + objT);
        System.out.println("- Article " + most + ": " + m1);

//        System.out.println("- Article " + i + ": " + objT);
//
//        for (Article a: T.getRepository().getLearningArticles())
//        {
////            if((double) a.getFeaturesVector().get("Unique_50%").getValue() > 0.5)
//                System.out.println(a);
//
//        }






    }


}
