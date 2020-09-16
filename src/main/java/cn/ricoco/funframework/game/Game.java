package cn.ricoco.funframework.game;

import cn.nukkit.Player;

import java.util.ArrayList;

public class Game {
    public String gameName;
    public ArrayList<Room> rooms=new ArrayList<>();
    public Game(String gname){
        gameName=gname;
    }
    public void addGame(Room room){
        rooms.add(room);
    }
    public Room getRoomById(String rid){
        Room rroom = null;
        for (Room room : rooms) {
            if (room.getId().equals(rid)) {
                rroom=room;
            }
        }
        return rroom;
    }
    public Room getRoomByPlayer(String pname){
        Room rroom = null;
        for (Room room : rooms) {
            ArrayList<Player> plL=room.playerL;
            for(Player p:plL){
                if(p.getName().equals(pname)){
                    rroom=room;
                }
            }
        }
        return rroom;
    }
    public Room getRoomByLevel(String lname){
        Room rroom = null;
        for (Room room : rooms) {
            String Level=room.wait.level.getName();
            if(Level.equals(lname)){
                rroom=room;
            }
        }
        return rroom;
    }
}
