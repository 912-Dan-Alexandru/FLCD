package domain;

import java.util.HashMap;
import java.util.List;


public class TokensTable {
    private final List<String> reservedWords;
    private final List<String> operators;
    private final List<String> separators;

    private final HashMap<String, Integer> codification = new HashMap<>();

    private final FA intDFA = new FA("C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\intDFA.in");
    private final FA identifierDFA = new FA("C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\identifierDFA.in");

    public TokensTable(List<String> reservedWords, List<String> operators, List<String> separators) {
        this.reservedWords = reservedWords;
        this.operators = operators;
        this.separators = separators;
        createCodification();
    }

    private void createCodification() {
        codification.put("identifier", 0);
        codification.put("constant", 1);

        int code = 2;

        for (String reservedWord : reservedWords) {
            codification.put(reservedWord, code);
            code++;
        }

        for (String operator : operators) {
            codification.put(operator, code);
            code++;
        }

        for (String separator : separators) {
            codification.put(separator, code);
            code++;
        }
    }

    public boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    public boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isPartOfOperator(char op) {
        return op == '!' || isOperator(String.valueOf(op));
    }

    public boolean isSeparator(String token) {
        return separators.contains(token);
    }

    public boolean isIdentifier(String token) {
//        String pattern = "^[a-zA-Z]([a-z|A-Z|0-9|_])*$";
//        return token.matches(pattern);
        return identifierDFA.checkSequence(token);
    }

    public boolean isConstant(String token) {
//        String numericPattern = "^0|[+|-][1-9]([0-9])*|[1-9]([0-9])*|[+|-][1-9]([0-9])*\\.([0-9])*|[1-9]([0-9])*\\.([0-9])*$";
        String charPattern = "^\'[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]\'";
        String stringPattern = "^\"[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]+\"";
        return token.matches(charPattern) ||
                intDFA.checkSequence(token) ||
                token.matches(stringPattern);
    }

    public Integer getCode(String token) {
        return codification.get(token);
    }
}

