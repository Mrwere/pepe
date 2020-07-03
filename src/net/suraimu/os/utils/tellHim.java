package net.suraimu.os.utils;

import net.suraimu.os.main.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

        

public class tellHim extends Core {  
            
    public static void tellHim(String what, CommandSender cs){
    if(msgs.get(what) == null) { cfg.set("messages."+what, "&cTranslation for message &d'"+what+"'&c not found, please contact administrator.");
                                 plugin.saveConfig(); updateMsgs();}
    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+msgs.get(what)));
    }
}
