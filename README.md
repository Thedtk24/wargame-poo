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
./mvnw clean package
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

## Support

Pour signaler un bug ou suggérer une amélioration, veuillez créer une issue sur le dépôt GitHub.

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails. 
