import java.sql.SQLOutput;

public class Test_Lexical_Analyzer {
    public static void main(String[] args) throws LexicalException{
        Lexical_Analyzer la = new Lexical_Analyzer();
        la.openTheFileAndAnalyze("test4.lua");
    }
}
