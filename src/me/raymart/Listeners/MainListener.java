package me.raymart.Listeners;

import java.util.List;
import me.raymart.NoSwear;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    int min;
    int max;
    static String upper;
    public static NoSwear plugin;
    public MainListener(NoSwear instance) {
        plugin = instance;
    }

    @EventHandler
    public void onSwear(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Location location = e.getPlayer().getLocation();
        for (String word : e.getMessage().toLowerCase().split(" "))
            if (!plugin.getConfig().getStringList("List").contains(word)) {
                continue;
            }
            else {
                e.setCancelled(true);
                p.sendMessage(plugin.getConfig().getString("Prefix").replaceAll("&", "§") + ChatColor.GRAY + "Dont use that sort of language");
                if(plugin.getConfig().getBoolean("Explosion", true)) {
                    p.getWorld().createExplosion(location, 0.5F);
                }	else if(plugin.getConfig().getBoolean("Explosion", false)) {
                }
                if(plugin.getConfig().getBoolean("Kick From Swearing", true)) {
                    p.kickPlayer(plugin.getConfig().getString("Kick Message").replaceAll("&", "§"));
                }
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("Command after swearing").replace("<player>", p.getName().replaceAll("none", "")));
            }
        this.min = 4;
        this.max = 35;
        upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (((e.getPlayer().hasPermission("noswear.bypass.caps") ? 0 : 1) & (e.getMessage().length() >= this.min ? 1 : 0) & (getUppercasePercentage(e.getMessage()) > this.max ? 1 : 0)) != 0) {
            e.setMessage(e.getMessage().toLowerCase());
        }
        if (!e.getPlayer().hasPermission("noswear.bypass.spam")) {
            if (plugin.getConfig().getString(e.getPlayer().getName()) == null) {
                plugin.getConfig().set(e.getPlayer().getName(), e.getMessage());
                return;
            }

            if (!e.getMessage().equalsIgnoreCase(
                    plugin.getConfig().getString(e.getPlayer().getName()))) {
                plugin.getConfig().set(e.getPlayer().getName(), e.getMessage());
                return;
            }

            if (e.getMessage().equalsIgnoreCase(
                    plugin.getConfig().getString(e.getPlayer().getName()))) {
                e.setCancelled(true);
                if (plugin.getConfig().getBoolean("spam kick")) {
                    e.getPlayer().kickPlayer(plugin.getConfig().getString("spam kick message").replaceAll("&", "§"));
                    return;
                }
                if(plugin.getConfig().getBoolean("spam warn")) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("spam warn message")));
                    return;
                }
            }
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player user = e.getPlayer();
        String JoinMessage = plugin.getConfig().getString("Welcome Message").replaceAll("&", "§").replace("<player>", user.getName());
        if(plugin.getConfig().getBoolean("Welcome", true)) {
            e.setJoinMessage(plugin.getConfig().getString("Prefix").replaceAll("&", "§") + JoinMessage);
        }else if (plugin.getConfig().getBoolean("Welcome", false)) {
            e.setJoinMessage(null);
        }
        if(plugin.getConfig().getBoolean("Notify", true)) {
            user.sendMessage(plugin.getConfig().getString("Prefix").replaceAll("&", "§") + plugin.getConfig().getString("Notify Message").replaceAll("&", "§").replace("<player>", user.getName()));
        }else if(plugin.getConfig().getBoolean("Notify", false)) {
            user.sendMessage("".replace("", null));
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player user = e.getPlayer();
        String QuitMessage = plugin.getConfig().getString("Welcome Message").replaceAll("&", "§").replace("<player>", user.getName());
        if(plugin.getConfig().getBoolean("Quit", true)) {
            e.setQuitMessage(plugin.getConfig().getString("Prefix").replaceAll("&", "§") + QuitMessage);
        }else if(plugin.getConfig().getBoolean("Quit", false)) {
            e.setQuitMessage(null);
        }
    }
    @EventHandler
    public void onSignChanged(SignChangeEvent e) {
        Player user = e.getPlayer();
        for (int sign = 0; sign < 4; sign++) {
            String msg = e.getLine(sign);
            List<String> list = plugin.getConfig().getStringList("List");

            for (int word = 0; word < list.toArray().length; word++) {
                if ((!msg.toLowerCase().contains(((String)list.get(word)).toLowerCase())) ||
                        (user.hasPermission("noswear.bypass.signswear"))) continue;
                e.setCancelled(true);
                if (plugin.getConfig().getBoolean("Kick From Swearing")) {
                    user.kickPlayer(plugin.getConfig().getString("Kick Message").replaceAll("&", "§"));
                }
                if (plugin.getConfig().getBoolean("Explosion")) {
                    user.getWorld().createExplosion(user.getLocation(), 0.5F);
                }
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("Command after swearing").replace("<player>", user.getName().replaceAll("none", "")));
            }
        }
    }

    @EventHandler
    public void onAdvertiseEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String n = e.getPlayer().getName();
        for (String m : e.getMessage().toLowerCase().split(" "))
            if(m.contains("http://") || m.contains("www.") || m.contains(".com") || m.contains(".net") || m.contains(".org") || m.contains(".ly") || m.contains(".biz") || m.contains(".com") || (m.contains("mc."))) {
                e.setCancelled(true);
                p.sendMessage("Do not advertise");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!plugin.getConfig().getBoolean("OP Adv")) {
                        if (player.isOp()) {
                            player.sendMessage(ChatColor.RED + n + " tried sending the message \"" + m + "\"");

                        }
                    }
                }
            }
    }

    public static boolean isUppercase(String string)
    {
        return upper.contains(string);
    }

    public static double getUppercasePercentage(String string)
    {
        double upper = 0.0D;
        for (int i = 0; i < string.length(); i++) {
            if (isUppercase(string.substring(i, i + 1))) {
                upper += 1.0D;
            }
        }
        return upper / string.length() * 100.0D;
    }
}