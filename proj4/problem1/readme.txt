---- Maeva Lecavelier 50191580 ----

1- cuda_ray.cu
	To compile cuda_ray.cu:
		$nvcc cuda_ray.cu -o cuda_ray
	To execute it:
		$./cuda_ray.exe result.ppm

2- openmp_ray.cpp
	I compiled it with Visual Studio. But this should work (g++ is not install on my computer)
		$g++ -fopenmp openmp_ray.cpp -o openmp_ray
	To execute it:
		$./openmp_ray.exe [num_thread] result.ppm //where num thread is strictly positive

The execution time is the time for threads to execute their part. The longest task is writing the bitmap into the file.
		