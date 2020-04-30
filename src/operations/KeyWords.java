package operations;

import model.Article;

import java.util.ArrayList;
import java.util.List;

public class KeyWords {

    private static boolean debug = false;

    public static String[] generate(Article[] articles, double significance)
    {
            long pastTime = System.currentTimeMillis();
            int i =0;

        List<String> keyWords = new ArrayList<>();
        for (Article article : articles)
        {
                int indexInVector = 0;

            String[] words = article.getWordsVector();
            for (String word : words)
            {
                if (!keyWords.contains(word) && word.length() > 2)
                {
                    if (calcTFIF(articles, words, word) > significance)
                    {
                        keyWords.add(word);

                            if(debug)System.out.println("KEYWORD: " + word.toUpperCase() + " in " + article.getTitle() + " [" + indexInVector + "]");
                    }
                }
                    if(debug)indexInVector++;
            }

            if(debug)
            {
                long time = System.currentTimeMillis();
                if(time > (pastTime+ 60*1000))
                {
                    System.out.println(i);
                    pastTime = System.currentTimeMillis();
                }
                i++;
            }
        }
        
        return keyWords.toArray(String[]::new);
    }

    private static double termFrequency(String[] wordsVector, String term)
    {
        int count = 0;
        if (wordsVector.length == 0)
            return 0;

        for (String word : wordsVector)
        {
            if (word.equalsIgnoreCase(term))
                count++;
        }

        return count / (double)wordsVector.length;
    }

    private static double inverseFrequency(Article[] articles, String term)
    {
        double count = 0;
        for (Article article : articles)
        {
            for (String word : article.getWordsVector())
            {
                if (word.equalsIgnoreCase(term))
                {
                    count++;
                    break;
                }
            }
        }
        if (count == 0)
            return 0;

        return Math.log(articles.length / count);
    }

    private static double calcTFIF(Article[] articles, String[] wordsVector, String term)
    {
        return termFrequency(wordsVector, term) * inverseFrequency(articles, term);
    }
}
