package cn.ricoco.bedwarsplus;

import cn.ricoco.funframework.game.Game;
import cn.ricoco.funframework.game.Room;

import java.util.Date;

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
                if(tick==0){
                    for(Room room: Main.game.rooms){
                        if(room.lowPlayer>room.playerL.size()){
                            room.otherInfo.put("waittime",new Date());
                        }else{

                        }
                    }
                }
                tick++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
