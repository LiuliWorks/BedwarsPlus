package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.ricoco.funframework.game.Room;
import cn.ricoco.funframework.game.Team;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;
import nukkitcoders.mobplugin.route.WalkerRouteFinder;

import java.util.HashMap;

public class IronGolem extends WalkingMonster implements EntitySmite {

    public static final int NETWORK_ID = 20;

    public String teamName="";
    public String teamColor="";
    public Room room=null;

    public IronGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
        this.setNameTagAlwaysVisible();
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 2.7f;
    }

    @Override
    public double getSpeed() {
        return 0.8;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
        this.setDamage(new float[] { 0, 21, 21, 21 });
        this.setMinDamage(new float[] { 0, 7, 7, 7 });
    }

    public void attackEntity(Entity player) {
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
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.id;
            pk.event = EntityEventPacket.ARM_SWING;
            this.getLevel().getPlayers().values().forEach((p -> p.dataPacket(pk)));
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        StringBuilder nameTag=new StringBuilder(teamColor+teamName);
        nameTag.append("§fHP [§a");
        for(int i=0;i<this.getHealth();i++){
            nameTag.append("=");
        }
        for(int i=0;i<(this.getMaxHealth()-this.getHealth());i++){
            nameTag.append(" ");
        }
        nameTag.append("§f]");
        this.setNameTag(nameTag.toString());
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return "BW+_IronGolem";
    }

    @Override
    public boolean canDespawn() {
        return false;
    }
}
