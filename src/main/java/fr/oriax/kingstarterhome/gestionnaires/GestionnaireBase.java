package fr.oriax.kingstarterhome.gestionnaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import fr.oriax.kingstarterhome.utilitaires.ConstructeurBase;
import fr.oriax.kingstarterhome.utilitaires.ValidateurEmplacement;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class GestionnaireBase {
    
    private final PluginPrincipal plugin;
    private final ValidateurEmplacement validateurEmplacement;
    private final ConstructeurBase constructeurBase;
    private final Random random;
    
    public GestionnaireBase(PluginPrincipal plugin) {
        this.plugin = plugin;
        this.validateurEmplacement = new ValidateurEmplacement(plugin);
        this.constructeurBase = new ConstructeurBase(plugin);
        this.random = new Random();
    }
    
    public boolean teleporterVersNouvelleBase(Player joueur) {
        Location emplacementValide = trouverEmplacementValide(joueur.getWorld());
        
        if (emplacementValide == null) {
            joueur.sendMessage(plugin.getConfigurationManager().getMessage("echec-teleportation"));
            return false;
        }

        demarrerSequenceIntroduction(joueur, emplacementValide);
        plugin.getGestionnaireCooldown().definirCooldown(joueur);
        
        return true;
    }
    
    private void demarrerSequenceIntroduction(Player joueur, Location emplacementValide) {
        joueur.sendMessage("");
        joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        joueur.sendMessage("");
        joueur.sendMessage("         §6§l⚡ INITIALISATION DE LA BASE ⚡");
        joueur.sendMessage("");
        joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        joueur.sendMessage("");
        
        compteAReboursAvantTeleportation(joueur, emplacementValide, 5);
    }
    
    private void compteAReboursAvantTeleportation(Player joueur, Location emplacementValide, int secondes) {
        if (secondes <= 0) {
            executerTeleportation(joueur, emplacementValide);
            return;
        }
        
        String barreProgression = genererBarreProgression(secondes, 5);
        joueur.sendTitle("§6§l⚡ TÉLÉPORTATION ⚡", "§e" + barreProgression + " §f" + secondes + "s");
        
        Location loc = joueur.getLocation();
        joueur.playSound(loc, Sound.NOTE_PLING, 1.0f, 1.0f + (0.2f * (5 - secondes)));
        
        for (int i = 0; i < 8; i++) {
            double angle = (System.currentTimeMillis() / 100.0) + (i * Math.PI / 4);
            double rayon = 1.5 - (secondes * 0.1);
            double x = Math.cos(angle) * rayon;
            double z = Math.sin(angle) * rayon;
            double y = (5 - secondes) * 0.3;
            
            Location particleLoc = loc.clone().add(x, y, z);
            joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
            joueur.playEffect(particleLoc, Effect.ENDER_SIGNAL, null);
        }
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            compteAReboursAvantTeleportation(joueur, emplacementValide, secondes - 1);
        }, 20L);
    }
    
    private String genererBarreProgression(int restant, int total) {
        int progression = total - restant;
        StringBuilder barre = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (i < progression) {
                barre.append("█");
            } else {
                barre.append("░");
            }
        }
        return barre.toString();
    }
    
    private void executerTeleportation(Player joueur, Location emplacementValide) {
        joueur.sendTitle("§6§l✦ TÉLÉPORTATION ✦", "§eGénération en cours...");
        
        Location loc = joueur.getLocation();
        joueur.playSound(loc, Sound.ENDERMAN_TELEPORT, 1.5f, 0.8f);
        
        for (int i = 0; i < 50; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                double angle = index * 0.6;
                double rayon = 2.0 - (index * 0.03);
                double x = Math.cos(angle) * rayon;
                double z = Math.sin(angle) * rayon;
                double y = index * 0.15;
                
                Location particleLoc = loc.clone().add(x, y, z);
                joueur.playEffect(particleLoc, Effect.PORTAL, null);
                joueur.playEffect(particleLoc, Effect.ENDER_SIGNAL, null);
                
                if (index % 10 == 0) {
                    joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
                }
            }, i);
        }
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.playSound(loc, Sound.PORTAL_TRAVEL, 1.0f, 1.2f);
        }, 20L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.sendTitle("§5§l◆ CONSTRUCTION ◆", "§dAssemblage des structures...");
            joueur.playSound(loc, Sound.ANVIL_USE, 0.8f, 1.5f);
        }, 60L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            chargerChunksPourBase(emplacementValide);
        }, 65L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            genererBaseProgressive(emplacementValide, joueur);
        }, 105L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.sendTitle("§b§l❖ FINALISATION ❖", "§3Préparation de l'arrivée...");
            joueur.playSound(loc, Sound.LEVEL_UP, 0.7f, 1.8f);
        }, 140L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            effectuerArriveeSpectaculaire(joueur, emplacementValide);
        }, 180L);
    }
    
    private void effectuerArriveeSpectaculaire(Player joueur, Location emplacementValide) {
        Location emplacementTeleportation = emplacementValide.clone().add(0.5, 2.5, 7.5);
        emplacementTeleportation.setYaw(180.0f);
        emplacementTeleportation.setPitch(0.0f);
        
        joueur.teleport(emplacementTeleportation);

        plugin.getGestionnaireFreeze().bloquerJoueurAuSol(joueur);
        
        joueur.sendTitle("§6§l✦✦✦ BIENVENUE ✦✦✦", "§e§lVotre base est prête !");
        joueur.playSound(emplacementTeleportation, Sound.ENDERDRAGON_GROWL, 0.5f, 2.0f);
        joueur.playSound(emplacementTeleportation, Sound.LEVEL_UP, 1.0f, 1.0f);
        
        for (int cercle = 0; cercle < 3; cercle++) {
            final int rayonCercle = cercle;
            for (int i = 0; i < 360; i += 10) {
                final int angle = i;
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    double rad = Math.toRadians(angle);
                    double rayon = 2.0 + (rayonCercle * 1.5);
                    double x = Math.cos(rad) * rayon;
                    double z = Math.sin(rad) * rayon;
                    double y = 0.5 + (rayonCercle * 0.5);
                    
                    Location particleLoc = emplacementTeleportation.clone().add(x, y, z);
                    joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
                    joueur.playEffect(particleLoc, Effect.HAPPY_VILLAGER, null);
                }, rayonCercle * 5L);
            }
        }
        
        for (int i = 0; i < 100; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                double angle = index * 0.8;
                double rayon = 5.0 - (index * 0.04);
                double x = Math.cos(angle) * rayon;
                double z = Math.sin(angle) * rayon;
                double y = 3.0 - (index * 0.02);
                
                Location particleLoc = emplacementTeleportation.clone().add(x, y, z);
                joueur.playEffect(particleLoc, Effect.PORTAL, null);
                
                if (index % 5 == 0) {
                    joueur.playEffect(particleLoc, Effect.ENDER_SIGNAL, null);
                }
            }, i / 2);
        }
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.playSound(emplacementTeleportation, Sound.ORB_PICKUP, 1.0f, 1.0f);
            
            for (int i = 0; i < 30; i++) {
                double offsetX = (random.nextDouble() - 0.5) * 4;
                double offsetZ = (random.nextDouble() - 0.5) * 4;
                double offsetY = random.nextDouble() * 3;
                
                Location particleLoc = emplacementTeleportation.clone().add(offsetX, offsetY, offsetZ);
                joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
            }
        }, 15L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            afficherInformationsArrivee(joueur, emplacementTeleportation);
        }, 30L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            demarrerTourGuideProgressif(joueur, emplacementValide);
        }, 60L);
    }
    
    private void afficherInformationsArrivee(Player joueur, Location emplacement) {
        String coordonnees = "(" + emplacement.getBlockX() + ", " + 
                            emplacement.getBlockY() + ", " + 
                            emplacement.getBlockZ() + ")";
        
        joueur.sendMessage("");
        joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        joueur.sendMessage("");
        joueur.sendMessage("         §6§l✦ TÉLÉPORTATION RÉUSSIE ✦");
        joueur.sendMessage("");
        joueur.sendMessage("  §7▸ Coordonnées: §e" + coordonnees);
        
        if (plugin.getIntegrationEssentials().estEssentialsActif()) {
            boolean succes = plugin.getIntegrationEssentials().definirHome(joueur, "base", emplacement);
            if (succes) {
                joueur.sendMessage("  §7▸ Point de home: §a✓ Défini avec succès");
            } else {
                joueur.sendMessage("  §7▸ Point de home: §c✗ Erreur lors de la définition");
            }
        } else {
            joueur.sendMessage("  §7▸ Point de home: §c✗ Essentials non disponible");
        }
        
        joueur.sendMessage("");
        joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        joueur.sendMessage("");
    }
    
    private void demarrerTourGuide(Player joueur, Location centre) {
        joueur.sendMessage("");
        joueur.sendMessage("  §6§l⚡ VISITE GUIDÉE DE VOTRE BASE ⚡");
        joueur.sendMessage("");
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerGlowstones(joueur, centre);
            joueur.sendMessage("  §7⚡ §eÉclairage §8» §aActivé");
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.FIZZ, 0.5f, 1.5f);
        }, 20L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerCoffres(joueur, centre);
            joueur.sendMessage("  §7▣ §eStockage §8» §a4 coffres prêts");
            joueur.playSound(centre.clone().add(0, 2, -9), Sound.CHEST_OPEN, 0.8f, 1.0f);
        }, 50L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerCultures(joueur, centre);
            joueur.sendMessage("  §7✿ §eAgriculture §8» §a4 zones cultivables");
            joueur.playSound(centre.clone().add(0, 2, 0), Sound.DIG_GRASS, 0.8f, 1.2f);
        }, 80L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerCentre(joueur, centre);
            joueur.sendMessage("  §7⚒ §eAtelier §8» §aTable de craft + 2 fourneaux");
            joueur.playSound(centre.clone().add(0, 2, 0), Sound.ANVIL_USE, 0.5f, 1.5f);
        }, 110L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.sendMessage("  §7♦ §eDécoration §8» §aLit, bibliothèque et mobilier");
            joueur.playSound(centre.clone().add(0, 2, -2), Sound.STEP_WOOD, 0.8f, 1.0f);
        }, 140L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerFinaleAmelioree(joueur, centre);
            
            joueur.sendMessage("");
            joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            joueur.sendMessage("");
            joueur.sendMessage("         §a§l✓ INSTALLATION TERMINÉE ✓");
            joueur.sendMessage("");
            joueur.sendMessage("  §7Votre base souterraine est maintenant");
            joueur.sendMessage("  §7entièrement opérationnelle !");
            joueur.sendMessage("");
            joueur.sendMessage("  §e§l➤ §eBon jeu et bonne exploration !");
            joueur.sendMessage("");
            joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            joueur.sendMessage("");
            
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.ENDERDRAGON_GROWL, 0.4f, 2.0f);
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.LEVEL_UP, 1.0f, 1.0f);
        }, 170L);
    }
    
    private void demarrerTourGuideProgressif(Player joueur, Location centre) {
        joueur.sendMessage("");
        joueur.sendMessage("  §6§l⚡ INSTALLATION DE VOTRE BASE ⚡");
        joueur.sendMessage("");
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            constructeurBase.revelerEclairage(centre);
            animerGlowstones(joueur, centre);
            joueur.sendMessage("  §7⚡ §eÉclairage §8» §aActivé");
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.FIZZ, 0.5f, 1.5f);
        }, 20L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            constructeurBase.revelerStockage(centre);
            animerCoffres(joueur, centre);
            joueur.sendMessage("  §7▣ §eStockage §8» §a4 coffres prêts");
            joueur.playSound(centre.clone().add(0, 2, -9), Sound.CHEST_OPEN, 0.8f, 1.0f);
        }, 50L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            constructeurBase.revelerAgriculture(centre);
            animerCultures(joueur, centre);
            joueur.sendMessage("  §7✿ §eAgriculture §8» §a4 zones cultivables");
            joueur.playSound(centre.clone().add(0, 2, 0), Sound.DIG_GRASS, 0.8f, 1.2f);
        }, 80L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            constructeurBase.revelerAtelier(centre);
            animerCentre(joueur, centre);
            joueur.sendMessage("  §7⚒ §eAtelier §8» §aTable de craft + 2 fourneaux");
            joueur.playSound(centre.clone().add(0, 2, 0), Sound.ANVIL_USE, 0.5f, 1.5f);
        }, 110L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.sendMessage("  §7♦ §eDécoration §8» §aLit, bibliothèque et mobilier");
            joueur.playSound(centre.clone().add(0, 2, -2), Sound.STEP_WOOD, 0.8f, 1.0f);
        }, 140L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animerFinaleAmelioree(joueur, centre);
            
            joueur.sendMessage("");
            joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            joueur.sendMessage("");
            joueur.sendMessage("         §a§l✓ INSTALLATION TERMINÉE ✓");
            joueur.sendMessage("");
            joueur.sendMessage("  §7Votre base souterraine est maintenant");
            joueur.sendMessage("  §7entièrement opérationnelle !");
            joueur.sendMessage("");
            joueur.sendMessage("  §e§l➤ §eBon jeu et bonne exploration !");
            joueur.sendMessage("");
            joueur.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            joueur.sendMessage("");
            
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.ENDERDRAGON_GROWL, 0.4f, 2.0f);
            joueur.playSound(centre.clone().add(0, 4, 0), Sound.LEVEL_UP, 1.0f, 1.0f);

            plugin.getGestionnaireFreeze().debloquerJoueur(joueur);
        }, 170L);
    }
    
    private void animerFinaleAmelioree(Player joueur, Location centre) {
        Location centerLoc = centre.clone().add(0, 4, 0);
        
        for (int vague = 0; vague < 3; vague++) {
            final int vagueIndex = vague;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (int i = 0; i < 360; i += 15) {
                    double angle = Math.toRadians(i);
                    double rayon = 3.0 + (vagueIndex * 2.0);
                    double x = Math.cos(angle) * rayon;
                    double z = Math.sin(angle) * rayon;
                    
                    Location particleLoc = centerLoc.clone().add(x, 0, z);
                    joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
                    joueur.playEffect(particleLoc, Effect.HAPPY_VILLAGER, null);
                }
            }, vague * 10L);
        }
        
        for (int i = 0; i < 80; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                double angle = index * 0.4;
                double rayon = 10 - (index * 0.1);
                if (rayon < 1) rayon = 1;
                
                double x = Math.cos(angle) * rayon;
                double z = Math.sin(angle) * rayon;
                double y = Math.sin(index * 0.2) * 2;
                
                Location particleLoc = centerLoc.clone().add(x, y, z);
                joueur.playEffect(particleLoc, Effect.PORTAL, null);
                
                if (index % 8 == 0) {
                    joueur.playEffect(particleLoc, Effect.ENDER_SIGNAL, null);
                }
                
                if (index % 4 == 0) {
                    Location sparkLoc = centerLoc.clone().add(
                        (random.nextDouble() - 0.5) * 12,
                        random.nextDouble() * 4,
                        (random.nextDouble() - 0.5) * 12
                    );
                    joueur.playEffect(sparkLoc, Effect.FIREWORKS_SPARK, null);
                }
            }, i / 2);
        }
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.playSound(centerLoc, Sound.FIREWORK_BLAST, 1.0f, 1.0f);
            joueur.playSound(centerLoc, Sound.FIREWORK_LARGE_BLAST, 0.8f, 1.2f);
        }, 20L);
    }
    
    private Location trouverEmplacementValide(World monde) {
        int tentativesMaximales = plugin.getConfigurationManager().getTentativesMaximales();
        int tailleBordure = plugin.getConfigurationManager().getTailleBordureMonde();
        
        for (int tentative = 0; tentative < tentativesMaximales; tentative++) {
            int x = random.nextInt(tailleBordure * 2) - tailleBordure;
            int z = random.nextInt(tailleBordure * 2) - tailleBordure;
            
            Location emplacement = trouverEmplacementSouterrain(monde, x, z);
            
            if (emplacement != null && validateurEmplacement.estEmplacementValide(emplacement)) {
                return emplacement;
            }
        }
        
        return null;
    }
    
    private Location trouverEmplacementSouterrain(World monde, int x, int z) {
        int profondeurMin = plugin.getConfigurationManager().getProfondeurMaximale();
        int profondeurMax = plugin.getConfigurationManager().getProfondeurMinimale();
        
        for (int y = profondeurMax; y >= profondeurMin; y--) {
            Location emplacement = new Location(monde, x, y, z);
            
            if (validateurEmplacement.estEmplacementSouterrainValide(emplacement)) {
                return emplacement;
            }
        }
        
        return null;
    }

    private void chargerChunksPourBase(Location centre) {
        World monde = centre.getWorld();
        int chunkCentreX = centre.getBlockX() >> 4;
        int chunkCentreZ = centre.getBlockZ() >> 4;

        for (int offsetX = -2; offsetX <= 2; offsetX++) {
            for (int offsetZ = -2; offsetZ <= 2; offsetZ++) {
                int chunkX = chunkCentreX + offsetX;
                int chunkZ = chunkCentreZ + offsetZ;

                Chunk chunk = monde.getChunkAt(chunkX, chunkZ);
                if (!chunk.isLoaded()) {
                    chunk.load(true);
                }
            }
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (int x = -10; x <= 10; x++) {
                for (int z = -10; z <= 10; z++) {
                    Location loc = centre.clone().add(x, 0, z);
                    loc.getBlock().getState();
                }
            }
        }, 10L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (int offsetX = -2; offsetX <= 2; offsetX++) {
                for (int offsetZ = -2; offsetZ <= 2; offsetZ++) {
                    int chunkX = chunkCentreX + offsetX;
                    int chunkZ = chunkCentreZ + offsetZ;
                    Chunk chunk = monde.getChunkAt(chunkX, chunkZ);
                    chunk.getBlock(8, centre.getBlockY(), 8).getState();
                }
            }
        }, 20L);
    }
    
    private void genererBase(Location emplacement) {
        constructeurBase.construireBase(emplacement);
    }
    
    private void genererBaseProgressive(Location emplacement, Player joueur) {
        constructeurBase.construireBaseProgressive(emplacement, joueur);
    }
    
    private void animerGlowstones(Player joueur, Location centre) {
        int[] positions = {-7, -4, -1, 2, 5};
        int delay = 0;
        
        for (int x : positions) {
            for (int z : positions) {
                final int currentDelay = delay;
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    Location glowLoc = centre.clone().add(x, 7, z);
                    joueur.playEffect(glowLoc, Effect.MOBSPAWNER_FLAMES, null);
                    joueur.playSound(glowLoc, Sound.FIZZ, 0.3f, 1.5f);
                }, currentDelay);
                delay += 1;
            }
        }
    }
    
    private void animerCoffres(Player joueur, Location centre) {
        int[] xPositions = {-5, -2, 2, 5};
        
        for (int i = 0; i < xPositions.length; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location coffreLoc = centre.clone().add(xPositions[index], 2, -9);
                
                for (int j = 0; j < 10; j++) {
                    double offsetX = (random.nextDouble() - 0.5) * 0.8;
                    double offsetY = random.nextDouble() * 1.5;
                    double offsetZ = (random.nextDouble() - 0.5) * 0.8;
                    
                    Location particleLoc = coffreLoc.clone().add(offsetX, offsetY, offsetZ);
                    joueur.playEffect(particleLoc, Effect.FIREWORKS_SPARK, null);
                }
                
                joueur.playSound(coffreLoc, Sound.CHEST_OPEN, 0.5f, 1.0f);
            }, i * 5L);
        }
    }
    
    private void animerCultures(Player joueur, Location centre) {
        int[][] zones = {
            {-7, -3, -5, -1},
            {3, 7, -5, -1},
            {-7, -3, 1, 5},
            {3, 7, 1, 5}
        };
        
        for (int i = 0; i < zones.length; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                int xMin = zones[index][0];
                int xMax = zones[index][1];
                int zMin = zones[index][2];
                int zMax = zones[index][3];
                
                int xCenter = (xMin + xMax) / 2;
                int zCenter = (zMin + zMax) / 2;
                Location centerLoc = centre.clone().add(xCenter, 2, zCenter);
                
                for (int j = 0; j < 15; j++) {
                    double offsetX = (random.nextDouble() - 0.5) * 5;
                    double offsetZ = (random.nextDouble() - 0.5) * 5;
                    double offsetY = random.nextDouble() * 0.5;
                    
                    Location particleLoc = centerLoc.clone().add(offsetX, offsetY, offsetZ);
                    joueur.playEffect(particleLoc, Effect.HAPPY_VILLAGER, null);
                }
                
                joueur.playSound(centerLoc, Sound.DIG_GRASS, 0.5f, 1.2f);
            }, i * 5L);
        }
    }
    
    private void animerCentre(Player joueur, Location centre) {
        Location workbenchLoc = centre.clone().add(0, 2, 0);
        Location furnace1Loc = centre.clone().add(1, 2, 0);
        Location furnace2Loc = centre.clone().add(-1, 2, 0);
        
        for (int i = 0; i < 20; i++) {
            final int delay = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                double angle = delay * 0.3;
                double radius = 1.5;
                double x = Math.cos(angle) * radius;
                double z = Math.sin(angle) * radius;
                
                Location particleLoc = workbenchLoc.clone().add(x, 0.5, z);
                joueur.playEffect(particleLoc, Effect.CRIT, null);
            }, i);
        }
        
        joueur.playSound(workbenchLoc, Sound.ANVIL_USE, 0.3f, 1.5f);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            joueur.playEffect(furnace1Loc, Effect.MOBSPAWNER_FLAMES, null);
            joueur.playEffect(furnace2Loc, Effect.MOBSPAWNER_FLAMES, null);
            joueur.playSound(furnace1Loc, Sound.FIRE_IGNITE, 0.5f, 1.0f);
        }, 10L);
    }
    
}