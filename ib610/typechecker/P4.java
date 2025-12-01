import java.io.*;
import java_cup.runtime.Symbol;

public class P4 {
    public static void main(String[] args) {
        FileReader input = null;
        
        // Handle Input File
        if (args.length == 0) {
            System.err.println("Usage: java P4 <inputfile>");
            System.exit(1);
        } else {
            try {
                input = new FileReader(args[0]);
            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found: " + args[0]);
                System.exit(1);
            }
        }

        try {
            // 1. Parsing
            parser P = new parser(new Yylex(input));
            
            Symbol result = P.parse();
            ProgramNode root = (ProgramNode) result.value;

            // 2. Name Analysis (Pass 1)
            if (Errors.fatalErrorCount == 0) {
                SymbolTable st = new SymbolTable();
                root.analyzeNames(st);
            }

            // 3. Decompiling (Verification)
            if (Errors.fatalErrorCount == 0) {
                PrintWriter out = new PrintWriter(System.out, true);
                root.decompile(out, 0); 
            }

            // 4. Type Checking (Pass 2)
            if (Errors.fatalErrorCount == 0) {
                root.checkTypes();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}