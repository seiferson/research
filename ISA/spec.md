# ISA Specification [v0.01][fly]

This project defines a small CPU emulator with a minimal but complete instruction set. The goal is to model a simple machine that is easy to implement, easy to document, and still capable of running general programs through branching, arithmetic, memory access, and stack-based subroutines.

## Machine Model

The emulator models a word-addressed CPU with the following components:

- `r0` through `r7`: eight general-purpose registers.
- `pc`: program counter.
- `sp`: stack pointer.
- `fl`: flag register.
- `ram`: main memory.

## Word Size

- One machine word is `16` bits.
- All general-purpose registers store one word.
- Memory stores words.
- Arithmetic wraps on overflow unless otherwise stated.

## Register Conventions

- `r0` may be used as a normal register. It is not reserved.
- `sp` grows downward when values are pushed.
- `pc` points to the next instruction to execute.

## Flags

The `fl` register exposes the following status flags:

- `zero`: set when a result is equal to zero.
- `nega`: set when a result is negative in signed interpretation.
- `carr`: set when an arithmetic operation produces a carry or borrow.
- `ovfl`: set when signed overflow occurs.

Unless noted otherwise, arithmetic, logic, shift, and compare instructions update flags. Control-flow and stack-control instructions do not update flags unless their definition says so.

## Operand Forms

The assembly syntax supports the following operand types:

- `rn`: a general-purpose register such as `r3`.
- `#value`: an immediate literal.
- `label`: a symbolic code address.
- `[addr]`: a direct memory address.
- `[rn]`: a memory address stored in a register.

## General Execution Rules

- Instructions are executed sequentially unless control flow changes `pc`.
- Two-operand instructions use `dest, src` order.
- Results are written to the first operand unless stated otherwise.
- Memory-to-memory arithmetic is not required. At least one operand should be a register.
- `call` and `retn` use the stack through `sp`.

## Instruction Set

### Data Movement

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `move` | `dest, src` | Copies the source value into the destination register. `src` may be a register or immediate. Flags are not changed. |
| `load` | `dest, src` | Loads a word from memory into a register. `src` may be `[addr]` or `[rn]`. Flags are not changed. |
| `stor` | `dest, src` | Stores a word from a register into memory. `dest` may be `[addr]` or `[rn]`. Flags are not changed. |
| `push` | `src` | Decrements `sp`, then writes the source value to the stack. Flags are not changed. |
| `pull` | `dest` | Reads the word at `sp` into the destination register, then increments `sp`. Flags are not changed. |

### Arithmetic

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `addi` | `dest, src` | Adds `src` to `dest` and stores the result in `dest`. Updates `zero`, `nega`, `carr`, and `ovfl`. |
| `subt` | `dest, src` | Subtracts `src` from `dest` and stores the result in `dest`. Updates `zero`, `nega`, `carr`, and `ovfl`. |
| `mult` | `dest, src` | Multiplies `dest` by `src` and stores the low word of the result in `dest`. Updates `zero`, `nega`, and `ovfl`. |
| `divi` | `dest, src` | Divides `dest` by `src` and stores the quotient in `dest`. Division by zero must raise an emulator fault. Updates `zero` and `nega`. |
| `modu` | `dest, src` | Divides `dest` by `src` and stores the remainder in `dest`. Division by zero must raise an emulator fault. Updates `zero` and `nega`. |

### Logic and Bit Operations

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `andd` | `dest, src` | Performs bitwise AND and stores the result in `dest`. Updates `zero` and `nega`. |
| `orrr` | `dest, src` | Performs bitwise OR and stores the result in `dest`. Updates `zero` and `nega`. |
| `xorr` | `dest, src` | Performs bitwise exclusive OR and stores the result in `dest`. Updates `zero` and `nega`. |
| `nott` | `dest` | Performs bitwise inversion on the destination register. Updates `zero` and `nega`. |
| `shlf` | `dest, src` | Shifts `dest` left by the count in `src`. Updates `zero`, `nega`, and `carr`. |
| `shrg` | `dest, src` | Shifts `dest` right logically by the count in `src`. Updates `zero` and `carr`. |

### Comparison and Testing

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `cmpr` | `left, right` | Compares the two operands by internally computing `left - right`. The operands are not modified. Updates `zero`, `nega`, `carr`, and `ovfl`. |
| `test` | `src` | Updates `zero` and `nega` from the source value without changing the source. Useful before conditional jumps. |

