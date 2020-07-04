package net.suraimu.os.listeners;

import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.utils.cpRespawn.cpRespawn;
import static net.suraimu.os.utils.scoreBoard.obj;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class hitEvent implements Listener{
    
    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent e){
       if(e.getCause() != DamageCause.PROJECTILE) return;
       Arrow a = (Arrow)e.getDamager();
       if(!(a.getShooter() instanceof Player) || !(e.getEntity() instanceof Player)) return;
       Player p = (Player)a.getShooter();
       if(!p.getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("bow")) return;
       //
       CraftPlayer hitp = (CraftPlayer)e.getEntity();
       if(playingplayers.get(p.getName()) == null || playingplayers.get(hitp.getName()) == null ) return;
       obj.getScore(p.getName()).setScore(obj.getScore(p.getName()).getScore()+1);
       hitp.setHealth(20.0);
       cpRespawn(hitp, playingplayers.get(p.getName()));
    }
}
