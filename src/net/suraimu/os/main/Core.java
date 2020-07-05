package net.suraimu.os.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.suraimu.os.listeners.hitEvent;
import net.suraimu.os.listeners.onDeath;
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
    public static Map<String, ArrayList<String>> arenaplayers;
    public static Map<String, String> playingplayers;
    public static Random rand;
    
public void onEnable(){
        plugin = this;
        cfg = plugin.getConfig();
        msgs = new HashMap<String, String>();
        activearenas = new HashMap<String, Boolean>();
        arenaplayers = new HashMap<>();
        playingplayers = new HashMap<>();
        addDefaults();
        prefix = cfg.getString("prefix"); 
        rand = new Random();
        
getCommand("oneshot").setExecutor((CommandExecutor) new oscommand());

getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aenabled"));
getServer().getPluginManager().registerEvents(new onDeath(), this);
getServer().getPluginManager().registerEvents(new hitEvent(), this);
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
     cfg.options().copyDefaults(true);
     cfg.addDefault("prefix", "&e&lOS&7  ");
     cfg.addDefault("messages.shotby", "§c§lYou were shot by §4§l%KILLER%");

     if(!(cfg.getConfigurationSection("messages") == null))for(String message : cfg.getConfigurationSection("messages").getKeys(false)) msgs.put(message, cfg.getString("messages."+message));

       if(!(cfg.getConfigurationSection("arena") == null))
          for(String thearena : cfg.getConfigurationSection("arena").getKeys(false)){
          if(cfg.get("arena."+thearena+".enabled") != null) activearenas.put(thearena, cfg.getBoolean("arena."+thearena+".enabled"));
       }
     plugin.saveConfig();
}

}
