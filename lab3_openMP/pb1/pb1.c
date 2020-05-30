 #include <omp.h>
 #include <stdio.h>

 #define NUM_THREADS 4
 #define NUM_END 10000

 int main(){
     int i;
     int sum = 0;
     double start, end;
     omp_set_num_threads(NUM_THREADS);
     start = omp_get_wtime();
#pragma omp parallel
    {
            #pragma omp for reduction(+:sum)
            for( i = 1; i <= NUM_END; i++){
                sum += i;
            }
    }
    end = omp_get_wtime();
    printf("sum 1+2+...+%d = %d\n",NUM_END, sum);
    printf("time elapsed %.3f ms\n", (end-start)*1000);
    return 1;
 }
