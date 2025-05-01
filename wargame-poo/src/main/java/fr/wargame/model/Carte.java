package fr.wargame.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int largeur;
    private final int hauteur;
    private final Map<Position, Terrain> terrains;
    private final Map<Position, Unite> unites;

    public Carte(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.terrains = new HashMap<>();
        this.unites = new HashMap<>();
    }

    public void setTerrain(Position position, Terrain terrain) {
        if (estPositionValide(position)) {
            terrains.put(position, terrain);
        }
    }

    public void placerUnite(Unite unite) {
        if (estPositionValide(unite.getPosition()) && !unites.containsKey(unite.getPosition())) {
            unites.put(unite.getPosition(), unite);
        }
    }

    public void deplacerUnite(Unite unite, Position nouvellePosition) {
        if (estPositionValide(nouvellePosition) && !unites.containsKey(nouvellePosition)) {
            unites.remove(unite.getPosition());
            unite.deplacer(nouvellePosition);
            unites.put(nouvellePosition, unite);
        }
    }

    public void retirerUnite(Unite unite) {
        unites.remove(unite.getPosition());
    }

    public boolean estPositionValide(Position position) {
        return position.getX() >= 0 && position.getX() < largeur &&
               position.getY() >= 0 && position.getY() < hauteur;
    }

    public boolean estPositionOccupee(Position position) {
        return unites.containsKey(position);
    }

    public Terrain getTerrain(Position position) {
        return terrains.getOrDefault(position, Terrain.PLAINE);
    }

    public Unite getUnite(Position position) {
        return unites.get(position);
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Map<Position, Unite> getUnites() {
        return new HashMap<>(unites);
    }
} 