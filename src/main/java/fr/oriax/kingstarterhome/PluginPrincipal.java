package fr.oriax.kingstarterhome;

import fr.oriax.kingstarterhome.commandes.CommandeBaseHome;
import fr.oriax.kingstarterhome.ecouteurs.EcouteurInteraction;
import fr.oriax.kingstarterhome.ecouteurs.EcouteurMouvement;
import fr.oriax.kingstarterhome.gestionnaires.GestionnaireBase;
import fr.oriax.kingstarterhome.gestionnaires.GestionnaireCooldown;
import fr.oriax.kingstarterhome.gestionnaires.GestionnaireFreeze;
import fr.oriax.kingstarterhome.gestionnaires.GestionnaireItem;
import fr.oriax.kingstarterhome.integration.IntegrationFactions;
import fr.oriax.kingstarterhome.integration.IntegrationEssentials;
import fr.oriax.kingstarterhome.utilitaires.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginPrincipal extends JavaPlugin {
    
    private static PluginPrincipal instance;
    private GestionnaireBase gestionnaireBase;
    private GestionnaireCooldown gestionnaireCooldown;
    private GestionnaireFreeze gestionnaireFreeze;
    private GestionnaireItem gestionnaireItem;
    private ConfigurationManager configurationManager;
    private IntegrationFactions integrationFactions;
    private IntegrationEssentials integrationEssentials;
    
    @Override
    public void onEnable() {
        instance = this;
        
        this.configurationManager = new ConfigurationManager(this);
        this.integrationFactions = new IntegrationFactions(this);
        this.integrationEssentials = new IntegrationEssentials(this);
        this.gestionnaireFreeze = new GestionnaireFreeze();
        this.gestionnaireBase = new GestionnaireBase(this);
        this.gestionnaireCooldown = new GestionnaireCooldown(this);
        this.gestionnaireItem = new GestionnaireItem(this);
        
        enregistrerCommandes();
        enregistrerEcouteurs();
        
        if (integrationFactions.estFactionsActif()) {
            getLogger().info("Integration Factions activee !");
        }
        
        if (integrationEssentials.estEssentialsActif()) {
            getLogger().info("Integration Essentials activee !");
        }
        
        getLogger().info("KingStarterHome active avec succes !");
    }
    
    @Override
    public void onDisable() {
        if (gestionnaireCooldown != null) {
            gestionnaireCooldown.sauvegarderCooldowns();
        }
        if (gestionnaireFreeze != null) {
            gestionnaireFreeze.debloquerTous();
        }
        getLogger().info("KingStarterHome desactive !");
    }
    
    private void enregistrerCommandes() {
        getCommand("basehome").setExecutor(new CommandeBaseHome(this));
    }
    
    private void enregistrerEcouteurs() {
        getServer().getPluginManager().registerEvents(new EcouteurInteraction(this), this);
        getServer().getPluginManager().registerEvents(new EcouteurMouvement(this), this);
    }
    
    public static PluginPrincipal getInstance() {
        return instance;
    }
    
    public GestionnaireBase getGestionnaireBase() {
        return gestionnaireBase;
    }
    
    public GestionnaireCooldown getGestionnaireCooldown() {
        return gestionnaireCooldown;
    }
    
    public GestionnaireFreeze getGestionnaireFreeze() {
        return gestionnaireFreeze;
    }
    
    public GestionnaireItem getGestionnaireItem() {
        return gestionnaireItem;
    }
    
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
    
    public IntegrationFactions getIntegrationFactions() {
        return integrationFactions;
    }
    
    public IntegrationEssentials getIntegrationEssentials() {
        return integrationEssentials;
    }
}