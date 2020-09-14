package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.ricoco.funframework.Utils.PlayerUtils;
import cn.ricoco.funframework.entity.FireballSpawner;
import cn.ricoco.funframework.game.Room;

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
        if((action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)||action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_AIR))){
            int oHId=PlayerUtils.getPlayerItemInHand(p).getId();
            if(oHId==385){
                new FireballSpawner(p.getPosition(),p.yaw,p.pitch);
                PlayerUtils.removeItemToPlayer(p, Item.get(385,0,1));
                event.setCancelled();
            }else if(oHId==355&&PlayerUtils.getPlayerItemInHand(p).getCustomName().equals(Variables.langjson.getString("returntolobby"))){
                Server.getInstance().dispatchCommand(p,"bwp leave");
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.isCancelled()){return;}
        Entity entity=event.getEntity();
        if(entity.getName().equals("BedwarsNPC")){
            event.setCancelled();
            if(entity.getNameTag().equals(Variables.langjson.getString("enhance_npc"))){
                System.out.println("ENHANCE_NPC_BW+");
            }
            if(entity.getNameTag().equals(Variables.langjson.getString("shop_npc"))){
                System.out.println("SHOP_NPC_BW+");
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled()){return;}
        Entity entity=event.getEntity();
        if(entity.getName().equals("BedwarsNPC")){
            event.setCancelled();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event){
        if(event.isCancelled()){return;}
        Player p=event.getPlayer();
        Room room=Main.game.getRoomByPlayer(p.getName());
        if(room==null){return;}
        if(room.roomStage==0||p.getGamemode()==3){
            event.setCancelled();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if(event.isCancelled()){return;}
        Player p=event.getPlayer();
        Room room=Main.game.getRoomByPlayer(p.getName());
        if(room==null){return;}
        if(room.roomStage==0||p.getGamemode()==3){
            event.setCancelled();
        }
    }
}
