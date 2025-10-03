package fr.oriax.kingstarterhome.utilitaires;

import fr.oriax.kingstarterhome.PluginPrincipal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConstructeurBase {
    
    private final PluginPrincipal plugin;
    
    public ConstructeurBase(PluginPrincipal plugin) {
        this.plugin = plugin;
    }
    
    public void construireBase(Location centre) {
        creerStructure(centre);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            placerCoffres(centre);
        }, 20L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            remplirCoffres(centre);
        }, 30L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            ajouterDecorations(centre);
        }, 40L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            placerPancartes(centre);
        }, 50L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            placerTorches(centre);
        }, 60L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            placerLitEtMobilier(centre);
        }, 70L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            creerSolCultures(centre);
        }, 80L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            placerCultures(centre);
        }, 100L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            forcerMiseAJourLumiere(centre);
        }, 110L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            verifierEtCorrigerFarmland(centre);
        }, 120L);
    }
    
    public void construireBaseProgressive(Location centre, Player joueur) {
        creerStructureSansLumiere(centre);
        ajouterDecorationsBase(centre);
    }
    
    public void revelerEclairage(Location centre) {
        placerTorches(centre);
        placerGlowstones(centre);
        forcerMiseAJourLumiere(centre);
    }
    
    public void revelerStockage(Location centre) {
        placerCoffres(centre);
        remplirCoffres(centre);
        placerPancartes(centre);
    }
    
    public void revelerAgriculture(Location centre) {
        creerSolCultures(centre);
        placerCultures(centre);
        verifierEtCorrigerFarmland(centre);
    }
    
    public void revelerAtelier(Location centre) {
        placerAtelier(centre);
    }
    
    private void creerStructure(Location centre) {
        Random random = new Random(centre.getBlockX() * 31L + centre.getBlockZ() * 17L);
        
        for (int x = -10; x <= 10; x++) {
            for (int y = 1; y <= 8; y++) {
                for (int z = -10; z <= 10; z++) {
                    Location emplacement = centre.clone().add(x, y, z);
                    Block bloc = emplacement.getBlock();

                    double distance = Math.sqrt(x * x + z * z);
                    boolean estDansOctogone = Math.abs(x) <= 10 && Math.abs(z) <= 10 && 
                                              Math.abs(x) + Math.abs(z) <= 14;

                    if (!estDansOctogone) {
                        continue;
                    }

                    if (y == 1) {
                        int rand = random.nextInt(100);
                        if (rand < 35) {
                            bloc.setType(Material.COBBLESTONE);
                        } else if (rand < 65) {
                            bloc.setType(Material.STONE);
                        } else if (rand < 85) {
                            bloc.setTypeIdAndData(98, (byte) 0, true);
                        } else if (rand < 95) {
                            bloc.setTypeIdAndData(1, (byte) 5, true);
                        } else {
                            bloc.setTypeIdAndData(98, (byte) 2, true);
                        }
                    }
                    else if (y == 8) {
                        if (distance <= 3) {
                            int rand = random.nextInt(100);
                            if (rand < 35) {
                                bloc.setType(Material.COBBLESTONE);
                            } else if (rand < 65) {
                                bloc.setType(Material.STONE);
                            } else if (rand < 85) {
                                bloc.setTypeIdAndData(98, (byte) 0, true);
                            } else if (rand < 95) {
                                bloc.setTypeIdAndData(1, (byte) 5, true);
                            } else {
                                bloc.setTypeIdAndData(98, (byte) 2, true);
                            }
                        }
                    }
                    else if (y == 7) {
                        if (distance <= 6) {
                            if ((x == -4 && z == -4) || (x == -4 && z == 0) || (x == -4 && z == 4) ||
                                (x == 0 && z == -4) || (x == 0 && z == 4) ||
                                (x == 4 && z == -4) || (x == 4 && z == 0) || (x == 4 && z == 4)) {
                                bloc.setType(Material.GLOWSTONE);
                            } else {
                                int rand = random.nextInt(100);
                                if (rand < 35) {
                                    bloc.setType(Material.COBBLESTONE);
                                } else if (rand < 65) {
                                    bloc.setType(Material.STONE);
                                } else if (rand < 85) {
                                    bloc.setTypeIdAndData(98, (byte) 0, true);
                                } else if (rand < 95) {
                                    bloc.setTypeIdAndData(1, (byte) 5, true);
                                } else {
                                    bloc.setTypeIdAndData(98, (byte) 2, true);
                                }
                            }
                        }
                    }
                    else if (y == 6 && distance > 6 && distance <= 9) {
                        int rand = random.nextInt(100);
                        if (rand < 35) {
                            bloc.setType(Material.COBBLESTONE);
                        } else if (rand < 65) {
                            bloc.setType(Material.STONE);
                        } else if (rand < 85) {
                            bloc.setTypeIdAndData(98, (byte) 0, true);
                        } else if (rand < 95) {
                            bloc.setTypeIdAndData(1, (byte) 5, true);
                        } else {
                            bloc.setTypeIdAndData(98, (byte) 2, true);
                        }
                    }
                    else if (distance >= 9.5 && y >= 2 && y <= 6) {
                        boolean estAuDessusCoffre = (y == 3 && z == -9 && (x == -5 || x == -2 || x == 2 || x == 5));
                        
                        if (estAuDessusCoffre) {
                            bloc.setType(Material.AIR);
                        } else {
                            int rand = random.nextInt(100);
                            if (rand < 35) {
                                bloc.setType(Material.COBBLESTONE);
                            } else if (rand < 65) {
                                bloc.setType(Material.STONE);
                            } else if (rand < 85) {
                                bloc.setTypeIdAndData(98, (byte) 0, true);
                            } else if (rand < 95) {
                                bloc.setTypeIdAndData(1, (byte) 5, true);
                            } else {
                                bloc.setTypeIdAndData(98, (byte) 2, true);
                            }
                        }
                    }
                    else {
                        bloc.setType(Material.AIR);
                    }
                }
            }
        }
    }
    
    private void placerCoffres(Location centre) {
        List<Location> emplacementsCoffres = genererEmplacementsCoffres(centre);
        
        for (Location emplacement : emplacementsCoffres) {
            Block bloc = emplacement.getBlock();
            bloc.setType(Material.CHEST);
            bloc.setData((byte) 3);
        }
    }
    
    private void remplirCoffres(Location centre) {
        List<Location> emplacementsCoffres = genererEmplacementsCoffres(centre);
        Random random = new Random();
        
        for (int i = 0; i < emplacementsCoffres.size(); i++) {
            Block bloc = emplacementsCoffres.get(i).getBlock();
            if (bloc.getType() == Material.CHEST) {
                BlockState state = bloc.getState();
                if (state instanceof Chest) {
                    Chest coffre = (Chest) state;
                    Inventory inventaire = coffre.getInventory();
                    
                    List<ItemStack> contenuCoffre = plugin.getConfigurationManager().getContenuCoffre(i + 1);
                    List<Integer> slotsDisponibles = new ArrayList<>();
                    for (int slot = 0; slot < inventaire.getSize(); slot++) {
                        slotsDisponibles.add(slot);
                    }
                    for (ItemStack item : contenuCoffre) {
                        if (!slotsDisponibles.isEmpty()) {
                            int indexAleatoire = random.nextInt(slotsDisponibles.size());
                            int slotChoisi = slotsDisponibles.remove(indexAleatoire);
                            inventaire.setItem(slotChoisi, item);
                        }
                    }
                }
            }
        }
    }
    
    private void ajouterDecorations(Location centre) {

        centre.clone().add(-8, 2, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 3, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 4, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 5, -6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(8, 2, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 3, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 4, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 5, -6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-8, 2, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 3, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 4, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 5, 6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(8, 2, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 3, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 4, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 5, 6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-6, 2, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 3, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 4, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 5, -8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(6, 2, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 3, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 4, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 5, -8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-6, 2, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 3, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 4, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 5, 8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(6, 2, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 3, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 4, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 5, 8).getBlock().setType(Material.SMOOTH_BRICK);

        for (int x = -5; x <= 5; x++) {
            if (Math.abs(x) >= 2) {
                centre.clone().add(x, 5, -8).getBlock().setType(Material.WOOD);
                centre.clone().add(x, 5, 8).getBlock().setType(Material.WOOD);
            }
        }
        for (int z = -5; z <= 5; z++) {
            if (Math.abs(z) >= 2) {
                centre.clone().add(-8, 5, z).getBlock().setType(Material.WOOD);
                centre.clone().add(8, 5, z).getBlock().setType(Material.WOOD);
            }
        }

        centre.clone().add(-9, 4, -7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 5, -7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 4, -7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 5, -7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 4, 7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 5, 7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 4, 7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 5, 7).getBlock().setType(Material.WEB);

        centre.clone().add(-7, 2, -8).getBlock().setType(Material.FENCE);
        centre.clone().add(7, 2, -8).getBlock().setType(Material.FENCE);
        centre.clone().add(-7, 2, 8).getBlock().setType(Material.FENCE);
        centre.clone().add(7, 2, 8).getBlock().setType(Material.FENCE);

        centre.clone().add(0, 1, -8).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(0, 1, 8).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-8, 1, 0).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(8, 1, 0).getBlock().setType(Material.GLOWSTONE);

        centre.clone().add(-5, 5, -3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 5, -3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-5, 5, 3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 5, 3).getBlock().setType(Material.GLOWSTONE);

        centre.clone().add(-5, 1, -6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 1, -6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-5, 1, 6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 1, 6).getBlock().setType(Material.GLOWSTONE);

        for (int z = -7; z <= -6; z++) {
            Block rail = centre.clone().add(-9, 2, z).getBlock();
            rail.setType(Material.RAILS);
        }
        for (int z = 6; z <= 7; z++) {
            Block rail = centre.clone().add(9, 2, z).getBlock();
            rail.setType(Material.RAILS);
        }

        centre.clone().add(0, 2, 0).getBlock().setType(Material.WORKBENCH);

        Block four1 = centre.clone().add(1, 2, 0).getBlock();
        four1.setTypeIdAndData(61, (byte) 3, true);
        Block four2 = centre.clone().add(-1, 2, 0).getBlock();
        four2.setTypeIdAndData(61, (byte) 3, true);



        for (int y = 2; y <= 4; y++) {
            centre.clone().add(-8, y, 6).getBlock().setType(Material.BOOKSHELF);
            centre.clone().add(-8, y, 7).getBlock().setType(Material.BOOKSHELF);
        }

        Block fauteuil = centre.clone().add(-7, 2, 7).getBlock();
        fauteuil.setType(Material.WOOD_STAIRS);
        fauteuil.setData((byte) 1);

        centre.clone().add(-6, 2, 7).getBlock().setType(Material.WOOD_STEP);

        centre.clone().add(-8, 2, -3).getBlock().setType(Material.FENCE);
        centre.clone().add(-8, 3, -3).getBlock().setType(Material.FENCE);
        centre.clone().add(-8, 4, -3).getBlock().setTypeIdAndData(50, (byte) 5, true);

        centre.clone().add(8, 2, 3).getBlock().setType(Material.FENCE);
        centre.clone().add(8, 3, 3).getBlock().setType(Material.FENCE);
        centre.clone().add(8, 4, 3).getBlock().setTypeIdAndData(50, (byte) 5, true);

        centre.clone().add(8, 2, -7).getBlock().setType(Material.LOG);
        centre.clone().add(8, 2, -7).getBlock().setData((byte) 4);
        centre.clone().add(8, 2, -6).getBlock().setType(Material.LOG);
        centre.clone().add(8, 3, -7).getBlock().setType(Material.LOG);

        centre.clone().add(0, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(0, 2, 1).getBlock().setData((byte) 13);
        centre.clone().add(-1, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(-1, 2, 1).getBlock().setData((byte) 13);
        centre.clone().add(1, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(1, 2, 1).getBlock().setData((byte) 13);

        centre.clone().add(-7, 2, 6).getBlock().setType(Material.CARPET);
        centre.clone().add(-7, 2, 6).getBlock().setData((byte) 11);
        centre.clone().add(-6, 2, 6).getBlock().setType(Material.CARPET);
        centre.clone().add(-6, 2, 6).getBlock().setData((byte) 11);

        centre.clone().add(-9, 3, 0).getBlock().setType(Material.PAINTING);
        centre.clone().add(9, 3, 0).getBlock().setType(Material.PAINTING);
        centre.clone().add(0, 3, -9).getBlock().setType(Material.PAINTING);

        centre.clone().add(-9, 3, 3).getBlock().setType(Material.WOOD_STEP);
        centre.clone().add(-9, 3, 3).getBlock().setData((byte) 8);
        centre.clone().add(9, 3, -3).getBlock().setType(Material.WOOD_STEP);
        centre.clone().add(9, 3, -3).getBlock().setData((byte) 8);

        Block jackNord = centre.clone().add(8, 3, -7).getBlock();
        jackNord.setTypeIdAndData(91, (byte) 0, true);

        Block jackSud = centre.clone().add(-8, 2, 5).getBlock();
        jackSud.setTypeIdAndData(91, (byte) 2, true);
    }
    
    private void placerLitEtMobilier(Location centre) {
        Block lit1 = centre.clone().add(0, 2, -2).getBlock();
        lit1.setTypeIdAndData(26, (byte) 2, false);

        Block lit2 = centre.clone().add(0, 2, -3).getBlock();
        lit2.setTypeIdAndData(26, (byte) 10, false);

        Block tableNuit1 = centre.clone().add(-1, 2, -2).getBlock();
        tableNuit1.setType(Material.WOOD_STAIRS);
        tableNuit1.setData((byte) 1);
        Block tableNuit2 = centre.clone().add(1, 2, -2).getBlock();
        tableNuit2.setType(Material.WOOD_STAIRS);
        tableNuit2.setData((byte) 0);

        centre.clone().add(-1, 2, -3).getBlock().setType(Material.CARPET);
        centre.clone().add(-1, 2, -3).getBlock().setData((byte) 14);
        centre.clone().add(0, 2, -4).getBlock().setType(Material.CARPET);
        centre.clone().add(0, 2, -4).getBlock().setData((byte) 14);
        centre.clone().add(1, 2, -3).getBlock().setType(Material.CARPET);
        centre.clone().add(1, 2, -3).getBlock().setData((byte) 14);
    }

    
    private List<Location> genererEmplacementsCoffres(Location centre) {
        List<Location> emplacements = new ArrayList<>();
        emplacements.add(centre.clone().add(-5, 2, -9));
        emplacements.add(centre.clone().add(-2, 2, -9));
        emplacements.add(centre.clone().add(2, 2, -9));
        emplacements.add(centre.clone().add(5, 2, -9));
        
        return emplacements;
    }

    private void forcerMiseAJourLumiere(Location centre) {
        for (int x = -10; x <= 10; x++) {
            for (int y = 1; y <= 8; y++) {
                for (int z = -10; z <= 10; z++) {
                    boolean estDansOctogone = Math.abs(x) <= 10 && Math.abs(z) <= 10 && 
                                              Math.abs(x) + Math.abs(z) <= 14;
                    
                    if (estDansOctogone) {
                        Location loc = centre.clone().add(x, y, z);
                        Block bloc = loc.getBlock();
                        bloc.getState().update(true, false);
                    }
                }
            }
        }
    }

    private void verifierEtCorrigerFarmland(Location centre) {
        int correctionCount = 0;

        for (int x = -7; x <= -3; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                Block blocSol = sol.getBlock();

                if (x == -5 && z == -3) {
                    continue;
                }

                if (blocSol.getType() == Material.DIRT || blocSol.getType() != Material.SOIL) {
                    blocSol.setType(Material.SOIL);
                    blocSol.setData((byte) 7);

                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.CARROT);
                        blocCulture.setData((byte) 7);
                    }
                    correctionCount++;
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                Block blocSol = sol.getBlock();
                
                if (x == 5 && z == -3) {
                    continue;
                }
                
                if (blocSol.getType() == Material.DIRT || blocSol.getType() != Material.SOIL) {
                    blocSol.setType(Material.SOIL);
                    blocSol.setData((byte) 7);
                    
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.POTATO);
                        blocCulture.setData((byte) 7);
                    }
                    correctionCount++;
                }
            }
        }

        for (int x = -7; x <= -3; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                Block blocSol = sol.getBlock();
                
                if (x == -5 && z == 3) {
                    continue;
                }
                
                if (blocSol.getType() == Material.DIRT || blocSol.getType() != Material.SOIL) {
                    blocSol.setType(Material.SOIL);
                    blocSol.setData((byte) 7);
                    
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.CROPS);
                        blocCulture.setData((byte) 7);
                    }
                    correctionCount++;
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                Block blocSol = sol.getBlock();
                
                if (blocSol.getType() != Material.SOUL_SAND) {
                    blocSol.setType(Material.SOUL_SAND);
                    
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.NETHER_WARTS);
                        blocCulture.setData((byte) 3);
                    }
                    correctionCount++;
                }
            }
        }
        
        if (correctionCount > 0) {
            plugin.getLogger().info("Correction de " + correctionCount + " blocs de farmland/soul sand à " + 
                                    centre.getBlockX() + ", " + centre.getBlockY() + ", " + centre.getBlockZ());
        }
    }

    private void creerSolCultures(Location centre) {
        for (int x = -7; x <= -3; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (x == -5 && z == -3) {
                    sol.getBlock().setType(Material.WATER);
                } else {
                    sol.getBlock().setType(Material.SOIL);
                    sol.getBlock().setData((byte) 7);
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (x == 5 && z == -3) {
                    sol.getBlock().setType(Material.WATER);
                } else {
                    sol.getBlock().setType(Material.SOIL);
                    sol.getBlock().setData((byte) 7);
                }
            }
        }

        for (int x = -7; x <= -3; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (x == -5 && z == 3) {
                    sol.getBlock().setType(Material.WATER);
                } else {
                    sol.getBlock().setType(Material.SOIL);
                    sol.getBlock().setData((byte) 7);
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                sol.getBlock().setType(Material.SOUL_SAND);
            }
        }
    }

    private void placerCultures(Location centre) {
        for (int x = -7; x <= -3; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (sol.getBlock().getType() == Material.SOIL) {
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.CARROT);
                        blocCulture.setData((byte) 7);
                    }
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = -5; z <= -1; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (sol.getBlock().getType() == Material.SOIL) {
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.POTATO);
                        blocCulture.setData((byte) 7);
                    }
                }
            }
        }

        for (int x = -7; x <= -3; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (sol.getBlock().getType() == Material.SOIL) {
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.CROPS);
                        blocCulture.setData((byte) 7);
                    }
                }
            }
        }

        for (int x = 3; x <= 7; x++) {
            for (int z = 1; z <= 5; z++) {
                Location sol = centre.clone().add(x, 1, z);
                if (sol.getBlock().getType() == Material.SOUL_SAND) {
                    Location culture = centre.clone().add(x, 2, z);
                    Block blocCulture = culture.getBlock();
                    if (blocCulture.getType() == Material.AIR) {
                        blocCulture.setType(Material.NETHER_WARTS);
                        blocCulture.setData((byte) 3);
                    }
                }
            }
        }
    }

    private void placerPancartes(Location centre) {
        List<Location> emplacementsCoffres = genererEmplacementsCoffres(centre);
        
        for (int i = 0; i < emplacementsCoffres.size(); i++) {
            Location emplacementCoffre = emplacementsCoffres.get(i);
            Block blocCoffre = emplacementCoffre.getBlock();
            
            if (blocCoffre.getType() == Material.CHEST) {
                String textePancarte = plugin.getConfigurationManager().getTextePancarteCoffre(i + 1);
                
                if (textePancarte != null && !textePancarte.isEmpty()) {
                    Location emplacementPancarte = emplacementCoffre.clone().add(0, 0, 1);
                    byte orientation = 3;
                    
                    Block blocPancarte = emplacementPancarte.getBlock();
                    
                    blocPancarte.setType(Material.WALL_SIGN);
                    blocPancarte.setData(orientation);
                    
                    BlockState state = blocPancarte.getState();
                    if (state instanceof Sign) {
                        Sign pancarte = (Sign) state;
                        
                        String[] lignes = textePancarte.split("\\|");
                        for (int j = 0; j < Math.min(lignes.length, 4); j++) {
                            pancarte.setLine(j, lignes[j].replace("&", "§"));
                        }
                        
                        pancarte.update(true);
                    }
                }
            }
        }
    }

    private void placerTorches(Location centre) {
        for (int z = -8; z <= 8; z += 4) {
            if (z != 0) {
                Block torchWest = centre.clone().add(-9, 3, z).getBlock();
                torchWest.setTypeIdAndData(50, (byte) 1, true);

                Block torchEast = centre.clone().add(9, 3, z).getBlock();
                torchEast.setTypeIdAndData(50, (byte) 2, true);
            }
        }
        
        for (int x = -8; x <= 8; x += 4) {
            if (x != 0) {
                Block torchNorth = centre.clone().add(x, 3, -9).getBlock();
                torchNorth.setTypeIdAndData(50, (byte) 3, true);

                Block torchSouth = centre.clone().add(x, 3, 9).getBlock();
                torchSouth.setTypeIdAndData(50, (byte) 4, true);
            }
        }

        for (int z = -6; z <= 6; z += 4) {
            Block torchWest = centre.clone().add(-9, 5, z).getBlock();
            torchWest.setTypeIdAndData(50, (byte) 1, true);

            Block torchEast = centre.clone().add(9, 5, z).getBlock();
            torchEast.setTypeIdAndData(50, (byte) 2, true);
        }
        
        for (int x = -6; x <= 6; x += 4) {
            Block torchNorth = centre.clone().add(x, 5, -9).getBlock();
            torchNorth.setTypeIdAndData(50, (byte) 3, true);

            Block torchSouth = centre.clone().add(x, 5, 9).getBlock();
            torchSouth.setTypeIdAndData(50, (byte) 4, true);
        }
    }
    
    private void creerStructureSansLumiere(Location centre) {
        Random random = new Random(centre.getBlockX() * 31L + centre.getBlockZ() * 17L);
        
        for (int x = -10; x <= 10; x++) {
            for (int y = 1; y <= 8; y++) {
                for (int z = -10; z <= 10; z++) {
                    Location emplacement = centre.clone().add(x, y, z);
                    Block bloc = emplacement.getBlock();

                    double distance = Math.sqrt(x * x + z * z);
                    boolean estDansOctogone = Math.abs(x) <= 10 && Math.abs(z) <= 10 && 
                                              Math.abs(x) + Math.abs(z) <= 14;

                    if (!estDansOctogone) {
                        continue;
                    }

                    if (y == 1) {
                        int rand = random.nextInt(100);
                        if (rand < 35) {
                            bloc.setType(Material.COBBLESTONE);
                        } else if (rand < 65) {
                            bloc.setType(Material.STONE);
                        } else if (rand < 85) {
                            bloc.setTypeIdAndData(98, (byte) 0, true);
                        } else if (rand < 95) {
                            bloc.setTypeIdAndData(1, (byte) 5, true);
                        } else {
                            bloc.setTypeIdAndData(98, (byte) 2, true);
                        }
                    }
                    else if (y == 8) {
                        if (distance <= 3) {
                            int rand = random.nextInt(100);
                            if (rand < 35) {
                                bloc.setType(Material.COBBLESTONE);
                            } else if (rand < 65) {
                                bloc.setType(Material.STONE);
                            } else if (rand < 85) {
                                bloc.setTypeIdAndData(98, (byte) 0, true);
                            } else if (rand < 95) {
                                bloc.setTypeIdAndData(1, (byte) 5, true);
                            } else {
                                bloc.setTypeIdAndData(98, (byte) 2, true);
                            }
                        }
                    }
                    else if (y == 7) {
                        if (distance <= 6) {
                            int rand = random.nextInt(100);
                            if (rand < 35) {
                                bloc.setType(Material.COBBLESTONE);
                            } else if (rand < 65) {
                                bloc.setType(Material.STONE);
                            } else if (rand < 85) {
                                bloc.setTypeIdAndData(98, (byte) 0, true);
                            } else if (rand < 95) {
                                bloc.setTypeIdAndData(1, (byte) 5, true);
                            } else {
                                bloc.setTypeIdAndData(98, (byte) 2, true);
                            }
                        }
                    }
                    else if (y == 6 && distance > 6 && distance <= 9) {
                        int rand = random.nextInt(100);
                        if (rand < 35) {
                            bloc.setType(Material.COBBLESTONE);
                        } else if (rand < 65) {
                            bloc.setType(Material.STONE);
                        } else if (rand < 85) {
                            bloc.setTypeIdAndData(98, (byte) 0, true);
                        } else if (rand < 95) {
                            bloc.setTypeIdAndData(1, (byte) 5, true);
                        } else {
                            bloc.setTypeIdAndData(98, (byte) 2, true);
                        }
                    }
                    else if (distance >= 9.5 && y >= 2 && y <= 6) {
                        boolean estAuDessusCoffre = (y == 3 && z == -9 && (x == -5 || x == -2 || x == 2 || x == 5));
                        
                        if (estAuDessusCoffre) {
                            bloc.setType(Material.AIR);
                        } else {
                            int rand = random.nextInt(100);
                            if (rand < 35) {
                                bloc.setType(Material.COBBLESTONE);
                            } else if (rand < 65) {
                                bloc.setType(Material.STONE);
                            } else if (rand < 85) {
                                bloc.setTypeIdAndData(98, (byte) 0, true);
                            } else if (rand < 95) {
                                bloc.setTypeIdAndData(1, (byte) 5, true);
                            } else {
                                bloc.setTypeIdAndData(98, (byte) 2, true);
                            }
                        }
                    }
                    else {
                        bloc.setType(Material.AIR);
                    }
                }
            }
        }
    }
    
    private void ajouterDecorationsBase(Location centre) {
        centre.clone().add(-8, 2, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 3, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 4, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 5, -6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(8, 2, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 3, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 4, -6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 5, -6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-8, 2, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 3, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 4, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-8, 5, 6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(8, 2, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 3, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 4, 6).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(8, 5, 6).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-6, 2, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 3, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 4, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 5, -8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(6, 2, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 3, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 4, -8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 5, -8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(-6, 2, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 3, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 4, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(-6, 5, 8).getBlock().setType(Material.SMOOTH_BRICK);

        centre.clone().add(6, 2, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 3, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 4, 8).getBlock().setType(Material.SMOOTH_BRICK);
        centre.clone().add(6, 5, 8).getBlock().setType(Material.SMOOTH_BRICK);

        for (int x = -5; x <= 5; x++) {
            if (Math.abs(x) >= 2) {
                centre.clone().add(x, 5, -8).getBlock().setType(Material.WOOD);
                centre.clone().add(x, 5, 8).getBlock().setType(Material.WOOD);
            }
        }
        for (int z = -5; z <= 5; z++) {
            if (Math.abs(z) >= 2) {
                centre.clone().add(-8, 5, z).getBlock().setType(Material.WOOD);
                centre.clone().add(8, 5, z).getBlock().setType(Material.WOOD);
            }
        }

        centre.clone().add(-9, 4, -7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 5, -7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 4, -7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 5, -7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 4, 7).getBlock().setType(Material.WEB);
        centre.clone().add(-9, 5, 7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 4, 7).getBlock().setType(Material.WEB);
        centre.clone().add(9, 5, 7).getBlock().setType(Material.WEB);

        centre.clone().add(-7, 2, -8).getBlock().setType(Material.FENCE);
        centre.clone().add(7, 2, -8).getBlock().setType(Material.FENCE);
        centre.clone().add(-7, 2, 8).getBlock().setType(Material.FENCE);
        centre.clone().add(7, 2, 8).getBlock().setType(Material.FENCE);

        for (int z = -7; z <= -6; z++) {
            Block rail = centre.clone().add(-9, 2, z).getBlock();
            rail.setType(Material.RAILS);
        }
        for (int z = 6; z <= 7; z++) {
            Block rail = centre.clone().add(9, 2, z).getBlock();
            rail.setType(Material.RAILS);
        }

        for (int y = 2; y <= 4; y++) {
            centre.clone().add(-8, y, 6).getBlock().setType(Material.BOOKSHELF);
            centre.clone().add(-8, y, 7).getBlock().setType(Material.BOOKSHELF);
        }

        Block fauteuil = centre.clone().add(-7, 2, 7).getBlock();
        fauteuil.setType(Material.WOOD_STAIRS);
        fauteuil.setData((byte) 1);

        centre.clone().add(-6, 2, 7).getBlock().setType(Material.WOOD_STEP);

        centre.clone().add(-8, 2, -3).getBlock().setType(Material.FENCE);
        centre.clone().add(-8, 3, -3).getBlock().setType(Material.FENCE);
        centre.clone().add(-8, 4, -3).getBlock().setTypeIdAndData(50, (byte) 5, true);

        centre.clone().add(8, 2, 3).getBlock().setType(Material.FENCE);
        centre.clone().add(8, 3, 3).getBlock().setType(Material.FENCE);
        centre.clone().add(8, 4, 3).getBlock().setTypeIdAndData(50, (byte) 5, true);

        centre.clone().add(8, 2, -7).getBlock().setType(Material.LOG);
        centre.clone().add(8, 2, -7).getBlock().setData((byte) 4);
        centre.clone().add(8, 2, -6).getBlock().setType(Material.LOG);
        centre.clone().add(8, 3, -7).getBlock().setType(Material.LOG);

        centre.clone().add(0, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(0, 2, 1).getBlock().setData((byte) 13);
        centre.clone().add(-1, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(-1, 2, 1).getBlock().setData((byte) 13);
        centre.clone().add(1, 2, 1).getBlock().setType(Material.CARPET);
        centre.clone().add(1, 2, 1).getBlock().setData((byte) 13);

        centre.clone().add(-7, 2, 6).getBlock().setType(Material.CARPET);
        centre.clone().add(-7, 2, 6).getBlock().setData((byte) 11);
        centre.clone().add(-6, 2, 6).getBlock().setType(Material.CARPET);
        centre.clone().add(-6, 2, 6).getBlock().setData((byte) 11);

        centre.clone().add(-9, 3, 0).getBlock().setType(Material.PAINTING);
        centre.clone().add(9, 3, 0).getBlock().setType(Material.PAINTING);
        centre.clone().add(0, 3, -9).getBlock().setType(Material.PAINTING);

        centre.clone().add(-9, 3, 3).getBlock().setType(Material.WOOD_STEP);
        centre.clone().add(-9, 3, 3).getBlock().setData((byte) 8);
        centre.clone().add(9, 3, -3).getBlock().setType(Material.WOOD_STEP);
        centre.clone().add(9, 3, -3).getBlock().setData((byte) 8);

        Block jackNord = centre.clone().add(8, 3, -7).getBlock();
        jackNord.setTypeIdAndData(91, (byte) 0, true);

        Block jackSud = centre.clone().add(-8, 2, 5).getBlock();
        jackSud.setTypeIdAndData(91, (byte) 2, true);
    }
    
    private void placerGlowstones(Location centre) {
        centre.clone().add(0, 1, -8).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(0, 1, 8).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-8, 1, 0).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(8, 1, 0).getBlock().setType(Material.GLOWSTONE);

        centre.clone().add(-5, 5, -3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 5, -3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-5, 5, 3).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 5, 3).getBlock().setType(Material.GLOWSTONE);

        centre.clone().add(-5, 1, -6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 1, -6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-5, 1, 6).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(5, 1, 6).getBlock().setType(Material.GLOWSTONE);
        
        centre.clone().add(-4, 7, -4).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-4, 7, 0).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(-4, 7, 4).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(0, 7, -4).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(0, 7, 4).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(4, 7, -4).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(4, 7, 0).getBlock().setType(Material.GLOWSTONE);
        centre.clone().add(4, 7, 4).getBlock().setType(Material.GLOWSTONE);
    }
    
    private void placerAtelier(Location centre) {
        centre.clone().add(0, 2, 0).getBlock().setType(Material.WORKBENCH);

        Block four1 = centre.clone().add(1, 2, 0).getBlock();
        four1.setTypeIdAndData(61, (byte) 3, true);
        Block four2 = centre.clone().add(-1, 2, 0).getBlock();
        four2.setTypeIdAndData(61, (byte) 3, true);

        Block lit1 = centre.clone().add(0, 2, -2).getBlock();
        lit1.setTypeIdAndData(26, (byte) 2, false);

        Block lit2 = centre.clone().add(0, 2, -3).getBlock();
        lit2.setTypeIdAndData(26, (byte) 10, false);

        Block tableNuit1 = centre.clone().add(-1, 2, -2).getBlock();
        tableNuit1.setType(Material.WOOD_STAIRS);
        tableNuit1.setData((byte) 1);
        Block tableNuit2 = centre.clone().add(1, 2, -2).getBlock();
        tableNuit2.setType(Material.WOOD_STAIRS);
        tableNuit2.setData((byte) 0);

        centre.clone().add(-1, 2, -3).getBlock().setType(Material.CARPET);
        centre.clone().add(-1, 2, -3).getBlock().setData((byte) 14);
        centre.clone().add(0, 2, -4).getBlock().setType(Material.CARPET);
        centre.clone().add(0, 2, -4).getBlock().setData((byte) 14);
        centre.clone().add(1, 2, -3).getBlock().setType(Material.CARPET);
        centre.clone().add(1, 2, -3).getBlock().setData((byte) 14);
    }
}