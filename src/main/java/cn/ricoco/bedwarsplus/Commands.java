package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.funframework.Utils.OtherUtils;
import cn.ricoco.funframework.Utils.ScoreboardUtils;
import cn.ricoco.funframework.fakeinventories.inventory.ChestFakeInventory;
import cn.ricoco.funframework.fakeinventories.inventory.DoubleChestFakeInventory;
import cn.ricoco.funframework.game.PlayerBackupData;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.manager.CageManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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
                p.teleport(room.wait);
                JSONObject json=room.otherInfo.getJSONObject("pos");
                room.playerBackupDataMap.put(p,new PlayerBackupData(p,new JSONObject()));
                p.getInventory().clearAll();
                if(room.playerL.size()==0){
                    CageManager.CreateCage(Variables.cagejson,room.wait);
                }else if(room.playerL.size()>1){
                    room.otherInfo.put("waittime",room.otherInfo.getLong("waittime")-room.otherInfo.getJSONObject("wait").getInteger("player"));
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
                JSONArray rList=Variables.configjson.getJSONObject("autojoin").getJSONArray(args[1]);
                ArrayList<Integer> rArray=new ArrayList<>();
                int rAllPlayerC=0;
                for(int i=0;i<rList.size();i++){
                    Room rLRoom=Main.game.getRoomById(rList.getString(i));
                    if(rLRoom.roomStage==0){
                        rArray.add(rLRoom.playerL.size());
                        rAllPlayerC+=rLRoom.playerL.size();
                    }
                }
                if(rAllPlayerC==0){
                    Server.getInstance().dispatchCommand(p,"bwp join "+rList.getString(OtherUtils.randInt(0,rList.size())));
                }else{
                    Collections.sort(rArray);
                    Integer roomPlayers=rArray.get(rArray.size()-1);
                    for(int i=0;i<rList.size();i++){
                        Room rLRoom=Main.game.getRoomById(rList.getString(i));
                        if(rLRoom.roomStage==0&&rLRoom.playerL.size()==roomPlayers){
                            Server.getInstance().dispatchCommand(p,"bwp join "+rList.getString(i));
                        }
                    }
                }
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
            case "test":
//                ChestFakeInventory inv=new DoubleChestFakeInventory();
//                for(int i=0;i<inv.getSize();i++){
//                    Item item=Item.get(1,0);
//                    item.setCustomName(i+"");
//                    inv.setItem(i,item);
//                }
//                p.addWindow(inv);
                break;
        }
        return false;
    }
}
