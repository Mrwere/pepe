package net.suraimu.os.utils;

import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.utils.lobbyCountdown.arenaplayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


public class scoreBoard {



        public static int length;
        static int gameGuard;
        
        public static Scoreboard board;
        public static Objective obj;
                                  
       
        public static void gameScore(String arena){
                gameGuard = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                       
                        @Override
                        public void run() {
                            if(!cfg.getString("arena."+arena+".STATUS").equals("BUSY")){
                                cfg.set("arena."+arena+".STATUS", "BUSY");
                                plugin.saveConfig();
                                }
     
       if(length > 0){
               if(arenaplayers.get(arena).isEmpty()) Bukkit.getScheduler().cancelTask(gameGuard);
               
           for(int i = 0; arenaplayers.get(arena).size() > i; i++){
               CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
               cp.setLevel(length);
               if(length < 6) cp.playSound(cp.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 100);
               }
                
           
           length--;
                                } else {
                                for(int i = 0; arenaplayers.get(arena).size() > i; i++)  {}//TODO: přidat teleportaci na spawn/místo před připojením
                                   
                                    cfg.set("arena."+arena+".STATUS", "OPEN");
                                    plugin.saveConfig();
                                  Bukkit.getScheduler().cancelTask(gameGuard);
                                }
                                
                               
                        }
                }, 0L, 20L);
        }
        
    public static void setupBoard(String arena){
    length = 600;
    board = (Scoreboard) plugin.getServer().getScoreboardManager().getNewScoreboard();
    obj = scoreBoard.board.registerNewObjective(
                                        "suraimu",
                                        "suraimu", 
                                        "suraimu");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&9--> &3&l"+arena+"&9 <--"));
    }
}
