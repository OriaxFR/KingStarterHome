package fr.oriax.kingstarterhome.integration;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class IntegrationFactions {
    
    private final Plugin plugin;
    private boolean factionsActif;
    private Class<?> boardClass;
    private Class<?> fLocationClass;
    private Class<?> factionClass;
    private Method getBoardInstance;
    private Method getFactionAt;
    private Method isWilderness;
    
    public IntegrationFactions(Plugin plugin) {
        this.plugin = plugin;
        this.factionsActif = verifierFactions();
    }
    
    private boolean verifierFactions() {
        Plugin factionsPlugin = plugin.getServer().getPluginManager().getPlugin("Factions");
        if (factionsPlugin == null || !factionsPlugin.isEnabled()) {
            return false;
        }
        
        try {
            boardClass = Class.forName("com.massivecraft.factions.Board");
            fLocationClass = Class.forName("com.massivecraft.factions.FLocation");
            factionClass = Class.forName("com.massivecraft.factions.Faction");
            
            getBoardInstance = boardClass.getMethod("getInstance");
            getFactionAt = boardClass.getMethod("getFactionAt", fLocationClass);
            isWilderness = factionClass.getMethod("isWilderness");
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Impossible de charger l'integration SaberFactions: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean estFactionsActif() {
        return factionsActif;
    }
    
    public boolean estDansWilderness(Location emplacement) {
        if (!factionsActif) {
            return true;
        }
        
        try {
            Object fLocation = fLocationClass.getConstructor(Location.class).newInstance(emplacement);
            Object board = getBoardInstance.invoke(null);
            Object faction = getFactionAt.invoke(board, fLocation);
            Boolean wilderness = (Boolean) isWilderness.invoke(faction);
            
            return wilderness != null && wilderness;
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors de la verification Wilderness: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean estAssezLoinDesClaimsEnnemis(Location emplacement, int distanceMinimale) {
        if (!factionsActif) {
            return true;
        }
        
        try {
            int chunksAVerifier = (int) Math.ceil(distanceMinimale / 16.0);
            
            Chunk chunkCentral = emplacement.getChunk();
            int chunkX = chunkCentral.getX();
            int chunkZ = chunkCentral.getZ();
            
            Object board = getBoardInstance.invoke(null);
            
            for (int x = -chunksAVerifier; x <= chunksAVerifier; x++) {
                for (int z = -chunksAVerifier; z <= chunksAVerifier; z++) {
                    Object fLocation = fLocationClass.getConstructor(String.class, int.class, int.class)
                            .newInstance(emplacement.getWorld().getName(), chunkX + x, chunkZ + z);
                    Object faction = getFactionAt.invoke(board, fLocation);
                    Boolean wilderness = (Boolean) isWilderness.invoke(faction);
                    
                    if (wilderness != null && !wilderness) {
                        double distance = calculerDistanceChunks(chunkX, chunkZ, chunkX + x, chunkZ + z) * 16;
                        if (distance < distanceMinimale) {
                            return false;
                        }
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors de la verification des claims ennemis: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private double calculerDistanceChunks(int x1, int z1, int x2, int z2) {
        int dx = x2 - x1;
        int dz = z2 - z1;
        return Math.sqrt(dx * dx + dz * dz);
    }
}