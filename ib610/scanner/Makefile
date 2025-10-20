####
# This Makefile can be used to make a scanner for the Simple language
# (Yylex.class) and to make a program that tests the scanner (P2.class).
#
# make clean removes all generated files.
#
###

###
# define the Java compiler and flags
###
##                                                                             
JC = javac
CLASSPATH =  .:../jars/java-cup-11b-runtime.jar
FLAGS = -g -cp $(CLASSPATH)



###
# Here are the rules.
###

P2.class: P2.java Yylex.class sym.class
	$(JC) $(FLAGS) P2.java

Yylex.class: simple.jlex.java Errors.class sym.class
	$(JC) $(FLAGS) simple.jlex.java

simple.jlex.java: simple.jlex
	jflex simple.jlex # use jflex it is more flexiblej
	mv Yylex.java simple.jlex.java # rename the file produced by jflex

sym.class: sym.java
	$(JC) $(FLAGS) sym.java

Errors.class: Errors.java
	$(JC) $(FLAGS) Errors.java

clean:
	rm -f *.class simple.jlex.java *.zip


test:	test.sim P2.class
	@echo "If you get an error below your Scanner does not work yet!"
	@echo "Modify the simple.jlex specification to implement the language!"
	java -cp $(CLASSPATH) P2 test.sim

test2:	test2.sim P2.class
	@echo "If you get an error below your Scanner does not work yet!"
	@echo "Modify the simple.jlex specification to implement the language!"
	java -cp $(CLASSPATH) P2 test2.sim

test3:	test3.sim P2.class
	@echo "If you get an error below your Scanner does not work yet!"
	@echo "Modify the simple.jlex specification to implement the language!"
	java -cp $(CLASSPATH) P2 test3.sim




###
# handout
###

handout:
	zip handout.zip Errors.java Makefile simple.jlex P2.java test.sim test2.sim sym.java eof.sim
###
# submit
###

submit:
	zip submit.zip *.java Makefile simple.jlex test*.sim
