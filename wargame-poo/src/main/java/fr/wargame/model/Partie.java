package fr.wargame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partie implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Carte carte;
    private final List<Unite> unitesJoueur1;
    private final List<Unite> unitesJoueur2;
    private int joueurCourant;
    private int tour;
    private static final int TAUX_RECUPERATION = 10;
    public static final int TOUR_MAX = 20; // Nombre de tours maximum pour la victoire du défenseur
    private final ZoneVisibilite zoneVisibilite;

    public Partie(Carte carte) {
        this.carte = carte;
        this.unitesJoueur1 = new ArrayList<>();
        this.unitesJoueur2 = new ArrayList<>();
        this.joueurCourant = 1;
        this.tour = 1;
        this.zoneVisibilite = new ZoneVisibilite(carte);
    }

    public void ajouterUnite(Unite unite) {
        if (unite.getJoueur() == 1) {
            unitesJoueur1.add(unite);
        } else {
            unitesJoueur2.add(unite);
        }
        carte.placerUnite(unite);
        
        // Mettre à jour la visibilité pour le joueur de l'unité
        zoneVisibilite.calculerVisibilitePourJoueur(unite.getJoueur());
    }

    public void initialiserVisibilite() {
        // Calculer la visibilité initiale pour les deux joueurs
        zoneVisibilite.calculerVisibilitePourJoueur(1);
        zoneVisibilite.calculerVisibilitePourJoueur(2);
    }

    public void finDeTour() {
        // Récupération des PV pour les unités non déplacées et non attaquées
        List<Unite> unitesJoueurCourant = (joueurCourant == 1) ? unitesJoueur1 : unitesJoueur2;
        for (Unite unite : unitesJoueurCourant) {
            if (unite.estVivant() && !unite.peutSeDeplacer()) {
                unite.recupererPV(TAUX_RECUPERATION);
            }
        }

        // Changement de joueur
        joueurCourant = (joueurCourant == 1) ? 2 : 1;

        // Si le joueur 1 reprend la main, on incrémente le tour
        if (joueurCourant == 1) {
            tour++;
        }

        // Réinitialisation des points de déplacement
        reinitialiserPointsDeplacement();

        // Mise à jour de la visibilité pour le nouveau joueur
        zoneVisibilite.calculerVisibilitePourJoueur(joueurCourant);
    }

    private void reinitialiserPointsDeplacement() {
        List<Unite> unitesJoueurCourant = (joueurCourant == 1) ? unitesJoueur1 : unitesJoueur2;
        for (Unite unite : unitesJoueurCourant) {
            if (unite.estVivant()) {
                unite.reinitialiserPointsDeplacement();
            }
        }
    }

    public boolean partieTerminee() {
        // Victoire par destruction totale OU par défense jusqu'au tour max
        return unitesJoueur1.stream().noneMatch(Unite::estVivant) ||
               unitesJoueur2.stream().noneMatch(Unite::estVivant) ||
               tour > TOUR_MAX;
    }

    public int getJoueurGagnant() {
        if (!partieTerminee()) return 0;
        boolean joueur1Vivant = unitesJoueur1.stream().anyMatch(Unite::estVivant);
        boolean joueur2Vivant = unitesJoueur2.stream().anyMatch(Unite::estVivant);

        if (tour > TOUR_MAX) {
            // Si le défenseur (joueur 1) a survécu, il gagne
            if (joueur1Vivant) return 1;
            if (joueur2Vivant) return 2;
            return 0; // Match nul (plus aucune unité)
        }
        if (joueur1Vivant && !joueur2Vivant) return 1;
        if (!joueur1Vivant && joueur2Vivant) return 2;
        return 0;
    }

    // Getters
    public Carte getCarte() {
        return carte;
    }

    public int getJoueurCourant() {
        return joueurCourant;
    }

    public int getTour() {
        return tour;
    }

    public List<Unite> getUnitesJoueur1() {
        return new ArrayList<>(unitesJoueur1);
    }

    public List<Unite> getUnitesJoueur2() {
        return new ArrayList<>(unitesJoueur2);
    }

    public ZoneVisibilite getZoneVisibilite() {
        return zoneVisibilite;
    }
} 