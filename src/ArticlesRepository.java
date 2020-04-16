
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

        System.out.println(articles.length + " articles.");
        System.out.println(learningArticles.length + " learning.");
        System.out.println(testingArticles.length + " testing.");

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
        ArticlesCleaner.stem(article.getFirstVector());
        ArticlesCleaner.stem(article.getLastVector());
    }

    private void splitSets(float learningPercent)
    {
        int learningSize = (int) Math.ceil(articles.length * learningPercent);
        int testingSize = articles.length - learningSize;
        Article[] learning = new Article[learningSize];
        Article[] testing = new Article[testingSize];

        for (int i = 0; i < learningSize; i++)
            learning[i] = articles[i];

        for (int i = 0; i < testingSize; i++)
            testing[i] = articles[learningSize + i];

        learningArticles = learning;
        testingArticles = testing;
    }

}
