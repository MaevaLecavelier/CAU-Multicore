class ex2 {

  private static int NUM_END = 16;


  static int sum(int[] arr) {
    int s = 0;
    for(int i = 0; i < arr.length; i++) s += arr[i];
    return(s);
  }


  public static void main(String[] args){
    if (args.length == 1) NUM_END = Integer.parseInt(args[0]);

    int[] int_arr = new int [NUM_END];
    int i, s;
    for(i = 0; i<NUM_END; i++) int_arr[i] = i + 1;
    s = sum(int_arr);
    System.out.println("Sum = "+s);
  } //end main

}
