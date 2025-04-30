# Wargame Tactique Tour par Tour – Projet POO 2025

Ce projet est une implémentation d’un **wargame tactique au tour par tour**, développé en Java dans le cadre du module de Programmation Orientée Objet (IATIC 3). Il simule des batailles entre unités sur un plateau hexagonal, avec une interface graphique Swing et une logique de jeu complète (déplacement, attaque, terrain, IA...).

---

## Objectifs pédagogiques

- Approfondir la programmation orientée objet (héritage, polymorphisme, encapsulation)
- Maîtriser la bibliothèque graphique **Swing**
- Appliquer les principes de conception logicielle (MVC, SOLID)
- Travailler en **équipe pluridisciplinaire**

---

## Fonctionnalités principales

- Plateau de jeu hexagonal avec différents types de terrain
- Cinq types d’unités avec statistiques variées (attaque, défense, portée, déplacement)
- Système de combat au corps à corps ou à distance, influencé par le terrain
- Brouillard de guerre et champ de vision par unité
- Jeu multijoueur local : humains ou IA
- Sauvegarde et chargement de parties
- Interface utilisateur claire avec aide intégrée

---

## Architecture du projet

### Diagramme de classes UML

![Diagramme classe](https://github.com/user-attachments/assets/462afc18-6975-4d2f-bade-e990788536f2)

### Scénarios d'utilisation

- Déplacement d'une unité sur le plateau
- Attaque d'une unité ennemie
- Repos d'une unité blessée
- Victoire par anéantissement de l’ennemi ou survie jusqu’au dernier tour
- Tour d’un joueur IA (comportement simple basé sur la proximité)

---

## Installation et exécution

### Prérequis

Java 17 ou supérieur

Maven 3.x

### Compliation 

```
mvn clean install
```


