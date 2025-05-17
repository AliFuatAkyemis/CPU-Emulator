import java.io.*;
import java.util.Scanner;

public class a120210808043 {
	public static void main(String[] args) {
		String filename = args[0]; //Program
		String config = args[1]; //Configuration

		CPUEmulator cpu = null; //Initial CPU

		try {
			//-----------------------------------------------------
			//initial pc and load address extraction
			BufferedReader br = new BufferedReader(new FileReader(config));

			int a = Integer.valueOf(br.readLine().substring(2).trim(), 16);
			int b = Integer.valueOf(br.readLine().substring(2).trim(), 16);

			short loadAddress = (short) a;
			cpu = new CPUEmulator((short) b, (short) a);

			br.close();
			//-----------------------------------------------------

			//-----------------------------------------------------
			//Determining the length of instruction file
			br = new BufferedReader(new FileReader(filename));

			String str = br.readLine();
			int n = 0;
			
			while (str != null) {
				n++;
				str = br.readLine();
			}

			br.close();
			//------------------------------------------------------
			
			//------------------------------------------------------
			//Putting all the instructions in a short array
			br = new BufferedReader(new FileReader(filename));
			short[] arr = new short[n];

			for (int i = 0; i < n; i++) {
				int instruction = Integer.valueOf(br.readLine().trim(), 2);
				arr[i] = (short) instruction; 
			}

			cpu.loadProgram(loadAddress, arr); //Program loading
			br.close();
			//-------------------------------------------------------
			//Checking the debug and single-step options
			if (args.length > 2) cpu.debugMode(Boolean.parseBoolean(args[2]));
			if (args.length > 3) cpu.singleStepMode(Boolean.parseBoolean(args[3]));
			//-------------------------------------------------------

			//-------------------------------------------------------
			//Execution phase
			do {
				cpu.executeNext();
			} while (cpu.getCpuState());

			cpu.hitRatio(); //Hit ratio
			//-------------------------------------------------------
			new Scanner(System.in).next();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
