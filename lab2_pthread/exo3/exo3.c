#include <pthread.h>
#include <stdio.h>
#include <time.h>
#include <math.h>

#define NUM_THREADS 10
#define NUM_STEPS 1000000

float global_abs = 0;
double sum = 0;
pthread_mutex_t mutex1;
float step = 1/(float)NUM_STEPS;

void *integrate(void *ignored){
    double local_abs, partial_sum, x = 0;
    do{
        pthread_mutex_lock(&mutex1);
        local_abs = global_abs;
        global_abs += step;
        pthread_mutex_unlock(&mutex1);

        if(local_abs < global_abs){
            partial_sum += step*(4/(1+local_abs*local_abs));
        }
    }
    while(global_abs <= 1);
    pthread_mutex_lock(&mutex1);
    sum += partial_sum;
    pthread_mutex_unlock(&mutex1);
}

int main(){
    pthread_t threads[NUM_THREADS];
    pthread_mutex_init(&mutex1, NULL);
    int t;
    clock_t start, end;
    float diffTime;
    printf("main thread start\n");
    start = clock();
    for (t = 0; t < NUM_THREADS; t++){
        pthread_create(&threads[t], NULL, integrate, NULL);
    }
    for (t = 0; t < NUM_THREADS; t++){
        pthread_join(threads[t], NULL);
    }
    end = clock();
    diffTime = ((float)(end - start)/CLOCKS_PER_SEC)*1000;
    printf("Time taken for the calculation: %.2f ms\n", diffTime);
    printf("Integration of 4.0/(1+x²) from 0 to 1 is: %f\n",sum);
    printf("main thread end\n");
}
