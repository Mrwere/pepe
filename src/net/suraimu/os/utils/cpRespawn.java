package net.suraimu.os.utils;

import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.rand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;


public class cpRespawn {
    public static void cpRespawn(CraftPlayer cp, String arena){
    int i = 0;
    while(cfg.get("arena."+arena+".spawn"+i) != null) i++;
    int rspawn = rand.nextInt(i);
    Location ploc = cp.getLocation();
    ploc.getWorld().spawnParticle(Particle.ASH, ploc, 100);
    
    cp.getInventory().clear();
    cp.getInventory().addItem(new ItemStack(Material.ARROW));
    cp.getInventory().addItem(new ItemStack(Material.BOW));
    
    Location newspawn = (Location) cfg.get("arena."+arena+".spawn"+rspawn);
    newspawn.setPitch(ploc.getPitch());
    newspawn.setYaw(ploc.getYaw());
    cp.teleport(newspawn);
    }
}
