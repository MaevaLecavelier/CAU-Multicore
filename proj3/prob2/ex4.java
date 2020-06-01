/*
    * Maeva Lecavelier - 50191580
    *
    * AtomicInteger
*/
/*
    * No special situation here.
    * Only a Atomic Integer with 24 threads
    * 8 will addAndGet and 8 will getAndAdd.
    * 8 threads with random sleep time will set
    * randomly a new value to the atomic integer

*/

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class ex4 {

    public static void main(String[] args){
        AtomicInteger count = new AtomicInteger();
        Random random = new Random();
        int number = random.nextInt(100);
        count.set(number);
        System.out.println("Initial value: count ="+count.get());

        for(int i = 0; i < 5; i++){
            new AddGetThread("ag"+i, count).start();
            new GetAddThread("ga"+i, count).start();
            new SetThread("s"+i, count).start();
        }
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Final value: count = "+count.get());
    }
}

class AddGetThread extends Thread {
    AtomicInteger count;
    String name;

    AddGetThread(String name, AtomicInteger count){
        this.name = name;
        this.count = count;
    }

    public void run(){
        try{
            Thread.sleep((int)(Math.random()*1000));
            System.out.println("Thread "+this.name+" wants addAndGet");
            System.out.println("Thread "+this.name+" addAndGet: count="+this.count.addAndGet(1));
            System.out.println("Thread "+this.name+" addAndGet done. Count="+this.count.get());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

class GetAddThread extends Thread {
    AtomicInteger count;
    String name;

    GetAddThread(String name, AtomicInteger count){
        this.name = name;
        this.count = count;
    }

    public void run(){
        try{
            Thread.sleep((int)(Math.random()*1000));
            System.out.println("Thread "+this.name+" wants getAndAdd");
            System.out.println("Thread "+this.name+" getAndAdd: count="+this.count.getAndAdd(1));
            System.out.println("Thread "+this.name+" getAndAdd done. Count="+this.count.get());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}


class SetThread extends Thread {
    AtomicInteger count;
    String name;

    SetThread(String name, AtomicInteger count){
        this.name = name;
        this.count = count;
    }

    public void run(){
        try{
            Thread.sleep((int)(Math.random()*1000));
            Random random = new Random();
            int number = random.nextInt(100);
            System.out.println("Thread "+this.name+" wants to set new value: "+number+".");
            this.count.set(number);
            System.out.println("Thread "+this.name+" set done. Count="+this.count.get());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
