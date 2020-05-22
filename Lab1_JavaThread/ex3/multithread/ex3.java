class IntegrateThread extends Thread {
  long begin, end;
  double res, step;

  IntegrateThread(long b, long e, double s){
    this.begin = b;
    this.end = e;
    this.step = s;
    this.res = 0;
  }

  public void run(){
    long i;
    double x;
    for(i = this.begin; i < this.end; i++){
      x = (i+0.5)*this.step;
      this.res += 4/(1+x*x);
    }
  }

}


class ex3 {

  private static long NUM_STEPS = 100000;
  private static final int NUM_THREAD = 4;


  static double integrate(){

    double sum, step, res;
    long gap = NUM_STEPS/NUM_THREAD;
    step = 1/(double)NUM_STEPS;
    sum = 0.0;
    IntegrateThread[] threads = new IntegrateThread[NUM_THREAD];

    for(int i = 0; i < NUM_THREAD; i++){
      threads[i] = new IntegrateThread(i*gap, (i+1)*gap, step);
      threads[i].start();
    }

    for(int i = 0; i < NUM_THREAD; i++){
      try{
        threads[i].join();
      }
      catch(InterruptedException e) {}
      sum += threads[i].res;
    }
    res = sum*step;
    return(res);
  }



  public static void main(String[] args){

    double finalSum;
    finalSum = integrate();
    System.out.println("The result is: "+finalSum);

  }


}
