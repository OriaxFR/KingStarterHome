package fr.oriax.kingstarterhome.gestionnaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GestionnaireCooldown {
    
    private final PluginPrincipal plugin;
    private final Map<UUID, Long> cooldowns;
    private final File fichierCooldowns;
    private FileConfiguration configCooldowns;
    private final long DUREE_COOLDOWN = 24 * 60 * 60 * 1000L;
    
    public GestionnaireCooldown(PluginPrincipal plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
        this.fichierCooldowns = new File(plugin.getDataFolder(), "cooldowns.yml");
        
        chargerCooldowns();
    }
    
    public boolean peutUtiliser(Player joueur) {
        UUID uuid = joueur.getUniqueId();
        
        if (!cooldowns.containsKey(uuid)) {
            return true;
        }
        
        long tempsRestant = cooldowns.get(uuid) - System.currentTimeMillis();
        return tempsRestant <= 0;
    }
    
    public void definirCooldown(Player joueur) {
        UUID uuid = joueur.getUniqueId();
        long finCooldown = System.currentTimeMillis() + DUREE_COOLDOWN;
        cooldowns.put(uuid, finCooldown);
        
        sauvegarderCooldowns();
    }
    
    public long getTempsRestant(Player joueur) {
        UUID uuid = joueur.getUniqueId();
        
        if (!cooldowns.containsKey(uuid)) {
            return 0;
        }
        
        long tempsRestant = cooldowns.get(uuid) - System.currentTimeMillis();
        return Math.max(0, tempsRestant);
    }
    
    public String formaterTempsRestant(long millisecondes) {
        if (millisecondes <= 0) {
            return "Aucun";
        }
        
        long secondes = millisecondes / 1000;
        long minutes = secondes / 60;
        long heures = minutes / 60;
        
        secondes %= 60;
        minutes %= 60;
        
        if (heures > 0) {
            return String.format("%dh %02dm %02ds", heures, minutes, secondes);
        } else if (minutes > 0) {
            return String.format("%dm %02ds", minutes, secondes);
        } else {
            return String.format("%ds", secondes);
        }
    }
    
    public void resetCooldown(Player joueur) {
        UUID uuid = joueur.getUniqueId();
        cooldowns.remove(uuid);
        sauvegarderCooldowns();
    }
    
    private void chargerCooldowns() {
        if (!fichierCooldowns.exists()) {
            return;
        }
        
        configCooldowns = YamlConfiguration.loadConfiguration(fichierCooldowns);
        
        for (String uuidString : configCooldowns.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);

                long finCooldown;
                if (configCooldowns.isConfigurationSection(uuidString)) {
                    if (configCooldowns.contains(uuidString + "._ts")) {
                        finCooldown = configCooldowns.getLong(uuidString + "._ts");
                    }
                    else if (configCooldowns.contains(uuidString + ".timestamp")) {
                        finCooldown = configCooldowns.getLong(uuidString + ".timestamp");
                    }
                    else if (configCooldowns.contains(uuidString + ".expiration")) {
                        finCooldown = configCooldowns.getLong(uuidString + ".expiration");
                    }
                    else {
                        continue;
                    }
                } else {
                    finCooldown = configCooldowns.getLong(uuidString);
                }
                
                if (finCooldown > System.currentTimeMillis()) {
                    cooldowns.put(uuid, finCooldown);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("UUID invalide dans cooldowns.yml: " + uuidString);
            }
        }
    }
    
    public void sauvegarderCooldowns() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            configCooldowns = new YamlConfiguration();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            for (Map.Entry<UUID, Long> entry : cooldowns.entrySet()) {
                if (entry.getValue() > System.currentTimeMillis()) {
                    String uuidString = entry.getKey().toString();

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                    String pseudo = offlinePlayer.getName();
                    String dateExpiration = dateFormat.format(new Date(entry.getValue()));

                    configCooldowns.set(uuidString + ".pseudo", pseudo != null ? pseudo : "Inconnu");
                    configCooldowns.set(uuidString + ".cooldown", dateExpiration);
                    configCooldowns.set(uuidString + "._ts", entry.getValue());
                }
            }
            
            configCooldowns.save(fichierCooldowns);
        } catch (IOException e) {
            plugin.getLogger().severe("Erreur lors de la sauvegarde des cooldowns: " + e.getMessage());
        }
    }
}