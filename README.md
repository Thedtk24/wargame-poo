# Wargame - Projet POO IATIC 3

Ce projet est un jeu de stratégie tactique tour par tour développé dans le cadre du cours de Programmation Orientée Objet à l'IATIC 3.

## Description

Le jeu se déroule sur une grille hexagonale où deux joueurs s'affrontent en déplaçant leurs unités et en combattant. Le jeu comprend différents types de terrain et d'unités, chacun avec ses propres caractéristiques.

### Caractéristiques principales

- Plateau de jeu hexagonal
- Différents types de terrain (plaine, forêt, montagne, etc.)
- Plusieurs types d'unités (infanterie, archer)
- Combat au tour par tour
- Mode joueur contre joueur ou contre l'IA
- Interface graphique avec Swing

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur

## Installation

1. Cloner le dépôt :
```bash
git clone [URL_DU_DEPOT]
cd wargame-poo
```

2. Compiler le projet :
```bash
mvn clean package
```

## Exécution

Pour lancer le jeu :
```bash
java -jar target/wargame-poo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Comment jouer

1. Au début de la partie, chaque joueur dispose de plusieurs unités placées sur le plateau
2. À chaque tour :
   - Sélectionnez une unité en cliquant dessus
   - Déplacez-la en cliquant sur une case valide
   - Attaquez les unités ennemies à portée
3. Le jeu se termine quand :
   - Toutes les unités d'un joueur sont détruites
   - Le nombre maximum de tours est atteint

## Structure du projet

```
src/
├── main/
│   └── java/
│       └── fr/
│           └── iatic/
│               └── wargame/
│                   ├── model/      # Classes du modèle
│                   ├── view/       # Classes de l'interface graphique
│                   └── controller/ # Classes de contrôle
```

## Auteurs

Nana Claudia

Junior Koudogbo

Thed Arthur 

Rayane Riffay

Ben-Mohammed Marwan

## Licence

Ce projet est réalisé dans le cadre d'un cours universitaire et ne peut être utilisé sans autorisation.
