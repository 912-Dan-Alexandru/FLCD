import domain.ScannerX;
import domain.SymbolTable;

import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
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
}
