package cn.ricoco.funframework.Utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class PlayerUtils {
    public static void removeItemToPlayer(Player player, Item item){
        player.getInventory().removeItem(item);
    }
    public static Item getPlayerItemInHand(Player player){
        return player.getInventory().getItemInHand();
    }
}
