package com.github.mrmks.mc.cnpcbridge.attack;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class AttackListener implements Listener {

    private boolean active;
    private final HashMap<String, ScriptAttackListener> map = new HashMap<>();

    public AttackListener() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    private void register(String uuid, ScriptAttackListener listener) {
        if (active) map.put(uuid, listener);
    }

    public void unregister(String uuid) {
        map.remove(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        String uuid = damager.getUniqueId().toString();
        if (map.containsKey(uuid)) {
            Entity target = event.getEntity();
            map.get(uuid).onAttack(
                    target.getName(),
                    target.getUniqueId().toString(),
                    event.getFinalDamage(),
                    target instanceof Player
            );
        }
    }
}
