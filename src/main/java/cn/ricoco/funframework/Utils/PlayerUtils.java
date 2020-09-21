package cn.ricoco.funframework.Utils;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;

import java.util.Map;

public class PlayerUtils {
    public static void removeItemToPlayer(Player player, Item item){
        player.getInventory().removeItem(item);
    }
    public static Item getPlayerItemInHand(Player player){
        return player.getInventory().getItemInHand();
    }
    public static void addItemToSlot(Player p,int slot,Item item){
        Map<Integer,Item> invm=p.getInventory().getContents();
        invm.put(slot,item);
        p.getInventory().setContents(invm);
    }
    public static Boolean checkDeath(EntityDamageByEntityEvent event,Boolean ifCancel){
        if(ifCancel){
            event.setCancelled();
        }
        return (event.getEntity().getHealth() - event.getDamage()) <= 0.3;
    }
    public static Boolean checkDeath(EntityDamageEvent event,Boolean ifCancel){
        if(ifCancel){
            event.setCancelled();
        }
        return (event.getEntity().getHealth() - event.getDamage()) <= 0.3;
    }
}
