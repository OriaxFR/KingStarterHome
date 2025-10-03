package fr.oriax.kingstarterhome.integration;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class IntegrationEssentials {
    
    private final Plugin plugin;
    private boolean essentialsActif;
    private Essentials essentials;
    
    public IntegrationEssentials(Plugin plugin) {
        this.plugin = plugin;
        this.essentialsActif = verifierEssentials();
    }
    
    private boolean verifierEssentials() {
        Plugin essentialsPlugin = plugin.getServer().getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin == null || !essentialsPlugin.isEnabled()) {
            plugin.getLogger().warning("Essentials n'est pas installe ou desactive");
            return false;
        }
        
        if (!(essentialsPlugin instanceof Essentials)) {
            plugin.getLogger().warning("Le plugin Essentials trouve n'est pas compatible");
            return false;
        }
        
        essentials = (Essentials) essentialsPlugin;
        plugin.getLogger().info("Integration EssentialsX activee avec succes");
        return true;
    }
    
    public boolean estEssentialsActif() {
        return essentialsActif;
    }
    
    public boolean definirHome(Player joueur, String nomHome, Location emplacement) {
        if (!essentialsActif) {
            plugin.getLogger().warning("Impossible de definir le home: Essentials n'est pas actif");
            return false;
        }
        
        try {
            User user = essentials.getUser(joueur);
            if (user == null) {
                plugin.getLogger().warning("Impossible de recuperer l'utilisateur Essentials pour " + joueur.getName());
                return false;
            }
            
            user.setHome(nomHome, emplacement);
            plugin.getLogger().info("Home '" + nomHome + "' defini pour " + joueur.getName() + " aux coordonnees " + 
                                   emplacement.getBlockX() + ", " + emplacement.getBlockY() + ", " + emplacement.getBlockZ());
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Erreur lors de la definition du home: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}