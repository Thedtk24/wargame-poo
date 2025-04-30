package fr.wargame.model;

public enum Terrain {
    VILLAGE(1, 40),
    FORTERESSE(1, 60),
    EAU_PROFONDE(0, 0),
    PLAINE(1, 20),
    FORET(2, 40),
    COLLINE(2, 50),
    MONTAGNE(3, 60);

    private final int pointsDeplacement;
    private final int bonusDefense;

    Terrain(int pointsDeplacement, int bonusDefense) {
        this.pointsDeplacement = pointsDeplacement;
        this.bonusDefense = bonusDefense;
    }

    public int getPointsDeplacement() {
        return pointsDeplacement;
    }

    public int getBonusDefense() {
        return bonusDefense;
    }
} 