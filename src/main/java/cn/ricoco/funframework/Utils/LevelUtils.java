package cn.ricoco.funframework.Utils;

import cn.nukkit.level.Level;

public class LevelUtils {
    public static void setLevelWeather(Level level, String mode){
        if (!mode.equals("clear")) {
            if(mode.equals("rain")){
                level.setRaining(true);
                level.setThundering(false);
            }else if(mode.equals("thunder")){
                level.setThundering(true);
                level.setRaining(false);
            }
        } else {
            level.setRaining(false);
            level.setThundering(false);
        }
    }
}
