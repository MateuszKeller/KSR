import java.util.Arrays;

public class Manager {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello World");

        Classification T = new Classification();
        T.fillRepository();

        int i = 0;

        Article objT = T.getRepository().getArticles()[i];
        System.out.println(objT);
        System.out.println("------------------------------");
        System.out.println(Arrays.toString(objT.getFirstVector()));
        System.out.println(Arrays.toString(objT.getLastVector()));
        System.out.println("------------------------------");


    }
}
