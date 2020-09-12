package cn.ricoco.funframework.Utils;

import java.util.Date;

public class OtherUtils {
    public static int getTime(){
        return (int)new Date().getTime()/1000;
    }
}
