/*
* Maeva Lecavelier - 50191580
* problem1 : pc_static.java
*/


import java.time.LocalDate;


class pc_static {

    private static final int NUM_END = 200000;
    private static int NUM_THREAD = 4;


    public static void main(String [] args) {
        int i, counter, gap, leftover;
        /*
        *   gap and leftover are used for distributing data to threads
        *   in case of NUM_END/NUM_THREAD isn't an integer
        */
        long beginning, ending, diffTime;


        if (args.length==1) {
            NUM_THREAD = Integer.parseInt(args[0]);
        }

        gap = NUM_END/NUM_THREAD;
        leftover = NUM_END%NUM_THREAD;

        System.out.println("Num thread: "+NUM_THREAD);

        PrimeThreadStatic[] threads = new PrimeThreadStatic[NUM_THREAD];

        counter = 0;

        beginning = System.currentTimeMillis();

        for(i = 0; i < NUM_THREAD - 1; i++){
            threads[i] = new PrimeThreadStatic(i*gap, (i+1)*gap);
            threads[i].start();
        }
        // the last thread get the left numbers in case of NUM_END/NUM_THREAD isn't an int
        threads[NUM_THREAD-1] = new PrimeThreadStatic(i*gap, (i+1)*gap + leftover);
        threads[NUM_THREAD-1].start();

        try{
            for(i = 0; i < NUM_THREAD; i++){
                threads[i].join();
                counter += threads[i].counter;
            }
        }
        catch(InterruptedException e) {}

        ending = System.currentTimeMillis();
        diffTime = ending - beginning;
        for(i = 0; i < NUM_THREAD; i++)  System.out.println("Thread "+i+" execution time: "+threads[i].exeTime+" ms.");
        System.out.println("\nTotal exection time: "+diffTime+" ms.");
        System.out.println("Between 1 and "+(NUM_END - 1)+", there are : "+counter+" prime numbers.");
    }
}


class PrimeThreadStatic extends Thread {
    int begin, end, counter;
    long exeTime;

    PrimeThreadStatic(int begin, int end){
        this.begin = begin;
        this.end = end;
        this.counter = 0;
    }

    public void run(){
        this.exeTime = 0;
        long startTime = System.currentTimeMillis();
        for(int x = this.begin; x < this.end; x++){
            if(isPrime(x)) this.counter++;
        }
        long endTime = System.currentTimeMillis();
        this.exeTime = endTime - startTime;
    }

    private static boolean isPrime(int x){
        int i;
        if(x <= 1) return false;
        for(i = 2; i < x; i++){
            if(x%i == 0) return false;
        }
        return true;
    }

}
