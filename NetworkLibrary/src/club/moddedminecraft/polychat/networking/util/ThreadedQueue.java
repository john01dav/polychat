package club.moddedminecraft.polychat.networking.util;

import java.util.LinkedList;

public abstract class ThreadedQueue<T> {
    private Thread thread;
    private LinkedList<T> queue = new LinkedList<>();

    public final synchronized void start(){
        thread = new Thread(this::queueThread);
        thread.start();
    }

    public final synchronized void stop(){
        thread.stop();
    }

    public final synchronized void enqueue(T obj){
        queue.push(obj);
        if(thread != null) thread.interrupt();
    }

    private final void queueThread(){
        try{
            init();
            while(true){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){}
                T nextItem;
                while((nextItem = getNextItem()) != null){
                    handle(nextItem);
                }
            }
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    private final synchronized T getNextItem(){
        if(queue.isEmpty()){
            return null;
        }else{
            return queue.pop();
        }
    }

    protected abstract void init() throws Throwable;
    protected abstract void handle(T obj) throws Throwable;

}
