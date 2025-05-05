# Structure du Projet Wargame

Ce document présente la structure complète du projet Wargame à travers différents diagrammes UML.

## 1. Diagrammes de Classes

### 1.1 Modèle Principal

```plantuml
@startuml
package model {
    class Unite {
        - id: int
        - type: TypeUnite
        - position: Position
        - pointsDeVie: int
        - pointsAttaque: int
        - pointsDefense: int
        - portee: int
        + deplacer(Position)
        + attaquer(Unite)
        + estVivante(): boolean
    }

    class Carte {
        - largeur: int
        - hauteur: int
        - terrains: Terrain[][]
        + getTerrain(Position): Terrain
        + estValide(Position): boolean
    }

    class Partie {
        - joueurs: Joueur[]
        - carte: Carte
        - tourActuel: int
        + initialiser()
        + jouerTour()
        + estTerminee(): boolean
    }

    class Position {
        - x: int
        - y: int
        + distance(Position): int
    }

    class Terrain {
        - type: TypeTerrain
        - coutDeplacement: int
        - bonusDefense: int
    }

    enum TypeUnite {
        INFANTERIE
        CAVALERIE
        ARTILLERIE
    }

    class ZoneVisibilite {
        - unite: Unite
        - casesVisibles: Position[]
        + calculerVisibilite()
    }

    class Combat {
        - attaquant: Unite
        - defenseur: Unite
        + calculerDegats(): int
        + executer()
    }

    Unite "1" -- "1" TypeUnite
    Unite "1" -- "1" Position
    Unite "1" -- "1" ZoneVisibilite
    Carte "1" -- "*" Terrain
    Partie "1" -- "1" Carte
    Combat "1" -- "2" Unite
}
@enduml
```

**Explication :** Le modèle principal contient les classes fondamentales du jeu. La classe `Unite` représente les unités militaires avec leurs caractéristiques et actions. `Carte` gère le terrain de jeu, tandis que `Partie` orchestre le déroulement du jeu. Les autres classes supportent des fonctionnalités spécifiques comme le combat et la visibilité.

### 1.2 Interface Graphique

```plantuml
@startuml
package gui {
    class FenetrePrincipale {
        - panneauJeu: PanneauJeu
        - menu: JMenuBar
        + initialiser()
        + afficherMessage(String)
    }

    class PanneauJeu {
        - carte: Carte
        - hexagones: HexagoneTerrain[][]
        + afficherCarte()
        + mettreAJour()
    }

    class HexagoneTerrain {
        - position: Position
        - terrain: Terrain
        - unite: Unite
        + dessiner(Graphics)
        + estSelectionne(): boolean
    }

    FenetrePrincipale "1" -- "1" PanneauJeu
    PanneauJeu "1" -- "*" HexagoneTerrain
}
@enduml
```

**Explication :** L'interface graphique est construite autour de trois classes principales. `FenetrePrincipale` gère la fenêtre principale du jeu, `PanneauJeu` affiche la carte, et `HexagoneTerrain` représente chaque case hexagonale du terrain.

### 1.3 Intelligence Artificielle

```plantuml
@startuml
package ia {
    class IAJoueur {
        - difficulte: NiveauDifficulte
        - partie: Partie
        + choisirAction(): Action
        + evaluerPosition(): int
        + calculerMeilleurMouvement(): Position
    }

    enum NiveauDifficulte {
        FACILE
        MOYEN
        DIFFICILE
    }

    class Action {
        - type: TypeAction
        - unite: Unite
        - cible: Position
    }

    enum TypeAction {
        DEPLACEMENT
        ATTAQUE
        PASSER
    }

    IAJoueur "1" -- "1" NiveauDifficulte
    IAJoueur "1" -- "*" Action
}
@enduml
```

**Explication :** Le système d'IA est composé de `IAJoueur` qui prend des décisions basées sur la difficulté choisie. Les actions possibles sont définies par l'enum `TypeAction`.

## 2. Diagrammes de Séquence

### 2.1 Déroulement d'une Partie

```plantuml
@startuml
actor Joueur
participant Partie
participant Carte
participant Unite
participant Combat

Joueur -> Partie: initialiser()
Partie -> Carte: creer()
Partie -> Unite: placerUnites()
loop pour chaque joueur
    Partie -> Joueur: jouerTour()
    Joueur -> Unite: deplacer()
    Unite -> Carte: verifierDeplacement()
    alt si combat
        Unite -> Combat: executer()
        Combat -> Unite: calculerDegats()
    end
end
@enduml
```

**Explication :** Ce diagramme montre le flux principal du jeu, de l'initialisation au déroulement des tours, incluant les déplacements et les combats.

### 2.2 Système de Combat

```plantuml
@startuml
participant UniteAttaquante
participant Combat
participant UniteDefenseur
participant Terrain

UniteAttaquante -> Combat: executer()
Combat -> UniteAttaquante: getPointsAttaque()
Combat -> UniteDefenseur: getPointsDefense()
Combat -> Terrain: getBonusDefense()
Combat -> Combat: calculerDegats()
Combat -> UniteDefenseur: subirDegats(int)
@enduml
```

**Explication :** Le système de combat gère les interactions entre unités, prenant en compte les caractéristiques des unités et les bonus du terrain.

## 3. Diagrammes d'État

### 3.1 États d'une Unité

```plantuml
@startuml
state Unite {
    [*] --> Attente
    Attente --> Deplacement : déplacer
    Attente --> Attaque : attaquer
    Deplacement --> Attente : déplacement terminé
    Attaque --> Attente : attaque terminée
    Attente --> [*] : unité détruite
}
@enduml
```

**Explication :** Une unité peut être dans différents états : en attente, en déplacement, en attaque, ou détruite.

## 4. Diagrammes de Composants

```plantuml
@startuml
package "Wargame" {
    [Model] as Model
    [GUI] as GUI
    [IA] as IA
}

Model --> GUI : notifications
GUI --> Model : commandes
IA --> Model : actions
Model --> IA : état du jeu

note right of Model
  Gestion du jeu
  Logique métier
end note

note right of GUI
  Interface utilisateur
  Affichage
end note

note right of IA
  Intelligence artificielle
  Prise de décision
end note
@enduml
```

**Explication :** L'architecture du jeu est divisée en trois composants principaux qui interagissent entre eux : le modèle, l'interface graphique et l'IA.

## 5. Diagramme de Déploiement

```plantuml
@startuml
node "Application" {
    package "wargame.poo" {
        [model]
        [gui]
        [ia]
    }
}

node "Bibliothèques" {
    [JavaFX]
    [JUnit]
}

[model] --> [JavaFX]
[gui] --> [JavaFX]
[ia] --> [JUnit]
@enduml
```

**Explication :** Le diagramme de déploiement montre l'organisation des packages et leurs dépendances avec les bibliothèques externes. 