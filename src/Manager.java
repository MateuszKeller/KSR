import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification T = new Classification();
        T.fillRepository();
        T.generateKeyWords();

        int i = 0;

        Article objT = T.getRepository().getArticles()[i];
        System.out.println("Article " + i+1 + ": " + objT);
        System.out.println("KeyWords- " + T.getKeywords().length + ": "+ Arrays.toString(T.getKeywords()));




    }
}
