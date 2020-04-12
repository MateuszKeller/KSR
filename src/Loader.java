import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Loader {

    private static String[] filesNames = {"reut2-000.sgm", "reut2-001.sgm", "reut2-002.sgm", "reut2-003.sgm","reut2-004.sgm","reut2-005.sgm",
            "reut2-006.sgm", "reut2-007.sgm", "reut2-008.sgm", "reut2-009.sgm","reut2-010.sgm","reut2-011.sgm","reut2-012.sgm","reut2-013.sgm",
            "reut2-014.sgm", "reut2-015.sgm", "reut2-016.sgm", "reut2-017.sgm","reut2-018.sgm","reut2-019.sgm","reut2-020.sgm","reut2-021.sgm"};

    public static Article[]  loadFiles() throws Exception
    {
        int amount = 22;
        List<Article> articles = new ArrayList<>();
        //if(amount < 1 || amount > 22){ System.out.println("ERR - wrong files amount!"); throw new Exception("wrong files amount!"); }

        for(int f = 0; f< amount; f++)
        {
            File file = new File("data/"+filesNames[f]);
            Article[] articlesWithinFile = parseFile(file);
            Collections.addAll(articles, articlesWithinFile);

                {System.out.println(articlesWithinFile[articlesWithinFile.length-1].toString());}
        }
        return articles.toArray(new Article[0]);
    }

    private static Article[] parseFile(File file) throws IOException {
        Scanner scanner = new Scanner(file).useDelimiter("\\A");
        String content;
        if(scanner.hasNext())
            content = scanner.next();
        else content = "";

        content = content.replace("BODY>", "CONTENT>");
        Document document = Jsoup.parse(content);
        Elements reuters = document.getElementsByTag("REUTERS");

            {System.out.println(file.getName()+ ": " + reuters.size());}

        Article[] articlesWithinFile = new Article[reuters.size()];

        for (int e = 0; e < articlesWithinFile.length; e++)
        {
            Element element = reuters.get(e);

            Elements title = element.getElementsByTag("TITLE");
            Elements places = element.getElementsByTag("PLACES");
            Elements body = element.getElementsByTag("CONTENT");

            Elements places_items = places.select("D");
            String[] places_names = new String[places_items.size()];
            for (int i = 0; i < places_names.length; i++)
                places_names[i] = places_items.get(i).text();

            Article article = new Article(title.text(), places_names, body.text());
            articlesWithinFile[e] = article;
        }

        return articlesWithinFile;
    }

}
