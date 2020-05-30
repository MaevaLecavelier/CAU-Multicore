/* static load balancing for integration calculation */
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <omp.h>

int main(int argc, char* argv[]){
    long num_steps = 100000;
    int num_threads = 4;
    int chunk = num_steps/num_threads;
    if(argc == 2){
        num_threads = atoi(argv[1]);
        chunk = num_steps/num_threads;
    }
    else if(argc == 3){
        num_threads =atoi(argv[1]);
        chunk = atoi(argv[2]);
    }
    printf("Number of thread: %d\n", num_threads);
    printf("Number of chunks: %d\n", chunk);

    int i;
    double pi, x, step, sum = 0.0;
    double start, end;
    omp_set_num_threads(num_threads);

    step = 1.0/(float)num_steps;
    printf("Main thread starts.\n");
    start = omp_get_wtime();

#pragma omp parallel for reduction(+:sum) private(x) schedule(guided, chunk)
    for(i = 0; i < num_steps; i++){
        x = (i+0.5)*step;
        sum += 4.0/(1.0+x*x);
    }

    end = omp_get_wtime();
    printf("Main thread ends.\n");
    pi = step*sum;
    printf("Exection time: %.3f ms\n", (end-start)*1000);
    printf("Pi : %f\n", pi);
}
