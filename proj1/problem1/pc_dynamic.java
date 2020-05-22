/*
* Maeva Lecavelier - 50191580
* problem1 : pc_dynamic.java
*/

import java.time.LocalDate;



class pc_dynamic {

    private static final int NUM_END = 200000;
    private static int NUM_THREAD = 4;

    public static void main(String [] args) {
        int i, j, counter;
        long beginning, ending, diffTime;
        counter = 0;

        if (args.length==1) {
            NUM_THREAD = Integer.parseInt(args[0]);
        }

        Thread mainThread = Thread.currentThread();
        Index index = new Index(0);

        PrimeThreadDynamic[] threads = new PrimeThreadDynamic[NUM_THREAD];

        beginning = System.currentTimeMillis();
        for(i = 0; i < NUM_THREAD; i++){
            threads[i] = new PrimeThreadDynamic(NUM_END, index);
            threads[i].start();
        }

        try{
            for(i = 0; i < NUM_THREAD; i++){
                threads[i].join();
                counter += threads[i].counter;
            }
        }
        catch (InterruptedException e) {}
        ending = System.currentTimeMillis();
        diffTime = ending - beginning;
        System.out.println("Num thread: "+NUM_THREAD+"\n");

        for(i = 0; i < NUM_THREAD; i++){
            System.out.println("Thread "+i+" : "+threads[i].exeTime+" ms.");
        }

        System.out.println("\nTotal execution time: "+diffTime+"ms");
        System.out.println("Between 1 and "+(NUM_END - 1)+", there are : "+counter+" prime numbers.");

      }

}


class PrimeThreadDynamic extends Thread {
    int max, counter;
    Index index;
    long exeTime;

    PrimeThreadDynamic(int numend, Index index){
        this.index = index;
        this.max = numend;
        this.counter = 0;
        this.exeTime = 0;
    }

    public void run(){
        int indexInt = index.getNextValue();
        long startTime = System.currentTimeMillis();
        /*
        *   threads stay in the loop until the
        *   static parameter index is equal to max (=NUM_END)
        */
        while(indexInt < this.max){
            if(isPrime(indexInt)) this.counter++;
            indexInt = index.getNextValue();
        }

        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        this.exeTime += diffTime;
    }

    private boolean isPrime(int x){
        int i;
        if(x <= 1) return false;
        for(i = 2; i < x; i++){
            if(x%i == 0) return false;
        }
        return true;
    }

}

// for easier manipulation of the static parameter
class Index {
    public static int value = 0;

    public Index(int value){
        this.value = value;
    }

    public synchronized int getNextValue(){
        this.value += 1;
        return this.value;
    }
}
