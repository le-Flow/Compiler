###
# This Makefile can be used to make a parser for the Simple language
# (Yylex.class) and to make a program (P5.class) that tests the parser and
# the unparse methods in ast.java.
#
# The default makes both the parser and the test program.
#
# make clean removes all generated files.
#
###
CLASSPATH =  .:../jars/java-cup-11b-runtime.jar
JARFILE = ../jars/java-cup-11b.jar
FLAGS = -g -cp $(CLASSPATH)

# CHANGED: Added dependencies for Codegen, SymbolTable, and Types
P5.class: P5.java parser.class Yylex.class ASTnode.class IO.class Codegen.class SymbolTable.class Types.class
	javac $(FLAGS) P5.java

# CHANGED: Added -expect 2 flag
parser.java: simple.cup
	java -jar $(JARFILE) -expect 2 < simple.cup

# CHANGED: Added SymbolTable and Types dependencies
parser.class: parser.java ASTnode.class Yylex.class Errors.class SymbolTable.class Types.class
	javac $(FLAGS) parser.java

Yylex.class: simple.jlex.java sym.class Errors.class
	javac $(FLAGS) simple.jlex.java

# CHANGED: Added SymbolTable, Types and Codegen dependencies
ASTnode.class: ast.java Sequence.class NoCurrentException.class SymbolTable.class Types.class Codegen.class
	javac $(FLAGS) ast.java

# ADDED: Compile Codegen
Codegen.class: Codegen.java
	javac $(FLAGS) Codegen.java

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
	jflex simple.jlex 
	mv Yylex.java simple.jlex.java 

sym.class: sym.java
	javac $(FLAGS) sym.java

# CHANGED: Added -expect 2 flag
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

hello:  hello.s
	spim -file hello.s

###
# clean
###
clean:
	rm -f *~ *.class parser.java simple.jlex.java sym.java

test:   test.sim P5.class 
	@echo "Running P5 on test.sim..."
	java -cp $(CLASSPATH) P5 test.sim test.s

###
# submit
###

submit:
	zip submit.zip *.java test.sim Makefile simple.jlex simple.cup