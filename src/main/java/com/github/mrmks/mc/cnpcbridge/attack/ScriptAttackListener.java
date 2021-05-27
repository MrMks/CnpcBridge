package com.github.mrmks.mc.cnpcbridge.attack;

public interface ScriptAttackListener {
    /**
     *
     * @param target The target you attack to;
     * @param uuid The uuid of your target in String
     * @param damage how many damage you dealt to your target in final
     */
    void onAttack(String target, String uuid, double damage, boolean isPlayer);
}
