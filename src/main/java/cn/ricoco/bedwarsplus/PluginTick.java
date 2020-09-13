package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.funframework.Utils.EntityUtils;
import cn.ricoco.funframework.Utils.LevelUtils;
import cn.ricoco.funframework.Utils.OtherUtils;
import cn.ricoco.funframework.Utils.ScoreboardUtils;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.Team;
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
        JSONArray SBJArr_Wait = Variables.langjson.getJSONArray("waitsb");
        JSONArray SBJArr_Already = Variables.langjson.getJSONArray("alreadysb");
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
                                JSONArray enhance_npcs=posJson.getJSONArray("shop_npcs");
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
                                for (Player p : plL) {
                                    Team t=room.teamL.get(tcount);
                                    t.playerL.add(p);
                                    p.teleport(t.spawn);
                                    p.getInventory().clearAll();
                                    p.setNameTag(t.color+p.getName());
                                    tcount++;
                                    if(tcount>=room.teamL.size()){
                                        tcount=0;
                                    }
                                }
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
