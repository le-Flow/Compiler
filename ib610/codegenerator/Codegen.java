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
    public static final String ACC = "$a0"; // Accumulator

    // for pretty printing generated code
    private static final int MAXLEN = 4;

    // for generating labels
    private static int currLabel = 0;

    public static void init(PrintWriter pw) {
        p = pw;
    }

    // **********************************************************************
    // GENERATE OPERATIONS
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

    public static void generateWithComment(String opcode, String comment, String arg1, String arg2) {
        generateWithComment(opcode, comment, arg1, arg2, "");
    }
    public static void generateWithComment(String opcode, String comment, String arg1) {
        generateWithComment(opcode, comment, arg1, "", "");
    }
    public static void generateWithComment(String opcode, String comment) {
        generateWithComment(opcode, comment, "", "", "");
    }

    // Delegate methods
    public static void generate(String opcode, String arg1, String arg2, String arg3) {
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
    public static void generate(String opcode, String arg1, String arg2, int arg3) {
        generate(opcode, arg1, arg2, Integer.toString(arg3));
    }
    public static void generate(String opcode, String arg1, int arg2) {
        generate(opcode, arg1, Integer.toString(arg2));
    }

    // **********************************************************************
    // generateIndexed
    // **********************************************************************
    public static void generateIndexed(String opcode, String arg1,
                                      String arg2, int arg3, String comment) {
        String offsetStr = arg3 + "(" + arg2 + ")";
        int space = MAXLEN - opcode.length() + 2;
        p.print("\t" + opcode);
        for (int k = 1; k <= space; k++) p.print(" ");
        p.print(arg1 + ", " + offsetStr);
        if (comment != "") p.print("\t\t#" + comment);
        p.println();
    }

    public static void generateIndexed(String opcode, String arg1, String arg2, int arg3) {
        generateIndexed(opcode, arg1, arg2, arg3, "");
    }                   

    // **********************************************************************
    // Label Helpers
    // **********************************************************************

    // Used by AST (legacy call support)
    public static void generateLabel(String label) {
        genLabel(label, "");
    }

    public static void genLabel(String label, String comment) {
        p.print(label + ":");
        if (comment != "") p.print("\t\t#" + comment);
        p.println();
    }
    
    // THIS WAS MISSING: The overload required by ast.java
    public static void genLabel(String label) {
        genLabel(label, "");
    }

    public static void generateLabeled(String label, String opcode, String comment, String arg1) {
        p.print(label + ":");
        generateWithComment(opcode, comment, arg1);
    }

    public static void generateLabeled(String label, String opcode, String comment) {
        p.print(label + ":");
        generateWithComment(opcode, comment);
    }

    // **********************************************************************
    // Stack Operations
    // **********************************************************************
    public static void genPush(String s) {
        generateIndexed("sw", s, SP, 0, "PUSH");
        generate("subu", SP, SP, 4);                       
    }

    public static void genPop(String s) {
        generate("addu", SP, SP, 4);
        generateIndexed("lw", s, SP, 0, "POP");
    }

    // **********************************************************************
    // NEW: Generic Binary Operation
    // Use this in Parser for: ADD, SUB, MUL, DIV
    // **********************************************************************
    public static void genBinaryOp(String opcode) {
        genPop(T1); // Right operand
        genPop(T0); // Left operand
        generate(opcode, T0, T0, T1);
        genPush(T0); // Push result
    }

    // **********************************************************************
    // genCompare (FIXED)
    // Now correctly pops values from stack before comparing
    // **********************************************************************
    public static void genCompare(String op) {
        String trueLab = nextLabel();
        String doneLab = nextLabel();
        
        generate(op, T0, T1, trueLab); 
        
        // Case: False
        generate("li", ACC, FALSE);
        genPush(ACC);
        generate("b", doneLab);
        
        // Case: True
        generateLabel(trueLab);
        generate("li", ACC, TRUE);
        genPush(ACC);
        
        generateLabel(doneLab);
    }              

    // **********************************************************************
    // NEW: Method Epilogue
    // Call this at the end of every method in your Parser
    // **********************************************************************
    public static void genEpilogue(String methodName) {
        genLabel(methodName + "_exit"); // Using genLabel here is safer now
        
        if (methodName.equals("main")) {
            // Main must invoke syscall 10 to exit correctly
            generate("li", V0, "10");
            generate("syscall");
        } else {
            // Standard return for other methods
            generate("move", SP, FP);       // Restore SP
            generateIndexed("lw", RA, FP, -4); // Restore RA
            generateIndexed("lw", FP, FP, 0);  // Restore FP
            generate("jr", RA);             // Return
        }
    }

    public static void genComment(String comment) {
        p.println("\t\t# " + comment);
    }

    public static String nextLabel() {
        return "._L" + (currLabel++);
    }                               
}