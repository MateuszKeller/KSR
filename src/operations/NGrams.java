package operations;

public class NGrams {

    private static int n;

    public void setN(int n) { NGrams.n = n; }

    public static double similarity(String word1, String word2)
    {
        double sim = 0;
        
        if (word1 == null || word2 == null ) { return 0; }
        if (word1.length() < n || word2.length() < n) { return 0; }
        
        String longer, shorter;
        
        if(word1.length() >  word2.length())
        {
            longer = word1;
            shorter = word2;
        }
        else 
        {
            longer = word2;
            shorter = word1;
        }

        String[] grams = splitToGrams(longer);
        for (int i = 0; i < shorter.length() + 1 - n; i++) 
        {
            for (String sub : grams)
                if (sub.equals(shorter.substring(i, i + n)))
                    sim++;
        }
        
        return sim;
    }

    private static String[] splitToGrams(String word)
    {
        String[] ret = new String[word.length() + 1 - n];

        for (int i = 0; i < word.length() + 1 - n; i++)
            ret[i] = word.substring(i, i + n);

        return ret;
    }
}
