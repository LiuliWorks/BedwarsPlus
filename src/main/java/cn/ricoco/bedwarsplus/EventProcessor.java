package cn.ricoco.bedwarsplus;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;

public class EventProcessor implements Listener {
    private final Main plugin;
    public EventProcessor(Main main) {
        this.plugin = main;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event){
        PlayerInteractEvent.Action action = event.getAction();
        if(action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)||action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_AIR)){
//            Entity.createEntity(Entity.Primed);
        }
    }
}
