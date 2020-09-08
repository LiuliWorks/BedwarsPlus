package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
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
                JSONObject json=room.otherInfo.getJSONObject("pos");
                if(room.playerL.size()==0){
                    Server.getInstance().loadLevel(json.getString("level"));
                    Position ppos= Position.fromObject(new Vector3(json.getJSONObject("wait").getDouble("x"),json.getJSONObject("wait").getDouble("y"),json.getJSONObject("wait").getDouble("z")),Server.getInstance().getLevelByName(json.getString("level")));
                    p.teleport(ppos);
                    CageManager.CreateCage(Variables.cagejson,ppos);
                    p.teleport(ppos);
                    p.setGamemode(3);
                    p.setHealth(20F);
                }
        }
        return false;
    }
}
