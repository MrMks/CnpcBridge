package com.github.mrmks.mc.cnpcbridge.damage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class DamageListener implements Listener {
    private boolean active = false;
    private final HashMap<String, ScriptDamageListener> listeners = new HashMap<>();
    public boolean isActive(){
        return active;
    }

    public void register(String uuid, ScriptDamageListener listener){
        if (active) listeners.put(uuid, listener);
    }

    public void unregister(String uuid){
        listeners.remove(uuid);
    }

    public DamageListener(){
        active = true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void entityDamage(EntityDamageEvent event){
        if (event == null || event.getEntity() == null) return;
        if (listeners.containsKey(event.getEntity().getUniqueId().toString())) {
            String name;
            String uuid;
            boolean isplayer;
            if (event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) event;
                Entity entity = ede.getDamager();
                if (entity instanceof Projectile) {
                    Projectile projectile = (Projectile) entity;
                    ProjectileSource source = projectile.getShooter();
                    if (source instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) source;
                        name = livingEntity.getName();
                        uuid = livingEntity.getUniqueId().toString();
                        isplayer = source instanceof Player;
                    } else {
                        name = "block";
                        uuid = "none";
                        isplayer = false;
                    }
                } else if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    name = livingEntity.getName();
                    uuid = livingEntity.getUniqueId().toString();
                    isplayer = livingEntity instanceof Player;
                } else {
                    name = "others";
                    uuid = "none";
                    isplayer = false;
                }
            } else {
                name = "block";
                uuid = "none";
                isplayer = false;
            }
            float damage = (float) event.getFinalDamage();
            int type = event.getCause().ordinal();
            listeners.get(event.getEntity().getUniqueId().toString()).onDamaged(name,uuid,damage, isplayer, type);
        }
        Iterator<String> iterator = listeners.keySet().iterator();
        while (iterator.hasNext()) {
            UUID uuid = UUID.fromString(iterator.next());
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null || entity.isDead()) iterator.remove();
        }
    }
}
