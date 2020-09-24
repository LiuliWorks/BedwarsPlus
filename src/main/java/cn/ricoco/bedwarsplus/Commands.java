package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.bedwarsplus.managers.GameManager;
import cn.ricoco.funframework.Utils.OtherUtils;
import cn.ricoco.funframework.Utils.PlayerUtils;
import cn.ricoco.funframework.entity.FloatingTextSpawner;
import cn.ricoco.funframework.game.PlayerBackupData;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.manager.CageManager;
import cn.ricoco.funframework.game.manager.FloatingItem;
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
        if(sender.isPlayer()){
            p=Server.getInstance().getPlayer(sender.getName());
        }
        switch (args[0]){
            case "join": {
                Room room = Main.game.getRoomById(args[1]);
                if(room==null){
                    p.sendMessage(Variables.langjson.getString("roomnotfound"));
                    return false;
                }
                for (int i = 0; i < room.playerL.size(); i++) {
                    if (room.playerL.get(i).getName().equals(p.getName())) {
                        p.sendMessage(Variables.langjson.getString("notinroom"));
                        return false;
                    }
                }
                p.teleport(room.wait);
                JSONObject json = room.otherInfo.getJSONObject("pos");
                room.playerBackupDataMap.put(p, new PlayerBackupData(p, new JSONObject()));
                p.getInventory().clearAll();
                if (room.playerL.size() == 0) {
                    CageManager.CreateCage(Variables.cagejson,Position.fromObject(new Vector3(room.wait.x,room.wait.y-1,room.wait.z),room.wait.level));
                } else if (room.playerL.size() > 1) {
                    room.otherInfo.put("waittime", room.otherInfo.getLong("waittime") - room.otherInfo.getJSONObject("wait").getInteger("player"));
                }
                p.teleport(room.wait);
                if (room.roomStage == 0) {
                    p.setGamemode(2);
                    room.playerL.add(p);
                } else {
                    p.setGamemode(3);
                }
                p.setHealth(20F);
                Item blobby = Item.get(355, 0);
                blobby.setCustomName(Variables.langjson.getString("returntolobby"));
                PlayerUtils.addItemToSlot(p, 8, blobby);
                room.allMassage(Variables.langjson.getString("joinmessage").replaceAll("%1", p.getName()).replaceAll("%2", room.playerL.size() + "").replaceAll("%3", room.maxPlayer + ""));
                break;
            }
            case "leave": {
                GameManager.leaveGame(p);
                break;
            }
            case "autojoin": {
                JSONArray rList = Variables.configjson.getJSONObject("autojoin").getJSONArray(args[1]);
                ArrayList<Integer> rArray = new ArrayList<>();
                int rAllPlayerC = 0,allFull=0;
                for (int i = 0; i < rList.size(); i++) {
                    Room rLRoom = Main.game.getRoomById(rList.getString(i));
                    if (rLRoom.roomStage == 0) {
                        rArray.add(rLRoom.playerL.size());
                        rAllPlayerC += rLRoom.playerL.size();
                        allFull=1;
                    }
                }
                if(allFull!=1){
                    p.sendMessage(Variables.langjson.getString("allroomfull"));
                }
                if (rAllPlayerC == 0) {
                    Server.getInstance().dispatchCommand(p, "bwp join " + rList.getString(OtherUtils.randInt(0, rList.size())));
                } else {
                    Collections.sort(rArray);
                    Integer roomPlayers = rArray.get(rArray.size() - 1);
                    for (int i = 0; i < rList.size(); i++) {
                        Room rLRoom = Main.game.getRoomById(rList.getString(i));
                        if (rLRoom.roomStage == 0 && rLRoom.playerL.size() == roomPlayers) {
                            Server.getInstance().dispatchCommand(p, "bwp join " + rList.getString(i));
                        }
                    }
                }
                break;
            }
            case "listrooms": {
                StringBuilder rMes = new StringBuilder("Rooms list of Bedwars+:\n");
                for (Room room1 : Main.game.rooms) {
                    rMes.append("id:").append(room1.id).append(" stage:").append(room1.roomStage).append(" name:").append(room1.name).append(" players:").append(room1.playerL.size()).append("/").append(room1.maxPlayer).append("\n");
                }
                if (sender.isPlayer()) {
                    p.sendMessage(rMes.toString());
                } else {
                    Server.getInstance().getLogger().info(rMes.toString());
                }
                break;
            }
            case "t": {
                Position pos=p.getPosition();
                Entity k = Entity.createEntity("Bedwars+_IronGolem", pos.level.getChunk(((int) pos.x) >> 4, ((int) pos.z) >> 4), Entity.getDefaultNBT(new Vector3(pos.x, pos.y, pos.z)));
                k.spawnToAll();
                break;
            }
            case "q": {
                Position pos=p.getPosition();
                Entity k = Entity.createEntity("Bedwars+_Silverfish", pos.level.getChunk(((int) pos.x) >> 4, ((int) pos.z) >> 4), Entity.getDefaultNBT(new Vector3(pos.x, pos.y, pos.z)));
                k.spawnToAll();
                break;
            }
//                ChestFakeInventory inv=new DoubleChestFakeInventory();
//                for(int i=0;i<inv.getSize();i++){
//                    Item item=Item.get(1,0);
//                    item.setCustomName(i+"");
//                    inv.setItem(i,item);
//                }
//                p.addWindow(inv);
        }
        return false;
    }
}
