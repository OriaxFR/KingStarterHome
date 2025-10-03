package fr.oriax.kingstarterhome.ecouteurs;

import fr.oriax.kingstarterhome.PluginPrincipal;
import fr.oriax.kingstarterhome.interfaces.InterfaceBaseHome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EcouteurInteraction implements Listener {
    
    private final PluginPrincipal plugin;
    
    public EcouteurInteraction(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void surInteractionJoueur(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player joueur = event.getPlayer();
        ItemStack item = joueur.getItemInHand();
        
        if (!plugin.getGestionnaireItem().estItemBaseHome(item)) {
            return;
        }
        
        event.setCancelled(true);

        InterfaceBaseHome interfaceMenu = new InterfaceBaseHome(plugin);
        interfaceMenu.ouvrirInterface(joueur);
    }
    
    @EventHandler
    public void surClicInventaire(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player joueur = (Player) event.getWhoClicked();
        
        if (event.getInventory().getTitle().contains("Base Automatique")) {
            event.setCancelled(true);
            
            if (event.getSlot() == 13) {
                joueur.closeInventory();
                if (!plugin.getGestionnaireCooldown().peutUtiliser(joueur)) {
                    long tempsRestant = plugin.getGestionnaireCooldown().getTempsRestant(joueur);
                    String tempsFormate = plugin.getGestionnaireCooldown().formaterTempsRestant(tempsRestant);
                    
                    joueur.sendMessage(plugin.getConfigurationManager().getMessage("cooldown-actif", "temps", tempsFormate));
                    return;
                }
                ItemStack itemEnMain = joueur.getItemInHand();
                if (!plugin.getGestionnaireItem().estItemBaseHome(itemEnMain)) {
                    joueur.sendMessage("§cVous devez avoir l'item de téléportation en main !");
                    return;
                }
                if (itemEnMain.getAmount() > 1) {
                    itemEnMain.setAmount(itemEnMain.getAmount() - 1);
                } else {
                    joueur.setItemInHand(null);
                }
                plugin.getGestionnaireBase().teleporterVersNouvelleBase(joueur);
            }
        }
    }
}