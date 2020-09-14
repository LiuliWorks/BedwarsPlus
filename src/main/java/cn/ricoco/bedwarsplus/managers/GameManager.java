package cn.ricoco.bedwarsplus.managers;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.bedwarsplus.Main;
import cn.ricoco.bedwarsplus.Variables;
import cn.ricoco.funframework.Utils.ScoreboardUtils;
import cn.ricoco.funframework.game.Room;
import com.alibaba.fastjson.JSONObject;

public class GameManager {
    public static void leaveGame(Player p){
        Room pRNow = Main.game.getRoomByPlayer(p.getName());
        if (pRNow == null) {
            for (Room room1 : Main.game.rooms) {
                if (room1.wait.level.getName().equals(p.getPosition().level.getName())) {
                    JSONObject posJson = room1.otherInfo.getJSONObject("pos").getJSONObject("back");
                    room1.playerBackupDataMap.get(p).restoreData();
                    p.teleport(Position.fromObject(new Vector3(posJson.getDouble("x"), posJson.getDouble("y"), posJson.getDouble("z")), Server.getInstance().getLevelByName(posJson.getString("l"))));
                    p.sendMessage(Variables.langjson.getString("returnedtolobby"));
                    return;
                }
            }
            p.sendMessage(Variables.langjson.getString("notinroom"));
        } else {
            if (pRNow.roomStage == 0) {
                pRNow.playerL.remove(p);
                pRNow.playerBackupDataMap.get(p).restoreData();
                JSONObject posJson = pRNow.otherInfo.getJSONObject("pos").getJSONObject("back");
                ScoreboardUtils.removeSB(p);
                p.teleport(Position.fromObject(new Vector3(posJson.getDouble("x"), posJson.getDouble("y"), posJson.getDouble("z")), Server.getInstance().getLevelByName(posJson.getString("l"))));
                p.sendMessage(Variables.langjson.getString("returnedtolobby"));
                pRNow.allMassage(Variables.langjson.getString("leavemessage").replaceAll("%1", p.getName()).replaceAll("%2", pRNow.playerL.size() + "").replaceAll("%3", pRNow.maxPlayer + ""));
            }
        }
    }
}
