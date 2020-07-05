package net.suraimu.os.listeners;

import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.utils.cpRespawn.cpRespawn;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onDeath implements Listener{
     public void onDeath(PlayerDeathEvent e){
      if(playingplayers.get(e.getEntity().getName()) == null) return;
      Player p = e.getEntity();
      e.setKeepInventory(true);
      e.setKeepLevel(false);
      cpRespawn((CraftPlayer)p, playingplayers.get(e.getEntity().getName()));
     }
}
