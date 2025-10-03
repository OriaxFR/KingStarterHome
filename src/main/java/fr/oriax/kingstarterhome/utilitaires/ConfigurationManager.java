package fr.oriax.kingstarterhome.utilitaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigurationManager {
    
    private final PluginPrincipal plugin;
    private FileConfiguration config;
    
    public ConfigurationManager(PluginPrincipal plugin) {
        this.plugin = plugin;
        chargerConfiguration();
    }
    
    private void chargerConfiguration() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        definirValeursParDefaut();
    }
    
    private void definirValeursParDefaut() {
        config.addDefault("distance-minimale-spawn", 1000);
        config.addDefault("distance-minimale-coffre", 50);
        config.addDefault("distance-minimale-claim-ennemi", 32);
        config.addDefault("distance-minimale-base-existante", 100);
        config.addDefault("profondeur-minimale", 50);
        config.addDefault("profondeur-maximale", 20);
        config.addDefault("taille-bordure-monde", 10000);
        config.addDefault("tentatives-maximales", 50);
        
        config.addDefault("coffres.nombre", 4);
        
        config.addDefault("messages.cooldown-actif", "&cVous devez attendre encore {temps} avant de pouvoir utiliser cet item !");
        config.addDefault("messages.teleportation-reussie", "&aVous avez ete teleporte vers votre nouvelle base !");
        config.addDefault("messages.echec-teleportation", "&cImpossible de trouver un emplacement valide pour votre base.");
        config.addDefault("messages.base-creee", "&aVotre base a ete generee avec succes !");
        config.addDefault("messages.home-defini", "&aVotre home a ete defini a cet emplacement !");
        config.addDefault("messages.base-existante-detectee", "&cUne base existe deja a proximite de cet emplacement.");
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public int getDistanceMinimaleSpawn() {
        return config.getInt("distance-minimale-spawn", 1000);
    }
    
    public int getDistanceMinimaleCoffre() {
        return config.getInt("distance-minimale-coffre", 50);
    }
    
    public int getDistanceMinimaleClaimEnnemi() {
        return config.getInt("distance-minimale-claim-ennemi", 32);
    }
    
    public int getProfondeurMinimale() {
        return config.getInt("profondeur-minimale", 50);
    }
    
    public int getProfondeurMaximale() {
        return config.getInt("profondeur-maximale", 20);
    }
    
    public int getTailleBordureMonde() {
        return config.getInt("taille-bordure-monde", 10000);
    }
    
    public int getTentativesMaximales() {
        return config.getInt("tentatives-maximales", 50);
    }
    
    public int getDistanceMinimaleBaseExistante() {
        return config.getInt("distance-minimale-base-existante", 100);
    }
    
    public int getNombreCoffres() {
        return config.getInt("coffres.nombre", 4);
    }
    
    public List<ItemStack> getContenuCoffre(int numeroCoffre) {
        List<ItemStack> contenu = new ArrayList<>();
        Random random = new Random();
        
        ConfigurationSection coffresSection = config.getConfigurationSection("coffres.contenu.coffre" + numeroCoffre);
        if (coffresSection == null) {
            return getContenuCoffreParDefaut(numeroCoffre);
        }
        
        for (String key : coffresSection.getKeys(false)) {
            ConfigurationSection itemSection = coffresSection.getConfigurationSection(key);
            if (itemSection != null) {
                String materialName = itemSection.getString("material");
                int quantite = itemSection.getInt("quantite", 1);
                int chance = itemSection.getInt("chance", 100);
                
                if (random.nextInt(100) < chance) {
                    try {
                        Material material = Material.valueOf(materialName);
                        contenu.add(new ItemStack(material, quantite));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Materiau inconnu dans coffre " + numeroCoffre + ": " + materialName);
                    }
                }
            }
        }

        if (contenu.isEmpty()) {
            return getContenuCoffreParDefaut(numeroCoffre);
        }
        
        return contenu;
    }
    
    private List<ItemStack> getContenuCoffreParDefaut(int numeroCoffre) {
        List<ItemStack> contenu = new ArrayList<>();

        int coffreType = ((numeroCoffre - 1) % 4) + 1;
        
        switch (coffreType) {
            case 1:
                contenu.add(new ItemStack(Material.WOOD, 32));
                contenu.add(new ItemStack(Material.COBBLESTONE, 64));
                contenu.add(new ItemStack(Material.COAL, 16));
                contenu.add(new ItemStack(Material.BREAD, 8));
                break;
            case 2:
                contenu.add(new ItemStack(Material.IRON_INGOT, 8));
                contenu.add(new ItemStack(Material.IRON_PICKAXE, 1));
                contenu.add(new ItemStack(Material.IRON_SWORD, 1));
                contenu.add(new ItemStack(Material.TORCH, 32));
                break;
            case 3:
                contenu.add(new ItemStack(Material.SEEDS, 16));
                contenu.add(new ItemStack(Material.POTATO_ITEM, 8));
                contenu.add(new ItemStack(Material.CARROT_ITEM, 8));
                contenu.add(new ItemStack(Material.WATER_BUCKET, 1));
                break;
            case 4:
                contenu.add(new ItemStack(Material.BED, 1));
                contenu.add(new ItemStack(Material.WORKBENCH, 1));
                contenu.add(new ItemStack(Material.FURNACE, 1));
                contenu.add(new ItemStack(Material.CHEST, 4));
                break;
        }
        
        return contenu;
    }
    
    public String getMessage(String cle) {
        return config.getString("messages." + cle, "&cMessage non trouve: " + cle)
                .replace("&", "§");
    }
    
    public String getMessage(String cle, String... remplacements) {
        String message = getMessage(cle);
        
        for (int i = 0; i < remplacements.length; i += 2) {
            if (i + 1 < remplacements.length) {
                message = message.replace("{" + remplacements[i] + "}", remplacements[i + 1]);
            }
        }
        
        return message;
    }

    public String getTextePancarteCoffre(int numeroCoffre) {
        String texte = config.getString("coffres.contenu.coffre" + numeroCoffre + ".pancarte", null);
        
        if (texte == null || texte.isEmpty()) {
            return getTextePancarteParDefaut(numeroCoffre);
        }
        
        return texte;
    }

    private String getTextePancarteParDefaut(int numeroCoffre) {
        int coffreType = ((numeroCoffre - 1) % 4) + 1;
        
        switch (coffreType) {
            case 1:
                return "&6Bois|&6& Pierre";
            case 2:
                return "&cMinerais|&c& Outils";
            case 3:
                return "&aNourriture";
            case 4:
                return "&eTorches|&e& Utiles";
            default:
                return "";
        }
    }
}