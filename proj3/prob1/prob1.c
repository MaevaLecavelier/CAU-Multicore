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
    if(argc != 3){
        printf("You should specify which schedule you want (1->static, 2-> dynamic, 3->guided) and how many threads to use.\n");
        exit(0);
    }
    int num_threads, i, tid, schedule;
    int end_num = 200000;
    int sum = 0;
    double start, end;
    schedule = atoi(argv[1]);
    num_threads = atoi(argv[2]);
    printf("Number of threads: %d\n", num_threads);
    omp_set_num_threads(num_threads);


    if(schedule == 1){
        start = omp_get_wtime();
        #pragma omp parallel for reduction(+:sum) private(tid) schedule(static)
        for(i = 0; i < end_num; i++){
            if(isPrime(i)) sum++;
        }
        end = omp_get_wtime();
        printf("Static load balancing: %.4f ms.\n",(end-start)*1000);
    }

    else if(schedule == 2){
        start = omp_get_wtime();
        #pragma omp parallel for reduction(+:sum) private(tid) schedule(dynamic, 4)
        for(i = 0; i < end_num; i++){
            if(isPrime(i)) sum += 1;
        }
        end = omp_get_wtime();
        printf("Dynamic load balancing: %.4f ms.\n",(end-start)*1000);
    }

    else if(schedule == 3){
        sum = 0;
        start = omp_get_wtime();
    #pragma omp parallel for reduction(+:sum) private(tid) schedule(guided, 4)
        for(i = 0; i < end_num; i++){
            if(isPrime(i)) sum += 1;
        }
        end = omp_get_wtime();
        printf("Guided load balancing: %.4f ms.\n",(end-start)*1000);
    }

    printf("\nThere are %d prime number between 0 and %d.\n", sum, end_num);

}
