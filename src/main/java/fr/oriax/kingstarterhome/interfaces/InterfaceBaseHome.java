package fr.oriax.kingstarterhome.interfaces;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InterfaceBaseHome {
    
    private final PluginPrincipal plugin;
    
    public InterfaceBaseHome(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    public void ouvrirInterface(Player joueur) {
        Inventory inventaire = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "✦ " + ChatColor.YELLOW + "Base Automatique" + ChatColor.GOLD + " ✦");
        
        remplirBordures(inventaire);
        placerItemTeleportation(inventaire, joueur);
        
        joueur.openInventory(inventaire);
    }
    
    private void remplirBordures(Inventory inventaire) {
        ItemStack bordureCyan = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        ItemMeta metaCyan = bordureCyan.getItemMeta();
        metaCyan.setDisplayName(" ");
        bordureCyan.setItemMeta(metaCyan);
        
        ItemStack bordureBleu = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta metaBleu = bordureBleu.getItemMeta();
        metaBleu.setDisplayName(" ");
        bordureBleu.setItemMeta(metaBleu);
        
        int[] slotsCyan = {0, 2, 4, 6, 8, 11, 15, 18, 20, 22, 24, 26};
        int[] slotsBleu = {1, 3, 5, 7, 9, 12, 14, 17, 19, 21, 23, 25};
        
        for (int slot : slotsCyan) {
            inventaire.setItem(slot, bordureCyan);
        }
        for (int slot : slotsBleu) {
            inventaire.setItem(slot, bordureBleu);
        }
    }
    
    private void placerItemTeleportation(Inventory inventaire, Player joueur) {
        ItemStack itemTp = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = itemTp.getItemMeta();
        
        if (plugin.getGestionnaireCooldown().peutUtiliser(joueur)) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "⚡ " + ChatColor.YELLOW + "Téléportation vers Base " + ChatColor.GOLD + "⚡");
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Cliquez pour vous téléporter vers",
                ChatColor.GRAY + "une nouvelle base générée dans",
                ChatColor.GRAY + "les profondeurs du monde !",
                "",
                ChatColor.GREEN + "" + ChatColor.BOLD + "✓ DISPONIBLE",
                "",
                ChatColor.DARK_GRAY + "▸ " + ChatColor.YELLOW + "Construction instantanée",
                ChatColor.DARK_GRAY + "▸ " + ChatColor.YELLOW + "Home automatique défini",
                ChatColor.DARK_GRAY + "▸ " + ChatColor.YELLOW + "Ressources incluses",
                "",
                ChatColor.RED + "⚠ " + ChatColor.GRAY + "L'item sera consommé"
            ));
        } else {
            long tempsRestant = plugin.getGestionnaireCooldown().getTempsRestant(joueur);
            String tempsFormate = plugin.getGestionnaireCooldown().formaterTempsRestant(tempsRestant);
            
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "✗ " + ChatColor.DARK_RED + "Téléportation Indisponible");
            meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Vous devez patienter avant de",
                ChatColor.GRAY + "pouvoir utiliser à nouveau cette",
                ChatColor.GRAY + "fonctionnalité premium !",
                "",
                ChatColor.RED + "" + ChatColor.BOLD + "✗ EN COOLDOWN",
                ChatColor.RED + "⏱ Temps restant: " + ChatColor.YELLOW + tempsFormate,
                "",
                ChatColor.DARK_GRAY + "Le cooldown évite les abus"
            ));
        }
        
        itemTp.setItemMeta(meta);
        inventaire.setItem(13, itemTp);
        
        placerItemsDecoratifs(inventaire);
    }
    
    private void placerItemsDecoratifs(Inventory inventaire) {
        int nombreCoffres = plugin.getConfigurationManager().getNombreCoffres();
        
        ItemStack itemInfo = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta metaInfo = itemInfo.getItemMeta();
        metaInfo.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "★ " + ChatColor.WHITE + "Informations de la Base");
        metaInfo.setLore(Arrays.asList(
            "",
            ChatColor.GRAY + "Votre base contiendra:",
            "",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + nombreCoffres + " coffres remplis de ressources",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "4 zones de cultures automatiques",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "Éclairage optimal (glowstones)",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "Mobilier et décorations",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "Atelier complet (craft, fours)",
            "",
            ChatColor.GRAY + "Forme et caractéristiques:",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "Structure octogonale premium",
            ChatColor.GOLD + "◆ " + ChatColor.YELLOW + "Toit en dôme à 3 niveaux",
            "",
            ChatColor.DARK_GRAY + "Design unique et fonctionnel !"
        ));
        itemInfo.setItemMeta(metaInfo);
        inventaire.setItem(10, itemInfo);
        
        ItemStack itemRegles = new ItemStack(Material.MAP);
        ItemMeta metaRegles = itemRegles.getItemMeta();
        metaRegles.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "★ " + ChatColor.WHITE + "Emplacement & Règles");
        metaRegles.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        metaRegles.setLore(Arrays.asList(
            "",
            ChatColor.GRAY + "Caractéristiques de l'emplacement:",
            "",
            ChatColor.GREEN + "✓ " + ChatColor.YELLOW + "Distance: 1000+ blocs du spawn",
            ChatColor.GREEN + "✓ " + ChatColor.YELLOW + "Profondeur: Sous-terre sécurisée",
            ChatColor.GREEN + "✓ " + ChatColor.YELLOW + "Zone: Wilderness uniquement",
            ChatColor.GREEN + "✓ " + ChatColor.YELLOW + "Sécurité: Loin des autres joueurs",
            "",
            ChatColor.RED + "⏱ " + ChatColor.GRAY + "Cooldown: " + ChatColor.YELLOW + "24 heures",
            "",
            ChatColor.DARK_GRAY + "Position aléatoire garantie"
        ));
        itemRegles.setItemMeta(metaRegles);
        inventaire.setItem(16, itemRegles);
    }
}