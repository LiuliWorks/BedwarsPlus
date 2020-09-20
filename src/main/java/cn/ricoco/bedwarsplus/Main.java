package cn.ricoco.bedwarsplus;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.ServicePriority;
import cn.ricoco.bedwarsplus.entity.BedwarsNPC;
import cn.ricoco.bedwarsplus.entity.BridgeEgg;
import cn.ricoco.bedwarsplus.managers.ShopManager;
import cn.ricoco.bedwarsplus.others.init;
import cn.ricoco.funframework.entity.Fireball;
import cn.ricoco.funframework.entity.FloatingText;
import cn.ricoco.funframework.fakeinventories.FakeInventoriesListener;
import cn.ricoco.funframework.fakeinventories.inventory.FakeInventories;
import cn.ricoco.funframework.game.Game;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.Utils.FileUtils;
import cn.ricoco.funframework.game.Team;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.Date;

public class Main extends PluginBase {
    public static Main plugin;
    public static Game game=null;
    public static String pluginName="BedwarsPlus";
    public static String JarDir=Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    @Override
    public void onEnable() {
        plugin = this;
        Long stime=new Date().getTime();
        //USED　BY FUNFRAMEWORK
        new File("./plugins/FunFramework/MAP_BACKUP").mkdirs();
        Entity.registerEntity("FireBall", Fireball.class);
        Entity.registerEntity("Bedwars+_FloatingText", FloatingText.class);
        FakeInventories fakeInventories = new FakeInventories();
        getServer().getServiceManager().register(FakeInventories.class, fakeInventories, this, ServicePriority.HIGHEST);
        getServer().getPluginManager().registerEvents(new FakeInventoriesListener(fakeInventories), this);
        //USED BY THIS PLUGIN
        init.checkPlugin();
        Entity.registerEntity("BedwarsNPC", BedwarsNPC.class);
        Entity.registerEntity("BridgeEgg", BridgeEgg.class);
        FileUtils.loadCFG("config.json",pluginName,JarDir);
        FileUtils.loadCFG("cage.json",pluginName,JarDir);
        FileUtils.loadCFG("shop.json",pluginName,JarDir);
        FileUtils.loadCFG("lang/zh_cn.json",pluginName,JarDir);
        FileUtils.loadCFG("lang/en_us.json",pluginName,JarDir);
        FileUtils.loadCFG("lang/ko_kr.json",pluginName,JarDir);//Korean translate by JuChanMin
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
            JSONObject json=room.getJSONObject("pos");
            Server.getInstance().loadLevel(json.getString("level"));
            Room roomC=new Room(room,room.getJSONArray("team"),room.getString("id"),room.getString("name"),room.getInteger("maxplayer"),room.getInteger("lowplayer"), Position.fromObject(new Vector3(json.getJSONObject("wait").getDouble("x"),json.getJSONObject("wait").getDouble("y"),json.getJSONObject("wait").getDouble("z")), Server.getInstance().getLevelByName(json.getString("level"))));
            JSONArray teams=room.getJSONArray("team");
            for(int j=0;j<teams.size();j++){
                JSONObject nteam=teams.getJSONObject(j);
                JSONObject posJson=json.getJSONObject(nteam.getString("id"));
                Team team=new Team(nteam.getString("id"),nteam.getString("color"),nteam.getString("name"),Position.fromObject(new Vector3(posJson.getDouble("x"),posJson.getDouble("y"),posJson.getDouble("z")),Server.getInstance().getLevelByName(json.getString("level"))),new JSONObject());
                roomC.addTeam(team);
            }
            game.addGame(roomC);
        }
        Variables.disableEntityDMG.add("ENTITY_EXPLOSION");
        Variables.disableEntityDMG.add("ENTITY_ATTACK");
        plugin.getServer().getCommandMap().register("bwp",new Commands("bwp","Bedwars+"));
        getServer().getPluginManager().registerEvents(new EventProcessor(this), this);
        getServer().getPluginManager().registerEvents(new ShopManager(), this);
        PluginTick.StartTick();
        this.getLogger().info(" _____   _____   _____   _          __   ___   _____    _____   _____   _       _   _   _____ ");
        this.getLogger().info("|  _  \\ | ____| |  _  \\ | |        / /  /   | |  _  \\  /  ___/ |  _  \\ | |     | | | | /  ___/");
        this.getLogger().info("| |_| | | |__   | | | | | |  __   / /  / /| | | |_| |  | |___  | |_| | | |     | | | | | |___ ");
        this.getLogger().info("|  _  { |  __|  | | | | | | /  | / /  / / | | |  _  /  \\___  \\ |  ___/ | |     | | | | \\___  \\");
        this.getLogger().info("| |_| | | |___  | |_| | | |/   |/ /  / /  | | | | \\ \\   ___| | | |     | |___  | |_| |  ___| |");
        this.getLogger().info("|_____/ |_____| |_____/ |___/|___/  /_/   |_| |_|  \\_\\ /_____/ |_|     |_____| \\_____/ /_____/ ");
        this.getLogger().info("By RicoGG.§aSuccessfuly Loaded in §e"+(new Date().getTime()-stime)+"ms!");
    }
    @Override
    public void onDisable() {
        getServer().getServiceManager().cancel(this);
    }
}
