package cn.ricoco.funframework.game.manager;

import cn.nukkit.level.Position;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CageManager {
    public static void CreateCage(JSONArray data, Position pos){
        for(int i=0;i<data.size();i++){
            JSONObject json=data.getJSONObject(i);
            pos.level.setBlockAt((int)pos.x+json.getInteger("x"),(int)pos.y+json.getInteger("y"), (int)pos.z+json.getInteger("z"),json.getInteger("id"),json.getInteger("damage"));
        }
    }
    public static void RemoveCage(JSONArray data, Position pos){
        for(int i=0;i<data.size();i++){
            JSONObject json=data.getJSONObject(i);
            pos.level.setBlockAt((int)pos.x+json.getInteger("x"),(int)pos.y+json.getInteger("y"), (int)pos.z+json.getInteger("z"),0);
        }
    }
}
