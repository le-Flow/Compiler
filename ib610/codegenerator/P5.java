import java.io.*;
import java_cup.runtime.*;

// **********************************************************************
// Main program to test the simple static checker and code generator.
//
// There should be 2 command-line arguments:
//    1. the file to be parsed (input.sim)
//    2. the output file for the MIPS code (output.s)
// **********************************************************************

public class P5 {
    public static void main(String[] args)
    throws IOException // may be thrown by the scanner
    {
        // 1. Check command-line args
        if (args.length != 2) {
            System.err.println("Usage: java P5 <input file> <output MIPS file>");
            System.exit(-1);
        }

        // 2. Open input file
        FileReader inFile = null;
        try {
            inFile = new FileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[0] + " not found.");
            System.exit(-1);
        }

        // 3. Open output file for MIPS code
        PrintWriter outFile = null;
        try {
            // Wir nutzen hier direkt PrintWriter, um sicherzugehen
            outFile = new PrintWriter(new FileOutputStream(args[1]));
        } catch (IOException ex) {
            System.err.println("File " + args[1] + " could not be opened.");
            System.exit(-1);
        }

        // 4. Parse the program
        parser P = new parser(new Yylex(inFile));
        Symbol root = null;

        try {
            root = P.parse(); 
            System.out.println("Simple program parsed correctly.");
        } catch (Exception ex){
            System.err.println("Exception during parsing:");
            ex.printStackTrace();
            System.exit(-1);
        }

        // 5. Semantic Analysis & Code Gen
        // Wir holen uns den Wurzelknoten des AST (ProgramNode)
        ProgramNode program = (ProgramNode) root.value;

        // PASS 1: Name Analysis (Symboltabelle aufbauen)
        //
        try {
            program.analyzeNames(new SymbolTable());
        } catch (Exception e) {
            // Falls analyzeNames nicht selbst exited
            System.err.println("Name Analysis Error: " + e.getMessage());
            System.exit(-1);
        }

        // PASS 2: Type Checking
        //
        program.checkTypes();

        // PASS 3: Code Generation
        // Wird nur ausgeführt, wenn vorher keine fatalen Fehler den Prozess gestoppt haben.
        //
        System.out.println("Generating MIPS code to " + args[1] + "...");
        
        Codegen.init(outFile); // Codegen mit dem Writer verbinden
        program.codeGen();     // Start der Rekursion
        
        outFile.close();       // WICHTIG: Puffer leeren und Datei schließen!
        
        System.out.println("Done.");
    }
}