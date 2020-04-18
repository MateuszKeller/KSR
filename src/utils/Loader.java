package utils;

import model.Article;
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
        int amount = 1;
        List<Article> articles = new ArrayList<>();
        //if(amount < 1 || amount > 22){ System.out.println("ERR - wrong files amount!"); throw new Exception("wrong files amount!"); }

        for(int f = 0; f< amount; f++)
        {
            File file = new File("data/"+filesNames[f]);
//            File file = new File("data/reut2-017.sgm");
            Article[] articlesWithinFile = parseFile(file);
            Collections.addAll(articles, articlesWithinFile);

//                {System.out.println(articlesWithinFile[0].toString());}
//                {System.out.println("----------------------------------------------");}
//                {System.out.println(articlesWithinFile[articlesWithinFile.length-2].toString());}
        }
        return articles.toArray(Article[]::new);
    }

    private static Article[] parseFile(File file) throws IOException
    {
        Scanner scanner = new Scanner(file).useDelimiter("\\A");
        String content;
        if(scanner.hasNext())
            content = scanner.next();
        else content = "";

        content = content.replace("BODY>", "CONTENT>");
        Document document = Jsoup.parse(content);
        Elements reuters = document.getElementsByTag("REUTERS");

        ArrayList<Article> articlesWithinFile = new ArrayList<>();
        for (Element element: reuters)
        {
            Elements title = element.getElementsByTag("TITLE");
            Elements places = element.getElementsByTag("PLACES");

            String body = element.getElementsByTag("CONTENT").text();
            body = body.replace("&\\t", "\t");

            Elements placesElement = places.select("D");
            String[] labels = new String[placesElement.size()];
            for (int i = 0; i < labels.length; i++)
                labels[i] = placesElement.get(i).text();

            boolean matching = matchLabels(true, labels);
            if (matching)
            {
                Article article = new Article(title.text(), labels, body);
                articlesWithinFile.add(article);
            }


        }
//            {System.out.println(file.getName()+ ": " + reuters.size() + "; Good: " + articlesWithinFile.size());}
        return articlesWithinFile.toArray(Article[]::new);
    }

    public static boolean matchLabels(boolean onlyProjectLabelsInPlaces, String[] places)
    {
        String[] labels = { "west-germany", "usa", "france", "uk", "canada", "japan"};
        for (String place : places)
        {
            boolean good = false;
            for (String label : labels)
            {
                if (label.equals(place))
                    good = true;
            }
            if(!onlyProjectLabelsInPlaces)
                return true;
            if (!good)
                return false;
        }
        return true;
    }

}
