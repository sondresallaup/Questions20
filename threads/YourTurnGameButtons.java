package no.appfabrikken.valpolicella.threads;

/**
 * Created by sondresallaup on 26.06.14.
 */
public class YourTurnGameButtons extends Thread {
    @Override
    public void run(){
        synchronized (this){

            notify();
        }
    }
}
