package me.raymart.cmd;

import java.util.List;
import me.raymart.NoSwear;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd implements CommandExecutor {
    public static NoSwear swear;
    public cmd(NoSwear instance) {
        swear = instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        String prefix = swear.getConfig().getString("Prefix").replace("&", "§") + " " + ChatColor.RESET;
        if(label.equalsIgnoreCase("noswear")) {
            if(sender.hasPermission("noswear.admin")) {
                if(args.length == 0) {
                    sender.sendMessage(prefix);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/noswear reload: &fto reload the config"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/noswear add <word>: &fto add swear word"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/noswear delete <word>: &fto delete swear word"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/noswear list: &fcheck all swear words"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/noswear chatcooldown <seconds>: &fset the chat cooldown"));				return true;
                }
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.reload")) {
                            swear.reloadConfig();
                            sender.sendMessage(ChatColor.GREEN + "config successfully reloaded");
                            return true;
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("add")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.add")) {
                            sender.sendMessage(ChatColor.RED + "/noswear add <word>");
                            return true;
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("delete")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.delete")) {
                            sender.sendMessage(ChatColor.RED + "/noswear delete <word>");
                            return true;
                        }else {
                            if(!(sender instanceof Player)) {
                                sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("list")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.list")) {
                            List<String> words = swear.getConfig().getStringList("List");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[&6List of Words&9]"));
                            for(String s : words) {
                                sender.sendMessage(ChatColor.GOLD + "* " + ChatColor.AQUA + s);
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                            return true;
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("clearchat") || (args[0].equalsIgnoreCase("cc"))) {
                        if(!sender.hasPermission("noswear.admin.clearchat")) {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "/clearchat <* for all players or <playername>");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("chatcooldown")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.chatcooldown")) {
                            sender.sendMessage(ChatColor.GOLD + "ChatCooldown: " + ChatColor.GREEN + String.valueOf(swear.getConfig().getInt("ChatCooldown.Interval")) + " second(s)");
                            return true;
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("ccall") || (args[0].equalsIgnoreCase("clearchatall"))) {
                        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                            for(int i = 0; i < 100; i++) {
                                players.sendMessage(" ");
                            }
                            String admin;
                            if(sender instanceof Player)
                                admin = "Admin";
                            else {
                                admin = "Console";
                            }
                            players.sendMessage(ChatColor.AQUA + "Chat has been cleared by " + ChatColor.GOLD + "" + ChatColor.BOLD + admin);
                            return true;
                        }
                        return true;
                    }else {
                        sender.sendMessage(prefix + ChatColor.DARK_RED + "Command not found");
                    }
                    return true;
                }
                if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("add")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        String word = args[1];
                        List words = swear.getConfig().getStringList("List");
                        if(sender.hasPermission("noswear.admin.add")) {
                            if(words.contains(word)) {
                                sender.sendMessage(ChatColor.RED + "the word " + ChatColor.GOLD + word + ChatColor.RED + " has already existed");
                                return true;
                            }
                            words.add(word);
                            swear.getConfig().set("List", words);
                            swear.saveConfig();
                            swear.reloadConfig();
                            sender.sendMessage(ChatColor.GREEN + "the word " + ChatColor.GOLD + word + ChatColor.GREEN + " has been added to the swear words");
                            return true;
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    String word = args[1];
                    List words = swear.getConfig().getStringList("List");
                    if(args[0].equalsIgnoreCase("delete")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(sender.hasPermission("noswear.admin.delete")) {
                            if(words.contains(word)) {
                                words.remove(word);
                                swear.getConfig().set("List", words);
                                sender.sendMessage(ChatColor.GREEN + "the word " + ChatColor.GOLD + word + ChatColor.GREEN + " has been removed to the swear words");
                            }else {
                                sender.sendMessage(ChatColor.RED + "the word " + ChatColor.GOLD + args[1] + ChatColor.RED + " does not exist");
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("clearchat") || (args[0]).equalsIgnoreCase("cc")) {
                        if(sender.hasPermission("noswear.admin.clearchat")) {
                            @SuppressWarnings("deprecation")
                            Player target = Bukkit.getServer().getPlayer(args[1]);
                            if(target == null) {
                                sender.sendMessage(ChatColor.RED + "Player " + target + " is not online");
                                return true;
                            }
                            String admin;
                            if(sender instanceof Player)
                                admin = "Admin";
                            else {
                                admin = "Console";
                            }
                            clearchat(target);
                            target.sendMessage(ChatColor.AQUA + "Chat has been cleared by " + ChatColor.GOLD + "" + ChatColor.BOLD + admin);
                            return true;
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("chatcooldown")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "You must be a player to do that");
                            return true;
                        }
                        if(!sender.hasPermission("noswear.admin.chatcooldown")) {
                            sender.sendMessage(ChatColor.RED + "You have no permission to do that");
                            return true;
                        }
                        try {
                            int interval = Integer.parseInt(args[1]);
                            swear.getConfig().set("Interval", Integer.valueOf(interval));
                            swear.saveConfig();
                            swear.reloadConfig();
                            sender.sendMessage(ChatColor.GREEN + "Updated time between messages to " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " second" + (interval == 1 ? "" : "s"));
                        }catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.GOLD + args[1] + ChatColor.RED + " is not a valid number");
                        }
                        return true;
                    }else {
                        sender.sendMessage(prefix + ChatColor.DARK_RED + "Command not found");
                    }
                    return true;
                }
            }else {
                sender.sendMessage(ChatColor.RED + "You have no permission to do that");
            }
            return true;
        }
        return false;
    }

    private void clearchat(Player p) {
        for(int i = 0; i < 100; i++) {
            p.sendMessage(" ");
        }
    }

}