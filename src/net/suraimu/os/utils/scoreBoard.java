package net.suraimu.os.utils;

import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.arenaplayers;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.main.Core.plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


public class scoreBoard {



        public static int length;
        static int gameGuard;
        
        public static Scoreboard board;
        public static Objective obj;
                                  
       
        public static void sbUpdate(String arena){
                setupScoreboard(arena);
                gameGuard = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                       
                        @Override
                        public void run() {
                         if(!activearenas.get(arena)){
                           if(length >= 1){
               if(arenaplayers.get(arena).isEmpty()) Bukkit.getScheduler().cancelTask(gameGuard);
               
           for(int i = 0; arenaplayers.get(arena).size() > i; i++){
               CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
               cp.setLevel(length);
               if(length <= 5) cp.playSound(cp.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 100);
               }
                
           
           length--;
                   } else activearenas.put(arena, true);
                         } else {
                                  
                                  if(activearenas.get(arena)) activearenas.put(arena, true);
                                  //handle leaving
                                  for(int i = 0; arenaplayers.get(arena).size() > i; i++){ 
                                      pLeave(Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
                                      arenaplayers.get(arena).remove(i);}
                                  Bukkit.getScheduler().cancelTask(gameGuard);
                                }
                                
                               
                        }
                }, 0L, 20L);
        }
        
    public static void setupScoreboard(String arena){
    length = 15;
    board = (Scoreboard) plugin.getServer().getScoreboardManager().getNewScoreboard();
    obj = scoreBoard.board.registerNewObjective(
                                        "suraimu",
                                        "suraimu", 
                                        "suraimu");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&9--> &3&l"+arena+"&9 <--"));
    }
    
    public static void pLeave(Player p){
    if(cfg.get("spawn") != null) p.teleport((Location) cfg.get("spawn"));
    playingplayers.remove(p.getName());
    p.setScoreboard((Scoreboard) plugin.getServer().getScoreboardManager().getNewScoreboard());
    }
}
