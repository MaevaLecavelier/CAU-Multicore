#include <omp.h>
#include <stdio.h>
#include <stdlib.h>


int isPrime(int n){
    if(n<=1)
        return 0;
    for(int i = 2; i < n; i++){
        if(n%i == 0)
            return 0;
    }
    return 1;
}


int main(int argc, char *argv[]){
    int num_threads = 4;
    int end_num = 10000;
    int sum = 0;
    int i,x;
    double start, end;
    if(argc == 2){
        num_threads = atoi(argv[1]);
    }

    printf("Number of threads: %d\n", num_threads);
    omp_set_num_threads(num_threads);
    start = omp_get_wtime();
#pragma omp parallel for reduction(+:sum) private(x) schedule(static)
    for(i = 0; i < end_num; i++){
        x = isPrime(i);
        if(x == 1) sum++;
    }
    end = omp_get_wtime();
    printf("Static load balancing: %.4f ms.\n",(end-start)*1000);


    sum = 0;
    start = omp_get_wtime();
#pragma omp parallel for reduction(+:sum) schedule(dynamic)
    for(i = 0; i < end_num; i++){
        if(isPrime(i)) sum += 1;
    }
    end = omp_get_wtime();
    printf("Dynamic load balancing: %.4f ms.\n",(end-start)*1000);


    sum = 0;
    start = omp_get_wtime();
#pragma omp parallel for reduction(+:sum) schedule(guided)
    for(i = 0; i < end_num; i++){
        if(isPrime(i)) sum += 1;
    }
    end = omp_get_wtime();
    printf("Guided load balancing: %.4f ms.\n",(end-start)*1000);

    printf("\nThere are %d prime number between 0 and %d.\n", sum, end_num);

}