### Control Flow

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `jump` | `target` | Sets `pc` to the target address unconditionally. |
| `jzer` | `target` | Jumps to the target if `zero` is set. |
| `jnzr` | `target` | Jumps to the target if `zero` is clear. |
| `jneg` | `target` | Jumps to the target if `nega` is set. |
| `jovf` | `target` | Jumps to the target if `ovfl` is set. |
| `call` | `target` | Pushes the current `pc` to the stack, then jumps to the target address. |
| `retn` | none | Pops the return address from the stack into `pc`. |

### System Control

| Mnemonic | Operands | Description |
| --- | --- | --- |
| `noop` | none | Performs no state change other than normal instruction advance. |
| `halt` | none | Stops execution cleanly. The emulator must terminate the current program without advancing further. |

## Minimum Capability Rationale

This instruction set is intentionally small, but it is sufficient for general computation because it includes:

- Data transfer between registers, memory, and immediates.
- Integer arithmetic for state changes and address calculations.
- Logic and shifts for masking, packing, and bit tests.
- Comparisons plus conditional and unconditional control flow.
- Stack operations for function calls and temporary storage.
- A halting instruction for explicit program termination.

## Execution

The emulator binary name is fixed and must always be `cpuemu`.

Programs are executed from the project root with this form:

```sh
./cpuemu <program_file>
```

Example:

```sh
./cpuemu program.inst
```

The first argument is the program file to evaluate.

## Program File Format

The runtime input file format is fixed-width and line-based so the emulator can read each instruction by direct substring split without token parsing.

Each program file must follow these rules:

- File extension should be `.inst`.
- Use lowercase only for opcodes and operands.
- Use one instruction per line.
- Do not use labels in `.inst` files.
- Do not use comments in `.inst` files.
- Use numeric addresses directly when a jump target or memory address is needed.
- Pad operand fields with spaces when an operand is shorter than its field width.
- Leave unused operand fields as spaces.

Each instruction line uses this layout:

| Columns | Width | Field | Rule |
| --- | --- | --- | --- |
| `1-4` | `4` | `opcode` | Exactly one four-letter instruction mnemonic. |
| `5` | `1` | `gap_1` | Must be one space. |
| `6-15` | `10` | `arg_1` | First operand, left-aligned and space-padded. |
| `16` | `1` | `gap_2` | Must be one space. |
| `17-26` | `10` | `arg_2` | Second operand, left-aligned and space-padded. |

This gives every instruction line a total width of `26` characters before the newline character.

The emulator should read the line as:

- `opcode = line[0:4]`
- `arg_1 = line[5:15]`
- `arg_2 = line[16:26]`

After splitting by position, each field may be right-trimmed for trailing spaces before evaluation. No delimiter parsing is required.

Allowed operand text inside each field keeps the same operand forms defined by this specification:

- `r0` through `r7`
- `#value`
- `[addr]`
- `[rn]`

## Operand Conventions

- Immediates use the `#` prefix.
- Direct memory references use brackets, such as `[1024]`.
- Indirect memory references use register brackets, such as `[r2]`.
- The first operand field is `dest` for two-operand instructions.
- The second operand field is `src` for two-operand instructions.

## Example

```text
move r1         #10       
move r2         #0        
addi r2         r1        
subt r1         #1        
test r1                   
jnzr #2                   
halt                      
```

This normalized `.inst` example accumulates the sum of values from `10` down to `1` into `r2`. The `jnzr #2` line jumps back to the instruction at line index `2`, using a resolved numeric target instead of a label.

## Emulator Requirements

- The emulator must decode all mnemonics exactly as defined here.
- Invalid opcodes must raise an emulator error.
- Invalid operand forms must raise an emulator error.
- Invalid line widths or invalid fixed-field spacing must raise an emulator error.
- Division by zero must raise an emulator fault.
- Stack underflow and stack overflow must raise emulator faults.
- Program execution must stop on `halt` or on a fatal fault.

## Future Extensions

The first implementation should stay within this document. Extensions such as interrupts, I/O ports, signed-vs-unsigned branch variants, wider registers, or floating-point support should be introduced only in later revisions.

## Appendix: Abbreviations

| Term | Meaning |
| --- | --- |
| `cpu` | Central Processing Unit |
| `isa` | Instruction Set Architecture |
| `ram` | Random Access Memory |
| `pc` | Program Counter |
| `sp` | Stack Pointer |
| `fl` | Flag Register |
| `dest` | Destination operand |
| `src` | Source operand |
| `zero` | Zero flag |
| `nega` | Negative flag |
| `carr` | Carry flag |
| `ovfl` | Overflow flag |
| `addr` | Memory address |
