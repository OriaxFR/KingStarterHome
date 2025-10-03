package fr.oriax.kingstarterhome.utilitaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.Arrays;
import java.util.List;

public class ValidateurEmplacement {
    
    private final PluginPrincipal plugin;
    
    private static final List<Material> BLOCS_DANGEREUX = Arrays.asList(
        Material.LAVA,
        Material.STATIONARY_LAVA,
        Material.FIRE,
        Material.CACTUS,
        Material.WEB,
        Material.TNT,
        Material.OBSIDIAN,
        Material.BEDROCK
    );
    
    public ValidateurEmplacement(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    public boolean estEmplacementValide(Location emplacement) {
        return estAssezLoinDuSpawn(emplacement) &&
               estAssezLoinDesCoffres(emplacement) &&
               estDansWilderness(emplacement) &&
               estSousTerre(emplacement) &&
               estDansLesBordures(emplacement) &&
               estAssezLoinDesClaimsEnnemis(emplacement) &&
               estSansBloDangereux(emplacement) &&
               estAssezLoinDesBasesExistantes(emplacement);
    }
    
    public boolean estEmplacementSouterrainValide(Location emplacement) {
        Block bloc = emplacement.getBlock();
        Block blocAuDessus = emplacement.clone().add(0, 1, 0).getBlock();
        Block blocEnDessous = emplacement.clone().add(0, -1, 0).getBlock();
        
        return bloc.getType() == Material.AIR &&
               blocAuDessus.getType() == Material.AIR &&
               blocEnDessous.getType() != Material.AIR &&
               !BLOCS_DANGEREUX.contains(blocEnDessous.getType()) &&
               blocEnDessous.getType() != Material.WATER;
    }
    
    private boolean estAssezLoinDuSpawn(Location emplacement) {
        int distanceMinimale = plugin.getConfigurationManager().getDistanceMinimaleSpawn();
        
        int x = emplacement.getBlockX();
        int z = emplacement.getBlockZ();
        
        double distanceOrigine = Math.sqrt(x * x + z * z);
        if (distanceOrigine < distanceMinimale) {
            return false;
        }
        
        Location spawn = emplacement.getWorld().getSpawnLocation();
        double distanceSpawn = emplacement.distance(spawn);
        return distanceSpawn >= distanceMinimale;
    }
    
    private boolean estAssezLoinDesCoffres(Location emplacement) {
        int rayon = plugin.getConfigurationManager().getDistanceMinimaleCoffre();
        
        for (int x = -rayon; x <= rayon; x++) {
            for (int y = -rayon; y <= rayon; y++) {
                for (int z = -rayon; z <= rayon; z++) {
                    Location emplacementVerification = emplacement.clone().add(x, y, z);
                    Block bloc = emplacementVerification.getBlock();
                    
                    if (bloc.getType() == Material.CHEST || bloc.getType() == Material.TRAPPED_CHEST) {
                        BlockState state = bloc.getState();
                        if (state instanceof Chest) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    private boolean estDansWilderness(Location emplacement) {
        try {
            return plugin.getIntegrationFactions().estDansWilderness(emplacement);
        } catch (Exception e) {
            plugin.getLogger().warning("Impossible de verifier le statut Wilderness: " + e.getMessage());
            return false;
        }
    }
    
    private boolean estSousTerre(Location emplacement) {
        int profondeurMin = plugin.getConfigurationManager().getProfondeurMaximale();
        int profondeurMax = plugin.getConfigurationManager().getProfondeurMinimale();
        
        int y = emplacement.getBlockY();
        return y <= profondeurMax && y >= profondeurMin;
    }
    
    private boolean estDansLesBordures(Location emplacement) {
        org.bukkit.WorldBorder worldBorder = emplacement.getWorld().getWorldBorder();
        
        if (worldBorder == null) {
            int tailleBordure = plugin.getConfigurationManager().getTailleBordureMonde();
            int x = Math.abs(emplacement.getBlockX());
            int z = Math.abs(emplacement.getBlockZ());
            return x <= tailleBordure && z <= tailleBordure;
        }
        
        Location centre = worldBorder.getCenter();
        double taille = worldBorder.getSize() / 2.0;
        
        double distanceX = Math.abs(emplacement.getX() - centre.getX());
        double distanceZ = Math.abs(emplacement.getZ() - centre.getZ());
        
        return distanceX <= taille && distanceZ <= taille;
    }
    
    private boolean estAssezLoinDesClaimsEnnemis(Location emplacement) {
        try {
            int distanceMinimale = plugin.getConfigurationManager().getDistanceMinimaleClaimEnnemi();
            return plugin.getIntegrationFactions().estAssezLoinDesClaimsEnnemis(emplacement, distanceMinimale);
        } catch (Exception e) {
            plugin.getLogger().warning("Impossible de verifier les claims ennemis: " + e.getMessage());
            return false;
        }
    }
    
    private boolean estSansBloDangereux(Location emplacement) {
        int rayonVerification = 10;
        
        for (int x = -rayonVerification; x <= rayonVerification; x++) {
            for (int y = -3; y <= 5; y++) {
                for (int z = -rayonVerification; z <= rayonVerification; z++) {
                    Location emplacementVerification = emplacement.clone().add(x, y, z);
                    Block bloc = emplacementVerification.getBlock();
                    
                    if (BLOCS_DANGEREUX.contains(bloc.getType())) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    private boolean estAssezLoinDesBasesExistantes(Location emplacement) {
        int rayonVerification = plugin.getConfigurationManager().getDistanceMinimaleBaseExistante();
        
        for (int x = -rayonVerification; x <= rayonVerification; x++) {
            for (int y = -10; y <= 10; y++) {
                for (int z = -rayonVerification; z <= rayonVerification; z++) {
                    Location emplacementVerification = emplacement.clone().add(x, y, z);
                    Block bloc = emplacementVerification.getBlock();
                    
                    if (bloc.getType() == Material.GLOWSTONE) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
}