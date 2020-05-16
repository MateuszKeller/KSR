package features;

import model.Article;
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
    public void setFeatures(Map<String, Feature> features) {
        featuresMap = features;
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

    public void extract(boolean[] whichFeatures)
    {
        if(whichFeatures[0]) keysInFirstPar();
        if(whichFeatures[1]) keysInLastPar();
        if(whichFeatures[2]) keysIn20();
        if(whichFeatures[3]) uniqKeysIn50();
        if(whichFeatures[4]) keysDensity();
        if(whichFeatures[5]) lengthAfterStoplist();
        if(whichFeatures[6]) deletedToAllRatio();
        if(whichFeatures[7]) keysToLeftRatio();
        if(whichFeatures[8]) firstKey();
        if(whichFeatures[9]) mostCommonKey();

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
        featuresMap.put("Unique_50%", new NumberFeature(number));
    }

    // 5. Density of Keywords
    public void keysDensity()
    {
        double density = keywordsMap.values().stream().reduce(0, Integer::sum);
        density /= article.getWordsAmount();
        featuresMap.put("Density", new NumberFeature(density));
    }

    // 6. Amount of words in text (after stop list)
    public void lengthAfterStoplist()
    {
        featuresMap.put("Length", new NumberFeature(article.getWordsVector().length));
    }

    // 7. Deleted words to text length ratio
    public void deletedToAllRatio()
    {
        double number = article.getStopsAmount();
        featuresMap.put("S/A_Ratio", new NumberFeature(number/ article.getWordsAmount()));
    }

    // 8. Number of keywords to number of words left after stoplist ratio
    public void keysToLeftRatio()
    {
        double number =  countKeywordsInText(article.getWordsVector());
        featuresMap.put("K/L_Ratio", new NumberFeature(number/ article.getWordsVector().length));
    }

    // 9. First Keyword
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

    // 10. Most common Keyword in whole text
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

    public void normalize() {
        double sum = 0, mean = 0;
        int count = 0;
        for (Feature feature : featuresMap.values()) {
            if (feature.getClass().equals(NumberFeature.class)) {
                NumberFeature numberFeature = (NumberFeature) feature;
                sum += numberFeature.getValue();
                count++;
            }
        }
        if (count > 0) {
            mean = sum / (double)count;
        }

        double dev_sum = 0, deviation = 0;

        for (Feature feature : featuresMap.values()) {
            if (feature.getClass().equals(NumberFeature.class)) {
                NumberFeature numberFeature = (NumberFeature) feature;
                double diff = (numberFeature.getValue() - mean);
                dev_sum += diff * diff;
            }
        }

        if (count > 0) {
            deviation = Math.sqrt(dev_sum / (double) count);
        }

        for (Feature feature : featuresMap.values()) {
            if (feature.getClass().equals(NumberFeature.class)) {
                NumberFeature numberFeature = (NumberFeature) feature;
                double normalized = (numberFeature.getValue() - mean) / deviation;
                numberFeature.setValue(normalized);
            }
        }
    }

}
