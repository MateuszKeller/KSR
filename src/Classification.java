import java.util.ArrayList;
import java.util.List;

public class Classification {

    private ArticlesRepository repository;
    private final String[] labels = { "west-germany", "usa", "france", "uk", "canada", "japan" };
    private List<String> keywords = new ArrayList<>();
    private boolean featuresExtracted = false;
    private int keywordsExtractionType = 0;
    private int keywordsCount = 5;
    private int keywordsExtractor = 0;

    public void fillRepository() throws Exception
    {
        repository = new ArticlesRepository(50);
    }

}
