package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.funframework.Utils.EntityUtils;
import cn.ricoco.funframework.Utils.LevelUtils;
import cn.ricoco.funframework.Utils.OtherUtils;
import cn.ricoco.funframework.Utils.ScoreboardUtils;
import cn.ricoco.funframework.entity.FloatingTextSpawner;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.Team;
import cn.ricoco.funframework.game.manager.CageManager;
import cn.ricoco.funframework.game.manager.FloatingItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class PluginTick {
    public static Runner runner;
    public static Thread thread;

    public static void StartTick() {
        runner = new Runner();
        thread = new Thread(runner);
        thread.start();
    }
}

class Runner implements Runnable {
    public void run() {
        String serverIp = Variables.configjson.getString("serverip");
        int SBCount = 0;
        String sbTitle = Variables.langjson.getString("gamename");
        String[] sbTitleL = sbTitle.split("");
        JSONObject SBJObj_SBs = Variables.langjson.getJSONObject("scoreboards");
        JSONArray SBJArr_Wait = SBJObj_SBs.getJSONArray("waitsb");
        JSONArray SBJArr_Already = SBJObj_SBs.getJSONArray("alreadysb");
        JSONArray SBJArr_InGame = SBJObj_SBs.getJSONArray("ingamesb");
        while (true) {
            try {
                StringBuilder SB_Title = new StringBuilder();
                if (SBCount < sbTitleL.length) {
                    for (int i = 0; i < sbTitleL.length; i++) {
                        if (i < SBCount) {
                            SB_Title.append("§f").append(sbTitleL[i]);
                        } else {
                            SB_Title.append("§e").append(sbTitleL[i]);
                        }
                    }
                } else if (SBCount == sbTitleL.length || SBCount == sbTitleL.length + 2) {
                    SB_Title = new StringBuilder("§f" + sbTitle);
                } else if (SBCount == sbTitleL.length + 1) {
                    SB_Title = new StringBuilder("§e" + sbTitle);
                } else if (SBCount == sbTitleL.length + 3) {
                    SB_Title = new StringBuilder("§e" + sbTitle);
                    SBCount = 0;
                }
                SBCount++;
                Thread.sleep(1000);
                for (Room room : Main.game.rooms) {
                    if(room.playerL.size()>0){
                        Level l=room.wait.level;
                        l.setTime(room.otherInfo.getInteger("time"));
                        LevelUtils.setLevelWeather(l,room.otherInfo.getString("weather"));
                    }
                    if(room.roomStage==0) {
                        if (room.lowPlayer > room.playerL.size()) {
                            room.otherInfo.put("waittime", OtherUtils.getTime());
                            ArrayList<Player> plL = room.playerL;
                            for (Player p : plL) {
                                ArrayList<String> SBArr = new ArrayList<>();
                                for (int i = 0; i < SBJArr_Wait.size(); i++) {
                                    SBArr.add(SBJArr_Wait.getString(i).replaceAll("%map%", room.otherInfo.getString("name")).replaceAll("%player%", p.getName()).replaceAll("%serverip%", serverIp).replaceAll("%stage%", room.playerL.size() + "/" + room.maxPlayer));
                                }
                                ScoreboardUtils.showSBFromArrayList(p, SBArr, SB_Title.toString());
                                p.getFoodData().setLevel(20);
                            }
                        } else {
                            ArrayList<Player> plL = room.playerL;
                            int time= (int) (room.otherInfo.getJSONObject("wait").getLong("default")-(OtherUtils.getTime()-room.otherInfo.getLong("waittime")));
                            String sbStr=Variables.langjson.getJSONObject("countdown").getString(time+"");
                            for (Player p : plL) {
                                ArrayList<String> SBArr = new ArrayList<>();
                                if(sbStr != null){
                                    p.sendTitle(sbStr);
                                    p.sendMessage(Variables.langjson.getJSONObject("countdown").getString("message").replaceAll("%1",time+""));
                                }
                                for (int i = 0; i < SBJArr_Already.size(); i++) {
                                    SBArr.add(SBJArr_Already.getString(i).replaceAll("%time%",time+"").replaceAll("%map%", room.otherInfo.getString("name")).replaceAll("%player%", p.getName()).replaceAll("%serverip%", serverIp).replaceAll("%stage%", room.playerL.size() + "/" + room.maxPlayer));
                                }
                                ScoreboardUtils.showSBFromArrayList(p, SBArr, SB_Title.toString());
                                p.getFoodData().setLevel(20);
                            }
                            if(time<0){
                                room.roomStage=1;
                                JSONObject posJson=room.otherInfo.getJSONObject("pos");
                                JSONArray shop_npcs=posJson.getJSONArray("shop_npcs");
                                JSONArray enhance_npcs=posJson.getJSONArray("enhance_npcs");
                                for(int i=0;i<shop_npcs.size();i++) {
                                    JSONObject nowPos=shop_npcs.getJSONObject(i);
                                    Entity entity = EntityUtils.spawnEntity("BedwarsNPC", Position.fromObject(new Vector3(nowPos.getDouble("x"),nowPos.getDouble("y"),nowPos.getDouble("z")),room.wait.level));
                                    entity.setNameTag(Variables.langjson.getString("shop_npc"));
                                    entity.setNameTagAlwaysVisible(true);
                                    entity.yaw=nowPos.getInteger("yaw");
                                    entity.pitch=nowPos.getInteger("pitch");
                                }
                                for(int i=0;i<enhance_npcs.size();i++) {
                                    JSONObject nowPos=enhance_npcs.getJSONObject(i);
                                    Entity entity = EntityUtils.spawnEntity("BedwarsNPC", Position.fromObject(new Vector3(nowPos.getDouble("x"),nowPos.getDouble("y"),nowPos.getDouble("z")),room.wait.level));
                                    entity.setNameTag(Variables.langjson.getString("enhance_npc"));
                                    entity.setNameTagAlwaysVisible(true);
                                    entity.yaw=nowPos.getInteger("yaw");
                                    entity.pitch=nowPos.getInteger("pitch");
                                }
                                int tcount=0;
                                JSONObject lastAtk=new JSONObject();
                                for (Player p : plL) {
                                    Team t=room.teamL.get(tcount);
                                    JSONObject atkInfo=new JSONObject();
                                    atkInfo.put("lastp","null");
                                    atkInfo.put("time",OtherUtils.getTime());
                                    lastAtk.put(p.getName(),atkInfo);
                                    t.playerL.add(p);
                                    p.teleport(t.spawn);
                                    p.getInventory().clearAll();
                                    p.setGamemode(0);
                                    p.setNameTag(t.color+p.getName());
                                    JSONObject pDataJson=new JSONObject();
                                    pDataJson.put("kill",0);
                                    pDataJson.put("final_kill",0);
                                    pDataJson.put("bed_broken",0);
                                    t.otherInfo.put(p.getName(),pDataJson);
                                    tcount++;
                                    if(tcount>=room.teamL.size()){
                                        tcount=0;
                                    }
                                }
                                room.otherInfo.put("lastatk",lastAtk);
                                for(Team t:room.teamL){
                                    if(t.playerL.size()>0){
                                        t.teamStage=0;
                                    }else{
                                        t.teamStage=2;
                                    }
                                }
                                JSONArray diamond_spawn=room.otherInfo.getJSONObject("pos").getJSONArray("diamond");
                                JSONObject gene_json=new JSONObject();
                                JSONArray dia_arr=new JSONArray();
                                JSONArray em_arr=new JSONArray();
                                gene_json.put("diamond_time",Variables.configjson.getJSONObject("resources").getJSONObject("diamond").getInteger("1"));
                                gene_json.put("emerald_time",Variables.configjson.getJSONObject("resources").getJSONObject("emerald").getInteger("1"));
                                gene_json.put("diamond_nowtime",gene_json.getInteger("diamond_time"));
                                gene_json.put("emerald_nowtime",gene_json.getInteger("emerald_time"));
                                for(Object diamond_single_obj:diamond_spawn){
                                    JSONObject diamond_single= (JSONObject) diamond_single_obj;
                                    Position pos=Position.fromObject(new Vector3(diamond_single.getDouble("x"),diamond_single.getDouble("y")+4,diamond_single.getDouble("z")),room.wait.level);
                                    FloatingTextSpawner.spawner(pos,Variables.langjson.getJSONObject("items").getString("diamond").replaceAll("%1",Variables.langjson.getJSONObject("tier").getString("1")));
                                    FloatingTextSpawner.spawner(Position.fromObject(new Vector3(pos.x, pos.y-0.5, pos.z), pos.level),Variables.langjson.getJSONObject("items").getString("spawn").replaceAll("%1",gene_json.getInteger("diamond_nowtime")+""));
                                    new FloatingItem(Item.get(57,0),(float)pos.x,(float)(pos.y-0.5),(float)pos.z).addFloatingItem(pos.level);
                                }
                                JSONArray emerald_spawn=room.otherInfo.getJSONObject("pos").getJSONArray("emerald");
                                for(Object emerald_single_obj:emerald_spawn){
                                    JSONObject emerald_single= (JSONObject) emerald_single_obj;
                                    Position pos=Position.fromObject(new Vector3(emerald_single.getDouble("x"),emerald_single.getDouble("y")+4,emerald_single.getDouble("z")),room.wait.level);
                                    JSONObject aEm=new JSONObject();
                                    FloatingTextSpawner.spawner(pos,Variables.langjson.getJSONObject("items").getString("emerald").replaceAll("%1",Variables.langjson.getJSONObject("tier").getString("1")));
                                    FloatingTextSpawner.spawner(Position.fromObject(new Vector3(pos.x, pos.y-0.5, pos.z), pos.level),Variables.langjson.getJSONObject("items").getString("spawn").replaceAll("%1",gene_json.getInteger("emerald_nowtime")+""));
                                    new FloatingItem(Item.get(133,0),(float)pos.x,(float)(pos.y-0.5),(float)pos.z).addFloatingItem(pos.level);
                                }
                                gene_json.put("diamond",dia_arr);
                                gene_json.put("emerald",em_arr);
                                room.otherInfo.put("generators",gene_json);
                                CageManager.RemoveCage(Variables.cagejson,Position.fromObject(new Vector3(room.wait.x,room.wait.y-1,room.wait.z),room.wait.level));
                            }
                        }
                    }else if(room.roomStage==1){
                        for(Team team:room.teamL){
                            ArrayList<String> SB_RoomStats = new ArrayList<>();
                            for(Team team1:room.teamL){
                                if(team1.teamStage==0){
                                    SB_RoomStats.add(SBJObj_SBs.getString("teamsb").replaceAll("%teamstat%",SBJObj_SBs.getString("team_with_bed").replaceAll("%players%",team1.playerL.size()+"")).replaceAll("%teamcolor%", team1.color).replaceAll("%teamname%", team1.name).replaceAll("%teamid%",team1.id));
                                }else if(team1.teamStage==1){
                                    SB_RoomStats.add(SBJObj_SBs.getString("teamsb").replaceAll("%teamstat%",SBJObj_SBs.getString("team_alive").replaceAll("%players%",team1.playerL.size()+"")).replaceAll("%teamcolor%", team1.color).replaceAll("%teamname%", team1.name).replaceAll("%teamid%",team1.id));
                                }else if(team1.teamStage==2){
                                    SB_RoomStats.add(SBJObj_SBs.getString("teamsb").replaceAll("%teamstat%",SBJObj_SBs.getString("team_eliminated").replaceAll("%players%",team1.playerL.size()+"")).replaceAll("%teamcolor%", team1.color).replaceAll("%teamname%", team1.name).replaceAll("%teamid%",team1.id));
                                }
                                if(team1.equals(team)){
                                    SB_RoomStats.set(SB_RoomStats.size()-1,SB_RoomStats.get(SB_RoomStats.size()-1)+" "+SBJObj_SBs.getString("yourteam"));
                                }
                            }
                            for(Player p:team.playerL){
                                JSONObject playerDataJson=team.otherInfo.getJSONObject(p.getName());
                                ArrayList<String> SBArr = new ArrayList<>();
                                for (int i = 0; i < SBJArr_InGame.size(); i++) {
                                    if(SBJArr_InGame.getString(i).contains("%teams%")){
                                        SBArr.add("§f  ");
                                        SBArr.addAll(SB_RoomStats);
                                        SBArr.add("§c   ");
                                    }else{
                                        SBArr.add(SBJArr_InGame.getString(i).replaceAll("%kill%",playerDataJson.getInteger("kill")+"").replaceAll("%final_kill%",playerDataJson.getInteger("final_kill")+"").replaceAll("%bed_broken%",playerDataJson.getInteger("bed_broken")+"").replaceAll("%serverip%", serverIp));
                                    }
                                }
                                ScoreboardUtils.showSBFromArrayList(p, SBArr, SB_Title.toString());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
