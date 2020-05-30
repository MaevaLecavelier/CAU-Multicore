#include <pthread.h>
#include <stdio.h>

void *c(void* i){
  printf("Thread %d says hi\n",i);
  printf("Thread %d says bye\n",i);
}

int main(int argc, char *argv[]){
  pthread_t threads[5];
  int rc, t;
  printf("main thread begins\n");
  for(t = 1; t <= 5; t++){
    rc = pthread_create(&threads[t], NULL, c, (void *)t);
    if(rc){
      printf("ERROR code id %d\n", rc);
      exit(-1);
    }
  }
  printf("main thread ends\n");
  pthread_exit(NULL);

}
