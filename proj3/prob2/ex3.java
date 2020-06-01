/*
    * Maeva Lecavelier - 50191580
    *
    * ReadWriteLock
*/
/*
    * The shared data is a lecture resource
    * 2 users (threads) can modify it: teacher and TA
    * 10 users (threads) can read it: students
    * For easier representation, the resource
    * is an array of random int.
*/

import java.util.concurrent.locks.*;
import java.util.Random;
import java.util.Arrays;


public class ex3 {
    static final int NUM_TEACHERS = 4;
    static final int NUM_STUDENTS = 10;

    public static void main(String[] args){
        int[] sharedResource = new int[10];
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();

        Random random = new Random();
        int number;
        for(int i = 0; i < 10; i++){
            number = random.nextInt(100);
            sharedResource[i] = number;
        }
        System.out.println("Array at the beginning: "+Arrays.toString(sharedResource));

        for(int i = 0; i < NUM_TEACHERS; i++){
            new Teacher(i, sharedResource, writeLock).start();
        }
        for(int i = 0; i < NUM_STUDENTS; i++){
            new Student(i, sharedResource, readLock).start();
        }
    }
}


class Teacher extends Thread{
    int[] sharedResource;
    Lock writeLock;
    int name;

    Teacher(int name, int[] resource, Lock lock){
        this.name = name;
        this.sharedResource = resource;
        this.writeLock = lock;
    }

    public void run(){
        Random random = new Random();
        int number = random.nextInt(100);
        int index = random.nextInt(10);
        System.out.println("Teacher "+this.name+" wants to write "+number+" to the resource.");
        this.writeLock.lock();
        this.sharedResource[index] = number;
        System.out.println("Teacher "+this.name+" writes "+number+" to the resource, at index "+index);
        try{
            Thread.sleep((int)(Math.random()*1000));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        this.writeLock.unlock();
        System.out.println("Teacher "+this.name+ " released the write lock.");
    }
}


class Student extends Thread {
    int[] sharedResource;
    Lock readLock;
    int name;

    Student(int name, int[] resource, Lock lock){
        this.name = name;
        this.sharedResource = resource;
        this.readLock = lock;
    }

    public void run(){
        System.out.println("Student "+this.name+" wants to read the resource.");
        this.readLock.lock();
        System.out.println("Student "+this.name+": "+Arrays.toString(this.sharedResource));
        try{
            Thread.sleep((int)(Math.random()*1000));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        this.readLock.unlock();
        System.out.println("Student "+this.name+ " released the read lock.");
    }
}
