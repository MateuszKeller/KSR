import java.util.Arrays;

public class Article {

    private String title;
    private String[] places;
    private String body;

    private int wordsAmount;
    private int stopsAmount;

    private String[] wordsVector;

    private String[] firstVector;
    private String[] lastVector;

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
    public String[] getFirstVector() { return firstVector; }
    public String[] getLastVector() { return lastVector; }
    public void setParagraphs() { this.firstVector = wordsVector; this.lastVector = wordsVector; }
    public void setParagraphs(String[] firstVector, String[] lastVector)
    {
        this.firstVector = firstVector;
        this.lastVector = lastVector;
    }

    public String toString()
    {
        String ret = "Title: " + title +
                " " + Arrays.toString(places) +
                "\nBody: " + body +
                "\nWords: " + wordsAmount + "; Stops: " + stopsAmount +
                "\nVector: " + Arrays.toString(wordsVector);
        return ret;
    }

}
