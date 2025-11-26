Based on the project description, here is a logical roadmap to implement the Semantic Analysis. This order prioritizes building the **data structures** first, then the **infrastructure**, and finally the **logic passes** (Name Analysis followed by Type Checking).

### Phase 1: Data Structures & Infrastructure
*Goal: Prepare the classes that hold type and scope information before implementing the logic.*

**1. `Types.java`**
* **Action:** Review or Implement.
* [cite_start]**Why:** You need constants or objects to represent `int`, `boolean`, `void`, and `error` types before you can store them in symbols [cite: 87-91].
* [cite_start]**Details:** Ensure you have a type for `ErrorType` to prevent cascading error messages[cite: 92].

**2. `Sym.java`**
* **Action:** Extend the class.
* **Why:** Symbols are the "values" stored in your symbol table.
* **Details:**
    * [cite_start]Add a field for the **Type** (using `Types.java`)[cite: 55].
    * [cite_start]For methods, add a field to store a list of parameter types[cite: 56].
    * [cite_start]Add a `toString` or `print` method to help with debugging and the decompiler output[cite: 60].

**3. `SymbolTable.java`**
* **Action:** Implement the class.
* **Why:** This is the core engine for Name Analysis.
* **Details:**
    * Implement a "Scope Stack" (e.g., a `LinkedList<HashMap<String, Sym>>`).
    * Add methods to:
        * `enterScope()` (push new map).
        * `exitScope()` (pop map).
        * `lookup(String name)` (search from top of stack down to global).
        * `insert(String name, Sym symbol)` (add to the current top map).
        * [cite_start]Check for duplicates in the *current* scope[cite: 33, 38].

**4. `ast.java` (Structural Changes Only)**
* **Action:** Modify `IdNode`.
* **Why:** The AST needs to hold the link to the symbol table.
* **Details:**
    * [cite_start]Add a field `Sym link` (or similar name) to `IdNode`[cite: 21].
    * [cite_start]Update the `decompile()` method in `IdNode` to print the type in parentheses `(type)` after the name[cite: 45].

---

### Phase 2: Driver & Compilation
*Goal: Get the project compiling with the new files so you can test incrementally.*

**5. `P4.java`**
* **Action:** Create the new main driver.
* **Why:** You need to replace `P3.java`.
* **Details:**
    * Copy `P3.java` structure.
    * After parsing, instantiate `SymbolTable`.
    * Call `root.analyzeNames()` (Pass 1).
    * Call `root.decompile()` (to check if names are linked).
    * [cite_start]Call `root.checkTypes()` (Pass 2) [cite: 42-44].

**6. `Makefile`**
* **Action:** Update.
* **Why:** To build the new files.
* [cite_start]**Details:** Include `P4.java` and ensure `Sym.java`, `SymbolTable.java`, and `Types.java` are compiled[cite: 23].

---

### Phase 3: Pass 1 - Name Analysis
*Goal: Link identifiers to their declarations and detect scope errors.*

**7. `ast.java` (Name Analysis Logic)**
* **Action:** Implement `analyzeNames(SymbolTable st)` methods.
* **Order of operations:**
    1.  **Declarations (`DeclNode` subclasses):**
        * Check if the name is already in the *current* scope. [cite_start]If so, print error `Multiply declared identifier`[cite: 86].
        * [cite_start]If not, create a `Sym` object (with type info) and `insert` it into the Symbol Table[cite: 63].
    2.  **Scopes (`MethodDecl`, `BlockNode`):**
        * Call `st.enterScope()` before processing children.
        * [cite_start]Call `st.exitScope()` after processing children[cite: 34].
    3.  **Variable Usage (`IdNode` in expressions/statements):**
        * Perform a `lookup` in the Symbol Table.
        * If found, link the `IdNode` to the `Sym`.
        * [cite_start]If not found, print error `Undeclared identifier`[cite: 86].
    4.  [cite_start]**Main Check:** Ensure a method named `main` was declared globally[cite: 79].

---

### Phase 4: Pass 2 - Type Checking
*Goal: Ensure operations (math, logic, assignment) are type-safe.*

**8. `ast.java` (Type Checking Logic)**
* **Action:** Implement `checkTypes()` methods.
* **Details:**
    * **Leaves (`IntLit`, `TrueNode`):** Return `Types.IntType` or `Types.BoolType`.
    * **Identifiers (`IdNode`):** Return the type stored in the linked `Sym`.
    * **Expressions (`PlusNode`, `AndNode`, etc.):**
        * Recursively check children.
        * [cite_start]Verify children match requirements (e.g., `+` needs `int`, `&&` needs `bool`)[cite: 97].
        * If mismatch, print error and return `ErrorType`.
        * [cite_start]If `ErrorType` is a child, do *not* print a new error (cascade prevention)[cite: 92].
    * **Statements (`AssignNode`, `IfStmt`):**
        * Verify LHS matches RHS type.
        * Verify conditions are `boolean`.
    * [cite_start]**Function Calls:** Check argument count and types against the method's `Sym`[cite: 101].
    * [cite_start]**Returns:** Check that non-void methods actually return a value of the correct type[cite: 105].

---

### Phase 5: Testing
*Goal: Verify correctness and edge cases.*

**9. Test Files**
* **Action:** Create `.sim` files.
* **Details:**
    * [cite_start]`name_errors.sim`: Test duplicate vars, undeclared vars, missing main[cite: 47].
    * [cite_start]`type_errors.sim`: Test `int + bool`, `bool || int`, invalid return types[cite: 48].
    * [cite_start]`test.sim`: A valid program to ensure no errors are thrown when correct[cite: 48].

**10. Docker Execution**
* **Action:** Run inside the container.
* [cite_start]**Details:** `docker run -it markusmock/ib610:typechecker /bin/bash`[cite: 27].

---

### Recommended Next Step
Would you like me to start by generating the code skeleton for **SymbolTable.java**, as that is the foundational dependency for the rest of the project?