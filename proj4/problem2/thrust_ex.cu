// Maeva Lecavelier - 50191580
// I choose exercice (b), integral approximation

#include <thrust/host_vector.h>
#include <thrust/device_vector.h>

#include <thrust/sequence.h>
#include <thrust/transform.h>

#include <stdio.h>
#include <time.h>
#include <iostream>


#define NUM_STEPS 200000
#define STEP 1/NUM_STEPS

struct integral
{
    __host__ __device__
        float operator() (int xi) const {
            float x;
            x = (float)xi * STEP;
            //printf("%f\n", (float)STEP * (4/(1+x*x)));
            return (float)STEP * (4/(1+x*x));
        }
};

int main(){
    printf("Execution begins...\n");
    clock_t start_time = clock();
    thrust::device_vector<float> index(NUM_STEPS+1, 1); //array with index
    thrust::device_vector<float> res(NUM_STEPS+1, 1); // array with rectangle area
    thrust::sequence(index.begin(), index.end()); //fill array from 0 to NUM_STEPS
    thrust::transform(index.begin(), index.end(), res.begin(), integral()); //res[0] = f(O)*STEP = area of one rectangle
    float sum = thrust::reduce(res.begin(), res.end()); //we add all little rectangle areas. The result is the integral
    clock_t end_time = clock();
    printf("Execution ends.\n");
    printf("Execution time: %d ms. \n", end_time - start_time);
    printf("pi=%f\n", sum); //print result

    return 0;
}
