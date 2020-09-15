package cn.ricoco.bedwarsplus.managers;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.ricoco.funframework.fakeinventories.inventory.FakeSlotChangeEvent;

public class ShopManager implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFakeSlotChange(FakeSlotChangeEvent event){

    }
}
