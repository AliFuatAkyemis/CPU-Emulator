# CPU Emulator

A 16-bit CPU emulator abstraction implemented in Java, originally developed for the **CSE206 Computer Organization** course at **Akdeniz University**.

## Project Overview

This project simulates a simple computer system, including a CPU with a basic instruction set, main memory, and a direct-mapped cache. It is designed to demonstrate low-level computer architecture principles such as instruction decoding, memory management, and cache hit/miss logic.

## System Specifications

*   **Architecture**: 16-bit CPU abstraction.
*   **Memory**: 65,536 bytes (64 KB) of byte-addressable main memory.
*   **Cache**: Direct-mapped, 16 bytes total (8 blocks × 2 bytes).
*   **Byte Ordering**: Little-endian.
*   **Instruction Format**: 16-bit instructions consisting of a 4-bit opcode and a 12-bit operand.

## System Flowchart

The following diagram illustrates the instruction execution cycle of the emulator:

```mermaid
graph TD
    Start([Start Emulator]) --> Init[Initialize PC, AC, Flags & Memory]
    Init --> Fetch[Fetch 16-bit Instruction at PC]
    Fetch --> Decode[Decode 4-bit Opcode & 12-bit Operand]
    Decode --> Execute{Execute Opcode}
    
    Execute -- "START (0000)" --> SetState[cpuState = true]
    Execute -- "LOAD/ADD/SUB/MUL" --> RegOp[Update AC with Immediate]
    Execute -- "LOADM/ADDM/SUBM/MULM" --> MemRead[Read from Cache/Memory to AC]
    Execute -- "STORE (0011)" --> MemWrite[Write AC to Cache/Memory]
    Execute -- "CMPM (0100)" --> Compare[Update Flag Register]
    Execute -- "CJMP (0101)" --> CondJump[Jump if Flag > 0]
    Execute -- "JMP (0110)" --> UncondJump[Set PC to Target Address]
    Execute -- "DISP (1101)" --> Display[Print Accumulator Value]
    Execute -- "HALT (1110)" --> Halt[cpuState = false]
    
    SetState --> IncPC[Increment PC by 2]
    RegOp --> IncPC
    MemRead --> IncPC
    MemWrite --> IncPC
    Compare --> IncPC
    Display --> IncPC
    Halt --> IncPC
    
    CondJump --> Loop{cpuState == true?}
    UncondJump --> Loop
    IncPC --> Loop
    
    Loop -- Yes --> Fetch
    Loop -- No --> End([End Execution])
```

## Instruction Set Architecture (ISA)

The emulator supports the following 15 instructions:

| Opcode | Mnemonic | Description |
| :--- | :--- | :--- |
| `0000` | **START** | Begin execution |
| `0001` | **LOAD** | AC = Immediate value |
| `0010` | **LOADM** | AC = Memory[address] (cached) |
| `0011` | **STORE** | Memory[address] = AC (cached) |
| `0100` | **CMPM** | Compare AC with Memory[address]; set flag |
| `0101` | **CJMP** | Jump to address if comparison flag > 0 |
| `0110` | **JMP** | Unconditional jump to address |
| `0111` | **ADD** | AC += Immediate value |
| `1000` | **ADDM** | AC += Memory[address] (cached) |
| `1001` | **SUB** | AC -= Immediate value |
| `1010` | **SUBM** | AC -= Memory[address] (cached) |
| `1011` | **MUL** | AC *= Immediate value |
| `1100` | **MULM** | AC *= Memory[address] (cached) |
| `1101` | **DISP** | Print Accumulator (AC) value |
| `1110` | **HALT** | Stop execution |

## Code Structure (UML)

The internal architecture follows a modular design, separating memory management, caching logic, and the central processing unit:

```mermaid
classDiagram
    class CPUEmulator {
        -Memory memory
        -Cache cache
        -boolean cpuState
        -boolean singleStep
        -boolean debug
        -short pc
        -short ac
        -short flag
        -short offset
        -Scanner step
        +CPUEmulator()
        +CPUEmulator(short pcOffset, short loadOffset)
        +debugMode(boolean state) void
        +singleStepMode(boolean state) void
        +getCpuState() boolean
        +hitRatio() void
        +loadProgram(short address, short[] arr) void
        +displayProgram(short programLength) void
        +executeNext() void
        +execute(short inst) void
    }

    class Memory {
        -int CAPACITY
        -byte[] memory
        +Memory()
        +Memory(int capacity)
        +size() int
        +readByte(short address) byte
        +readShort(short address) short
        +writeByte(short address, byte value) void
        +writeShort(short address, short value) void
        +check(short address) void
    }

    class Cache {
        -int CAPACITY
        -short[][] cache
        -double hit
        -double miss
        +Cache()
        +Cache(int capacity)
        +hitRatio() void
        +miss() void
        +write(short address, short value) void
        +read(short address) byte
        +contains(short address) boolean
    }

    CPUEmulator o-- Memory
    CPUEmulator o-- Cache
```

## File Formats

### `program.txt`
Contains binary instructions as 16-bit strings, one per line.
Example:
```text
0000000000000000
0001000000010100
...
```

### `config.txt`
Always consists of exactly two lines:
1.  **Program Load Address**: Hexadecimal (e.g., `0x1000`).
2.  **Initial PC Value**: Hexadecimal absolute address (e.g., `0x1000`).

## Getting Started

### Prerequisites
*   Java Development Kit (JDK) 8 or higher.

### Compilation and Execution
You can use the provided `run.sh` script to compile and run the emulator with default settings:
```bash
cd cpu_emulator
chmod +x run.sh
./run.sh
```

Alternatively, compile and run manually:
```bash
# Compile
javac -d bin cpu_emulator/emulator/*.java

# Run
java -cp bin a120210808043 program.txt config.txt [debug] [singleStep]
```

### Command-line Arguments
*   `program.txt`: Path to the instruction file.
*   `config.txt`: Path to the configuration file.
*   `debug` (optional): `true/false` - Displays instructions on the terminal during execution.
*   `singleStep` (optional): `true/false` - Activates step-by-step mode (press any key to advance).

## Project Structure

*   `cpu_emulator/`: Root project directory.
    *   `emulator/`: Source code (`.java` files).
        *   `CPUEmulator.java`: Core CPU logic and instruction decoding.
        *   `Memory.java`: Main memory simulation.
        *   `Cache.java`: Cache controller and hit/miss logic.
        *   `a120210808043.java`: Entry point and file loader.
    *   `bin/`: Destination for compiled `.class` files.
    *   `program.txt`: Default sample program.
    *   `config.txt`: Default configuration.
    *   `Report.pdf`: Detailed project report.
    *   `CSE206-A1_CPU_Emulatorv2.pdf`: Original assignment instructions.
