/*
    * Maeva Lecavelier - 50191580
    *
    * BlockingQueue and ArrayBlockingQueue
*/
/*
    * In store, only 10 toys can be stored in a shelf
    * So our ArrayBlockingQueue will reprensent the shelf.
    * Children will want to buy the toy and worker will
    * put new toys on the shelf.
    * Children consumes and worker produces
    * There are 10 children for 2 workers.
    }
*/

import java.util.concurrent.*;

public class ex1 {

    private static final int NUM_CHILD = 10;
    private static final int NUM_WORKER = 2;

	public static void main(String[] args) {
		BlockingQueue<String> store = new ArrayBlockingQueue<String>(10);

        Child child = new Child(store);
        Worker worker = new Worker(store);

        new Thread(child).start();
        new Thread(worker).start();
	}
}


class Child implements Runnable{
    protected BlockingQueue<String> store;


    Child(BlockingQueue<String> store){
        this.store = store;
    }

    @Override
    public void run(){
        try{
            for(int i = 0; i < 10; i++){
                System.out.println("Child n°"+i+" wants the toy.");
                store.take();
                System.out.println("Child n°"+i+" bought the toy.");
                Thread.sleep((int)(Math.random()*1000));
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

class Worker implements Runnable{
    protected BlockingQueue<String> store;


    Worker(BlockingQueue<String> store){
        this.store = store;
    }

    @Override
    public void run(){
        try{
            for(int i = 0; i < 10; i++){
                System.out.println("Worker n°"+i+" wants to add a toy.");
                store.put("toy");
                System.out.println("Worker n°"+i+" added the toy.");
                Thread.sleep((int)(Math.random()*2000));
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
