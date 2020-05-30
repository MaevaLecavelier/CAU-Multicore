/* static load balancing for integration calculation */
#include <stdio.h>
#include <omp.h>

#define NUM_STEPS 100000
#define NUM_THREADS 4

int main(int argc, char* argv[]){
    int i;
    double pi, x, step, sum = 0.0;
    double start, end;
    omp_set_num_threads(NUM_THREADS);
    step = 1.0/(float)NUM_STEPS;
    start = omp_get_wtime();
#pragma omp parallel for reduction(+:sum) private(x) schedule(dynamic)
    for(i = 0; i < NUM_STEPS; i++){
        x = (i+0.5)*step;
        sum += 4.0/(1.0+x*x);
    }
    end = omp_get_wtime();
    pi = step*sum;
    printf("Exection time: %.3f ms\n", (end-start)*1000);
    printf("Pi : %f\n", pi);
}
