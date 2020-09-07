package cn.ricoco.funframework.game;

import cn.nukkit.Player;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class Room {
    public int maxPlayer;
    public int lowPlayer;
    public int teams;
    public JSONArray teamData;
    public String id;
    public String name;
    public ArrayList<Team> teamL=new ArrayList<>();
    public ArrayList<Player> playerL=new ArrayList<>();
    public JSONObject otherInfo;
    public Room(JSONObject otherInfo,JSONArray teams,String id,String name,int maxPlayer,int lowPlayer){
        this.maxPlayer=maxPlayer;
        this.lowPlayer=lowPlayer;
        this.teams=teams.size();
        this.teamData=teams;
        this.id=id;
        this.name=name;
        this.otherInfo=otherInfo;
    }
    public void addTeam(Team team){
        teamL.add(team);
    }
    public String getId(){
        return this.id;
    }
}
