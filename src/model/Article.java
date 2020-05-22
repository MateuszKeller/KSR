package model;

import features.Feature;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Article {

    private String title;
    private String[] places;
    private String body;

    private int wordsAmount;
    private int stopsAmount;

    private String[] wordsVector;
    private Map<String, Feature> featuresVector;

    private String[] firstParagraph;
    private String[] lastParagraph;

    public Article(String title, String[] places, String body)
    {
        this.title = title;
        this.places = places;
        this.body = body;
    }

    public String getTitle() { return title; }
    public String[] getPlaces() { return places; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String[] getWordsVector() { return wordsVector; }
    public void setWordsVector(String[] wordsVector) { this.wordsVector = wordsVector; }
    public int getWordsAmount() { return wordsAmount; }
    public void setWordsAmount() { this.wordsAmount = ArticlesCleaner.split(body).length; }
    public int getStopsAmount() { return stopsAmount; }
    public void setStopsAmount() { this.stopsAmount = wordsAmount-wordsVector.length; }
    public String[] getFirstParagraph() { return firstParagraph; }
    public String[] getLastParagraph() { return lastParagraph; }
    public void setParagraphs() { this.firstParagraph = wordsVector; this.lastParagraph = wordsVector; }
    public void setParagraphs(String[] firstParagraph, String[] lastParagraph)
    {
        this.firstParagraph = firstParagraph;
        this.lastParagraph = lastParagraph;
    }

    public Map<String, Feature> getFeaturesVector() { return featuresVector; }
    public void setFeaturesVector(Map<String, Feature> featuresVector) { this.featuresVector = featuresVector; }

    public boolean checkLabel(String labelToCheck)
    {
//        List<String> labels = Arrays.asList(places);
//        return labels.contains(labelToCheck);

        return labelToCheck.equals(places[0]);
    }

    public String toString()
    {
        String ret = "Title: " + title +
                " " + Arrays.toString(places) +
                "\nBody: " + body +
                "\nWords: " + wordsAmount + "; Stops: " + stopsAmount +
                "\nVector- " + wordsVector.length + " : " + Arrays.toString(wordsVector);

        if (featuresVector!= null)
                ret += "\nFeatures:\n" + printFeatures();

        return ret;
    }

    public String printFeatures()
    {
        String ret = "";
        for (Map.Entry<String, Feature> feature : featuresVector.entrySet())
        {
            if(feature!= null)
            ret += "\t" + feature.getKey() + ": " + feature.getValue() + "\n";
        }

        return ret;
    }

}
