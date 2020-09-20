package cn.ricoco.funframework.fakeinventories.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeSlotChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final FakeInventory inventory;
    private final SlotChangeAction action;
    private boolean cancelled = false;

    public FakeSlotChangeEvent(Player player, FakeInventory inventory, SlotChangeAction action) {
        this.player = player;
        this.inventory = inventory;
        this.action = action;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }

    @Override
    public void setCancelled(boolean b) {

    }
    public static HandlerList getHandlers() {
        return handlers;
    }
}
