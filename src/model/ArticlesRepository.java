package model;

import utils.Loader;

public class ArticlesRepository {

    private Article[] articles;
    private Article[] learningArticles;
    private Article[] testingArticles;

    private ArticlesCleaner cleaner = new ArticlesCleaner();
    
    public ArticlesRepository(int learningPercent) throws Exception
    {
        articles = Loader.loadFiles();
        filter();
        splitSets((float)learningPercent/100);

            System.out.println(articles.length + " articles.\n" + learningArticles.length + " learning.\n" + testingArticles.length + " testing:");

    }

    public Article[] getArticles() { return articles; }
    public Article[] getLearningArticles() { return learningArticles; }
    public Article[] getTestingArticles() { return testingArticles; }

    private void removeChars(Article article)
    {
            article.setBody(ArticlesCleaner.removeChars(article));
            article.setWordsAmount();
    }

    private void filter()
    {
        for(Article article: articles)
        {
            removeChars(article);
            stopList(article);
            paragraphication(article);
            stemize(article);
        }
    }

    private void stopList(Article article)
    {
        article.setWordsVector(cleaner.filter(ArticlesCleaner.split(article.getBody())));
        article.setStopsAmount();
    }

    private void paragraphication(Article article)
    {
        String[] paragraphs = article.getBody().split("\t");
        if(paragraphs.length != 1)
            article.setParagraphs(cleaner.filter(ArticlesCleaner.split(paragraphs[0])), cleaner.filter(ArticlesCleaner.split(paragraphs[paragraphs.length-1])));
        else
            article.setParagraphs();
    }

    private void stemize(Article article)
    {
        ArticlesCleaner.stem(article.getWordsVector());
        ArticlesCleaner.stem(article.getFirstParagraph());
        ArticlesCleaner.stem(article.getLastParagraph());
    }

    private void splitSets(float learningPercent)
    {
        int learningSize = (int) Math.ceil(articles.length * learningPercent);
        int testingSize = articles.length - learningSize;
        Article[] learning = new Article[learningSize];
        Article[] testing = new Article[testingSize];

        System.arraycopy(articles, 0, learning, 0, learningSize);
        System.arraycopy(articles, learningSize, testing, 0, testingSize);

        learningArticles = learning;
        testingArticles = testing;
    }

    public int findMostWordsAfterStoplist()
    {
        int indexToReturn = 0;
        int max = 0;
        for(int i = 0; i < articles.length; i++)
        {
            int wordsAmount = articles[i].getWordsVector().length;
            if(wordsAmount > max)
            {
                max=wordsAmount;
                indexToReturn = i;
            }
        }

        return indexToReturn;
    }

    public String numbersInSets(String[] labels)
    {
        StringBuilder ret = new StringBuilder();
        for (String label: labels)
            ret.append("\t").append(label).append(": ").append(numberInSet(label)).append("\n");

        return ret.toString();
    }

    private String numberInSet(String label)
    {
        int count = 0;
        for (Article article: testingArticles)
            if(article.getPlaces()[0].equals(label)) count++;

        return Integer.toString(count);
    }

}
