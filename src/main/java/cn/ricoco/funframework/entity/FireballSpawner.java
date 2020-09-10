package cn.ricoco.funframework.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;

public class FireballSpawner {
    public FireballSpawner(Position pos,double yaw,double pitch){
        double f = 1;
        double p = (pitch + 90.0D) * Math.PI / 180.0D;
        double y = (yaw + 90.0D) * Math.PI / 180.0D;
        double posx = Math.sin(p) * Math.cos(y) * (Math.abs(90 - pitch) / 90.0D) + pos.x;
        double posy = Math.sin(p) * 0.03D + pos.y + 1.0D + ((90 - pitch) / 90.0D);
        double posz = Math.sin(p) * Math.sin(y) * (Math.abs(90 - pitch) / 90.0D) + pos.z;
        Entity k = Entity.createEntity("FireBall",pos.level.getChunk(((int)posx)>>4,((int)posz)>>4),Entity.getDefaultNBT(new Vector3(posx,posy,posz)));
        if (!(k instanceof Fireball)) {
            return;
        }
        Fireball fireball = (Fireball) k;
        fireball.setExplode(true);
        fireball.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));
        fireball.spawnToAll();
        pos.level.addSound(pos, Sound.MOB_GHAST_FIREBALL);
    }
}
