import java.util.concurrent.TimeUnit;

class SumThread extends Thread {
  int low, high;
  int[] arr;
  int res = 0;
  long executionTime;

  SumThread(int[] a, int l, int h) {
    low = l;
    high = h;
    arr = a;
  }

  public void run() {
    long startTime = System.nanoTime();
    for(int i = low; i < high; i++){
      res += arr[i];
    }
    long endTime = System.nanoTime();
    executionTime = (endTime - startTime)/1000;
  }
}

class ex2 {

  private static int NUM_END = 10000;
  private static int NUM_THREAD = 4;


  public static void main(String[] args){
    if(args.length == 2) {
      NUM_THREAD = Integer.parseInt(args[0]);
      NUM_END = Integer.parseInt(args[1]);
    }
    int[] int_arr = new int [NUM_END];
    int i, s;
    for(i = 0; i<NUM_END; i++) {
      int_arr[i] = i + 1;
    }
    long startTime = System.nanoTime();
    s = sum(int_arr);
    long endTime = System.nanoTime();
    long timeElapsed = (endTime - startTime)/1000;

    System.out.println("sum = "+s);
    System.out.println("Time elapsed = "+timeElapsed+" µs");
  }


  static int sum(int[] arr){
    int sum, i, len, step, totaltime;
    len = arr.length;
    step = len/NUM_THREAD;
    sum = 0;
    totaltime = 0;
    long tmpstarttime = System.nanoTime();
    SumThread[] threads = new SumThread[NUM_THREAD];
    for(i = 0; i < NUM_THREAD; i++){
      threads[i] = new SumThread(arr, i*step, (i+1)*step);
      threads[i].start();
    }

    for(i = 0; i < NUM_THREAD; i++){
      try{
        threads[i].join();
        //System.out.println("Execution time for thread "+i+" is : "+threads[i].executionTime+ " µs.");

      }
      catch (InterruptedException e) {}
      sum += threads[i].res;
      totaltime += threads[i].executionTime;
    }

    //System.out.println("Total time for the threads: "+totaltime+ " µs.");
    return sum;
  }


}
