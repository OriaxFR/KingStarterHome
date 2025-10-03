package fr.oriax.kingstarterhome.gestionnaires;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GestionnaireFreeze {
    
    private final Set<UUID> joueursBloques;
    private final Set<UUID> joueursEnAttenteDeSol;
    
    public GestionnaireFreeze() {
        this.joueursBloques = new HashSet<>();
        this.joueursEnAttenteDeSol = new HashSet<>();
    }

    public void bloquerJoueur(Player joueur) {
        joueursBloques.add(joueur.getUniqueId());
        joueur.setWalkSpeed(0.0f);
        joueur.setFlySpeed(0.0f);
    }

    public void bloquerJoueurAuSol(Player joueur) {
        joueursEnAttenteDeSol.add(joueur.getUniqueId());
    }

    public boolean verifierEtBloquerAuSol(Player joueur) {
        if (joueursEnAttenteDeSol.contains(joueur.getUniqueId()) && joueur.isOnGround()) {
            joueursEnAttenteDeSol.remove(joueur.getUniqueId());
            bloquerJoueur(joueur);
            return true;
        }
        return false;
    }

    public void debloquerJoueur(Player joueur) {
        joueursBloques.remove(joueur.getUniqueId());
        joueursEnAttenteDeSol.remove(joueur.getUniqueId());
        joueur.setWalkSpeed(0.2f);
        joueur.setFlySpeed(0.1f);
    }

    public boolean estBloque(Player joueur) {
        return joueursBloques.contains(joueur.getUniqueId());
    }

    public boolean estBloque(UUID uuid) {
        return joueursBloques.contains(uuid);
    }

    public boolean estEnAttenteDeSol(Player joueur) {
        return joueursEnAttenteDeSol.contains(joueur.getUniqueId());
    }

    public void debloquerTous() {
        joueursBloques.clear();
        joueursEnAttenteDeSol.clear();
    }
}