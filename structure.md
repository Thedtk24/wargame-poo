# Structure du Projet Wargame

Ce document présente la structure complète du projet Wargame à travers différents diagrammes UML.

## 1. Diagrammes de Classes

### 1.1 Modèle Principal

![classes_principales](https://github.com/user-attachments/assets/65953b65-3f41-4981-8aed-487b2d977725)

**Explication :** Le modèle principal contient les classes fondamentales du jeu. La classe `Unite` représente les unités militaires avec leurs caractéristiques et actions. `Carte` gère le terrain de jeu, tandis que `Partie` orchestre le déroulement du jeu. Les autres classes supportent des fonctionnalités spécifiques comme le combat et la visibilité.

### 1.2 Interface Graphique

![classes_interface_graphique](https://github.com/user-attachments/assets/c1bce207-3c61-4e0a-a378-7232753a99b4)

**Explication :** L'interface graphique est construite autour de trois classes principales. `FenetrePrincipale` gère la fenêtre principale du jeu, `PanneauJeu` affiche la carte, et `HexagoneTerrain` représente chaque case hexagonale du terrain.

### 1.3 Intelligence Artificielle

![classes_IA](https://github.com/user-attachments/assets/29f552b5-5980-4851-b244-14dde3656c66)

**Explication :** Le système d'IA est composé de `IAJoueur` qui prend des décisions basées sur la difficulté choisie. Les actions possibles sont définies par l'enum `TypeAction`.

## 2. Diagrammes de Séquence

### 2.1 Déroulement d'une Partie

![seq_deroulement_partie](https://github.com/user-attachments/assets/2fa359e3-cc02-4d04-958e-b24c721a62ce)

**Explication :** Ce diagramme montre le flux principal du jeu, de l'initialisation au déroulement des tours, incluant les déplacements et les combats.

### 2.2 Système de Combat

![seq_systeme_combat](https://github.com/user-attachments/assets/9246ba77-ab4c-4dfd-98dc-496a1b08e999)

**Explication :** Le système de combat gère les interactions entre unités, prenant en compte les caractéristiques des unités et les bonus du terrain.

## 3. Diagrammes d'État

### 3.1 États d'une Unité

![etat_unites](https://github.com/user-attachments/assets/fc4956b2-e5c3-4f0b-b652-45c7b155ec45)

**Explication :** Une unité peut être dans différents états : en attente, en déplacement, en attaque, ou détruite.

## 4. Diagrammes de Composants

![composants](https://github.com/user-attachments/assets/8dbf75ae-bd0b-452d-8d89-c2816a814c48)

**Explication :** L'architecture du jeu est divisée en trois composants principaux qui interagissent entre eux : le modèle, l'interface graphique et l'IA.

## 5. Diagramme de Déploiement

![deploiment](https://github.com/user-attachments/assets/d50ee7c7-494f-489b-ba2d-9622016e67fa)

**Explication :** Le diagramme de déploiement montre l'organisation des packages et leurs dépendances avec les bibliothèques externes. 
