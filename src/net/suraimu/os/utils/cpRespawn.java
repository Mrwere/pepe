package net.suraimu.os.utils;

import static net.suraimu.os.main.Core.cfg;
import static net.suraimu.os.main.Core.rand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;


public class cpRespawn {
    public static void cpRespawn(CraftPlayer cp, String arena){
    int i = 0;
    while(cfg.get("arena."+arena+".spawn"+i) != null) i++;
    int rspawn = rand.nextInt(i);
    Location ploc = cp.getLocation();
    Location newspawn = (Location) cfg.get("arena."+arena+".spawn"+rspawn);
    newspawn.setPitch(ploc.getPitch());
    newspawn.setYaw(ploc.getYaw());
    cp.teleport(newspawn);
    }
}
