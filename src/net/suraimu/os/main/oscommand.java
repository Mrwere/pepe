package net.suraimu.os.main;


import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.msgs;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.main.Core.prefix;
import static net.suraimu.os.main.Core.updateMsgs;
import static net.suraimu.os.utils.tellHim.tellHim;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class oscommand implements CommandExecutor {
    
                      
    
       public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
           
        if (label.equalsIgnoreCase("oneshot") || label.equalsIgnoreCase("os")){
       
         if (args.length != 0) {
             if (cs.hasPermission("os.user")){
                 
                    if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) tellHim("helppage", cs);
                    if(args[0].equalsIgnoreCase("join")){ tellHim("connecting", cs);}
                    if(args[0].equalsIgnoreCase("leave")){ tellHim("userinfo.left", cs);}
                 
                 if(cs.hasPermission("os.admin")){
                    if(args[0].equalsIgnoreCase("reload")){ tellHim("reloadsuccess", cs);  updateMsgs();}
                  if(args.length >= 1){
                    if(args[0].equalsIgnoreCase("arena")){
                   if(args.length >= 2){
                    if(activearenas.get(args[1]) != null){
                     if(activearenas.get(args[1]) == true){
                      tellHim("disablearenafirst", cs);
                      return true;
                     } else arenaCommands(cs, cmd, label, args);
                    } else if(args.length >= 3 && args[2].equalsIgnoreCase("create")){
                       if(cfg.get("arena."+args[1]+".enabled") == null){
                             cfg.set("arena."+args[1]+".enabled", false);
                             activearenas.put(args[1], false);
                             plugin.saveConfig();
                             cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&7Arena &d'"+args[1]+"' &7created."));
                       } else tellHim("arenaunavailable", cs);
                    } else tellHim("arenanotfound", cs);
                    } else tellHim("arenausage", cs);
                    }
                    }
             }
           } else tellHim("noperm", cs);
             
            } else tellHim("helppage", cs);

        }
        return true;
    }
       
       public boolean arenaCommands(CommandSender cs, Command cmd, String label, String[] args){
           if(args.length >= 3){
            if(args[2].equalsIgnoreCase("addspawn")){
               if(!(cs instanceof Player)){tellHim("onlyforplayers", cs); return true;}
             int supremei = 0;
               while(cfg.getString("arena."+args[1]+"spawn"+supremei) != null) supremei++;
               Location l = ((Player)cs).getLocation();
            cfg.set("arena."+args[1]+".spawn"+supremei+".world", l.getWorld().getName());
            cfg.set("arena."+args[1]+".spawn"+supremei+".x", l.getX());
            cfg.set("arena."+args[1]+".spawn"+supremei+".y", l.getY());
            cfg.set("arena."+args[1]+".spawn"+supremei+".z", l.getZ());
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn&7 added for arena &5"+args[1]+"&7."));
            }
           if(args[2].equalsIgnoreCase("remspawn")){
            if(!(args.length >= 4)){tellHim("remspawnusage", cs); return true;}
            if(cfg.get("arena."+args[1]+".spawn"+args[3]) == null){tellHim("spawnnotfound", cs); return true;}
            
            cfg.set("arena."+args[1]+".spawn"+args[3], null);
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn"+args[3]+"&7 removed from arena &5"+args[1]+"&7."));
            }
           if(args[2].equalsIgnoreCase("spawninfo")){
            if(!(args.length >= 4)){tellHim("spawninfousage", cs); return true;}
            if(cfg.getConfigurationSection("arena."+args[1]+".spawn"+args[3]) == null){tellHim("spawnnotfound", cs); return true;}
            
            String w = cfg.getString("arena."+args[1]+".spawn"+args[3]+".world");
            double x = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".x"));
            double y = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".y"));
            double z = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".z"));
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn"+args[3]+"&7's x:&d"+x+"&7 y:&d"+y+"&7 z:&d"+z+"&7 world:&d"+w+"&7."));
            }
           if(args[2].equalsIgnoreCase("spawnlist")){
           int i = 0;
          while(cfg.get("arena."+args[1]+".spawn"+i) != null) i++;
           cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&7Found &d"+i+"&7 spawns in arena &5"+args[1]+"&7."));
            }
           } else tellHim("arenausage", cs);
           
           return true;
       }
    
}