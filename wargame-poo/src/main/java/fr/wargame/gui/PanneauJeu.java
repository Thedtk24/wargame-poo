package fr.wargame.gui;

import fr.wargame.model.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class PanneauJeu extends JPanel {
    private final Partie partie;
    private final List<HexagoneTerrain> hexagones;
    private Unite uniteSelectionnee;
    private Position positionSelectionnee;
    private BufferedImage imageFond;
    private BufferedImage imageBrouillard;
    private Set<Position> deplacementsPossibles;
    private final Map<TypeUnite, BufferedImage> imagesUnites;

    public PanneauJeu(Partie partie) {
        this.partie = partie;
        this.hexagones = new ArrayList<>();
        this.uniteSelectionnee = null;
        this.positionSelectionnee = null;
        this.deplacementsPossibles = new HashSet<>();
        this.imagesUnites = new HashMap<>();

        // Charger l'image du brouillard
        try {
            this.imageBrouillard = ImageIO.read(getClass().getResourceAsStream("/images/brouillard.png"));
            
            // Charger les images des unités
            imagesUnites.put(TypeUnite.INFANTERIE, ImageIO.read(getClass().getResourceAsStream("/images/infanterie.png")));
            imagesUnites.put(TypeUnite.INFANTERIE_LOURDE, ImageIO.read(getClass().getResourceAsStream("/images/infanterie_lourde.png")));
            imagesUnites.put(TypeUnite.CAVALERIE, ImageIO.read(getClass().getResourceAsStream("/images/cavalerie.png")));
            imagesUnites.put(TypeUnite.MAGE, ImageIO.read(getClass().getResourceAsStream("/images/mage.png")));
            imagesUnites.put(TypeUnite.ARCHER, ImageIO.read(getClass().getResourceAsStream("/images/archer.png")));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
        }

        initialiserHexagones();
        configurerInteractions();
        setPreferredSize(calculerTaillePanneau());
        genererImageFond();
        
        // Initialiser la visibilité
        partie.initialiserVisibilite();
    }

    private void initialiserHexagones() {
        Carte carte = partie.getCarte();
        for (int y = 0; y < carte.getHauteur(); y++) {
            for (int x = 0; x < carte.getLargeur(); x++) {
                Position pos = new Position(x, y);
                hexagones.add(new HexagoneTerrain(pos, carte.getTerrain(pos)));
            }
        }
    }

    private void configurerInteractions() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gererClic(e.getPoint());
            }
        });
    }

    private Dimension calculerTaillePanneau() {
        int maxX = 0;
        int maxY = 0;
        for (HexagoneTerrain hex : hexagones) {
            Point centre = hex.getCentre();
            maxX = Math.max(maxX, centre.x);
            maxY = Math.max(maxY, centre.y);
        }
        return new Dimension(maxX + 60, maxY + 60);
    }

    private void genererImageFond() {
        Dimension taille = calculerTaillePanneau();
        imageFond = new BufferedImage(taille.width, taille.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imageFond.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner les hexagones sur l'image de fond
        for (HexagoneTerrain hex : hexagones) {
            hex.dessinerFond(g2d);
        }

        g2d.dispose();
    }

    private void calculerDeplacementsPossibles(Position position, int pointsDeplacement) {
        deplacementsPossibles.clear();
        if (pointsDeplacement <= 0) return;

        // Map pour stocker le coût de déplacement pour atteindre chaque position
        Map<Position, Integer> coutsDeplacement = new HashMap<>();
        Set<Position> casesAVisiter = new HashSet<>();
        
        // Initialisation
        casesAVisiter.add(position);
        coutsDeplacement.put(position, 0);

        while (!casesAVisiter.isEmpty()) {
            Set<Position> nouvellesCases = new HashSet<>();
            for (Position pos : casesAVisiter) {
                int coutActuel = coutsDeplacement.get(pos);
                
                // Dans un wargame, on ne peut se déplacer que dans les 6 directions adjacentes
                Position[] directions = {
                    new Position(0, -1),   // Haut
                    new Position(1, -1),   // Haut-droite
                    new Position(1, 0),    // Droite
                    new Position(0, 1),    // Bas
                    new Position(-1, 1),   // Bas-gauche
                    new Position(-1, 0)    // Gauche
                };

                for (Position dir : directions) {
                    Position nouvellePos = new Position(pos.getX() + dir.getX(), pos.getY() + dir.getY());
                    
                    // Vérifier si la position est valide, non occupée, visible et n'est pas de l'eau
                    if (partie.getCarte().estPositionValide(nouvellePos) && 
                        !partie.getCarte().estPositionOccupee(nouvellePos) &&
                        partie.getZoneVisibilite().estVisible(nouvellePos, partie.getJoueurCourant()) &&
                        partie.getCarte().getTerrain(nouvellePos) != Terrain.EAU_PROFONDE) {
                        
                        int coutTerrain = partie.getCarte().getTerrain(nouvellePos).getPointsDeplacement();
                        if (coutTerrain > 0) {
                            int nouveauCout = coutActuel + coutTerrain;
                            
                            // Si le nouveau coût est inférieur au coût précédent ou si c'est une nouvelle position
                            if (nouveauCout <= pointsDeplacement && 
                                (!coutsDeplacement.containsKey(nouvellePos) || nouveauCout < coutsDeplacement.get(nouvellePos))) {
                                
                                coutsDeplacement.put(nouvellePos, nouveauCout);
                                deplacementsPossibles.add(nouvellePos);
                                nouvellesCases.add(nouvellePos);
                            }
                        }
                    }
                }
            }
            casesAVisiter = nouvellesCases;
        }
    }

    private void gererClic(Point point) {
        for (HexagoneTerrain hex : hexagones) {
            if (hex.contient(point)) {
                Position pos = hex.getPosition();
                Unite unite = partie.getCarte().getUnite(pos);

                if (uniteSelectionnee == null) {
                    if (unite != null && unite.getJoueur() == partie.getJoueurCourant()) {
                        uniteSelectionnee = unite;
                        positionSelectionnee = pos;
                        calculerDeplacementsPossibles(pos, unite.getPointsDeplacementRestants());
                    }
                } else {
                    if (unite != null && unite.getJoueur() != partie.getJoueurCourant()) {
                        // Attaque
                        if (estAPortee(uniteSelectionnee.getPosition(), pos)) {
                            Combat.resoudre(uniteSelectionnee, unite, hex.getTerrain());
                            uniteSelectionnee.consommerPointsDeplacement(1);
                            mettreAJourVisibilite();
                        }
                    } else if (unite == null && deplacementsPossibles.contains(pos)) {
                        // Déplacement
                        int coutDeplacement = hex.getTerrain().getPointsDeplacement();
                        if (coutDeplacement > 0 && uniteSelectionnee.getPointsDeplacementRestants() >= coutDeplacement) {
                            partie.getCarte().deplacerUnite(uniteSelectionnee, pos);
                            uniteSelectionnee.consommerPointsDeplacement(coutDeplacement);
                            mettreAJourVisibilite();
                        }
                    }
                    uniteSelectionnee = null;
                    positionSelectionnee = null;
                    deplacementsPossibles.clear();
                }
                repaint();
                break;
            }
        }
    }

    private boolean estAPortee(Position pos1, Position pos2) {
        return pos1.distance(pos2) <= 1.5; // Distance pour attaque adjacente
    }

    private void mettreAJourVisibilite() {
        // Mettre à jour la visibilité pour le joueur courant
        partie.getZoneVisibilite().calculerVisibilitePourJoueur(partie.getJoueurCourant());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner l'image de fond
        if (imageFond != null) {
            g2d.drawImage(imageFond, 0, 0, null);
        }

        // Dessiner les déplacements possibles
        if (uniteSelectionnee != null) {
            g2d.setColor(new Color(173, 216, 230, 80));
            for (Position pos : deplacementsPossibles) {
                HexagoneTerrain hex = trouverHexagone(pos);
                if (hex != null) {
                    g2d.fill(hex.creerForme());
                    g2d.setColor(new Color(173, 216, 230, 150));
                    g2d.draw(hex.creerForme());
                    g2d.setColor(new Color(173, 216, 230, 80));
                }
            }
        }

        // Dessiner les unités
        for (Unite unite : partie.getCarte().getUnites().values()) {
            dessinerUnite(g2d, unite);
        }

        // Mettre en surbrillance l'unité sélectionnée
        if (uniteSelectionnee != null) {
            HexagoneTerrain hex = trouverHexagone(positionSelectionnee);
            if (hex != null) {
                g2d.setColor(new Color(255, 255, 0, 100));
                g2d.fill(hex.creerForme());
            }
        }

        // Dessiner le brouillard
        dessinerBrouillard(g2d);
    }

    private void dessinerUnite(Graphics2D g2d, Unite unite) {
        HexagoneTerrain hex = trouverHexagone(unite.getPosition());
        if (hex != null) {
            Point centre = hex.getCentre();
            int taille = 40; // Taille plus grande pour les images
            
            // Dessiner l'image de l'unité
            BufferedImage imageUnite = imagesUnites.get(unite.getType());
            if (imageUnite != null) {
                // Redimensionner l'image si nécessaire
                Image imageRedimensionnee = imageUnite.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
                
                // Calculer la position pour centrer l'image
                int x = centre.x - taille/2;
                int y = centre.y - taille/2;
                
                // Dessiner l'image
                g2d.drawImage(imageRedimensionnee, x, y, null);
                
                // Ajouter un indicateur d'équipe (petit cercle en haut à droite de l'image)
                int rayonIndicateur = 6;
                int xIndicateur = x + taille - rayonIndicateur - 2;
                int yIndicateur = y + rayonIndicateur + 2;
                
                // Dessiner le cercle de l'équipe
                g2d.setColor(unite.getJoueur() == 1 ? Color.BLUE : Color.RED);
                g2d.fillOval(xIndicateur - rayonIndicateur, yIndicateur - rayonIndicateur, 
                            rayonIndicateur * 2, rayonIndicateur * 2);
                
                // Ajouter une bordure blanche pour le rendre plus visible
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(xIndicateur - rayonIndicateur, yIndicateur - rayonIndicateur, 
                            rayonIndicateur * 2, rayonIndicateur * 2);
                
                // Dessiner les points de vie
                String pv = String.valueOf(unite.getPointsDeVie());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(pv, 
                             centre.x - fm.stringWidth(pv)/2, 
                             centre.y + taille/3 + fm.getAscent()/2);
            } else {
                // Fallback si l'image n'est pas chargée
                g2d.setColor(unite.getJoueur() == 1 ? Color.BLUE : Color.RED);
                g2d.fillOval(centre.x - taille/2, centre.y - taille/2, taille, taille);
                g2d.setColor(Color.WHITE);
                String pv = String.valueOf(unite.getPointsDeVie());
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(pv, centre.x - fm.stringWidth(pv)/2, centre.y + fm.getAscent()/2);
            }
        }
    }

    private HexagoneTerrain trouverHexagone(Position position) {
        return hexagones.stream()
                .filter(hex -> hex.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    private void dessinerBrouillard(Graphics2D g2d) {
        if (imageBrouillard == null) return;

        // Sauvegarder l'état du Graphics2D
        Composite oldComposite = g2d.getComposite();

        for (HexagoneTerrain hex : hexagones) {
            Position pos = hex.getPosition();
            Point centre = hex.getCentre();

            if (!partie.getZoneVisibilite().estVisible(pos, partie.getJoueurCourant())) {
                // Zone non visible : brouillard opaque
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // Dessiner le brouillard
                g2d.drawImage(imageBrouillard,
                    centre.x - HexagoneTerrain.RAYON,
                    centre.y - HexagoneTerrain.RAYON,
                    2 * HexagoneTerrain.RAYON,
                    2 * HexagoneTerrain.RAYON,
                    null);
            }
        }

        // Restaurer l'état du Graphics2D
        g2d.setComposite(oldComposite);
    }
} 