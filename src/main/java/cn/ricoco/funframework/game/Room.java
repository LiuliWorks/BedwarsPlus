package cn.ricoco.funframework.game;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
    public int maxPlayer;
    public int lowPlayer;
    public int teams;
    public JSONArray teamData;
    public String id;
    public String name;
    public ArrayList<Team> teamL=new ArrayList<>();
    public ArrayList<Player> playerL=new ArrayList<>();
    public Map<Player,PlayerBackupData> playerBackupDataMap=new HashMap<>();
    public int roomStage=0;
    public Position wait;
    public JSONObject otherInfo;
    public Room(JSONObject otherInfo,JSONArray teams,String id,String name,int maxPlayer,int lowPlayer,Position wait){
        Server.getInstance().loadLevel(otherInfo.getJSONObject("pos").getString("level"));
        this.maxPlayer=maxPlayer;
        this.lowPlayer=lowPlayer;
        this.teams=teams.size();
        this.teamData=teams;
        this.id=id;
        this.name=name;
        this.otherInfo=otherInfo;
        this.wait=wait;
    }
    public void addTeam(Team team){
        teamL.add(team);
    }
    public String getId(){
        return this.id;
    }
    public void allMassage(String msg){
        for(Player p:playerL){
            p.sendMessage(msg);
        }
    }
}
