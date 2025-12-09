import java.io.*;

public class Codegen {
    // file into which generated code is written
    public static PrintWriter p = null;

    // values of true and false
    public static final String TRUE = "-1";
    public static final String FALSE = "0";

    // registers
    public static final String FP = "$fp";
    public static final String SP = "$sp";
    public static final String RA = "$ra"; // Return Address
    public static final String A0 = "$a0"; // Argument 0 / Syscall arg
    public static final String V0 = "$v0"; // Return value / Syscall ID
    public static final String T0 = "$t0"; // Temp 0
    public static final String T1 = "$t1"; // Temp 1
    public static final String ACC = "$a0"; // Accumulator (in Simple oft a0)

    // for pretty printing generated code
    private static final int MAXLEN = 4;

    // for generating labels
    private static int currLabel = 0;

    // **********************************************************************
    // Initialize output
    // **********************************************************************
    public static void init(PrintWriter pw) {
        p = pw;
    }

    // ********************************************************************** // GENERATE OPERATIONS
    // **********************************************************************

    public static void generateWithComment(String opcode, String comment,
                                           String arg1, String arg2,
                                           String arg3) {
        int space = MAXLEN - opcode.length() + 2;

        p.print("\t" + opcode);
        if (arg1 != "") {
            for (int k = 1; k <= space; k++) p.print(" ");
            p.print(arg1);
            if (arg2 != "") {
                p.print(", " + arg2);
                if (arg3 != "") p.print(", " + arg3);
            }
        }           
        if (comment != "") p.print("\t\t#" + comment);
        p.println();
    }

    public static void generateWithComment(String opcode, String comment,
                                           String arg1, String arg2) {
        generateWithComment(opcode, comment, arg1, arg2, "");
    }

    public static void generateWithComment(String opcode, String comment,
                                           String arg1) {
        generateWithComment(opcode, comment, arg1, "", "");
    }

    public static void generateWithComment(String opcode, String comment) {
        generateWithComment(opcode, comment, "", "", "");
    }

    // **********************************************************************
    // generate (Delegate to generateWithComment with empty comment)
    // **********************************************************************
    public static void generate(String opcode, String arg1, String arg2,
                                String arg3) {
        generateWithComment(opcode, "", arg1, arg2, arg3);
    }

    public static void generate(String opcode, String arg1, String arg2) {
        generateWithComment(opcode, "", arg1, arg2, "");
    }

    public static void generate(String opcode, String arg1) {
        generateWithComment(opcode, "", arg1, "", "");
    }

    public static void generate(String opcode) {
        generateWithComment(opcode, "", "", "", "");
    }

    // **********************************************************************
    // generate with integer args (Overloading helper)
    // **********************************************************************
    public static void generate(String opcode, String arg1, String arg2,
                                int arg3) {
        generate(opcode, arg1, arg2, Integer.toString(arg3));
    }

    public static void generate(String opcode, String arg1, int arg2) {
        generate(opcode, arg1, Integer.toString(arg2));
    }

    // **********************************************************************
    // generateIndexed
    // Output: opcode arg1, arg3(arg2)   # comment
    // Example: lw $t0, 4($fp)
    // **********************************************************************
    public static void generateIndexed(String opcode, String arg1,
                                      String arg2, int arg3, String comment)
    {
        // Format: opcode arg1, offset(base)
        String offsetStr = arg3 + "(" + arg2 + ")";
        
        // Wir nutzen generateWithComment manuell, um das Format "arg1, offset" zu bauen
        int space = MAXLEN - opcode.length() + 2;
        p.print("\t" + opcode);
        for (int k = 1; k <= space; k++) p.print(" ");
        p.print(arg1 + ", " + offsetStr);
        
        if (comment != "") p.print("\t\t#" + comment);
        p.println();
    }

    public static void generateIndexed(String opcode, String arg1,
                                       String arg2, int arg3) {
        generateIndexed(opcode, arg1, arg2, arg3, "");
    }                   

    public static void generateLabel(String label) {
        p.println(label + ":");
    }

    // **********************************************************************
    // generateLabeled
    // **********************************************************************
    public static void generateLabeled(String label, String opcode,
                                       String comment, String arg1) {
        p.print(label + ":");
        generateWithComment(opcode, comment, arg1);
    }

    public static void generateLabeled(String label, String opcode,
                                       String comment) {
        p.print(label + ":");
        generateWithComment(opcode, comment);
    }

    // **********************************************************************
    // genPush
    // Stack wächst nach unten (subu). 
    // Hier implementiert als: Store, then Decrement.
    // **********************************************************************
    public static void genPush(String s) {
        generateIndexed("sw", s, SP, 0, "PUSH");
        generate("subu", SP, SP, 4);                       
    }

    // **********************************************************************
    // genPop
    // Muss das Gegenteil von genPush sein:
    // Decrement war zuletzt -> also erst Increment, dann Load.
    // **********************************************************************
    public static void genPop(String s) {
        generate("addu", SP, SP, 4);
        generateIndexed("lw", s, SP, 0, "POP");
    }

    // **********************************************************************
    // genCompare
    // Simuliert boolesche Vergleiche.
    // Ablauf:
    // 1. Branch, wenn Bedingung wahr, zum trueLabel
    // 2. Ansonsten: Push "false" (0) und springe zum Ende
    // 3. trueLabel: Push "true" (-1)
    // 4. Ende
    // **********************************************************************
    public static void genCompare(String op) {
        String trueLab = nextLabel();
        String doneLab = nextLabel();
        
        // Annahme: Operanden sind in T0 und T1 (so wird das im Compiler meist vorbereitet)
        generate(op, T0, T1, trueLab); 
        genPush(FALSE);
        generate("b", doneLab);
        genLabel(trueLab);
        genPush(TRUE);
        genLabel(doneLab);
    }                    

    public static void genComment(String comment) {
        p.println("\t\t# " + comment);
    }

    // **********************************************************************
    // genLabel
    // **********************************************************************
    public static void genLabel(String label, String comment) {
        p.print(label + ":");
        if (comment != "") p.print("\t\t#" + comment);
        p.println();
    }

    public static void genLabel(String label) {
        genLabel(label, "");
    }

    // **********************************************************************
    // Return a different label each time: ._L0 ._L1 ._L2
    // **********************************************************************
    public static String nextLabel() {
        return "._L" + (currLabel++);
    }                               
}