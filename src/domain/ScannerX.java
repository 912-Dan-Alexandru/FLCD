package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class ScannerX {

    private final PIF pif = new PIF();
    private final int capacity = 13;
    private final SymbolTable symbolTable = new SymbolTable(capacity);

    private final String programFile;
    private final String PIFFile;
    private final String STFile;
    private final TokensTable tokensTable;

    public ScannerX(String programFile, String PIFFile, String STFile, String ttFile) throws FileNotFoundException {
        this.programFile = programFile;
        this.PIFFile = PIFFile;
        this.STFile = STFile;
        this.tokensTable = createTokensTable(ttFile);
    }

    private TokensTable createTokensTable(String lfile) throws FileNotFoundException {
        List<String> reservedWords = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        List<String> separators = new ArrayList<>();

        try{
            File file = new File(lfile);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()){

                String line = reader.nextLine();
                String identifier = line.split(":",2)[0];
                String tokens = line.split(":",2)[1];
                for (String token : tokens.split(" ")){
                    if (Objects.equals(identifier, "reservedWords"))
                        reservedWords.add(token);
                    if (Objects.equals(identifier, "operators"))
                        operators.add(token);
                    if (Objects.equals(identifier, "separators"))
                        separators.add(token);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println(reservedWords);
//        System.out.println(operators);
//        System.out.println(separators);
        return new TokensTable(reservedWords, operators, separators);
    }

    public void scan() {
        List<Pair<String, Integer>> tokenPairs = new ArrayList<>();
        try {
            File file = new File(programFile);
            Scanner reader = new Scanner(file);

            int lineNr = 1;

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                List<String> tokens = getTokens(line);
//                System.out.println(tokens);

                for (String token : tokens) {
                    tokenPairs.add(new Pair<>(token.strip(), lineNr));
                }

                ++lineNr;
            }

            reader.close();

            buildPIF(tokenPairs);
            writeResults();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTokens(String line) {
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < line.length(); ++i) {
            if (tokensTable.isSeparator(String.valueOf(line.charAt(i))) && !(String.valueOf(line.charAt(i))).equals(" ")) {
                tokens.add(String.valueOf(line.charAt(i)));
            } else if (line.charAt(i) == '\"') {
                String constant = identifyStringConstant(line, i);
                tokens.add(constant);
                i += constant.length() - 1;
            } else if (line.charAt(i) == '\'') {
                String constant = identifyCharConstant(line, i);
                tokens.add(constant);
                i += constant.length() - 1;
            } else if (line.charAt(i) == '-') {
                String token = identifyMinusToken(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (line.charAt(i) == '+') {
                String token = identifyPlusToken(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (tokensTable.isPartOfOperator(line.charAt(i))) {
                String operator = identifyOperator(line, i);
                tokens.add(operator);
                i += operator.length() - 1;
            } else if (line.charAt(i) != ' ') {
                String token = identifyToken(line, i);
                tokens.add(token);
                i += token.length() - 1;
            }
        }
        return tokens;
    }

    public String identifyStringConstant(String line, int position) {
        StringBuilder constant = new StringBuilder();

        for (int i = position; i < line.length(); ++i) {
            if ((tokensTable.isSeparator(String.valueOf(line.charAt(i))) || tokensTable.isOperator(String.valueOf(line.charAt(i)))) && ((i == line.length() - 2 && line.charAt(i + 1) != '\"') || (i == line.length() - 1)))
                break;
            constant.append(line.charAt(i));
            if (line.charAt(i) == '\"' && i != position)
                break;
        }

        return constant.toString();
    }

    public String identifyCharConstant(String line, int position) {
        StringBuilder constant = new StringBuilder();

        for (int i = position; i < line.length(); ++i) {
            if ((tokensTable.isSeparator(String.valueOf(line.charAt(i))) || tokensTable.isOperator(String.valueOf(line.charAt(i)))) && ((i == line.length() - 2 && line.charAt(i + 1) != '\'') || (i == line.length() - 1)))
                break;
            constant.append(line.charAt(i));
            if (line.charAt(i) == '\'' && i != position)
                break;
        }

        return constant.toString();
    }

    public String identifyMinusToken(String line, int position, ArrayList<String> tokens) {
        //minus is preceded by an identifier, constant -> minus is an operator
        if (tokensTable.isIdentifier(tokens.get(tokens.size() - 1)) || tokensTable.isConstant(tokens.get(tokens.size() - 1))) {
            return "-";
        }

        //minus is preceded by operator or separator -> assign a negative number or condition with negative number -> minus belongs to a numeric constant
        StringBuilder token = new StringBuilder();
        token.append('-');

        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.'); ++i) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public String identifyPlusToken(String line, int position, ArrayList<String> tokens) {
        //plus is preceded by an identifier, constant -> plus is an operator
        if (tokensTable.isIdentifier(tokens.get(tokens.size() - 1)) || tokensTable.isConstant(tokens.get(tokens.size() - 1))) {
            return "+";
        }

        //plus is preceded by operator or separator -> assign a positive number or condition with positive number -> plus belongs to a numeric constant
        StringBuilder token = new StringBuilder();
        token.append('+');

        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.'); ++i) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public String identifyOperator(String line, int position) {
        StringBuilder operator = new StringBuilder();
        operator.append(line.charAt(position));
        operator.append(line.charAt(position + 1));

        if (tokensTable.isOperator(operator.toString()))
            return operator.toString();

        return String.valueOf(line.charAt(position));
    }

    public String identifyToken(String line, int position) {
        StringBuilder token = new StringBuilder();

        for (int i = position; i < line.length()
                && !tokensTable.isSeparator(String.valueOf(line.charAt(i)))
                && !tokensTable.isPartOfOperator(line.charAt(i))
                && line.charAt(i) != ' '; ++i) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public void buildPIF(List<Pair<String, Integer>> tokens) {
        List<String> invalidTokens = new ArrayList<>();
        boolean isLexicallyCorrect = true;
        for (Pair<String, Integer> tokenPair : tokens) {
            String token = tokenPair.getKey();

            if (tokensTable.isOperator(token) || tokensTable.isReservedWord(token) || tokensTable.isSeparator(token)) {
                int code = tokensTable.getCode(token);
                pif.add(code, new Pair<>(-1, -1));
            } else if (tokensTable.isIdentifier(token)) {
                symbolTable.add(token);
                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(0, position);
//                System.out.println(token);
            } else if (tokensTable.isConstant(token)) {
                symbolTable.add(token);
                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(1, position);
            } else if (!invalidTokens.contains(token)) {
                invalidTokens.add(token);
                isLexicallyCorrect = false;
                System.out.println("Error at line " + tokenPair.getValue() + ": invalid token " + token);
            }
        }

        if (isLexicallyCorrect) {
            System.out.println("Program is lexically correct");
        }
    }

    public void writeResults() {
        try {
            File pifFile = new File(PIFFile);
            FileWriter pifFileWriter = new FileWriter(pifFile, false);
            BufferedWriter pifWriter = new BufferedWriter(pifFileWriter);
            pifWriter.write(pif.toString());
            pifWriter.close();

            File symbolTableFile = new File(STFile);
            FileWriter symbolTableFileWriter = new FileWriter(symbolTableFile, false);
            BufferedWriter symbolTableWriter = new BufferedWriter(symbolTableFileWriter);
            symbolTableWriter.write(symbolTable.toString());
            symbolTableWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
