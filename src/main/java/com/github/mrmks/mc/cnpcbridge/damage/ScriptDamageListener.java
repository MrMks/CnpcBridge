package com.github.mrmks.mc.cnpcbridge.damage;

public interface ScriptDamageListener {
    /**
     *
     * @param name The name of the damager
     * @param uuid the uuid of the damager in String
     * @param damage how many damage the damager dealt to you;
     * @param isPlayer whether the damager is a player or not
     * @param type the id of the event type, also see {@link org.bukkit.event.entity.EntityDamageEvent.DamageCause}
     */
    void onDamaged(String name, String uuid, float damage, boolean isPlayer, int type);
}
