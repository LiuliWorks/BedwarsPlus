package cn.ricoco.funframework.Utils;

import cn.nukkit.Player;

public class Exp {
    private int exp;
    private int lv;
    public Exp(int lv, int exp){
        this.lv=lv;
        this.exp=exp;
    }
    public int getLv(){
        return lv;
    }
    public int getExp(){
        return exp;
    }
    public void setToPlayer(Player p){
        p.setExperience(this.exp,this.lv);
    }
}
