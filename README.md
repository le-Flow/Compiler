# Simple Language Compiler (IB610)

A compiler for the **Simple** language, built incrementally across multiple praktikum assignments. The compiler is implemented in Java using JFlex (lexer) and CUP (parser).

## Project Structure

```
ib610/
├── jars/                  # CUP runtime JARs
│   ├── java-cup-11b.jar
│   └── java-cup-11b-runtime.jar
├── scanner/               # P2 – Lexer / Scanner
├── parser/                # P3 – Parser + AST
└── typechecker/           # P4 – Name Analysis + Type Checking
```

## Compiler Phases

### Phase 1 – Scanner (`scanner/`)
Lexical analysis of Simple source files using JFlex.

**Tokens recognised:** keywords (`int`, `boolean`, `void`, `class`, `if`, `else`, `while`, `do`, `return`, `true`, `false`, `public`, `static`, `String`, `System.out.println`), identifiers, integer literals, string literals, operators, and delimiters.

**Comment handling:** single-line (`//`) and multi-line (`/* ... */`) comments via a JFlex state machine.

**Build & run:**
```bash
cd ib610/scanner
make          # builds Yylex and P2
make test     # runs P2 on test.sim
make submit   # creates submit.zip for Moodle
```

---

### Phase 2 – Parser (`parser/`)
Syntax analysis using CUP, producing an Abstract Syntax Tree (AST).

**Grammar highlights (`simple.grammar`):**
- `public class` wrapper with a class body of field and method declarations
- Methods are `public static void` or `public static int`
- Statements: `print`, assignment, `if`/`else`, `do`/`while`, `return`, function calls, nested blocks, `switch`/`case`
- Expressions: arithmetic, logical, comparison, unary minus/not, function calls, literals
- Power operator `**` (right-associative) — requires scanner extension

**Build & run:**
```bash
cd ib610/parser
make          # builds parser and P3
make test     # runs P3 on test.sim
make submit   # creates submit.zip for Moodle
```

---

### Phase 3 – Type Checker (`typechecker/`)
Semantic analysis: name analysis (scope/symbol table) followed by type checking.

**Name analysis:**
- Scope stack via `SymbolTable.java` (`enterScope` / `exitScope` / `lookup` / `insert`)
- Links each `IdNode` in the AST to its `Sym` declaration
- Errors: `Multiply declared identifier`, `Undeclared identifier`, missing `main`

**Type checking:**
- Type representation in `Types.java` (`IntType`, `BoolType`, `VoidType`, `ErrorType`)
- Validates operand types for all expressions and statements
- Checks function call argument counts and types
- Checks return types against method signatures
- `ErrorType` propagation prevents cascading error messages

**Build & run:**
```bash
cd ib610/typechecker
make          # builds parser, AST, SymbolTable, Types, and P4
make test     # runs P4 on test.sim
make submit   # creates submit.zip (P4.java, SymbolTable.java, Types.java, test files)
```

---

## Prerequisites

| Tool | Purpose |
|------|---------|
| `javac` / JDK | Compile Java sources |
| [JFlex](https://www.jflex.de/) | Generate the lexer from `simple.jlex` |
| CUP (`java-cup-11b.jar`) | Generate the parser from `simple.cup` |
| Docker (optional) | `docker run -it markusmock/ib610:typechecker /bin/bash` |

## Test Files (`.sim`)

Each phase includes `.sim` test programs for the Simple language:

| File | Purpose |
|------|---------|
| `test.sim` | Valid program — should produce no errors |
| `NameErrors.sim` | Duplicate / undeclared identifiers, missing `main` |
| `TypeErrors.sim` | Type mismatches, bad return types, wrong argument types |
| `functionCalls.sim` | Function call scenarios |
| `type_example*.sim` | Various valid and invalid typing examples |
