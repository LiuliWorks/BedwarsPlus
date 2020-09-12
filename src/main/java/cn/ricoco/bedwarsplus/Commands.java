package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.funframework.Utils.ScoreboardUtils;
import cn.ricoco.funframework.game.PlayerBackupData;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.manager.CageManager;
import com.alibaba.fastjson.JSONObject;

public class Commands extends Command {
    public Commands(String name, String description) {
        super(name, description);
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p=null;
        String pname=null;
        if(sender.isPlayer()){
            p=Server.getInstance().getPlayer(sender.getName());
            pname=p.getName();
        }
        switch (args[0]){
            case "join":
                Room room=Main.game.getRoomById(args[1]);
                for(int i=0;i<room.playerL.size();i++){
                    if(room.playerL.get(i).getName().equals(pname)){
                        p.sendMessage(Variables.langjson.getString("notinroom"));
                        return false;
                    }
                }
                JSONObject json=room.otherInfo.getJSONObject("pos");
                room.playerBackupDataMap.put(p,new PlayerBackupData(p,new JSONObject()));
                p.getInventory().clearAll();
                if(room.playerL.size()==0){
                    p.teleport(room.wait);
                    CageManager.CreateCage(Variables.cagejson,room.wait);
                }
                p.teleport(room.wait);
                if(room.roomStage==0) {
                    p.setGamemode(2);
                    room.playerL.add(p);
                }else{
                    p.setGamemode(3);
                }
                p.setHealth(20F);
                room.allMassage(Variables.langjson.getString("joinmessage").replaceAll("%1",p.getName()).replaceAll("%2",room.playerL.size()+"").replaceAll("%3",room.maxPlayer+""));
                break;
            case "leave":
                Room pRNow=Main.game.getRoomByPlayer(pname);
                if(pRNow==null){
                    p.sendMessage(Variables.langjson.getString("alreadyinroom"));
                    return false;
                }else{
                    if(pRNow.roomStage==0){
                        pRNow.playerL.remove(p);
                        pRNow.playerBackupDataMap.get(p).restoreData();
                        JSONObject posJson=pRNow.otherInfo.getJSONObject("pos").getJSONObject("back");
                        ScoreboardUtils.removeSB(p);
                        p.teleport(Position.fromObject(new Vector3(posJson.getDouble("x"),posJson.getDouble("y"),posJson.getDouble("z")),Server.getInstance().getLevelByName(posJson.getString("l"))));
                        pRNow.allMassage(Variables.langjson.getString("leavemessage").replaceAll("%1",p.getName()).replaceAll("%2",pRNow.playerL.size()+"").replaceAll("%3",pRNow.maxPlayer+""));
                    }
                }
                break;
            case "autojoin":
                break;
            case "listrooms":
                StringBuilder rMes= new StringBuilder("Rooms list of Bedwars+:\n");
                for(Room room1:Main.game.rooms){
                    rMes.append("id:").append(room1.id).append(" name:").append(room1.name).append(" players:").append(room1.playerL.size()).append("/").append(room1.maxPlayer).append("\n");
                }
                if(sender.isPlayer()){
                    p.sendMessage(rMes.toString());
                }else{
                    Server.getInstance().getLogger().info(rMes.toString());
                }
                break;
        }
        return false;
    }
}
