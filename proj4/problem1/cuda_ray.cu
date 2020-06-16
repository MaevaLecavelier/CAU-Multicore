#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <ctime>
#include <chrono>
#include <thread>

#define SPHERES 20

#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048

struct Sphere {
	float   r, b, g;
	float   radius;
	float   x, y, z;
};


__global__
void kernel(Sphere* s, unsigned char* ptr)
{
    int x,y;
	x = blockIdx.x/8; // in range 0 to 2048, will be fixed for 2048 iterations (during this time, y will increment)
	y = threadIdx.x + 256*(blockIdx.x%8); //in range 0 to 2048, 256 is total number of threads, for the first block, it's just the thread ID

	int offset = x + y * 2048;
	float ox = (x - 2048 / 2);
	float oy = (y - 2048 / 2);

	//printf("x:%d, y:%d, ox:%f, oy:%f\n",x,y,ox,oy);

	float r = 0, g = 0, b = 0;
	float   maxz = -2e10f;
	for (int i = 0; i < 20; i++) {

		float   n, t;
        float dx = ox - s[i].x;
        float dy = oy - s[i].y;
        if (dx * dx + dy * dy < s[i].radius * s[i].radius) {
            float dz = sqrtf(s[i].radius * s[i].radius - dx * dx - dy * dy);
            n = dz / sqrtf(s[i].radius * s[i].radius);
            t = dz + s[i].z;
        }
        else{
            t = -2e10f;

        }
		if (t > maxz) {
			float fscale = n;
			r = s[i].r * fscale;
			g = s[i].g * fscale;
			b = s[i].b * fscale;
			maxz = t;
		}
	}
	ptr[offset * 4 + 0] = (int)(r * 255);
	ptr[offset * 4 + 1] = (int)(g * 255);
	ptr[offset * 4 + 2] = (int)(b * 255);
	ptr[offset * 4 + 3] = 255;

}


void ppm_write(unsigned char* bitmap, int xdim, int ydim, FILE* fp)
{
	int i, x, y;
	fprintf(fp, "P3\n");
	fprintf(fp, "%d %d\n", xdim, ydim);
	fprintf(fp, "255\n");
	for (y = 0;y < ydim;y++) {
		for (x = 0;x < xdim;x++) {
			i = x + y * xdim;
			fprintf(fp, "%d %d %d ", bitmap[4 * i], bitmap[4 * i + 1], bitmap[4 * i + 2]);
		}
		fprintf(fp, "\n");
	}
}


int main(int argc, char* argv[])
{
	unsigned char* bitmap;
	Sphere *dev_s; //for spheres in the GPU
	unsigned char *dev_bitmap; //for result in GPU
	srand(time(NULL));

	if (argc != 2) {
		printf("> a.out [filename.ppm]\n");
		printf("for example, '> a.out result.ppm'.\n");
		exit(0);
	}

	FILE* fp = fopen(argv[1], "w");

	Sphere* temp_s = (Sphere*)malloc(sizeof(Sphere) * SPHERES);
	for (int i = 0; i < SPHERES; i++) {
		temp_s[i].r = rnd(1.0f);
		temp_s[i].g = rnd(1.0f);
		temp_s[i].b = rnd(1.0f);
		temp_s[i].x = rnd(2000.0f) - 1000;
		temp_s[i].y = rnd(2000.0f) - 1000;
		temp_s[i].z = rnd(2000.0f) - 1000;
		temp_s[i].radius = rnd(200.0f) + 40;
	}
	cudaMalloc((void **)&dev_s, sizeof(Sphere) * SPHERES); //allocate place for sphere in GPU
	cudaMemcpy(dev_s, temp_s, sizeof(Sphere) * SPHERES, cudaMemcpyHostToDevice); //copy CPU Spheres into GPU

	bitmap = (unsigned char*)malloc(sizeof(unsigned char) * DIM * DIM * 4);
	cudaMalloc((void **)&dev_bitmap, sizeof(unsigned char) * DIM * DIM * 4); //allocate place for result in GPU
	cudaMemcpy(dev_bitmap, bitmap, sizeof(unsigned char) * DIM * DIM * 4, cudaMemcpyHostToDevice); //copy CPU bitmap into GPU

	printf("Execution begins...\n");
	clock_t start_time = clock();
    int block_size = 256; //256 threads by block
    int num_block = (DIM*DIM + block_size - 1)/block_size; //16384 blocks
    kernel<<<num_block,block_size>>>(dev_s, dev_bitmap);
    cudaDeviceSynchronize(); //CPU waits for GPU

	cudaMemcpy(bitmap, dev_bitmap, sizeof(unsigned char) * DIM * DIM * 4, cudaMemcpyDeviceToHost); //copy back the result from GPU to CPU

	clock_t end_time = clock(); //end of cuda computation
	ppm_write(bitmap, DIM, DIM, fp); //write result in file

	// free allocation
	fclose(fp);
	free(bitmap);
	free(temp_s);
	cudaFree(temp_s);

	clock_t diff_time = end_time - start_time;
	printf("\t Execution time: %d ms. \n", diff_time);
	printf("Execution ended.\n");

	return 0;
}
