package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.Team;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;

import java.util.HashMap;

public class EntityGuarder extends WalkingMonster {
    public static int NETWORK_ID = 0;

    private Boolean showSwing=false;

    public String teamName="";
    public String teamColor="";
    public Room room=null;

    public EntityGuarder(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void setFollowTarget(Entity target) {
        if(target==null){
            return;
        }
        boolean canATK=true;
        if(room!=null && target instanceof Player){
            Team team=room.getTeamByPlayer(target.getName());
            if(team.name.equals(teamName)){
                canATK=false;
            }
        }
        if(canATK) {
            this.setFollowTarget(target, true);
        }
    }

    @Override
    public void setFollowTarget(Entity target, boolean attack) {
        if(target==null){
            return;
        }
        boolean canATK=true;
        if(room!=null && target instanceof Player){
            Team team=room.getTeamByPlayer(target.getName());
            if(team.name.equals(teamName)){
                canATK=false;
            }
        }
        if(canATK) {
            this.setFollowTarget(target, true);
        }
        this.canAttack = attack;
    }


    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        StringBuilder nameTag=new StringBuilder(teamColor+teamName);
        nameTag.append("§f[§a");
        for(int i=0;i<this.getHealth();i++){
            nameTag.append("=");
        }
        for(int i=0;i<(this.getMaxHealth()-this.getHealth());i++){
            nameTag.append(" ");
        }
        nameTag.append("§f] ");
        nameTag.append((int)(1200-this.age)/20);
        nameTag.append("s");
        this.setNameTag(nameTag.toString());
        if (this.age > 1200){
            this.kill();
        }
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public void attackEntity(Entity player) {
        if(player==null){
            return;
        }
        boolean canATK=true;
        if(room!=null && player instanceof Player){
            Team team=room.getTeamByPlayer(player.getName());
            if(team.name.equals(teamName)){
                canATK=false;
            }
        }
        if (this.attackDelay > 23 && this.distanceSquared(player) < 4 && canATK) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());

            if (player instanceof Player) {
                HashMap<Integer, Float> armorValues = new ArmorPoints();

                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0f);
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            if(showSwing) {
                EntityEventPacket pk = new EntityEventPacket();
                pk.eid = this.id;
                pk.event = EntityEventPacket.ARM_SWING;
                this.getLevel().getPlayers().values().forEach((p -> p.dataPacket(pk)));
            }
            EntityDamageByEntityEvent entityDamageByEntityEvent=new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0F);
            player.attack(entityDamageByEntityEvent);
        }
    }

    @Override
    public boolean canDespawn() {
        return false;
    }
}
