package features;

import model.Article;

import java.lang.reflect.Array;
import java.util.*;

public class FeatureExtractor {

    private Article article;
    private ArrayList<String> keywords;
    private Map<String, Integer> keywordsMap = new LinkedHashMap<>();
    private Map<String, Feature> featuresMap = new LinkedHashMap<>();

    public FeatureExtractor(Article article, String[] keywords) {
        this.article = article;
        this.keywords = new ArrayList<>(Arrays.asList(keywords));

        setKeywordsMap();
    }

    public Map<String, Feature> getFeatures() {
        return featuresMap;
    }

    public void setKeywordsMap()
    {
        for (String key : keywords)
        {
            int sum = 0;
            for (String word : article.getWordsVector())
            {
                if (word.equals(key))
                    sum++;

            }
            keywordsMap.put(key, sum);
        }
    }

    public int countKeywordsInText(String[] text)
    {
        int ret = 0;
        ArrayList<String> listOfWords = new ArrayList<>(Arrays.asList(text));

        for(String word: listOfWords)
        {
            if(keywords.contains(word))
                ret++;
        }

        return ret;
    }

    // 1. Number of Keywords in first paragraph
    public void keysInFirstPar()
    {
        int number = countKeywordsInText(article.getFirstParagraph());
        featuresMap.put("First_paragraph", new NumberFeature(number));
    }

    // 2. Number of Keywords in last paragraph
    public void keysInLastPar()
    {
        int number = countKeywordsInText(article.getFirstParagraph());
        featuresMap.put("Last_paragraph", new NumberFeature(number));
    }

    // 3. Number of Keywords in first 20 % of text
    public void keysIn20()
    {
        int number = countKeywordsInText(article.getFirstParagraph());
        featuresMap.put("20%", new NumberFeature(number));
    }

    // 4. Number of unique Keywords in first 50 % of text
    public void uniqKeysIn50()
    {
        int number = countKeywordsInText(article.getFirstParagraph());
        featuresMap.put("Unique50%", new NumberFeature(number));
    }

    // 5. Density of Keywords
    public void keysDensity()
    {
        double density = keywordsMap.values().stream().reduce(0, Integer::sum);
        density /= article.getWordsAmount();
        featuresMap.put("Density", new NumberFeature(density));
    }

    // 6. First Keyword
    public void firstKey()
    {
        ArrayList<String> listOfWords = new ArrayList<>(Arrays.asList(article.getWordsVector()));
        for(String word: listOfWords)
        {
            if(keywords.contains(word))
            {
                featuresMap.put("First", new TextFeature(word));
                return;
            }
        }

        featuresMap.put("First", new TextFeature(null));

    }

    // 7. Most common Keyword in whole text
    public void mostCommonKey()
    {
        int amount = 0;
        String mostCommon = null;


        for (Map.Entry<String, Integer> keyword : keywordsMap.entrySet())
        {
            if (keyword.getValue() > amount)
            {
                mostCommon = keyword.getKey();
                amount = keyword.getValue();
            }
        }

        featuresMap.put("Most_Common", new TextFeature(mostCommon));

    }

    // 8. Amount of words in text (after stop list)
    public void lengthAfterStoplist()
    {
        featuresMap.put("Length", new NumberFeature(article.getWordsVector().length));
    }

    // 9. Deleted words to text length ratio
    public void deletedToAllRatio()
    {
        double number = article.getStopsAmount();
        featuresMap.put("S/A_Ratio", new NumberFeature(number/ article.getWordsAmount()));
    }

    // 10. Number of keywords to text length ratio
    public void keysToAllRatio()
    {
        double number =  countKeywordsInText(article.getWordsVector());
        featuresMap.put("K/A_Ratio", new NumberFeature(number/ article.getWordsAmount()));
    }

}
