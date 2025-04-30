package fr.wargame.ia;

import fr.wargame.model.*;
import java.util.*;

public class IAJoueur {
    private final Partie partie;
    private final int joueurIA;

    public IAJoueur(Partie partie, int joueurIA) {
        this.partie = partie;
        this.joueurIA = joueurIA;
    }

    public void jouerTour() {
        List<Unite> unitesIA = (joueurIA == 1) ? partie.getUnitesJoueur1() : partie.getUnitesJoueur2();
        List<Unite> unitesEnnemi = (joueurIA == 1) ? partie.getUnitesJoueur2() : partie.getUnitesJoueur1();

        // Pour chaque unité de l'IA
        for (Unite unite : unitesIA) {
            if (!unite.estVivant()) continue;

            // Chercher l'ennemi le plus proche
            Unite ciblePlusProche = null;
            double distanceMin = Double.MAX_VALUE;

            for (Unite ennemi : unitesEnnemi) {
                if (!ennemi.estVivant()) continue;
                
                double distance = unite.getPosition().distance(ennemi.getPosition());
                if (distance < distanceMin) {
                    distanceMin = distance;
                    ciblePlusProche = ennemi;
                }
            }

            if (ciblePlusProche != null) {
                // Si l'ennemi est adjacent, l'attaquer
                if (unite.getPosition().distance(ciblePlusProche.getPosition()) <= 1.5) {
                    Combat.resoudre(unite, ciblePlusProche, 
                        partie.getCarte().getTerrain(ciblePlusProche.getPosition()));
                    unite.consommerPointsDeplacement(1);
                }
                // Sinon, essayer de se rapprocher
                else if (unite.peutSeDeplacer()) {
                    deplacerVersPosition(unite, ciblePlusProche.getPosition());
                }
            }
        }
    }

    private void deplacerVersPosition(Unite unite, Position cible) {
        // Trouver la meilleure case adjacente qui nous rapproche de la cible
        Position meilleurDeplacement = null;
        double meilleurDistance = Double.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                Position nouvellePos = new Position(
                    unite.getPosition().getX() + dx,
                    unite.getPosition().getY() + dy
                );

                if (!partie.getCarte().estPositionValide(nouvellePos)) continue;
                if (partie.getCarte().estPositionOccupee(nouvellePos)) continue;

                Terrain terrain = partie.getCarte().getTerrain(nouvellePos);
                if (terrain.getPointsDeplacement() == 0) continue; // Eau profonde
                if (unite.getPointsDeplacementRestants() < terrain.getPointsDeplacement()) continue;

                double distance = nouvellePos.distance(cible);
                if (distance < meilleurDistance) {
                    meilleurDistance = distance;
                    meilleurDeplacement = nouvellePos;
                }
            }
        }

        if (meilleurDeplacement != null) {
            Terrain terrain = partie.getCarte().getTerrain(meilleurDeplacement);
            partie.getCarte().deplacerUnite(unite, meilleurDeplacement);
            unite.consommerPointsDeplacement(terrain.getPointsDeplacement());
        }
    }
} 