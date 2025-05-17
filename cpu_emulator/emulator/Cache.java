public class Cache {
	//Initial capacity of cache
	private static final int CAPACITY = 8;
	//2D array to store tag and short values
	private short[][] cache;
	//variables to track cache hit
	private double hit = 0, miss = 0;

	//Constructors
	public Cache() {
		this(CAPACITY);
	}

	public Cache(int capacity) {
		cache = new short[capacity][2];
		for (int i = 0; i < cache.length; i++)
			cache[i][0] = (short) 0xF000;
	}

	//Basic calculation of hit ratio
	public void hitRatio() {
		System.out.print("Hit ratio : "); 
		System.out.println((double) ((hit/(hit+miss))*100));
	}

	//Cache miss modifier
	public void miss() {
		miss++;
	}

	//Cache-Write
	public void write(short address, short value) {
		//Obtaining tag and line datas
		short tag = (short) ((address & 0xFFF0) >>> 4);
		byte line = (byte) ((address & 0xE) >>> 1);

		//Putting the values
		cache[line][0] = tag;
		cache[line][1] = value;
	}

	//Cache-Read
	public byte read(short address) {
		//Obtaining tag, line and block datas
		short tag = (short) ((address & 0xFFF0) >>> 4);
		byte line = (byte) ((address & 0xE) >>> 1);
		byte block = (byte) (address & 0x1);
		
		//Reaching the value (2 bytes)
		short data = cache[line][1];

		//Returning the desired part of data
		if (block == 0) return (byte) (data & 0xFF);
		else return (byte) (((data & 0xFF00) >>> 8) & 0xFF);
	}

	//Check for cache contains the data
	public boolean contains(short address) {
		//Obtaining tag, and line datas
		short tag = (short) ((address & 0xFFF0) >>> 4);
		byte line = (byte) ((address & 0xE) >>> 1);


		//Check part
		if (cache[line][0] == tag) {
			hit++;
			return true;
		}
		miss++;

		return false;
	}
}
