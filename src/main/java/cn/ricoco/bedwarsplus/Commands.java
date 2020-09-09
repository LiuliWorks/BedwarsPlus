package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.manager.CageManager;
import com.alibaba.fastjson.JSONObject;

public class Commands extends Command {
    public Commands(String name, String description) {
        super(name, description);
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.isPlayer()) {
            return false;
        }
        Player p= Server.getInstance().getPlayer(sender.getName());
        String levelName=p.getPosition().getLevel().getName(),pname=p.getName();
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
                if(Main.game.getRoomByPlayer(p.getName())==null){
                    p.sendMessage(Variables.langjson.getString("alreadyinroom"));
                    return false;
                }
                break;
            case "autojoin":
                break;
        }
        return false;
    }
}
