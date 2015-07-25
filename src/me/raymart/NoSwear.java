package me.raymart;

import me.raymart.Listeners.MainListener;
import me.raymart.cmd.cmd;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NoSwear extends JavaPlugin {
    public static NoSwear plugin;

    public void onEnable() {
        getProcess();
        getLogger().info("[NoSwear] successfully enabled");
    }

    public void onDisable() {
        getLogger().info("[NoSwear] successfully disabled");
    }

    public void getProcess() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainListener(this), this);
        getCommand("noswear").setExecutor(new cmd(this));
        saveDefaultConfig();
    }
}