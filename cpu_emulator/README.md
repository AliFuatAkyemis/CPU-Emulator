CSE 206 CPU Emulator:

Abstract general purpose cpu emulator, as a default configuration run.sh runs the program.txt including. 
Compiles all the classes together by this format "java -cp bin Main program.txt config.txt". 
There are some additional configurations for running like debug mode(displays instructions on terminal) and single step mode (activates step by step mode).
These options can be activated by this format "java -cp bin Main program.txt config.txt [debug(true/false)] [singleStep(true/false)]" ("[]" means optional.
/emulator folder has the java files and /bin folder is the destination folder for compiled files.
You can also change this options by methods in CPUEmulator.java file code structure has been shown below.

Code Structure:

("*" = attribute, "->" = method)


-CPUEmulator.java

*memory:Memory
*cache:Cache
*cpuState:Boolean
*singleStep:Boolean
*debug:Boolean
*pc:int
*ac:int
*flag:int
*offset:short
*step:Scanner

->CPUEmulator():(Constructor)
->CPUEmulator(pcOffset:short, loadOffset:short):(Constructor)
->debugMode(state:boolean):void
->singleStepMode(state:boolean):void
->getCpuState():boolean
->hitRatio():void
->loadProgram(address:short, arr:short[]):void
->displayProgram(programLength:short):void
->executeNext():void
->execute(inst:short):void


-Memory.java

*CAPACITY:int
*memory:byte[]

->Memory():(Constructor)
->Memory(capacity:int):(Constructor)
->size():int
->readByte(address:short):byte
->readShort(address:short):short
->writeByte(address:short, value:byte):void
->writeShort(address:short, value:short):void
->check(address:short):void


-Cache.java

*CAPACITY:int
*cache:short[][]
*hit:double
*miss:double

->Cache():(Constructor)
->Cache(capacity:int):(Constructor)
->hitRatio():void
->miss():void
->write(address:short, value:short):void
->read(address:short):byte
->contains(address:short):boolean

To make testing phase easier after the execution finished program cannot shut itself done.
Because program can be runned from run.sh file directly and without this waiting time it closes the result screen immediately so at the end program waits for an input (Scanner(System.in)).
You can also shut the program by (Ctrl + C).
Single step mode is also working like that. Every step should be passed by giving an input like a letter. Press a letter and enter it to pass the next instruction.
