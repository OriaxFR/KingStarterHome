package fr.oriax.kingstarterhome.gestionnaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GestionnaireItem {
    
    private final PluginPrincipal plugin;
    private static final String TEXTURE_TERRE_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzljODg4MWU0MjkxNWE5ZDI5YmI2MWExNmZiMjZkMDU5OTEzMjA0ZDI2NWRmNWI0MzliM2Q3OTJhY2Q1NiJ9fX0=";
    
    public GestionnaireItem(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    public ItemStack creerItemBaseHome() {
        ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) tete.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Base Automatique");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Clic droit pour ouvrir le menu",
            ChatColor.GRAY + "de teleportation vers une base",
            ChatColor.GRAY + "automatiquement generee !",
            "",
            ChatColor.RED + "Cooldown: 24 heures"
        ));
        
        appliquerTexturePersonnalisee(meta, TEXTURE_TERRE_BASE64);
        tete.setItemMeta(meta);
        
        return tete;
    }
    
    public boolean estItemBaseHome(ItemStack item) {
        if (item == null || item.getType() != Material.SKULL_ITEM) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Base Automatique");
    }
    
    private void appliquerTexturePersonnalisee(SkullMeta meta, String textureBase64) {
        try {
            java.lang.reflect.Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");
            
            Object profile = gameProfileClass.getConstructor(java.util.UUID.class, String.class)
                    .newInstance(java.util.UUID.randomUUID(), null);
            
            Object properties = gameProfileClass.getMethod("getProperties").invoke(profile);
            Object property = propertyClass.getConstructor(String.class, String.class)
                    .newInstance("textures", textureBase64);
            
            properties.getClass().getMethod("put", Object.class, Object.class)
                    .invoke(properties, "textures", property);
            
            profileField.set(meta, profile);
        } catch (Exception e) {
            plugin.getLogger().warning("Impossible d'appliquer la texture de la planete Terre: " + e.getMessage());
        }
    }
}