# About
Author: Jory Anderson
Date: 03/02/2018
Description: To help me mine cryptocurrency while I am out and about. This will create a timer and attach it to a process(es), and will ask the user when to shutoff, and add that to current system time.


#Procedure
https://docs.oracle.com/javase/9/docs/api/java/util/Timer.html
https://docs.oracle.com/javase/9/docs/api/java/util/TimerTask.html


1. Create a ProcessTimer object that implements Thread
	Inst. Variables:
		private Timer timer
		private int process - the pid
		private int hours, minutes, seconds
	Methods:
		private getPID()
		getShutdownTimeInMilliseconds()
			return System.currentTimeMillis() + convertHours() + convertMinutes() +  convertSeconds()
		private isProcessRunning()
		private terminateProcess()
			for Process shutdown
		private shutdown()
			For system shutdown
	main()
		Create/Start timer.
		Create TimerTask to terminateProcess() or shutdown() at getShutdownTimeInMilliseconds()
			Check if process is still running before doing anything destructive.
		exit (release) thread
		
		
2. GUI:
	Try using JavaFX before using deprecated libs.
	https://docs.oracle.com/javase/9/docs/api/javax/swing/Timer.html
	https://docs.oracle.com/javase/9/docs/api/java/awt/event/ActionEvent.html
	
	1. Show running executables and their pid
		(Give option for system shutdown or process shutdown?)
	2. Select an executable and the time until termination, and click 'Add' to add to the timer. 
	3. Grey-out 'Add'
	4. Create thread, add necessary information from above, and run thread.
		4b. If terminateProcess() selected, loop back to 1.