import domain.FA;
import domain.ScannerX;
import domain.SymbolTable;

import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("1. Test DFA");
        System.out.println("2. Scanner");
        System.out.println("Your option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {
            case 1 -> DFAfunc();
            case 2 -> scannerfunc();
        }
    }

    private static void scannerfunc() throws Exception{
        System.out.println();
        System.out.println("Correct program");
        ScannerX scannerP1 = new ScannerX("C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\P1.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\PIF1.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\ST1.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\tokens.txt");
        scannerP1.scan();

        System.out.println("Error program");
        ScannerX scannerP1Err = new ScannerX("C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\P1Err.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\PIF1Err.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\ST1Err.txt",
                "C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\tokens.txt");
        scannerP1Err.scan();
    }

    private static void printMenu(){
        System.out.println("1. Print states.");
        System.out.println("2. Print alphabet.");
        System.out.println("3. Print final states.");
        System.out.println("4. Print transitions.");
        System.out.println("5. Check if sequence is accepted by DFA.");
        System.out.println("0. Exit");
    }

    private static void DFAfunc() {
        FA fa = new FA("C:\\Users\\Alex\\IdeaProjects\\lab2\\src\\FA.in");

        System.out.println("FA read from file.");
        printMenu();
        System.out.println("Your option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while(option != 0) {
            switch (option) {
                case 1 -> System.out.println(fa.writeStates());
                case 2 -> System.out.println(fa.writeAlphabet());
                case 3 -> System.out.println(fa.writeFinalStates());
                case 4 -> System.out.println(fa.writeTransitions());
                case 5 -> {
                    if(fa.checkIfDFA()) {
                        System.out.println("Your sequence: ");
                        Scanner scanner2 = new Scanner(System.in);
                        String sequence = scanner2.nextLine();

                        if (fa.checkSequence(sequence))
                            System.out.println("Sequence is valid");
                        else
                            System.out.println("Invalid sequence");
                    }
                    else {
                        System.out.println("FA is not deterministic.");
                    }
                }
            }
            System.out.println();
            printMenu();
            System.out.println("Your option: ");
            option = scanner.nextInt();
        }
    }
}
