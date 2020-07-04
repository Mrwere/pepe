package net.suraimu.os.main;


import java.util.ArrayList;
import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.arenaplayers;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.main.Core.prefix;
import static net.suraimu.os.main.Core.updateMsgs;
import static net.suraimu.os.utils.lobbyCountdown.startCountdown;
import static net.suraimu.os.utils.tellHim.tellHim;
import org.bukkit.Bukkit;
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
                    if(args[0].equalsIgnoreCase("join")){
                     if((activearenas.get(args[1]) != null && activearenas.get(args[1])) && cs instanceof Player){
                        tellHim("connecting", cs);
                       ((Player)cs).teleport((Location) cfg.get("arena."+args[1]+".lobby"));
                       if(arenaplayers.get(args[1]) == null) arenaplayers.put(args[1], new ArrayList<>());
                       arenaplayers.get(args[1]).add("Mr_were");
                       startCountdown(args[1]);
                     } else tellHim("arenanotfound", cs);
                    }
                    if(args[0].equalsIgnoreCase("leave")){ tellHim("leaving", cs);}
                 
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
             //set spawn command (there are players teleported after game ends)
            if(args[0].equalsIgnoreCase("setspawn")){
               if(!(cs instanceof Player)){tellHim("onlyforplayers", cs); return true;}
            cfg.set("spawn", ((Player)cs).getLocation());
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dMain spawn &7set successully!"));
            }
             }
           } else tellHim("noperm", cs);
             
            } else tellHim("helppage", cs);

        }
        return true;
    }
       
       public boolean arenaCommands(CommandSender cs, Command cmd, String label, String[] args){
           if(args.length >= 3){
             //setup information
             if(args[2].equalsIgnoreCase("info")) tryOpen(cs,args[1], true);
             //open arena
             if(args[2].equalsIgnoreCase("setup") || args[2].equalsIgnoreCase("open")) tryOpen(cs,args[1], false);            
            //lobby set-up
             if(args[2].equalsIgnoreCase("setlobby")){
               if(!(cs instanceof Player)){tellHim("onlyforplayers", cs); return true;}
            cfg.set("arena."+args[1]+".lobby", ((Player)cs).getLocation());
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dLobby&7 added for arena &5"+args[1]+"&7."));
            }
            //spawn add command
            if(args[2].equalsIgnoreCase("addspawn")){
               if(!(cs instanceof Player)){tellHim("onlyforplayers", cs); return true;}
             int supremei = 0;
            while(cfg.getString("arena."+args[1]+".spawn"+supremei) != null) supremei++;
            cfg.set("arena."+args[1]+".spawn"+supremei, ((Player)cs).getLocation());
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn&7 added for arena &5"+args[1]+"&7."));
            }
           //spawn remove command
           if(args[2].equalsIgnoreCase("remspawn")){
            if(!(args.length >= 4)){tellHim("remspawnusage", cs); return true;}
            if(cfg.get("arena."+args[1]+".spawn"+args[3]) == null){tellHim("spawnnotfound", cs); return true;}
            
            cfg.set("arena."+args[1]+".spawn"+args[3], null);
            plugin.saveConfig();
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn"+args[3]+"&7 removed from arena &5"+args[1]+"&7."));
            }
           //informations about spawn
          /* if(args[2].equalsIgnoreCase("spawninfo")){
            if(!(args.length >= 4)){tellHim("spawninfousage", cs); return true;}
            if(cfg.getConfigurationSection("arena."+args[1]+".spawn"+args[3]) == null){tellHim("spawnnotfound", cs); return true;}
            
            String w = cfg.getString("arena."+args[1]+".spawn"+args[3]+".world");
            double x = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".x"));
            double y = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".y"));
            double z = Math.round(cfg.getDouble("arena."+args[1]+".spawn"+args[3]+".z"));
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&dSpawn"+args[3]+"&7's x:&d"+x+"&7 y:&d"+y+"&7 z:&d"+z+"&7 world:&d"+w+"&7."));
            }*/
           //how many spawns are in arena X?
           if(args[2].equalsIgnoreCase("spawnlist")){
           int i = 0;
          while(cfg.get("arena."+args[1]+".spawn"+i) != null) i++;
           cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&7Found &d"+i+"&7 spawns in arena &5"+args[1]+"&7."));
            }
           } else tellHim("arenausage", cs);
           
           return true;
       }
       
    public boolean tryOpen(CommandSender cs,String arena, boolean info){
           if(activearenas.get(arena) == true){tellHim("arenaalreadyopened", cs); return true;}
            Location lob = null;
           if(cfg.get("arena."+arena+".lobby") != null) lob = (Location)cfg.get("arena."+arena+".lobby");
            
            int i = 0;
            while(cfg.get("arena."+arena+".spawn"+i) != null) i++;
           if(info){
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&cArena: &5&o"+arena));
            if(lob!=null){cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lobby:  x:&d"+Math.round(lob.getX())+"&7 y:&d"+Math.round(lob.getY())+"&7 z:&d"+Math.round(lob.getZ())+"&7 world:&d"+lob.getWorld().getName()+"&7."));
            }else cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Lobby: &cnot found"));
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Found &d"+i+"&7 spawns in arena. &cNeeded: 2"));
            if(cfg.get("spawn") == null){cs.sendMessage(ChatColor.GRAY+"Main spawn: "+ChatColor.RED+"not found");
            }else{
                Location sloc = (Location)cfg.get("spawn");
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Main spawn:  x:&d"+Math.round(sloc.getX())+"&7 y:&d"+Math.round(sloc.getY())+"&7 z:&d"+Math.round(sloc.getZ())+"&7 world:&d"+sloc.getWorld().getName()+"&7."));}
            return true;
           }
           if(cfg.get("spawn") != null && lob != null && i >= 2){
           cfg.set("arena."+arena+".enabled", true);
           activearenas.put(arena, true);
           cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&9Arena &5'"+arena+"'&9 successfully opened. &7Join via /os join "+arena));
           } else cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&cCant open arena! Please check if everything is set up right via &9/os arena "+arena+" info"));
           return true;
    }
    
}