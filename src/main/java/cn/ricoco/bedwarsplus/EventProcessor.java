package cn.ricoco.bedwarsplus;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.bedwarsplus.entity.BridgeEgg;
import cn.ricoco.funframework.Utils.OtherUtils;
import cn.ricoco.funframework.Utils.PlayerUtils;
import cn.ricoco.funframework.entity.FireballSpawner;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.Team;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

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
            }else if(oHId==344){
                double f = 1,pitch=p.pitch,yaw=p.yaw;
                Position pos=p.getPosition();
                double P = (pitch + 90.0D) * Math.PI / 180.0D;
                double y = (yaw + 90.0D) * Math.PI / 180.0D;
                double posx = Math.sin(P) * Math.cos(y) * (Math.abs(90 - pitch) / 90.0D) + pos.x;
                double posy = Math.sin(P) * 0.03D + pos.y + 1.0D + ((90 - pitch) / 90.0D);
                double posz = Math.sin(P) * Math.sin(y) * (Math.abs(90 - pitch) / 90.0D) + pos.z;
                Entity k = Entity.createEntity("BridgeEgg",pos.level.getChunk(((int)posx)>>4,((int)posz)>>4),Entity.getDefaultNBT(new Vector3(posx,posy,posz)));
                BridgeEgg bridgeEgg = (BridgeEgg) k;
                bridgeEgg.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                        Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));
                bridgeEgg.spawnToAll();
                event.setCancelled();
                PlayerUtils.removeItemToPlayer(p, Item.get(344,0,1));
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.isCancelled()){return;}
        Entity entity=event.getEntity();
        Room room=Main.game.getRoomByLevel(entity.getPosition().level.getName());
        if(room==null){return;}
        if(entity.getName().equals("BedwarsNPC")){
            event.setCancelled();
            if(entity.getNameTag().equals(Variables.langjson.getString("enhance_npc"))){
                System.out.println("ENHANCE_NPC_BW+");
            }
            if(entity.getNameTag().equals(Variables.langjson.getString("shop_npc"))){
                System.out.println("SHOP_NPC_BW+");
            }
        }
        if(entity instanceof Player){
            Player p=(Player)entity;
            if(PlayerUtils.checkDeath(event)){
                Team dmgTeam=room.getTeamByPlayer(event.getDamager().getName());
                Team killedTeam=room.getTeamByPlayer(p.getName());
                room.allMassage(Variables.langjson.getJSONObject("kill").getString("kill").replaceAll("%killed_name%",p.getName()).replaceAll("%killed_color%", killedTeam.color).replaceAll("%killer_name%",event.getDamager().getName()).replaceAll("%killer_color%", dmgTeam.color));
            }else{
                JSONObject atkInfo=new JSONObject();
                atkInfo.put("lastp",event.getDamager().getName());
                atkInfo.put("time", OtherUtils.getTime());
                room.otherInfo.put("lastatk",atkInfo);
            }
        }else{

        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled()){return;}
        Entity entity=event.getEntity();
        Room room=Main.game.getRoomByLevel(entity.getPosition().level.getName());
        if(room==null){return;}
        if(entity.getName().equals("BedwarsNPC")){
            event.setCancelled();
        }
        String dmgCause=event.getCause().toString();
        if(!Variables.disableEntityDMG.contains(dmgCause)){
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
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event){
        if(event.isCancelled()){return;}
        Room room=Main.game.getRoomByLevel(event.getPosition().level.getName());
        List<Block> blockList=event.getBlockList();
    }
}
