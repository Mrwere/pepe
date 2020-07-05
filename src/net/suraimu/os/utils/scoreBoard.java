package net.suraimu.os.utils;

import java.util.HashMap;
import java.util.Map;
import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.arenaplayers;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.main.Core.prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
               
           if(length <= 5) for(int i = 0; arenaplayers.get(arena).size() > i; i++){
               CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
               cp.playSound(cp.getLocation(), Sound.BLOCK_GLASS_BREAK, 100, 100);
               }
                
           
           length--;
                   } else activearenas.put(arena, true);
                         } else {
                                      Map<String, Integer> winners = new HashMap<>();
                                      Map<String, String> winnersnames = new HashMap<>();
                                      
                                      winners.put("first",0);
                                      winners.put("second",0);
                                      winners.put("third",0);
                                      
                                      winnersnames.put("first","no-one");
                                      winnersnames.put("second","no-one");
                                      winnersnames.put("third","no-one");
                                      
                                  for(int i = 0; arenaplayers.get(arena).size() > i; i++){
                                      Player p = Bukkit.getPlayer(arenaplayers.get(arena).get(i));
                                      int thispscore = obj.getScore(p.getName()).getScore();
                                      if(thispscore > winners.get("first")){
                                        winners.put("first", thispscore);
                                        winnersnames.put("first", p.getName());
                                      } else if(thispscore > winners.get("second")){
                                        winners.put("second", thispscore);
                                        winnersnames.put("second", p.getName());
                                      }else if(thispscore > winners.get("third")){
                                       winners.put("third", thispscore);
                                       winnersnames.put("third", p.getName());
                                      }
                                      pLeave(p);
                                  }
                                  Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &7Arena: &5"+arena));
                                  if(winners.get("first") > 0) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l1. &6"+winnersnames.get("first")+"&8("+winners.get("first")+")"));
                                    else Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l1. &cno-one"));
                                  if(winners.get("second") > 0) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l2. &6"+winnersnames.get("second")+"&8("+winners.get("second")+")"));
                                    else Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l2. &cno-one"));
                                  if(winners.get("third") > 0) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l3. &6"+winnersnames.get("third")+"&8("+winners.get("third")+")"));
                                    else Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9&l3. &cno-one"));
                                  Bukkit.getScheduler().cancelTask(gameGuard);
                                }
                                
                               
                        }
                }, 0L, 20L);
        }
        
    public static void setupScoreboard(String arena){
    length = cfg.getInt("arena."+arena+".gamelength");
    board = (Scoreboard) plugin.getServer().getScoreboardManager().getNewScoreboard();
    obj = scoreBoard.board.registerNewObjective(
                                        "suraimu",
                                        "suraimu", 
                                        "suraimu");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.translateAlternateColorCodes('&',prefix+"&9"+arena));
    }
    
    public static void pLeave(Player p){
    if(cfg.get("spawn") != null) p.teleport((Location) cfg.get("spawn"));
    playingplayers.remove(p.getName());
    p.setScoreboard((Scoreboard) plugin.getServer().getScoreboardManager().getNewScoreboard());
    p.setGameMode(GameMode.SURVIVAL);
    p.getInventory().clear();
    }
}
