package operations;

import features.Feature;
import features.FeatureExtractor;
import features.NumberFeature;
import features.TextFeature;
import metrics.Metric;
import metrics.Street;
import model.Article;
import model.ArticlesRepository;
import model.Report;
import org.apache.poi.util.LittleEndianByteArrayInputStream;
import utils.Loader;

import java.util.*;

public class Classification {

    private ArticlesRepository repository;
    private static String[] labels = { "west-germany", "usa", "france", "uk", "canada", "japan" };
    private String[] keywords;
    private int k;// = 5;
    private Metric metric;// = new Street();
    private double significance = 0.5;
    private int learningPercent;// = 95;
    private boolean[] features;

    public Classification(int k, int learningPercent, Metric metric)
    {
        this.k = k;
        this.learningPercent = learningPercent;
        this.metric = metric;
    }

    public ArticlesRepository getRepository() { return repository; }
    public String[] getKeywords() { return keywords; }

    public void fillRepository() throws Exception
    {
        repository = new ArticlesRepository(learningPercent);
        System.out.println(repository.numbersInSets(labels));
    }

    public void generateKeyWords()
    {
        keywords = KeyWords.generate(repository.getLearningArticles(), significance);
    }

    public void extractFeatures(boolean[] whichFeatures)
    {
        System.out.println("Extraction");
        features = whichFeatures;
        for(Article article: repository.getArticles())
        {
            FeatureExtractor extractor = new FeatureExtractor(article, keywords);
            extractor.extract(features);
            article.setFeaturesVector(extractor.getFeatures());
        }
    }

    public void normalize()
    {
        System.out.println("Normalisation");
        Map<String, Double[]> minMax = findMinMax();
        for (Article article: repository.getArticles())
        {
            Map<String, Feature> features = article.getFeaturesVector();
            for(Map.Entry<String, Double[]> entry: minMax.entrySet())
            {
                double value = (double)features.get(entry.getKey()).getValue();
                double min = entry.getValue()[0];
                double max = entry.getValue()[1];
                value = (value - min) / max;
                features.put(entry.getKey(), new NumberFeature(value));
            }

            article.setFeaturesVector(features);
        }
    }

    private Map<String, Double[]> findMinMax()
    {
        Map<String, Double[]> ret = new LinkedHashMap<>();
        for(Map.Entry<String, Feature> entry: repository.getArticles()[0].getFeaturesVector().entrySet())
        {
            if (entry.getValue().getClass().equals(TextFeature.class)) continue;
            String key = entry.getKey();
            Double[] minMax = {1.d, 0.d};

            for (Article article: repository.getArticles())
            {
                double value = (double)article.getFeaturesVector().get(key).getValue();
                if(minMax[0] > value) minMax[0] = value;
                if(minMax[1] < value) minMax[1] = value;
            }
            minMax[1] = minMax[1]-minMax[0];
            ret.put(key, minMax);

        }
        return ret;
    }

    public void classify()
    {
        System.out.println("Classification");
        Report report = new Report(labels);

        for (Article test: repository.getTestingArticles())
        {
            Article[] nearestNeighbours = kNN(test);
            String supposedLabel = selectLabel(nearestNeighbours);

            if(test.checkLabel(supposedLabel))
                report.addTrue();
            else
                report.addFalse();

            if(test.getPlaces().length != 0)
                report.addToMatrix(test.getPlaces()[0], supposedLabel);
            else
                System.out.println(test);
        }

        System.out.println("Statistics");
        report.setStatistics();
        report.generateXLS(labels, k, metric, significance, learningPercent, features);

    }

    public Article[] kNN(Article articleForTesting)
    {
        Article[] ret = new Article[k];

        Map<Article, Double> distances = new LinkedHashMap <>();
        for (Article article: repository.getLearningArticles())
        {
            double d = metric.calcDistance(articleForTesting.getFeaturesVector(), article.getFeaturesVector());
            distances.put(article, d);
        }

        Map<Article, Double> sorted = sortByDistance(distances);
        int i = 0;
        for (Map.Entry<Article, Double> entry: sorted.entrySet())
        {
            if(i == k) return ret;

            ret[i] = entry.getKey();
            i++;
        }

        return ret;
    }

    private LinkedHashMap<Article, Double> sortByDistance(Map<Article, Double> mapToSort)
    {
        LinkedHashMap<Article, Double> ret = new LinkedHashMap<>();
        mapToSort.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> ret.put(x.getKey(), x.getValue()));
        return ret;
    }

    private String selectLabel(Article[] nearestArticles)
    {
        String ret = "";
        Map<String, Integer> labelsFound = new HashMap<>();
        for (Article article: nearestArticles)
        {
            for (String place : article.getPlaces())
            {
                if (labelsFound.containsKey(place)) labelsFound.put(place, labelsFound.get(place)+1);
                else labelsFound.put(place, 1);
            }
        }
        ret = Collections.max(labelsFound.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        return ret;
    }

    public void save()
    {
        String fileName = "keywords_" + significance + "_" + learningPercent;
        Loader.saveKeyWords(fileName, keywords);
    }

    public void load(String fileName)
    {
        keywords = Loader.loadKeyWords(fileName);
        String[] name = fileName.split("_");
        significance = Double.parseDouble(name[1]);
        learningPercent = Integer.parseInt(name[2]);

    }




}
