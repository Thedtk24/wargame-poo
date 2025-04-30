package fr.wargame.model;

import java.util.Random;

public class Combat {
    private static final Random random = new Random();
    private static final int POURCENTAGE_ALEATOIRE = 50;

    public static void resoudre(Unite attaquant, Unite defenseur, Terrain terrainDefenseur) {
        if (!attaquant.estVivant() || !defenseur.estVivant()) {
            return;
        }

        // Calcul du bonus de défense du terrain
        int bonusDefense = terrainDefenseur.getBonusDefense();
        int defenseEffective = defenseur.getType().getDefense() + 
            (defenseur.getType().getDefense() * bonusDefense) / 100;

        // Calcul des dégâts bruts
        int degatsBruts = attaquant.getType().getAttaque() - defenseEffective;
        
        if (degatsBruts <= 0) {
            degatsBruts = 1; // Dégâts minimum
        }

        // Application de l'aléatoire
        int variationAleatoire = random.nextInt(POURCENTAGE_ALEATOIRE + 1) - (POURCENTAGE_ALEATOIRE / 2);
        int degatsFinaux = Math.max(1, degatsBruts + (degatsBruts * variationAleatoire) / 100);

        // Application des dégâts
        defenseur.subirDegats(degatsFinaux);
    }
} 