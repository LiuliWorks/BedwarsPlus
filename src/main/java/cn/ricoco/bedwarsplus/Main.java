package cn.ricoco.bedwarsplus;

import cn.nukkit.plugin.PluginBase;
import cn.ricoco.funframework.game.Game;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.Utils.FileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

public class Main extends PluginBase {
    public static Main plugin;
    public static Game game=null;
    public static String pluginName="BedwarsPlus";
    public static String JarDir=Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    @Override
    public void onEnable() {
        plugin = this;
        FileUtils.loadCFG("config.json",pluginName,JarDir);
        FileUtils.loadCFG("cage.json",pluginName,JarDir);
        FileUtils.loadCFG("lang/zh_cn.json",pluginName,JarDir);
        FileUtils.loadCFG("lang/en_us.json",pluginName,JarDir);
        Variables.configjson=JSONObject.parseObject(FileUtils.readFile("./plugins/"+pluginName+"/config.json"));
        Variables.cagejson=JSONArray.parseArray(FileUtils.readFile("./plugins/"+pluginName+"/cage.json"));
        String langpath="./plugins/"+pluginName+"/lang/"+Variables.configjson.getString("lang")+".json";
        if(!new File(langpath).exists()){
            plugin.getLogger().warning("LANGUAGE \""+Variables.configjson.getString("lang")+".json\" NOT FOUND.LOADING EN_US.json");
            langpath="./plugins/BridgingPractise/lang/en_us.json";
        }
        Variables.langjson=JSONObject.parseObject(FileUtils.readFile(langpath));
        game=new Game(Variables.langjson.getString("gamename"));
        JSONArray rooms=Variables.configjson.getJSONArray("rooms");
        for(int i=0;i<rooms.size();i++){
            JSONObject room=rooms.getJSONObject(i);
            game.addGame(new Room(room,room.getJSONArray("team"),room.getString("id"),room.getString("name"),room.getInteger("maxplayer"),room.getInteger("lowplayer")));
        }
        plugin.getServer().getCommandMap().register("bwp",new Commands("bwp","Bedwars+"));
    }
}
