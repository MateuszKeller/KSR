import java.util.Arrays;

public class Article {

    private String title;
    private String[] places;
    private String body;

    public Article(String title, String[] places, String body) {
        this.title = title;
        this.places = places;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }
    public String[] getPlaces() {
        return places;
    }
    public String getBody() {
        return body;
    }

    public String toString() {
        String ret = "Title: " + title +
                " " + Arrays.toString(places) +
                "\nBody: " + body + '\n';
        return ret;
    }
}
