package fr.oriax.kingstarterhome.ecouteurs;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EcouteurMouvement implements Listener {
    
    private final PluginPrincipal plugin;
    
    public EcouteurMouvement(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void surMouvementJoueur(PlayerMoveEvent event) {
        Player joueur = event.getPlayer();
        if (plugin.getGestionnaireFreeze().estEnAttenteDeSol(joueur)) {
            plugin.getGestionnaireFreeze().verifierEtBloquerAuSol(joueur);
            return;
        }
        if (!plugin.getGestionnaireFreeze().estBloque(joueur)) {
            return;
        }
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            Location nouvelleLocation = from.clone();
            nouvelleLocation.setYaw(to.getYaw());
            nouvelleLocation.setPitch(to.getPitch());
            
            event.setTo(nouvelleLocation);
        }
    }
}