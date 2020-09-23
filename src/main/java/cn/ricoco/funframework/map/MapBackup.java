package cn.ricoco.funframework.map;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.ricoco.funframework.Utils.FileUtils;

public class MapBackup {
    public static void backupMap(String mapFolderName){
        try {
            FileUtils.Copydir("./worlds/"+mapFolderName,"./plugins/FunFramework/MAP_BACKUP/"+mapFolderName+"/");
        }catch (Exception e){
            System.out.println("FUNFRAMEWORK ERROR!Caused by a plugin.\n"+e);
        }
    }
    public static void restoreMap(String mapFolderName){
        Level thisLevel=Server.getInstance().getLevelByName(mapFolderName);
        if(thisLevel!=null){
            Server.getInstance().unloadLevel(thisLevel);
        }
        try {
            FileUtils.deldir("./worlds/"+mapFolderName);
            FileUtils.Copydir("./plugins/FunFramework/MAP_BACKUP/"+mapFolderName+"/","./worlds/"+mapFolderName);
            FileUtils.deldir("./plugins/FunFramework/MAP_BACKUP/"+mapFolderName+"/");
        }catch (Exception e){
            System.out.println("FUNFRAMEWORK ERROR!Caused by a plugin.\n"+e);
        }
    }
}
