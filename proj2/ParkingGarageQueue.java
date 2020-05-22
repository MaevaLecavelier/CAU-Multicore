/* -- Maeva Lecavelier 50191580 -- */

import java.util.concurrent.ArrayBlockingQueue;


public class ParkingGarageQueue {

    private static final int NUM_CARS = 40;

    public static void main(String[] args){
        ArrayBlockingQueue<String> parkingGarage = new ArrayBlockingQueue<String>(10);
        Thread[] cars = new Thread[NUM_CARS];
        for (int i = 1; i <= NUM_CARS; i++){
            cars[i-1] = new Thread(new Car("Car "+i, parkingGarage));
        }
    }
}


class Car extends Thread {
    public ArrayBlockingQueue<String> garage;
    public String name;

    public Car(String name, ArrayBlockingQueue<String> garage){
        this.name = name;
        this.garage = garage;
        start();
    }

    @Override
    public void run(){
        try{
            Thread.sleep((int)(Math.random()*20));//driving to access the parking
            while(garage.size() == 10){
                Thread.sleep((int)(Math.random()*1000)); //making a tour waiting the garage to have a free place
            }
            System.out.println(this.name + ": trying to enter");
            garage.put(this.name);
            System.out.println(this.name + ": entered");
            Thread.sleep((int)(Math.random()*2000));
            System.out.println(this.name + ": left");
            garage.remove(this.name);
        } catch( InterruptedException e){
            e.printStackTrace();
        }
    }

}
