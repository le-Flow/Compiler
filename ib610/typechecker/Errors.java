// Errors
class Errors {
    // REQUIRED: This field is checked by P4 to stop compilation
    public static int fatalErrorCount = 0;

    static void fatal(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " **ERROR** " + msg);
        fatalErrorCount++; // Increment counter
    }

    static void warn(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " **WARNING** " + msg);
    }
}