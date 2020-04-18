package operations;

import model.ArticlesRepository;

public class Classification {

    private ArticlesRepository repository;
    private String[] labels = { "west-germany", "usa", "france", "uk", "canada", "japan" };
    private String[] keywords;
    private double significance = 0.5;

    public ArticlesRepository getRepository() { return repository; }
    public String[] getKeywords() { return keywords; }

    public void fillRepository() throws Exception
    {
        repository = new ArticlesRepository(50);
    }

    public void generateKeyWords()
    {
        keywords = KeyWords.generate(repository.getLearningArticles(), significance);
    }



}
