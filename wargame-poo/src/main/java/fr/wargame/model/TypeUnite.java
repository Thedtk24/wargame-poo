package fr.wargame.model;

public enum TypeUnite {
    INFANTERIE(5, 3, 6, 4, 28),
    INFANTERIE_LOURDE(10, 10, 4, 4, 38),
    CAVALERIE(8, 3, 8, 6, 38),
    MAGE(5, 1, 5, 5, 24),
    ARCHER(6, 2, 5, 7, 33);

    private final int attaque;
    private final int defense;
    private final int deplacement;
    private final int vision;
    private final int pvMax;

    TypeUnite(int attaque, int defense, int deplacement, int vision, int pvMax) {
        this.attaque = attaque;
        this.defense = defense;
        this.deplacement = deplacement;
        this.vision = vision;
        this.pvMax = pvMax;
    }

    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }

    public int getDeplacement() {
        return deplacement;
    }

    public int getVision() {
        return vision;
    }

    public int getPvMax() {
        return pvMax;
    }
} 