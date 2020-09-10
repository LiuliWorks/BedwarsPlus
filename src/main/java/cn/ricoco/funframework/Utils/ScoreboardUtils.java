package cn.ricoco.funframework.Utils;

import cn.nukkit.Player;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardUtils {
    public static Map<String, Scoreboard> boards = new HashMap<>();
    public static void showSBFromArrayList(Player p, ArrayList list, String title){
        Scoreboard sb = ScoreboardAPI.createScoreboard();
        ScoreboardDisplay sbd = sb.addDisplay(DisplaySlot.SIDEBAR, "dumy", title);
        for(int i = 0; i < list.size(); ++i) {
            sbd.addLine((String) list.get(i), i);
        }
        if(boards.containsKey(p.getName())){
            boards.get(p.getName()).hideFor(p);
            sb.showFor(p);
            boards.put(p.getName(),sb);
        }else {
            sb.showFor(p);
            boards.put(p.getName(),sb);
        }
    }
    public static void removeSB(Player p){
        if(boards.containsKey(p.getName())){
            boards.get(p.getName()).hideFor(p);
        }
    }
}
