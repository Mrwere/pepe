package net.suraimu.os.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.playingplayers;
import static net.suraimu.os.utils.cpRespawn.cpRespawn;
import static net.suraimu.os.utils.scoreBoard.obj;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class hitEvent implements Listener{
    
    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent e){
       if(e.getCause() != DamageCause.PROJECTILE) return;
       Arrow a = (Arrow)e.getDamager();
       if(!(a.getShooter() instanceof Player) || !(e.getEntity() instanceof Player)) return;
       Player p = (Player)a.getShooter();
       CraftPlayer hitp = (CraftPlayer)e.getEntity();
       if(playingplayers.get(p.getName()) == null || playingplayers.get(hitp.getName()) == null ) return;
       if(!p.getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("bow")) return;
       if(p.getName().equals(hitp.getName())) return;
       obj.getScore(p.getName()).setScore(obj.getScore(p.getName()).getScore()+1);
       p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100, 6);
       p.getInventory().addItem(new ItemStack(Material.ARROW));
       hitp.setHealth(hitp.getMaxHealth());
       hitp.playSound(hitp.getLocation(), Sound.ENTITY_PLAYER_HURT, 100, 3);
       cpRespawn(hitp, playingplayers.get(p.getName()));
       if(cfg.get("messages.shotby") == null){hitp.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§c§lYou were shot by §4§l"+p.getName()));
       } else hitp.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(cfg.getString("messages.shotby").replace("%KILLER%", p.getName())));
    }
}
