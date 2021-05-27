package com.github.mrmks.mc.cnpcbridge;

import com.github.mrmks.mc.cnpcbridge.attack.AttackListener;
import com.github.mrmks.mc.cnpcbridge.cmd.CmdMulti;
import com.github.mrmks.mc.cnpcbridge.cmd.CmdTpcw;
import com.github.mrmks.mc.cnpcbridge.damage.DamageListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class CnpcBridge extends JavaPlugin {
    private DamageListener damageListener;
    private AttackListener attackListener;

    private static CnpcBridge bridge;

    @Override
    public void onLoad() {
        super.onLoad();
        FileConfiguration cfg = getConfig();
        if (cfg.getBoolean("setClassLoader", false)) {
            ClassLoader cl = this.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(cl);
        }
        if (!getDataFolder().exists()) {
            saveDefaultConfig();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        bridge = this;
        damageListener = new DamageListener();
        attackListener = new AttackListener();
        getCommand("mcmd").setExecutor(new CmdMulti());
        getCommand("tpcw").setExecutor(new CmdTpcw());
        getServer().getPluginManager().registerEvents(damageListener, this);
        getServer().getPluginManager().registerEvents(attackListener, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        HandlerList.unregisterAll(this);
        getCommand("mcmd").setExecutor(null);
        getCommand("tpcw").setExecutor(null);
        damageListener = null;
        attackListener = null;
        bridge = null;
    }

    public static DamageListener getDamageListener(){
        return bridge == null ? null : bridge.damageListener;
    }

    public static AttackListener getAttackListener() {
        return bridge == null ? null : bridge.attackListener;
    }

}
