package net.suraimu.os.utils;

import java.util.ArrayList;
import java.util.Map;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.utils.scoreBoard.board;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;


public class lobbyCountdown {



        public static int countdown = 60;
        static int cdtask;
        public static Map<String, ArrayList<String>> arenaplayers;


        public static void startCountdown(String arena){
                cdtask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                       
                        @Override
                        public void run() {

                            
       if(countdown > 0){
           if(arenaplayers.get(arena).isEmpty()) Bukkit.getScheduler().cancelTask(cdtask);;
           for(int i = 0; arenaplayers.get(arena).size() > i; i++){
             if(!Bukkit.getPlayer(arenaplayers.get(arena).get(i)).isOnline()){
               arenaplayers.get(arena).remove(i);
               }
             ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i))).setLevel(countdown);
             if(countdown < 6){
             CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
             Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&5"+arena+" &7starting in.. &e"+i+"&7s"));
             cp.playSound(cp.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 100);
           }
           }
               
               
            countdown--;
                  }else{
                                   
                                for(int i = 0; arenaplayers.get(arena).size() > i; i++){
                                   CraftPlayer cp = (CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i));
                                  // cp.teleport();
                                   cp.setLevel(0);
                                   cp.setGameMode(GameMode.ADVENTURE);
                                   cp.setScoreboard(board);
                                }
                                   countdown = 60;
                                   Bukkit.getScheduler().cancelTask(cdtask);
                                }
                               
                        }
                }, 0L, 20L);
        }
        
    public static void stopCountdown(){
   Bukkit.getScheduler().cancelTask(cdtask);
    }
}
