public class Memory {
	//Initial capacity of memory
	private static final int CAPACITY = 65536;
	//An array to store bytes
	private byte[] memory;

	//Constructors
	public Memory() {
		this(CAPACITY);
	}

	public Memory(int capacity) {
		memory = new byte[capacity];
	}

	//Size getter function
	public int size() {
		return memory.length;
	}

	//Memory-Read options
	public byte readByte(short address) {
		check(address);
		return (byte) (memory[address] & 0xFF);
	}

	public short readShort(short address) {
		check(address);
		byte a = (byte) memory[address++]; //Low order side
		check(address);
		return (short) ((memory[address] << 8) | (a & 0xFF)); //Merging 2 bytes into short
	}

	//Memory-Write options
	public void writeByte(short address, byte value) {
		check(address);
		memory[address] = value;
	}

	public void writeShort(short address, short value) {
		check(address);
		memory[address++] = (byte) (value & 0xFF); //Low order of 8 bits
		check(address);
		memory[address] = (byte) ((value & 0xFF00) >>> 8); //High order of 8 bits
	}

	//Address check
	public void check(short address) throws IllegalArgumentException {
		if (address < 0 || address >= memory.length) throw new IllegalArgumentException("Invalid memory address");
	}
}
