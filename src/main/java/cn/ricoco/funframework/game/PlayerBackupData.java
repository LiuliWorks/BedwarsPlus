package cn.ricoco.funframework.game;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.ricoco.funframework.Utils.Exp;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class PlayerBackupData {
    public Player player;
    public Map<Integer,Item> inv;
    public Exp exp;
    public int gamemode;
    public int hunger;
    public JSONObject otherInfo;
    public PlayerBackupData(Player player, JSONObject otherInfo){
        inv=player.getInventory().getContents();
        gamemode=player.getGamemode();
        hunger=player.getFoodData().getLevel();
        exp=new Exp(player.getExperience(),player.getExperienceLevel());
        this.player=player;
        this.otherInfo=otherInfo;
    }
    public void restoreData(){
        player.getInventory().setContents(inv);
        exp.setToPlayer(player);
        player.setGamemode(gamemode);
        player.getFoodData().setLevel(hunger);
    }
}
