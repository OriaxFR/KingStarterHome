package fr.oriax.kingstarterhome.commandes;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandeBaseHome implements CommandExecutor {
    
    private final PluginPrincipal plugin;
    
    public CommandeBaseHome(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande ne peut etre executee que par un joueur !");
            return true;
        }
        
        Player joueur = (Player) sender;
        
        if (args.length == 0) {
            afficherAide(joueur);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "give":
            case "donner":
                if (!joueur.hasPermission("kingstarterhome.admin")) {
                    joueur.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
                    return true;
                }
                donnerItem(joueur, args);
                break;
                
            case "reload":
            case "recharger":
                if (!joueur.hasPermission("kingstarterhome.admin")) {
                    joueur.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
                    return true;
                }
                rechargerPlugin(joueur);
                break;
                
            case "cooldown":
            case "delai":
                afficherCooldown(joueur, args);
                break;
                
            case "reset":
            case "resetcooldown":
                if (!joueur.hasPermission("kingstarterhome.admin")) {
                    joueur.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
                    return true;
                }
                resetCooldown(joueur, args);
                break;
                
            case "help":
            case "aide":
            default:
                afficherAide(joueur);
                break;
        }
        
        return true;
    }
    
    private void afficherAide(Player joueur) {
        joueur.sendMessage("");
        joueur.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=== KingStarterHome ===");
        joueur.sendMessage("");
        joueur.sendMessage(ChatColor.YELLOW + "/basehome give " + ChatColor.GRAY + "[joueur]");
        joueur.sendMessage(ChatColor.DARK_GRAY + "  » Donner l'item de base");
        joueur.sendMessage("");
        joueur.sendMessage(ChatColor.YELLOW + "/basehome cooldown " + ChatColor.GRAY + "[joueur]");
        joueur.sendMessage(ChatColor.DARK_GRAY + "  » Voir le cooldown restant");
        joueur.sendMessage("");
        joueur.sendMessage(ChatColor.YELLOW + "/basehome reset " + ChatColor.GRAY + "<joueur>");
        joueur.sendMessage(ChatColor.DARK_GRAY + "  » Reinitialiser le cooldown");
        joueur.sendMessage("");
        joueur.sendMessage(ChatColor.YELLOW + "/basehome reload");
        joueur.sendMessage(ChatColor.DARK_GRAY + "  » Recharger la configuration");
        joueur.sendMessage("");
    }
    
    private void donnerItem(Player joueur, String[] args) {
        Player cible = joueur;
        
        if (args.length > 1) {
            cible = plugin.getServer().getPlayer(args[1]);
            if (cible == null) {
                joueur.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
                return;
            }
        }
        
        ItemStack itemBase = plugin.getGestionnaireItem().creerItemBaseHome();
        cible.getInventory().addItem(itemBase);
        
        if (cible == joueur) {
            joueur.sendMessage(ChatColor.GREEN + "Vous avez recu l'item Base Automatique !");
        } else {
            joueur.sendMessage(ChatColor.GREEN + "Item donne a " + cible.getName() + " !");
            cible.sendMessage(ChatColor.GREEN + "Vous avez recu l'item Base Automatique de " + joueur.getName() + " !");
        }
    }
    
    private void rechargerPlugin(Player joueur) {
        plugin.reloadConfig();
        joueur.sendMessage(ChatColor.GREEN + "Configuration rechargee avec succes !");
    }
    
    private void afficherCooldown(Player joueur, String[] args) {
        Player cible = joueur;
        
        if (args.length > 1) {
            if (!joueur.hasPermission("kingstarterhome.admin")) {
                joueur.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de voir le cooldown d'autres joueurs !");
                return;
            }
            
            cible = plugin.getServer().getPlayer(args[1]);
            if (cible == null) {
                joueur.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
                return;
            }
        }
        
        if (plugin.getGestionnaireCooldown().peutUtiliser(cible)) {
            if (cible == joueur) {
                joueur.sendMessage(ChatColor.GREEN + "Vous pouvez utiliser la base automatique !");
            } else {
                joueur.sendMessage(ChatColor.GREEN + cible.getName() + " peut utiliser la base automatique !");
            }
        } else {
            long tempsRestant = plugin.getGestionnaireCooldown().getTempsRestant(cible);
            String tempsFormate = plugin.getGestionnaireCooldown().formaterTempsRestant(tempsRestant);
            
            if (cible == joueur) {
                joueur.sendMessage(ChatColor.RED + "Cooldown restant: " + tempsFormate);
            } else {
                joueur.sendMessage(ChatColor.RED + "Cooldown de " + cible.getName() + ": " + tempsFormate);
            }
        }
    }
    
    private void resetCooldown(Player joueur, String[] args) {
        if (args.length < 2) {
            joueur.sendMessage(ChatColor.RED + "Usage: /basehome reset <joueur>");
            return;
        }
        
        Player cible = plugin.getServer().getPlayer(args[1]);
        if (cible == null) {
            joueur.sendMessage(ChatColor.RED + "Joueur introuvable: " + args[1]);
            return;
        }
        
        if (plugin.getGestionnaireCooldown().peutUtiliser(cible)) {
            joueur.sendMessage(ChatColor.YELLOW + cible.getName() + " n'a pas de cooldown actif !");
            return;
        }
        
        plugin.getGestionnaireCooldown().resetCooldown(cible);
        joueur.sendMessage(ChatColor.GREEN + "Cooldown de " + ChatColor.GOLD + cible.getName() + ChatColor.GREEN + " reset avec succes !");
        cible.sendMessage(ChatColor.GREEN + "Votre cooldown a ete reset par " + ChatColor.GOLD + joueur.getName() + ChatColor.GREEN + " !");
    }
}