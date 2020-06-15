-- Maeva Lecavelier (마에바 르꺄블리에) 50191580 --

This is the readme.txt for proj3 prob1. Here are the instructions
for compilation and execution of the program.

Compile with OpenMP:
	$ gcc -fopenmp prob1.c
Run with two parameters: first to precising schedule and second to specify number of number threads.
-> schedule: 1 = static, 2 = dynamic, 3 = guided. Threads: any int number. Tested with {1,2,4,6,8,16}.
	$ ./a.out 1 6 // will run the code with static schedule and 6 threads. 
