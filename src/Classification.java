import java.util.ArrayList;
import java.util.List;

public class Classification {

    private ArticlesRepository repository;
    private final String[] labels = { "west-germany", "usa", "france", "uk", "canada", "japan" };
    private List<String> keywords = new ArrayList<>();

    public ArticlesRepository getRepository() { return repository; }

    public void fillRepository() throws Exception
    {
        repository = new ArticlesRepository(50);
    }

}
