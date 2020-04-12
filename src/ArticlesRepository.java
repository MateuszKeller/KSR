

public class ArticlesRepository {

    private Article[] objects;
    private Article[] filteredObjects;
    private Article[] learningObjects;
    private Article[] testingObjects;

    public ArticlesRepository(int learingSet) throws Exception
    {
        objects = Loader.loadFiles();
    }
}
