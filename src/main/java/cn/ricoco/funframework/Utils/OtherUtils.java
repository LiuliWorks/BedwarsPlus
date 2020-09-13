package cn.ricoco.funframework.Utils;

import java.util.Date;

public class OtherUtils {
    public static int getTime(){
        return (int)new Date().getTime()/1000;
    }
    public static boolean randBool(){
        double num=Math.random();
        if(num<0.5){
            return true;
        }
        else{
            return false;
        }
    }
    public static double randDouble(double max,double min){
        return (Math.random()*(max-min)+min);
    }
    public static int randInt(int max,int min){
        return (int) randDouble(max,min);
    }
}
