
import java.io.*;
import java.util.List;
import java.util.ArrayList;


public class Lexical_Analyzer {
    //MARK: Global
    int charClass;
    char[] lexeme = new char[100];
    char nextChar;
    int lenLex;
    int token;
    int nextToken;
    private List<Token> tokenlist;

    /**
     * This method will be resposible
     * @param fileName must be a String of a path to a file.
     */
    public void openTheFileAndAnalyze(String fileName){
        try{
            PrintWriter printWriter = new PrintWriter(new FileWriter( new File("TokenList4"), true));
            tokenlist = new ArrayList<Token>();
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String input;
            int lineNumber = 0;
            while ((input = bufferedReader.readLine()) != null){
                lineNumber++;
                processLine(input, lineNumber);
            }

            bufferedReader.close();
            printWriter.println(TokenType.EOS_TOK+" "+"EOS"+" "+lineNumber+" "+1);
            tokenlist.add(new Token(TokenType.EOS_TOK, "EOS", lineNumber, 1));
            printWriter.close();
        } catch(FileNotFoundException e){
            System.out.println("Error: "+e);
        } catch(IOException e){
            System.out.println("Error: "+e);
        } catch(LexicalException e){
            System.out.print("no new line" + e);

        }

    }
    /**
     * This
     * @param input - The line of code read from the file.
     * @param lineNumber - The row number essentially.
     */
    private void processLine(String input, int lineNumber) throws LexicalException, IOException
    {
        PrintWriter printWriter = new PrintWriter(new FileWriter( new File("TokenList4"), true));
        if(input == null)
            throw new IllegalArgumentException("The line is empty");
        if(lineNumber <= 0)
            throw new IllegalArgumentException("The line number is less then 0");
        int index = skipWhiteSpace(input, 0);

        while (index < input.length()){
            String lexeme = getLexeme(input, index);
            TokenType tokenType =  getTokenType(lexeme, lineNumber, index+1);
            printWriter.println(tokenType+" "+lexeme+" "+lineNumber+" "+(index+1));
            tokenlist.add(new Token(tokenType, lexeme, lineNumber, index + 1));
            System.out.println("At Row: "+ lineNumber+", Colmun: "+ (index+1) + " is lexeme: "+lexeme);
            index += lexeme.length();
            index = skipWhiteSpace(input, index);
        }
        printWriter.close();
    }
    private TokenType getTokenType(String lexeme, int rowNumber, int colNumber) throws LexicalException{
        if(lexeme == null || lexeme.length() == 0)
            throw new IllegalArgumentException("Invalid string argument");
        TokenType tokenType;
        if(Character.isDigit(lexeme.charAt(0)))
            if(allDigits(lexeme))
                tokenType  =TokenType.LITERAL_INTEGER_TOK;
            else
                throw new LexicalException ("Error at row: "+rowNumber+", Column: "+colNumber);
        else if(Character.isLetter(lexeme.charAt(0))){
            if(lexeme.length() == 1)
                tokenType = TokenType.ID_TOK;
            else
            switch(lexeme){
                case "if":
                    tokenType = TokenType.IF_TOK;
                    break;
                case "function":
                    tokenType = TokenType.FUNCTION_TOK;
                    break;
                case "then":
                    tokenType = TokenType.THEN_TOK;
                    break;
                case "end":
                    tokenType = TokenType.END_TOK;
                    break;
                case "else":
                    tokenType = TokenType.ELSE_TOK;
                    break;
                case "while":
                    tokenType = TokenType.WHILE_TOK;
                    break;
                case "do":
                    tokenType = TokenType.DO_TOK;
                    break;
                case "print":
                    tokenType = TokenType.PRINT_TOK;
                    break;
                case "repeat":
                    tokenType = TokenType.REPEAT_TOK;
                    break;
                case "until":
                    tokenType = TokenType.UNTIL_TOK;
                    break;
                    default: throw new LexicalException("Error at row: "+rowNumber+", Column"+colNumber);

            }

        }else{
            switch (lexeme){
                case "(":
                    tokenType = TokenType.LEFT_PAREN_TOK;
                    break;
                case ")":
                    tokenType = TokenType.RIGHT_PAREN_TOK;
                    break;
                case ">=":
                    tokenType = TokenType.GE_TOK;
                    break;
                case ">":
                    tokenType = TokenType.GT_TOK;
                    break;
                case "<=":
                    tokenType = TokenType.LE_TOK;
                    break;
                case "<":
                    tokenType = TokenType.LT_TOK;
                    break;
                case "==":
                    tokenType = TokenType.EQ_TOK;
                    break;
                case "~=":
                    tokenType = TokenType.NE_TOK;
                    break;
                case "+":
                    tokenType = TokenType.ADD_TOK;
                    break;
                case "-":
                    tokenType = TokenType.SUB_TOK;
                    break;
                case "*":
                    tokenType = TokenType.MUL_TOK;
                    break;
                case "/":
                    tokenType = TokenType.DIV_TOK;
                    break;
                case "=":
                    tokenType = TokenType.ASSIGN_TOK;
                    break;
                default: throw new LexicalException("Error");

            }

        }
        return tokenType;
    }
    private boolean allDigits(String lexeme){
        if(lexeme == null)
            throw new IllegalArgumentException("Nothing was pass in the String.");
        int i=0;
        while(i < lexeme.length() && Character.isDigit(lexeme.charAt(i)))
            i++;
        return i == lexeme.length();
    }
    private String getLexeme(String input, int index){
        if(input == null)
            throw new IllegalArgumentException("Nothing was passed in the String.");
        if(index < 0)
            throw new IllegalArgumentException("Index is less then Zero.");
        int i = index;
        while (i < input.length() && !Character.isWhitespace(input.charAt(i)))
            i++;

        return input.substring(index, i);
    }
    private int skipWhiteSpace(String input, int index){
        while(index < input.length() && Character.isWhitespace(input.charAt(index)))
            index++;
        return index;

    }
    public Token getLookAheadToken() throws LexicalException{
        if(tokenlist.isEmpty())
            throw new LexicalException("No more tokens.");
        return tokenlist.get(0);
    }
    public Token getNextToken() throws LexicalException{
        if(tokenlist.isEmpty())
            throw new LexicalException("No more tokens.");
        return tokenlist.remove(0);

    }

}

