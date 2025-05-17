# Wargame POO

Un jeu de stratégie au tour par tour développé en Java avec une interface graphique Swing.

## Prérequis

- Java JDK 17 ou supérieur
- Maven 3.6 ou supérieur

### Installation des prérequis

#### Windows
1. Installez Java :
   - Téléchargez le JDK depuis [le site d'Oracle](https://www.oracle.com/java/technologies/downloads/#java17)
   - Exécutez le fichier .exe téléchargé
   - Ajoutez Java au PATH système

2. Installez Maven :
   - Téléchargez Maven depuis [le site officiel](https://maven.apache.org/download.cgi)
   - Décompressez l'archive dans `C:\Program Files\Apache\maven`
   - Ajoutez Maven au PATH : `C:\Program Files\Apache\maven\bin`

#### Linux (Debian/Ubuntu)
```bash
# Installation de Java
sudo apt update
sudo apt install openjdk-17-jdk

# Installation de Maven
sudo apt install maven
```

#### macOS
```bash
# Avec Homebrew
brew install openjdk@17
brew install maven
```

## Installation du jeu

1. Clonez le dépôt :
```bash
git clone https://github.com/Thedtk24/wargame-poo.git
cd wargame-poo
```

2. Compilez le projet :
```bash
# Windows
mvn clean package

# Linux/macOS
mvn clean package
```

## Lancement du jeu

```bash
# Windows
java -jar target\wargame-poo-1.0-SNAPSHOT.jar

# Linux/macOS
java -jar target/wargame-poo-1.0-SNAPSHOT.jar
```

## Comment jouer

### Modes de jeu
- **Mode Solo** : Jouez contre l'ordinateur
- **Mode Deux Joueurs** : Jouez contre un autre joueur sur le même ordinateur

### Contrôles
- **Sélectionner une unité** : Clic gauche sur une de vos unités
- **Déplacer** : Clic gauche sur une case vide
- **Attaquer** : Clic gauche sur une unité ennemie adjacente
- **Fin de tour** : Bouton "Fin du tour"

### Types d'unités
| Unité | Attaque | Défense | Déplacement | Vision | PV | Description |
|-------|---------|---------|-------------|--------|-------|------------|
| Infanterie | 5 | 3 | 6 | 4 | 28 | Unité polyvalente |
| Infanterie lourde | 10 | 10 | 4 | 4 | 38 | Très résistante |
| Cavalerie | 8 | 3 | 8 | 6 | 38 | Grande mobilité |
| Mage | 5 | 1 | 5 | 5 | 24 | Fragile mais puissant |
| Archer | 6 | 2 | 5 | 7 | 33 | Bonne vision |

### Types de terrain
| Terrain | Coût de déplacement | Bonus défense | Description |
|---------|---------------------|---------------|-------------|
| Plaine | 1 | 20% | Terrain standard |
| Forêt | 2 | 40% | Bonne protection |
| Colline | 2 | 50% | Excellente défense |
| Montagne | 3 | 60% | Défense maximale |
| Village | 1 | 40% | Protection moyenne |
| Forteresse | 1 | 60% | Meilleure défense |
| Eau profonde | - | 0% | Infranchissable |

### Détails des terrains

#### 1. Village (Gris clair avec maisons)
- **Apparence** : Fond gris clair avec 3 petites maisons carrées
- **Bonus de défense** : +40%
- **Points de déplacement** : 1
- **Avantages** :
  - Bon bonus de défense
  - Coût de déplacement faible
- **Désavantages** :
  - Peu de couverture visuelle
- **Stratégie** : Idéal pour défendre des unités fragiles comme les mages ou les archers

#### 2. Forteresse (Gris foncé avec murs)
- **Apparence** : Fond gris foncé avec des murs
- **Bonus de défense** : +60%
- **Points de déplacement** : 1
- **Avantages** :
  - Meilleur bonus de défense du jeu
  - Coût de déplacement faible
- **Désavantages** :
  - Peu commun sur la carte
- **Stratégie** : Position clé à contrôler, idéal pour les unités de défense comme l'infanterie lourde

#### 3. Eau profonde (Bleu avec vagues)
- **Apparence** : Fond bleu avec motifs de vagues
- **Bonus de défense** : +0%
- **Points de déplacement** : 0 (infranchissable)
- **Avantages** :
  - Crée des barrières naturelles
- **Désavantages** :
  - Impassable
  - Aucun bonus de défense
- **Stratégie** : Utiliser comme barrière naturelle pour protéger vos flancs

#### 4. Plaine (Vert clair avec herbe)
- **Apparence** : Fond vert clair avec motifs d'herbe
- **Bonus de défense** : +20%
- **Points de déplacement** : 1
- **Avantages** :
  - Coût de déplacement faible
  - Bonne visibilité
- **Désavantages** :
  - Bonus de défense faible
- **Stratégie** : Idéal pour les unités rapides comme la cavalerie

#### 5. Forêt (Vert foncé avec arbres)
- **Apparence** : Fond vert foncé avec 3 arbres
- **Bonus de défense** : +40%
- **Points de déplacement** : 2
- **Avantages** :
  - Bon bonus de défense
  - Couverture visuelle
- **Désavantages** :
  - Coût de déplacement élevé
- **Stratégie** : Parfait pour tendre des embuscades ou protéger des unités fragiles

#### 6. Colline (Marron clair avec relief)
- **Apparence** : Fond marron clair avec relief
- **Bonus de défense** : +50%
- **Points de déplacement** : 2
- **Avantages** :
  - Très bon bonus de défense
  - Avantage de hauteur
- **Désavantages** :
  - Coût de déplacement élevé
- **Stratégie** : Position stratégique pour contrôler la zone

#### 7. Montagne (Gris foncé avec neige)
- **Apparence** : Fond gris foncé avec sommet enneigé
- **Bonus de défense** : +60%
- **Points de déplacement** : 3
- **Avantages** :
  - Meilleur bonus de défense du jeu
  - Excellente couverture
- **Désavantages** :
  - Coût de déplacement très élevé
- **Stratégie** : Difficile d'accès mais offre une excellente position défensive

### Mécaniques de déplacement détaillées

#### Règles générales
- Chaque unité a un nombre limité de points de déplacement par tour
- Le coût de déplacement dépend du terrain
- Les unités ne peuvent pas traverser les cases d'eau profonde
- Les unités ne peuvent pas se déplacer sur une case occupée par une autre unité

#### Exemples de déplacements
- Une unité avec 4 points de déplacement peut :
  - Se déplacer de 4 cases en plaine (1 point par case)
  - Se déplacer de 2 cases en forêt (2 points par case)
  - Se déplacer d'une seule case en montagne (3 points par case)
  - Ne peut pas traverser l'eau profonde

### Stratégie avancée

#### Positionnement défensif
- Utilisez les terrains avec un bonus de défense élevé (forteresse, montagne) pour vos unités fragiles
- Les forêts et collines offrent un bon compromis entre défense et mobilité
- Évitez de laisser vos unités en plaine sans protection

#### Positionnement offensif
- Les plaines sont idéales pour les unités rapides
- Utilisez les forêts pour approcher discrètement l'ennemi
- Les collines offrent une bonne position pour lancer des attaques

#### Contrôle du terrain
- Contrôlez les forteresses et les villages pour leurs bonus de défense
- Utilisez l'eau profonde comme barrière naturelle
- Les montagnes peuvent servir de points de contrôle stratégiques

### Règles du jeu
1. Chaque unité peut se déplacer selon ses points de mouvement
2. Le coût de déplacement dépend du terrain
3. Une unité peut attaquer une unité ennemie adjacente
4. Les dégâts dépendent de l'attaque, de la défense et du terrain
5. Une unité qui ne bouge pas récupère 10% de ses PV
6. La partie se termine quand toutes les unités d'un joueur sont détruites

### Conseils stratégiques
- Utilisez le terrain à votre avantage
- Protégez vos unités faibles derrière les plus résistantes
- Faites reposer les unités blessées pour récupérer des PV
- La cavalerie est excellente pour contourner l'ennemi
- Les archers sont plus efficaces sur les collines

## Problèmes connus

### Windows
- Si le jeu ne se lance pas, vérifiez que Java est bien dans le PATH
- En cas d'erreur "Unable to access jarfile", utilisez le chemin complet du fichier .jar

### Linux
- Si l'interface graphique ne s'affiche pas, vérifiez que vous avez un environnement de bureau installé
- En cas d'erreur de permissions : `chmod +x mvnw`

### macOS
- Sur les Mac M1/M2, utilisez la version ARM64 de Java
- En cas de problème de sécurité, autorisez l'application dans Préférences Système > Sécurité

### WSL (Windows Subsystem for Linux)
- Si le clonage du dépôt échoue avec une erreur de "safe directory", utilisez :
`git config --global --add safe.directory <chemin_du_projet>`

- Si le build Maven échoue avec "no POM in this directory", assurez-vous d’être dans le dossier contenant pom.xml

- Si le jeu ne se lance pas et affiche une erreur HeadlessException, installez Java avec interface graphique via :
`sudo apt install openjdk-21-jdk`

- Si vous avez installé une version headless, supprimez-la avec :
`sudo apt remove openjdk-*-headless`

  Vérifiez que la variable $DISPLAY est bien définie (echo $DISPLAY) et que WSLg est actif pour l'affichage graphique

- Si l’erreur "Unable to access jarfile" apparaît, vérifiez que le .jar a bien été généré dans le dossier target/ après le `mvn clean package`

## Support

Pour signaler un bug ou suggérer une amélioration, veuillez créer une issue sur le dépôt GitHub.

## Auteurs

- Junior K 
- Thed T
- Claudia N
- Rayane R
- Marwan B
