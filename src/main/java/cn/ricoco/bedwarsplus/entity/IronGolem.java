package cn.ricoco.bedwarsplus.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import nukkitcoders.mobplugin.entities.monster.WalkingMonster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IronGolem extends WalkingMonster {

    public static final int NETWORK_ID = 20;

    public IronGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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
        this.setMaxHealth(100);
        super.initEntity();
        this.setDamage(new float[] { 0, 21, 21, 21 });
        this.setMinDamage(new float[] { 0, 7, 7, 7 });
    }

    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared(player) < 4) {
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
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
        }
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        return !(creature instanceof Player) && creature.isAlive() && distance <= 60;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        return drops.toArray(new Item[0]);
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
