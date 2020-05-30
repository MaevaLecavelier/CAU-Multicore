#include <pthread.h>
#include <stdio.h>
#include <time.h>


#define NUM_THREADS 8
#define NUM_END 10000
#define array_size NUM_END

int arr[array_size];
int global_index;
int cpt = 0;
pthread_mutex_t mutex1;


int isPrime(int n){
    if((n == 0) || n == 1)
        return(0);
    for(int i = 2; i < n; i++){
        if(n%i == 0)
            return(0);
    }
    return(1);
}


void *isPrimeThread(void *ignored){
    int local_index, x = 0;
    int local_cpt = 0;
    do{
        pthread_mutex_lock(&mutex1);
        local_index = global_index;
        global_index++;
        pthread_mutex_unlock(&mutex1);

        if(local_index < array_size){
            x = isPrime(arr[local_index]);
            if(x == 1){
                local_cpt += 1;
            }
        }
    }while(global_index < array_size);

    pthread_mutex_lock(&mutex1);
    cpt += local_cpt;
    pthread_mutex_unlock(&mutex1);
}

int main(){
    pthread_t threads[NUM_THREADS];
    pthread_mutex_init(&mutex1, NULL);
    int i;
    clock_t start, end;
    float diff;
    for(i = 0; i < array_size; i++)
        arr[i]=i+1;

    printf("main thread begins\n");
    start = clock();
    for(i = 0; i < NUM_THREADS; i++){
        pthread_create(&threads[i], NULL, isPrimeThread, NULL);
    }
    for(i = 0; i < NUM_THREADS; i++){
        pthread_join(threads[i], NULL);
    }
    end = clock();
    diff = ((float)(end - start)/CLOCKS_PER_SEC)*1000;
    printf("main thread end.\n");
    printf("Execution time is %.3f ms\n", diff);
    printf("Between 0 and %d, there are %d prime numbers.\n", NUM_END, cpt);

}
