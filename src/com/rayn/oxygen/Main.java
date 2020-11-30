package com.rayn.oxygen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Main extends JavaPlugin {
    
    OxygenMeter oxygenMeter = new OxygenMeter(this);
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(oxygenMeter, this);
        Bukkit.addRecipe(getHelmetRecipe());
        Bukkit.addRecipe(getChestplateRecipe());
    }
    
    @Override
    public void onDisable() {
    
    }
    
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//
//        if (label.equalsIgnoreCase("helmet")) {
//
//            if (!(sender instanceof Player)) {
//                sender.sendMessage("This command is only for players!");
//                return true;
//            }
//
//            Player player = (Player) sender;
//
//            // gives helmet if there is nothing on their head.
//            if (player.getInventory().getHelmet() != null) {
//
//                player.sendMessage(ChatColor.RED + "Please take off your current helmet first.");
//
//            } else {
//                ItemStack helmet = new ItemStack(Material.GLASS);
//                ItemMeta helmetMeta = helmet.getItemMeta();
//                helmetMeta.setDisplayName("Helmet");
//                helmet.setItemMeta(helmetMeta);
//                player.getInventory().setHelmet(helmet);
//
//                player.sendMessage(ChatColor.BLUE + "Helmet equipped.");
//            }
//        }
//
//        return true;
//    }
    
    public ShapedRecipe getHelmetRecipe() {
        
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.setDisplayName(net.md_5.bungee.api.ChatColor.YELLOW + "Helmet");
        helmetMeta.setUnbreakable(true);
        helmetMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        // when applied adds 0 armor value
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.ADD_NUMBER);
        helmetMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        helmet.setItemMeta(helmetMeta);
        
        NamespacedKey key = new NamespacedKey(this, "helmet");
        ShapedRecipe recipe = new ShapedRecipe(key, helmet);
        recipe.shape("III", "IGI", "IRI");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('G', Material.GLASS_PANE);
        recipe.setIngredient('R', Material.REDSTONE);
        
        return recipe;
    }
    
    public ShapedRecipe getChestplateRecipe() {
        
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta chestplateMeta = chestplate.getItemMeta();
        chestplateMeta.setDisplayName(net.md_5.bungee.api.ChatColor.YELLOW + "Oxygen Tank");
        chestplateMeta.setUnbreakable(true);
        chestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        // when applied adds 0 armor value
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.ADD_NUMBER);
        chestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        chestplate.setItemMeta(chestplateMeta);
        
        NamespacedKey key = new NamespacedKey(this, "chestplate");
        ShapedRecipe recipe = new ShapedRecipe(key, chestplate);
        recipe.shape("I I", "IBI", "IRI");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('R', Material.REDSTONE);
        
        return recipe;
    }
}
