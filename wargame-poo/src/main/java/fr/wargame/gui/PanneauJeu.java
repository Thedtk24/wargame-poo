package fr.wargame.gui;

import fr.wargame.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PanneauJeu extends JPanel {
    private final Partie partie;
    private final List<HexagoneTerrain> hexagones;
    private Unite uniteSelectionnee;
    private Position positionSelectionnee;

    public PanneauJeu(Partie partie) {
        this.partie = partie;
        this.hexagones = new ArrayList<>();
        this.uniteSelectionnee = null;
        this.positionSelectionnee = null;

        initialiserHexagones();
        configurerInteractions();
        setPreferredSize(calculerTaillePanneau());
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

    private void gererClic(Point point) {
        for (HexagoneTerrain hex : hexagones) {
            if (hex.contient(point)) {
                Position pos = hex.getPosition();
                Unite unite = partie.getCarte().getUnite(pos);

                if (uniteSelectionnee == null) {
                    if (unite != null && unite.getJoueur() == partie.getJoueurCourant()) {
                        uniteSelectionnee = unite;
                        positionSelectionnee = pos;
                    }
                } else {
                    if (unite != null && unite.getJoueur() != partie.getJoueurCourant()) {
                        // Attaque
                        if (estAPortee(uniteSelectionnee.getPosition(), pos)) {
                            Combat.resoudre(uniteSelectionnee, unite, hex.getTerrain());
                            uniteSelectionnee.consommerPointsDeplacement(1);
                        }
                    } else if (unite == null && uniteSelectionnee.peutSeDeplacer()) {
                        // Déplacement
                        int coutDeplacement = hex.getTerrain().getPointsDeplacement();
                        if (coutDeplacement > 0 && uniteSelectionnee.getPointsDeplacementRestants() >= coutDeplacement) {
                            partie.getCarte().deplacerUnite(uniteSelectionnee, pos);
                            uniteSelectionnee.consommerPointsDeplacement(coutDeplacement);
                        }
                    }
                    uniteSelectionnee = null;
                    positionSelectionnee = null;
                }
                repaint();
                break;
            }
        }
    }

    private boolean estAPortee(Position pos1, Position pos2) {
        return pos1.distance(pos2) <= 1.5; // Distance pour attaque adjacente
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner les hexagones
        for (HexagoneTerrain hex : hexagones) {
            hex.dessiner(g2d);
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
    }

    private void dessinerUnite(Graphics2D g2d, Unite unite) {
        HexagoneTerrain hex = trouverHexagone(unite.getPosition());
        if (hex != null) {
            Point centre = hex.getCentre();
            int taille = 20;
            
            // Couleur selon le joueur
            g2d.setColor(unite.getJoueur() == 1 ? Color.BLUE : Color.RED);
            g2d.fillOval(centre.x - taille/2, centre.y - taille/2, taille, taille);
            
            // Bordure
            g2d.setColor(Color.BLACK);
            g2d.drawOval(centre.x - taille/2, centre.y - taille/2, taille, taille);
            
            // Points de vie
            g2d.setColor(Color.WHITE);
            String pv = String.valueOf(unite.getPointsDeVie());
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(pv, centre.x - fm.stringWidth(pv)/2, centre.y + fm.getAscent()/2);
        }
    }

    private HexagoneTerrain trouverHexagone(Position position) {
        return hexagones.stream()
                .filter(hex -> hex.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }
} 