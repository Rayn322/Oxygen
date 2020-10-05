package com.rayn.oxygen;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    
    OxygenMeter oxygenMeter = new OxygenMeter(this);
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(oxygenMeter, this);
    }
    
    @Override
    public void onDisable() {
    
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players!");
            return true;
        }
        
        if (label.equalsIgnoreCase("helmet")) {
            Player player = (Player) sender;
            
            // gives helmet if there is nothing on their head.
            if (player.getInventory().getHelmet() != null) {
                
                player.sendMessage(ChatColor.RED + "Please take off your current helmet first.");
                
            } else {
                ItemStack helmet = new ItemStack(Material.GLASS);
                ItemMeta helmetMeta = helmet.getItemMeta();
                helmetMeta.setDisplayName("Helmet");
                player.getInventory().setHelmet(helmet);
                
                player.sendMessage(ChatColor.BLUE + "Helmet equipped.");
            }
        }
        
        return true;
    }
}
