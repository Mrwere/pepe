package net.suraimu.os.utils;

import static net.suraimu.os.main.Core.activearenas;
import static net.suraimu.os.main.Core.arenaplayers;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.main.Core.plugin;
import static net.suraimu.os.main.Core.prefix;
import static net.suraimu.os.utils.scoreBoard.board;
import static net.suraimu.os.utils.scoreBoard.obj;
import static net.suraimu.os.utils.cpRespawn.cpRespawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;


public class lobbyCountdown {



        public static int countdown;
        static int cdtask;


        public static void startCountdown(String arena){
                cdtask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                       
                        @Override
                        public void run() {
       if(countdown > 0){
           if(arenaplayers.get(arena).isEmpty()) Bukkit.getScheduler().cancelTask(cdtask);;
           for(int i = 0; arenaplayers.get(arena).size() > i; i++){
             CraftPlayer cp = ((CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i)));
             if(!cp.isOnline()){
               arenaplayers.get(arena).remove(i);
               }
             cp.setLevel(countdown);
             if(countdown < 6){
             cp.sendTitle(ChatColor.translateAlternateColorCodes('&',prefix+"&5"+arena+""),
                          ChatColor.translateAlternateColorCodes('&',"&7starting in &e"+countdown+"&7s!"),0,8,0);
             //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',prefix+"&5"+arena+" &7starting in.. &e"+countdown+"&7s"));
             cp.playSound(cp.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 100);
           }
           }
               
               
            countdown--;
                  } else if(!(arenaplayers.get(arena).size() >= cfg.getInt("arena."+arena+".minplayers"))){
                      countdown = cfg.getInt("arena."+arena+".lobbycountdown");
                  }else{
                                   if(activearenas.get(arena)) activearenas.put(arena, false);
                                   net.suraimu.os.utils.scoreBoard.sbUpdate(arena);
                                for(int i = 0; arenaplayers.get(arena).size() > i; i++){
                                   CraftPlayer cp = (CraftPlayer)Bukkit.getPlayer(arenaplayers.get(arena).get(i));
                                   cpRespawn(cp, arena);
                                   cp.setLevel(0);
                                   cp.setGameMode(GameMode.ADVENTURE);
                                   cp.setScoreboard(board);
                                   cp.getInventory().clear();
                                   cp.getInventory().addItem(new ItemStack(Material.ARROW));
                                   cp.getInventory().addItem(new ItemStack(Material.BOW));
                                   playingplayers.put(cp.getName(), arena);
                                   obj.getScore(cp.getName()).setScore(0);
                                }
                                   cfg.getInt("arena."+arena+".lobbycountdown");
                                   Bukkit.getScheduler().cancelTask(cdtask);
                                }
                               
                        }
                }, 0L, 20L);
        }
        
}
