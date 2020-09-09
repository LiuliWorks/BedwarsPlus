package cn.ricoco.funframework.game;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class Team {
    public String id;
    public String color;
    public String name;
    public Position spawn;
    public ArrayList<Player> playerL=new ArrayList<>();
    public JSONObject otherInfo;
    public int teamStage=0;
    public Team(String id,String color,String name,Position spawn,JSONObject otherInfo){
        this.id=id;
        this.color=color;
        this.name=name;
        this.spawn=spawn;
        this.otherInfo=otherInfo;
    }
}
