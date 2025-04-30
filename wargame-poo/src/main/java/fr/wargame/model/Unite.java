package fr.wargame.model;

public class Unite {
    private final TypeUnite type;
    private Position position;
    private int pointsDeVie;
    private int pointsDeplacementRestants;
    private final int joueur; // 1 ou 2

    public Unite(TypeUnite type, Position position, int joueur) {
        this.type = type;
        this.position = position;
        this.pointsDeVie = type.getPvMax();
        this.pointsDeplacementRestants = type.getDeplacement();
        this.joueur = joueur;
    }

    public void subirDegats(int degats) {
        this.pointsDeVie = Math.max(0, this.pointsDeVie - degats);
    }

    public void recupererPV(int pourcentage) {
        if (estVivant()) {
            int pvRecuperes = (int) (type.getPvMax() * (pourcentage / 100.0));
            this.pointsDeVie = Math.min(type.getPvMax(), this.pointsDeVie + pvRecuperes);
        }
    }

    public void deplacer(Position nouvellePosition) {
        this.position = nouvellePosition;
    }

    public void reinitialiserPointsDeplacement() {
        this.pointsDeplacementRestants = type.getDeplacement();
    }

    public void consommerPointsDeplacement(int points) {
        this.pointsDeplacementRestants = Math.max(0, this.pointsDeplacementRestants - points);
    }

    public boolean peutSeDeplacer() {
        return pointsDeplacementRestants > 0;
    }

    public boolean estVivant() {
        return pointsDeVie > 0;
    }

    // Getters
    public TypeUnite getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public int getPointsDeplacementRestants() {
        return pointsDeplacementRestants;
    }

    public int getJoueur() {
        return joueur;
    }
} 