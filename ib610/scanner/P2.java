import java.io.*;
import java_cup.runtime.*;  // defines Symbol

// HINWEIS: Die Klasse 'Errors' muss separat existieren (z.B. in Errors.java)
// mit der statischen Methode fatal(int, int, String).

// **********************************************************************
// Main program to test the simple scanner (P2.java).
// **********************************************************************

public class P2 {
    public static void main(String[] args) {
        // check for command-line arg
        if (args.length != 1) {
            System.err.println("please supply name of file to be scanned.");
            System.exit(-1);
        }

        // open input file
        FileReader inFile = null;
        try {
            inFile = new FileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[0] + " not found.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        try {
            Symbol token = scanner.next_token();
            while (token.sym != sym.EOF) {
                // Ausgabe der Zeilen- und Spaltennummer
                System.out.print(((TokenVal)token.value).linenum + ":" +
                                 ((TokenVal)token.value).charnum + " ");

                // Ausgabe des Tokentyps und des Werts (falls vorhanden)
                switch (token.sym) {
                    // --- Literale mit Werten ---
                    case sym.INTLITERAL:
                        System.out.println("INTLITERAL (" +
                                           ((IntLitTokenVal)token.value).intVal +
                                           ")");
                        break;
                    case sym.STRINGLITERAL:
                        System.out.println("STRINGLITERAL");
                        break;
                    case sym.ID:
                        System.out.println("ID");
                        break;

                    // --- ERWEITERTE Operatoren & Separatoren ---
                    case sym.LCURLY:
                        System.out.println("LCURLY");
                        break;
                    case sym.RCURLY:
                        System.out.println("RCURLY");
                        break;
                    case sym.LPAREN:
                        System.out.println("LPAREN");
                        break;
                    case sym.RPAREN:
                        System.out.println("RPAREN");
                        break;
                    case sym.COMMA:
                        System.out.println("COMMA");
                        break;
                    case sym.ASSIGN:
                        System.out.println("ASSIGN");
                        break;
                    case sym.SEMICOLON:
                        System.out.println("SEMICOLON");
                        break;
                    case sym.PLUS:
                        System.out.println("PLUS");
                        break;
                    case sym.MINUS:
                        System.out.println("MINUS");
                        break;
                    case sym.TIMES:
                        System.out.println("TIMES");
                        break;
                    case sym.DIVIDE:
                        System.out.println("DIVIDE");
                        break;
                    case sym.NOT:
                        System.out.println("NOT");
                        break;
                    case sym.AND:
                        System.out.println("AND");
                        break;
                    case sym.OR:
                        System.out.println("OR");
                        break;
                    case sym.EQUALS:
                        System.out.println("EQUALS");
                        break;
                    case sym.NOTEQUALS:
                        System.out.println("NOTEQUALS");
                        break;
                    case sym.LESS:
                        System.out.println("LESS");
                        break;
                    case sym.GREATER:
                        System.out.println("GREATER");
                        break;
                    case sym.LESSEQ:
                        System.out.println("LESSEQ");
                        break;
                    case sym.GREATEREQ:
                        System.out.println("GREATEREQ");
                        break;

                    // --- Keywords ---
                    case sym.PRINT:
                        System.out.println("PRINT");
                        break;
                    case sym.STRING:
                        System.out.println("STRING");
                        break;
                    case sym.BOOLEAN:
                        System.out.println("BOOLEAN");
                        break;
                    case sym.CLASS:
                        System.out.println("CLASS");
                        break;
                    case sym.DO:
                        System.out.println("DO");
                        break;
                    case sym.ELSE:
                        System.out.println("ELSE");
                        break;
                    case sym.FALSE:
                        System.out.println("FALSE");
                        break;
                    case sym.IF:
                        System.out.println("IF");
                        break;
                    case sym.INT:
                        System.out.println("INT");
                        break;
                    case sym.PUBLIC:
                        System.out.println("PUBLIC");
                        break;
                    case sym.RETURN:
                        System.out.println("RETURN");
                        break;
                    case sym.STATIC:
                        System.out.println("STATIC");
                        break;
                    case sym.TRUE:
                        System.out.println("TRUE");
                        break;
                    case sym.VOID:
                        System.out.println("VOID");
                        break;
                    case sym.WHILE:
                        System.out.println("WHILE");
                        break;

                    default:
                        System.out.println("Unbekanntes Token: " + token.sym);
                        break;
                }
                token = scanner.next_token();
            }
        } catch (IOException ex) {
            System.err.println("unexpected IOException thrown by the scanner");
            System.exit(-1);
        }
    }
}