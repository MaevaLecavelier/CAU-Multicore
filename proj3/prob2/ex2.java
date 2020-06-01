/*
    * Maeva Lecavelier - 50191580
    *
    * Semaphore
*/
/*
    * The semaphore will protect a game where
    * only 4 players can play simultaneously.
    * there will be a pool of 9 players, and
    * when a player plays, it plays during a 2
    * mini games  during a random time.
    * once he played, he can't play again.
*/

import java.util.concurrent.*;

public class ex2 {


    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(4); //4 players at the same time
        System.out.println("Semaphore has "+semaphore.availablePermits()+" permits available.");
        Player p1 = new Player("Player A", semaphore);
        p1.start();
        Player p2 = new Player("Player B", semaphore);
        p2.start();
        Player p3 = new Player("Player C", semaphore);
        p3.start();
        Player p4 = new Player("Player D", semaphore);
        p4.start();
        Player p5 = new Player("Player E", semaphore);
        p5.start();
        Player p6 = new Player("Player F", semaphore);
        p6.start();
        Player p7 = new Player("Player G", semaphore);
        p7.start();
        Player p8 = new Player("Player H", semaphore);
        p8.start();
        Player p9 = new Player("Player I", semaphore);
        p9.start();
    }
}

class Player extends Thread{
    String name;
    Semaphore semaphore;

    Player(String name, Semaphore semaphore){
        this.name = name;
        this.semaphore = semaphore;
    }

    public void run(){
        try{
            System.out.println(this.name+": is acquiring lock...");
            this.semaphore.acquire();//acquire permits
            System.out.println(this.name+": lock acquired. Available permits: "+this.semaphore.availablePermits());
            for(int i = 1; i <=2; i++){ //play games
                Thread.sleep((int)(Math.random()*1000)); //time for one mini-game
                System.out.println(this.name+": has played "+i+" mini-game(s).Available permits: "+this.semaphore.availablePermits());
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        finally{
            System.out.println(this.name+": end of game. Releasing the lock...");
            this.semaphore.release();//release permit
            System.out.println(this.name+": lock released. Available permits: "+this.semaphore.availablePermits());
        }
    }
}
