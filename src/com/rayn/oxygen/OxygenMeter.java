package com.rayn.oxygen;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OxygenMeter implements Listener {
    
    static Main plugin;
    private final List<UUID> players = new ArrayList<>();
    private final List<Integer> oxygenLevels = new ArrayList<>();
    private final List<Boolean> isOutside = new ArrayList<>();
    private final List<BossBar> oxygenDisplay = new ArrayList<>();
    private final List<Boolean> isRegenerating = new ArrayList<>();
    private final List<Boolean> isLoosingOxygenQuickly = new ArrayList<>();
    private final double maxOxygen = 20;
    private boolean checkingIfOutside = false;
    
    public OxygenMeter(Main instance) {
        plugin = instance;
    }
    
    // regular max air is 300 ticks
    // one minute of air is 1200 ticks
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        
        if (!checkingIfOutside) {
            checkIfOutside(player.getWorld());
            checkingIfOutside = true;
        }
        
        if (!players.contains(player.getUniqueId())) {
            players.add(player.getUniqueId());
            oxygenLevels.add((int) maxOxygen);
            isOutside.add(false);
            BossBar bar = Bukkit.getServer().createBossBar("Oxygen Level", BarColor.BLUE, BarStyle.SOLID);
            bar.addPlayer(player);
            oxygenDisplay.add(bar);
            isRegenerating.add(false);
            isLoosingOxygenQuickly.add(false);
            
            System.out.println("added player");
        }
        
        int playerIndex = players.indexOf(player.getUniqueId());
        BossBar bar = oxygenDisplay.get(playerIndex);
        bar.addPlayer(player);
        bar.setTitle(oxygenLevels.get(playerIndex) + " seconds remaining");
    }
    
    public void checkIfOutside(World world) {
        new BukkitRunnable() {
            
            @Override
            public void run() {
                for (int i = 0; i < world.getPlayers().size(); i++) {
                    Player playerI = world.getPlayers().get(i);
                    int playerIndex = players.indexOf(playerI.getUniqueId());
                    
                    if (playerI.getLocation().getBlock().getLightFromSky() == 0) {
                        isOutside.set(playerIndex, false);
                    } else {
                        isOutside.set(playerIndex, true);
                    }
                    
                    if (isOutside.get(playerIndex)) {
                        if (hasEquipment(playerI) && oxygenLevels.get(playerIndex) > 0) {
                            int oxygenLevel = oxygenLevels.get(playerIndex);
                            oxygenLevels.set(playerIndex, oxygenLevel - 1);
                            
                            BossBar bar = oxygenDisplay.get(playerIndex);
                            bar.setTitle(oxygenLevels.get(playerIndex) + " seconds remaining");
                            bar.setProgress((double) oxygenLevels.get(playerIndex) / maxOxygen);
                            
                        } else {
                            
                            if (!isLoosingOxygenQuickly.get(playerIndex) && oxygenLevels.get(playerIndex) != 0) {
                                loosingOxygenQuickly(playerI);
                                isLoosingOxygenQuickly.set(playerIndex, true);
                            }
                            if (oxygenLevels.get(playerIndex) == 0) {
                                Bukkit.getScheduler().runTaskLater(plugin, () -> playerI.damage(1), 20);
                            }
                            
                            BossBar bar = oxygenDisplay.get(playerIndex);
                            bar.setTitle(oxygenLevels.get(playerIndex) + " seconds remaining");
                            bar.setProgress((double) oxygenLevels.get(playerIndex) / maxOxygen);
                        }
                        
                    } else {
                        if (!isRegenerating.get(playerIndex) && oxygenLevels.get(playerIndex) != maxOxygen) {
                            regenerateOxygen(playerI);
                            isRegenerating.set(playerIndex, true);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }
    
    public void regenerateOxygen(Player player) {
        new BukkitRunnable() {
            
            @Override
            public void run() {
                int playerIndex = players.indexOf(player.getUniqueId());
                
                if (oxygenLevels.get(playerIndex) < maxOxygen && !isOutside.get(playerIndex)) {
                    int oxygenLevel = oxygenLevels.get(playerIndex);
                    oxygenLevels.set(playerIndex, oxygenLevel + 1);
                    
                    BossBar bar = oxygenDisplay.get(playerIndex);
                    bar.setTitle(oxygenLevels.get(playerIndex) + " seconds remaining");
                    bar.setProgress((double) oxygenLevels.get(playerIndex) / maxOxygen);
                }
                
                if (oxygenLevels.get(playerIndex) == maxOxygen) {
                    isRegenerating.set(playerIndex, false);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20, 1);
    }
    
    public void loosingOxygenQuickly(Player player) {
        new BukkitRunnable() {
            
            @Override
            public void run() {
                int playerIndex = players.indexOf(player.getUniqueId());
                
                if (isOutside.get(playerIndex)) {
                    int oxygenLevel = oxygenLevels.get(playerIndex);
                    oxygenLevels.set(playerIndex, oxygenLevel - 1);
                    
                    BossBar bar = oxygenDisplay.get(playerIndex);
                    bar.setTitle(oxygenLevels.get(playerIndex) + " seconds remaining");
                    bar.setProgress((double) oxygenLevels.get(playerIndex) / maxOxygen);
                }
                
                if (oxygenLevels.get(playerIndex) == 0 || hasEquipment(player)) {
                    isLoosingOxygenQuickly.set(playerIndex, false);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 3);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        int playerIndex = players.indexOf(event.getEntity().getUniqueId());
        oxygenLevels.set(playerIndex, (int) maxOxygen);
    }
    
    public boolean hasEquipment(Player player) {
        
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        
        if (helmet != null && chestplate != null && helmet.hasItemMeta() && chestplate.hasItemMeta()) {
            // wow thanks intellij this is a lot simpler
            return helmet.getItemMeta().isUnbreakable() && chestplate.getItemMeta().isUnbreakable();
        }
        return false;
    }
}