package cn.ricoco.bedwarsplus;

public class PluginTick {
    public static Runner runner;
    public static Thread thread;
    public static void StartTick(){
        runner = new Runner();
        thread = new Thread(runner);
        thread.start();
    }
}

class Runner implements Runnable{
    public int tick=0;
    public void run() {
        while (true){
            try{
                Thread.sleep(500);
                tick++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
