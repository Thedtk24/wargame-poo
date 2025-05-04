package fr.wargame.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class ZoneVisibilite {
    private final Carte carte;
    private final Map<Integer, Set<Position>> positionsVisiblesParJoueur;
    private final Set<Position> positionsExplorees;

    public ZoneVisibilite(Carte carte) {
        this.carte = carte;
        this.positionsVisiblesParJoueur = new HashMap<>();
        this.positionsVisiblesParJoueur.put(1, new HashSet<>());
        this.positionsVisiblesParJoueur.put(2, new HashSet<>());
        this.positionsExplorees = new HashSet<>();
    }

    public void calculerVisibilitePourJoueur(int joueur) {
        // Réinitialiser les positions visibles pour ce joueur
        positionsVisiblesParJoueur.get(joueur).clear();

        // Calculer la visibilité pour chaque unité du joueur
        for (Unite unite : carte.getUnites().values()) {
            if (unite.getJoueur() == joueur && unite.estVivant()) {
                calculerVisibiliteUnite(unite);
            }
        }
    }

    private void calculerVisibiliteUnite(Unite unite) {
        Position positionUnite = unite.getPosition();
        int porteeVision = unite.getPorteeVision();
        int joueur = unite.getJoueur();

        // Calculer les positions visibles dans un rayon autour de l'unité
        for (int dx = -porteeVision; dx <= porteeVision; dx++) {
            for (int dy = -porteeVision; dy <= porteeVision; dy++) {
                Position pos = new Position(positionUnite.getX() + dx, positionUnite.getY() + dy);
                
                // Vérifier si la position est valide et dans la portée de vision
                if (carte.estPositionValide(pos) && 
                    positionUnite.distance(pos) <= porteeVision) {
                    positionsVisiblesParJoueur.get(joueur).add(pos);
                    positionsExplorees.add(pos);
                }
            }
        }
    }

    public boolean estVisible(Position position, int joueur) {
        return positionsVisiblesParJoueur.get(joueur).contains(position);
    }

    public boolean estExplore(Position position) {
        return positionsExplorees.contains(position);
    }

    public Set<Position> getPositionsVisibles(int joueur) {
        return new HashSet<>(positionsVisiblesParJoueur.get(joueur));
    }

    public Set<Position> getPositionsExplorees() {
        return new HashSet<>(positionsExplorees);
    }
} 