package cn.ricoco.bedwarsplus.others;

import cn.nukkit.plugin.Plugin;
import cn.ricoco.bedwarsplus.Main;
import cn.ricoco.funframework.Utils.FileUtils;

import java.util.Map;

public class init {
    public static void checkPlugin(){
        Map<String, Plugin> plugins= Main.plugin.getServer().getPluginManager().getPlugins();
        if (!plugins.containsKey("ScoreboardPlugin")){
            try {
                FileUtils.downloadPlugin("https://resources.bedwar.cf/depends/ScoreboardAPI.jar","ScoreboardAPI");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
