/*
* Maeva Lecavelier - 50191580
* problem2 : MatmultD.java
*/

import java.util.*;
import java.lang.*;
import java.time.LocalDate;

public class MatmultD {
    private static Scanner sc = new Scanner(System.in);
    public static int[][] result;

    public static void main(String [] args) {
        int NUM_THREAD = 0;
        int i;
        long startTime, endTime, diffTime;
        if(args.length == 1) NUM_THREAD = Integer.valueOf(args[0]);
        else NUM_THREAD = 6;

        int a[][] = readMatrix();
        int b[][] = readMatrix();
        int[] dimensions = new int[2];
        dimensions[0] = getDimension(a)[0];
        dimensions[1] = getDimension(b)[1];
        result = new int[dimensions[0]][dimensions[1]];

        startTime = System.currentTimeMillis();
        MultThread[] threads = new MultThread[NUM_THREAD];

        for(i = 0; i < NUM_THREAD; i++){
            threads[i] = new MultThread(a, b, result);
            threads[i].start();
        }

        try{
            for(i = 0; i < NUM_THREAD; i++){
                threads[i].join();
            }
        }
        catch (InterruptedException e) {}
        endTime = System.currentTimeMillis();
        diffTime = endTime - startTime;
        System.out.println("\nSum of the result matrix: "+sumMatrix(result)+".\n");
        System.out.println("Execution time:");
        for(i = 0; i < NUM_THREAD; i++){
            System.out.println(threads[i].getName()+": "+threads[i].exeTime+" ms.");
        }
        System.out.println("Main thread :"+diffTime+" ms.");



    }

    public static void printMatrix(int[][] mat) {
      System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < columns; j++) {
            System.out.printf("%4d " , mat[i][j]);
            sum+=mat[i][j];
          }
          System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
      }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static int[] getDimension(int[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        int[] res = new int[2];
        res[0] = rows;
        res[1] = columns;
        return res;
    }

    public static int sumMatrix(int[][] mat){
        int rows = mat.length;
        int cols = mat[0].length;
        int sum = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                sum += mat[i][j];
            }
        }
        return sum;
    }
}


class MultThread extends Thread {
    //static object for making a commun point to all thread
    static int[] couple = new int[]{0,-1};
    long exeTime;
    static int[][] finalMatrix, a, b;
    int m, n, p;

    MultThread(int[][] a, int[][] b, int[][] res){
        // a: [m][n], b: [n][p], final: [m][p]
        this.a = a;
        this.b = b;
        this.finalMatrix = res;
        this.exeTime = 0;
        this.m = a.length;
        this.p = b[0].length;
        this.n = a[0].length;
    }

    public void run(){
        int[] coupleCopy = getNextCouple(this.p);
        long startTime = System.currentTimeMillis();

        while(coupleCopy[0] < this.m){
            finalMatrix[coupleCopy[0]][coupleCopy[1]] = 0;
            for(int k = 0; k < this.n; k++){
                finalMatrix[coupleCopy[0]][coupleCopy[1]] += this.a[coupleCopy[0]][k]*this.b[k][coupleCopy[1]];
            }
            coupleCopy = getNextCouple(this.p);
        }

        long endTime = System.currentTimeMillis();
        this.exeTime = endTime - startTime;
    }

    //static and synchronized so only one thread can access it
    public static synchronized int[] getNextCouple(int p){
        couple[1] += 1;
        if(couple[1] == p){
            couple[0] += 1;
            couple[1] = 0;
        }
        /*
        *  I use a copy because, if couple is updated while
        *  a thread is running in the while for loop, this may
        *  lead to (1) an out of range error and (2) some couples
        *  can be skiped.
        */
        int[] copy = new int[2];
        copy[0] = couple[0];
        copy[1] = couple[1];
        return copy;
    }

    public synchronized int reset(int x){
        return -1;
    }
}
