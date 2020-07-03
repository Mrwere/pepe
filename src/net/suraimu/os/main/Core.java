package net.suraimu.os.main;

import java.util.HashMap;
import java.util.Map;
import static net.suraimu.os.main.Core.cfg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

public class Core extends JavaPlugin{

    public static Core plugin;
    public static FileConfiguration cfg;
    public static Map<String, String> msgs;
    public static String prefix;
    public static Map<String, Boolean> activearenas;
    
public void onEnable(){
        plugin = this;
        cfg = plugin.getConfig();
        msgs = new HashMap<String, String>();
        activearenas = new HashMap<String, Boolean>();
        addDefaults();
        prefix = cfg.getString("prefix");        
        
getCommand("oneshot").setExecutor((CommandExecutor) new oscommand());

getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aenabled"));
}

public void onDisable(){
getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&cdisabled"));
}

public static void updateMsgs(){
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
        Bukkit.getPluginManager().enablePlugin(plugin);
}

public void addDefaults(){
     cfg.addDefault("prefix", "&e&lOS &6>> ");

     if(!(cfg.getConfigurationSection("messages") == null))for(String message : cfg.getConfigurationSection("messages").getKeys(false)) msgs.put(message, cfg.getString("messages."+message));

       if(!(cfg.getConfigurationSection("arena") == null))
          for(String thearena : cfg.getConfigurationSection("arena").getKeys(false)){
          if(cfg.get("arena."+thearena+".enabled") != null) activearenas.put(thearena, cfg.getBoolean("arena."+thearena+".enabled"));
       }
}

}
