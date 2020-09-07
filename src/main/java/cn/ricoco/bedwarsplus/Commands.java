package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.ricoco.funframework.game.Room;

public class Commands extends Command {
    public Commands(String name, String description) {
        super(name, description);
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.isPlayer()) {
            return false;
        }
        if(args.length!=1){return false;}
        Player p= Server.getInstance().getPlayer(sender.getName());
        String levelName=p.getPosition().getLevel().getName(),pname=p.getName();
        switch (args[0]){
            case "join":
                Room room=Main.game.getRoomById(args[1]);
        }
        return false;
    }
}
