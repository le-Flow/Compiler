###
# This Makefile can be used to make a parser for the Simple language
# (Yylex.class) and to make a program (P4.class) that tests the parser,
# name analysis, and type checking.
#
# The default makes both the parser and the test program.
#
# make clean removes all generated files.
#
###
CLASSPATH =  .:../jars/java-cup-11b-runtime.jar
JARFILE = ../jars/java-cup-11b.jar
FLAGS = -g -cp $(CLASSPATH)

# CHANGED: Target is P4.class. Added SymbolTable and Types dependencies.
P4.class: P4.java parser.class Yylex.class ASTnode.class IO.class SymbolTable.class Types.class
	javac $(FLAGS) P4.java

# CHANGED: Added SymbolTable and Types dependencies.
parser.class: parser.java ASTnode.class Yylex.class Errors.class SymbolTable.class Types.class
	javac $(FLAGS) parser.java

# KEPT: -expect 2 flag
parser.java: simple.cup
	java -jar $(JARFILE) -expect 2 < simple.cup

Yylex.class: simple.jlex.java sym.class Errors.class
	javac $(FLAGS) simple.jlex.java

# CHANGED: Added SymbolTable and Types dependencies.
ASTnode.class: ast.java Sequence.class NoCurrentException.class SymbolTable.class Types.class
	javac $(FLAGS) ast.java

# ADDED: Compile SymbolTable
SymbolTable.class: SymbolTable.java Types.class
	javac $(FLAGS) SymbolTable.java

# ADDED: Compile Types
Types.class: Types.java
	javac $(FLAGS) Types.java

simple.jlex.java: simple.jlex
	@echo "IMPORTANT You must replace the simple.jlex file with your scanner"
	@echo "The provided simple.jlex is incomplete and just has what"
	@echo "Is needed so that the incompled parser compiles"
	jflex simple.jlex # use jflex it is more flexible
	mv Yylex.java simple.jlex.java # rename the file produced by jflex

sym.class: sym.java
	javac $(FLAGS) sym.java

# KEPT: -expect 2 flag
sym.java: simple.cup
	java -cp $(CLASSPATH) java_cup.Main -expect 2 < simple.cup

Errors.class: Errors.java
	javac Errors.java

Sequence.class: Sequence.java
	javac $(FLAGS) Sequence.java

NoCurrentException.class: NoCurrentException.java
	javac NoCurrentException.java

IO.class: IO.java
	javac $(FLAGS) IO.java

###
# clean
###
clean:
	rm -f *~ *.class parser.java simple.jlex.java sym.java

# CHANGED: Runs P4 instead of P3
test:   test.sim P4.class 
	@echo "Running P4 on test.sim..."
	java -cp $(CLASSPATH) P4 test.sim

###
# submit
###

# CHANGED: Added P4, SymbolTable, Types, and the required test files
submit:
	zip submit.zip P4.java SymbolTable.java Types.java *.java test.sim NameErrors.sim TypeErrors.sim Makefile simple.jlex simple.cup 

###
# handout
###

handout:
	zip handout.zip test.sim Makefile simple.grammar ast.java Errors.java IO.java NoCurrentException.java P3.java Sequence.java simple.cup simple.grammar