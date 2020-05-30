#include <pthread.h>
#include <stdio.h>
#include <time.h>

#define NUM_THREADS 4
#define NUM_END 10000
#define array_size NUM_END

int a[array_size];
int global_index = 0;
int sum = 0;
pthread_mutex_t mutex1;


void *add(void *ignored){
  int local_index, partial_sum = 0;
  do{
    pthread_mutex_lock(&mutex1);
    local_index = global_index;
    global_index++;
    pthread_mutex_unlock(&mutex1);

    if(local_index < array_size)
      partial_sum += *(a +local_index);
  }
  while(local_index < array_size);

  pthread_mutex_lock(&mutex1);
  sum += partial_sum;
  pthread_mutex_unlock(&mutex1);
}


int main(){
  pthread_t threads[NUM_THREADS];
  pthread_mutex_init(&mutex1, NULL);
  int rc, t;
  clock_t start, end;
  float diffTime;
  for (t = 1; t <= NUM_END; t++){
    a[t-1] = t;
  }
  printf("main thread start\n");
  start = clock();
  for (t = 0; t < NUM_THREADS; t++){
    pthread_create(&threads[t], NULL, add, NULL);

  }
  for (t = 0; t < NUM_THREADS; t++){
    pthread_join(threads[t], NULL);
  }
  end = clock();
  diffTime = ((float)(end - start)/CLOCKS_PER_SEC)*1000;
  printf("Time taken for the calculation: %.2f ms\n", diffTime);
  printf("Sum from 1 to %d is: %d\n",NUM_END,sum);
  printf("main thread end\n");
}
