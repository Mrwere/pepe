package net.suraimu.os.main;


import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;
import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.arenaplayers;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.main.Core.prefix;
import static net.suraimu.os.main.Core.updateMsgs;
import static net.suraimu.os.utils.lobbyCountdown.startCountdown;
import static net.suraimu.os.utils.scoreBoard.pLeave;
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
                        
                        Player p = (Player)cs;
                        //thanks https://www.spigotmc.org/threads/i-cant-figure-out-how-to-make-sure-player-inventory-is-empty.243831/
                       if(((int) Stream.of(p.getInventory().getContents()).filter(Objects::isNull).count()) != p.getInventory().getSize()){
                       tellHim("emptyinvfirst",cs);
                       return true;
                       } else if(cfg.get("arena."+args[1]+".maxplayers") != null && 
                                 arenaplayers.get(args[1]) != null &&
                                 cfg.getInt("arena."+args[1]+".maxplayers") < arenaplayers.get(args[1]).size()){
                       tellHim("arenaisfull",cs);
                       return true;
                       }
                       tellHim("connecting", cs);
                       p.teleport((Location) cfg.get("arena."+args[1]+".lobby"));
                       if(arenaplayers.get(args[1]) == null) arenaplayers.put(args[1], new ArrayList<>());
                       arenaplayers.get(args[1]).add(p.getName());
                       net.suraimu.os.utils.lobbyCountdown.countdown = cfg.getInt("arena."+args[1]+".lobbycountdown");
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
                     if(activearenas.get(args[1]) == true && !args[2].equalsIgnoreCase("disable")){
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
             //close arena
             if(args[2].equalsIgnoreCase("disable")){
                 String arena = args[1];
                  cfg.set("arena."+arena+".enabled", false);
                  activearenas.put(arena, false);
               if(arenaplayers.get(arena) != null)for(int i = 0; arenaplayers.get(arena).size() > i; i++){
                  pLeave(Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
                  arenaplayers.get(arena).remove(i);
               }
               tellHim("arenaclosed",cs);
             }
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
           //set min||max players
           if(args[2].equalsIgnoreCase("setmin") || args[2].equalsIgnoreCase("setmax")){
            if(!(args.length >= 4)){tellHim("setminmaxusage", cs); return true;}
                try {
                int theINT = Integer.parseInt(args[3]);
                if(args[2].equalsIgnoreCase("setmin")){
                    cfg.set("arena."+args[1]+".minplayers",theINT); 
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aSuccessfully &7set &d"+args[3]+"&7 as min player count to start for arena &5"+args[1]+"&7."));
                }else{ 
                    cfg.set("arena."+args[1]+".maxplayers",theINT);
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aSuccessfully &7set &d"+args[3]+"&7 as max player count to start for arena &5"+args[1]+"&7."));
                }
                plugin.saveConfig();
               
                  } catch (NumberFormatException numberException) {
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&d"+args[3]+"&7 is not a number, therefore it cant be used."));
                  }
            }
           //set lobby countdown and game length
           if(args[2].equalsIgnoreCase("setlength") || args[2].equalsIgnoreCase("setlobbycountdown")){
            if(!(args.length >= 4)){tellHim("setlengthsage", cs); return true;}
                try {
                int theINT = Integer.parseInt(args[3]);
                if(args[2].equalsIgnoreCase("setlength")){
                    cfg.set("arena."+args[1]+".gamelength",theINT); 
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aSuccessfully &7set &d"+args[3]+"s&7 as new game length for arena &5"+args[1]+"&7."));
                }else{ 
                    cfg.set("arena."+args[1]+".lobbycountdown",theINT);
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&aSuccessfully &7set &d"+args[3]+"s&7 as new lobby wait time for arena &5"+args[1]+"&7."));
                }
                plugin.saveConfig();
               
                  } catch (NumberFormatException numberException) {
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&d"+args[3]+"&7 is not a number, therefore it cant be used."));
                  }
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
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Found &d"+i+"&7 spawns in arena."));
            if(cfg.get("spawn") == null){cs.sendMessage(ChatColor.GRAY+"Main spawn: "+ChatColor.RED+"not found");
            }else{
                Location sloc = (Location)cfg.get("spawn");
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Main spawn:  x:&d"+Math.round(sloc.getX())+"&7 y:&d"+Math.round(sloc.getY())+"&7 z:&d"+Math.round(sloc.getZ())+"&7 world:&d"+sloc.getWorld().getName()+"&7."));
            }
            if(cfg.get("arena."+arena+".maxplayers") == null)cs.sendMessage(ChatColor.GRAY+"Max players: "+ChatColor.RED+"none");
                else cs.sendMessage(ChatColor.GRAY+"Max players: "+ChatColor.BLUE+cfg.get("arena."+arena+".maxplayers"));
            if(cfg.get("arena."+arena+".minplayers") == null)cs.sendMessage(ChatColor.GRAY+"Min players: "+ChatColor.RED+"none");
                else cs.sendMessage(ChatColor.GRAY+"Min players: "+ChatColor.BLUE+cfg.get("arena."+arena+".minplayers"));
            if(cfg.get("arena."+arena+".lobbycountdown") == null)cs.sendMessage(ChatColor.GRAY+"Lobby countdown: "+ChatColor.RED+"none");
                else cs.sendMessage(ChatColor.GRAY+"Lobby countdown: "+ChatColor.BLUE+cfg.get("arena."+arena+".lobbycountdown")+"s");
            if(cfg.get("arena."+arena+".gamelength") == null)cs.sendMessage(ChatColor.GRAY+"Lobby countdown: "+ChatColor.RED+"none");
                else cs.sendMessage(ChatColor.GRAY+"Game length: "+ChatColor.BLUE+cfg.get("arena."+arena+".gamelength")+"s");
            return true;
           }
           if(cfg.get("spawn") != null && lob != null && i >= 1 && cfg.get("arena."+arena+".minplayers") != null &&
              cfg.get("arena."+arena+".maxplayers") != null && cfg.get("arena."+arena+".gamelength") != null && cfg.get("arena."+arena+".lobbycountdown") != null){
           cfg.set("arena."+arena+".enabled", true);
           activearenas.put(arena, true);
           cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&9Arena &5'"+arena+"'&9 successfully opened. &7Join via /os join "+arena));
           } else cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+"&cCant open arena! Please check if everything is set up right via &9/os arena "+arena+" info"));
           return true;
    }
    
}