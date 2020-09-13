package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.ricoco.funframework.Utils.PlayerUtils;
import cn.ricoco.funframework.entity.FireballSpawner;

public class EventProcessor implements Listener {
    private final Main plugin;
    public EventProcessor(Main main) {
        this.plugin = main;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event){
        if(event.isCancelled()){return;}
        Player p=event.getPlayer();
        if(Main.game.getRoomByPlayer(p.getName())==null){return;}
        PlayerInteractEvent.Action action = event.getAction();
        if((action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)||action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_AIR))&&PlayerUtils.getPlayerItemInHand(p).getId()==385){
            new FireballSpawner(p.getPosition(),p.yaw,p.pitch);
            PlayerUtils.removeItemToPlayer(p, Item.get(385,0,1));
            event.setCancelled();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.isCancelled()){return;}
        Entity entity=event.getEntity();
        if(entity.getName().equals("BedwarsNPC")){
            event.setCancelled();
            Server.getInstance().getLogger().info("NPC_HURTED");
        }
    }
}
