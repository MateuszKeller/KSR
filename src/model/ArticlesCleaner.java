package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Article;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class ArticlesCleaner {

    private static String[] stopWords;

    public ArticlesCleaner() throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("data/stoplist.txt"));
        List<String> words = new ArrayList<>();
        while (scanner.hasNextLine())
            words.add(scanner.nextLine());

        scanner.close();
        stopWords = words.toArray(String[]::new);
    }

    public String[] getStopWords() {
        return stopWords;
    }
    public String toString() { return "Stop words: " + Arrays.toString(stopWords); }

    public static String removeChars(Article article) { return article.getBody().replaceAll("[^A-Za-z\\s+]", "").toLowerCase(); }
    public static String[] split(String text) { return text.split("\\s+"); }

    public static String stem(String word)
    {
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public static void stem(String[] vector)
    {
        for (int i = 0; i < vector.length; i++)
            vector[i] = stem(vector[i]);
    }

    public String[] filter(String[] body)
    {
        ArrayList<String> ret = new ArrayList<>();
        List<String> stops = Arrays.asList(stopWords);

        for (String word: body)
        {
            if(!stops.contains(word))
                ret.add(word);
        }
        return ret.toArray(String[]::new);
    }


}
