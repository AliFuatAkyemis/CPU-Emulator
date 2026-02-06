import java.util.Scanner;

public class CPUEmulator {
	//Memory instance
	private Memory memory;
	//Cache instance
	private Cache cache;
	//CPU state (execution started or not)
	private boolean cpuState, singleStep, debug;
	//Registers
	private short pc, ac, flag;
	//Load address
	private short offset;
	//Scanner for single step mode
	private Scanner step = new Scanner(System.in);
	
	//Constructors
	public CPUEmulator() {
		this((short) 0, (short) 0);
	}

	public CPUEmulator(short pcOffset, short loadOffset) {
		pc = (short) pcOffset;
		ac = (short) 0;
		flag = (short) 0;
		cpuState = false;
		singleStep = false;
		debug = false;
		offset = (short) loadOffset;
		memory = new Memory();
		cache = new Cache();
	}

	//Debug mode
	public void debugMode(boolean state) {
		debug = state;
	}

	//Single step mode
	public void singleStepMode(boolean state) {
		singleStep = state;
		if (state) debug = state;
	}

	//CPU state
	public boolean getCpuState() {
		return cpuState;
	}

	//Cache hit ratio
	public void hitRatio() {
		cache.hitRatio();
	}

	//Program loader
	public void loadProgram(short address, short[] arr) {
		for (int i = 0; i < arr.length; i++) {
			memory.writeShort((short) ((2*i)+address), (short) arr[i]);
		}
	}

	public void displayProgram(short programLength) {
		for (int i = offset; i < (offset+2*programLength); i += 2) {
			System.out.println(memory.readShort((short) i) & 0xFFFF);
		}
	}

	//Next instruction method
	public void executeNext() {
		execute(memory.readShort((short) pc));
		if (singleStep) step.next();
	}

	//Instruction decoder and executer
	public void execute(short inst) {
		//Determining the opcode and operand
		byte opcode = (byte) ((inst & 0xF000) >>> 12);
		short operand = (short) (inst & 0xFFF);
	
		//Decode and execute part
		switch (opcode) {
		case 0:
			//START
			if (debug) System.out.println("Start");
			cpuState = true;
			pc += (short) 2;
			break;
		case 1:
			//LOAD
			if (debug) System.out.println("Load");
			if (!cpuState) return;
			ac = (short) operand;
			pc += (short) 2;
			break;
		case 2:
			//LOADM
			if (debug) System.out.println("LoadM");
			if (!cpuState) return;
			if (cache.contains(operand)) {
				ac = (short) cache.read(operand);
			} else {
				ac = memory.readByte(operand);
				if (operand%2 == 0) cache.write((short) operand, memory.readShort(operand));
				else cache.write(operand, memory.readShort((short) (operand-1)));
			}
			pc += 2;
			break;
		case 3:
			//STORE
			if (debug) System.out.println("Store");
			if (!cpuState) return;
			cache.miss();
			memory.writeByte((short) operand, (byte) ac);
			if (operand%2 == 0) cache.write((short) operand, memory.readShort(operand));
			else cache.write((short) operand, memory.readShort((short) (operand-1)));
			pc += 2;
			break;
		case 4:
			//CMPM
			if (debug) System.out.println("Compare");
			if (!cpuState) return;
			if (cache.contains((short) operand)) {
				flag = cache.read((short) operand) == ac ? (short) 1 : (short) 0;
			} else {
				flag = memory.readByte((short) operand) == ac ? (short) 1 : (short) 0;
			}
			pc += 2;
			break;
		case 5:
			//CJMP
			if (debug) System.out.println("Conditional Jump");
			if (!cpuState) return;
			pc = flag > 0 ? (short) (offset + 2*operand) : (short) (pc+2);
			break;
		case 6:
			//JMP
			if (debug) System.out.println("Unconditional Jump");
			if (!cpuState) return;
			pc = (short) (offset + 2*operand);
			break;
		case 7:
			//ADD
			if (debug) System.out.println("Add");
			if (!cpuState) return;
			ac += (short) operand;
			pc += (short) 2;
			break;
		case 8:
			//ADDM
			if (debug) System.out.println("AddM");
			if (!cpuState) return;
			if (cache.contains((short) operand)) {
				ac += cache.read((short) operand);
			} else {
				ac += memory.readByte((short) operand);
				if (operand%2 == 0) cache.write((short) operand, memory.readShort(operand));
				else cache.write((short) operand, memory.readShort((short) (operand-1)));
			}
			pc += (short) 2;
			break;
		case 9:
			//SUB
			if (debug) System.out.println("Sub");
			if (!cpuState) return;
			ac -= (short) operand;
			pc += (short) 2;
			break;
		case 10:
			//SUBM
			if (debug) System.out.println("SubM");
			if (!cpuState) return;
			if (cache.contains((short) operand)) {
				ac -= cache.read((short) operand);
			} else {
				ac -= memory.readByte((short) operand);
				if (operand%2 == 0) cache.write((short) operand, memory.readShort(operand));
				else cache.write((short) operand, memory.readShort((short) (operand-1)));
			}
			pc += (short) 2;
			break;
		case 11:
			//MUL
			if (debug) System.out.println("Mul");
			if (!cpuState) return;
			ac *= (short) operand;
			pc += (short) 2;
			break;
		case 12:
			//MULM
			if (debug) System.out.println("MulM");
			if (!cpuState) return;
			if (cache.contains((short) operand)) {
				ac *= cache.read((short) operand);
			} else {
				ac *= memory.readByte((short) operand);
				if (operand%2 == 0) cache.write((short) operand, memory.readShort(operand));
				else cache.write((short) operand, memory.readShort((short) (operand-1)));
			}
			pc += 2;
			break;
		case 13:
			//DISP
			if (debug) System.out.println("Display");
			if (!cpuState) return;
			System.out.println("Accumulator: " + (ac & 0xFF));
			pc += (short) 2;
			break;
		case 14:
			//HALT
			if (debug) System.out.println("Halt");
			if (!cpuState) return;
			cpuState = false;
			pc += (short) 2;
			break;
		}
	}
}
