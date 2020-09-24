package cn.ricoco.bedwarsplus.others;

import cn.nukkit.plugin.Plugin;
import cn.ricoco.bedwarsplus.Main;
import cn.ricoco.funframework.Utils.FileUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
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
        if (!plugins.containsKey("MobPlugin")){
            try {
                Main.plugin.getLogger().warning("CANNOT FOUND PLUGIN MobPlugin!Loading as a dependence!");
                File jarPlugin=new File("./plugins/"+Main.pluginName+"/depends/MobPlugin.jar");
                if(jarPlugin.exists()){
                    injectClass(jarPlugin,"nukkitcoders.mobplugin");
                }else{
                    FileUtils.downloadFile("https://resources.bedwar.cf/depends/MobPlugin.jar","./plugins/"+Main.pluginName+"/depends","MobPlugin.jar");
                    injectClass(new File("./plugins/"+Main.pluginName+"/depends/MobPlugin.jar"),"nukkitcoders.mobplugin");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectClass(File file, String classLoader) throws Exception {
        Main.plugin.getLogger().warning("Attempting to reflect " + classLoader + " classpath");
        URLClassLoader autoload = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(autoload, file.toURI().toURL());
    }
}
