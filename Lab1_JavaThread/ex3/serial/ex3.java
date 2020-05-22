class ex3 {
  private static double NUM_STEPS = 100000;

  public static void main(String[] args){

    double i, x, sum, step;
    sum = 0.0;
    step = 1/NUM_STEPS;
    for(i = 0; i < NUM_STEPS; i++){
      x = (i + 0.5)*step;
      sum += f(x);
    }
    sum = sum * step;
    System.out.println(sum);
  }

  static double f(double x){
    double res;
    res = 4/(1+x*x);
    return(res);
  }
}
