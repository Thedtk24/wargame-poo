package fr.wargame.gui;

import fr.wargame.model.Position;
import fr.wargame.model.Terrain;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class HexagoneTerrain {
    private static final int RAYON = 30;
    private final Position position;
    private final Terrain terrain;
    private final Path2D.Double forme;
    private final Point centre;

    public HexagoneTerrain(Position position, Terrain terrain) {
        this.position = position;
        this.terrain = terrain;
        this.centre = calculerCentre();
        this.forme = creerForme();
    }

    private Point calculerCentre() {
        int x = position.getX();
        int y = position.getY();
        
        // Décalage pour les lignes impaires
        int decalageX = (y % 2 == 0) ? 0 : RAYON;
        
        return new Point(
            x * (2 * RAYON) + RAYON + decalageX,
            y * (int)(RAYON * Math.sqrt(3)) + RAYON
        );
    }

    public Path2D.Double creerForme() {
        Path2D.Double path = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * i;
            int x = (int)(centre.x + RAYON * Math.cos(angle));
            int y = (int)(centre.y + RAYON * Math.sin(angle));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }

    public void dessinerFond(Graphics2D g2d) {
        // Sauvegarder l'état du Graphics2D
        AffineTransform oldTransform = g2d.getTransform();
        Shape oldClip = g2d.getClip();
        
        // Définir le clip pour ne dessiner que dans l'hexagone
        g2d.setClip(forme);

        // Dessiner la texture selon le type de terrain
        switch (terrain) {
            case VILLAGE:
                dessinerVillage(g2d);
                break;
            case FORTERESSE:
                dessinerForteresse(g2d);
                break;
            case EAU_PROFONDE:
                dessinerEau(g2d);
                break;
            case PLAINE:
                dessinerPlaine(g2d);
                break;
            case FORET:
                dessinerForet(g2d);
                break;
            case COLLINE:
                dessinerColline(g2d);
                break;
            case MONTAGNE:
                dessinerMontagne(g2d);
                break;
        }

        // Restaurer l'état du Graphics2D
        g2d.setClip(oldClip);
        g2d.setTransform(oldTransform);
        
        // Dessiner la bordure
        g2d.setColor(Color.BLACK);
        g2d.draw(forme);
    }

    private void dessinerVillage(Graphics2D g2d) {
        // Fond gris clair
        g2d.setColor(new Color(220, 220, 220));
        g2d.fill(forme);
        
        // Maisons
        g2d.setColor(new Color(180, 180, 180));
        for (int i = 0; i < 3; i++) {
            double angle = 2.0 * Math.PI / 3 * i;
            int x = (int)(centre.x + RAYON/2 * Math.cos(angle));
            int y = (int)(centre.y + RAYON/2 * Math.sin(angle));
            g2d.fill(new Rectangle2D.Double(x-5, y-5, 10, 10));
        }
    }

    private void dessinerForteresse(Graphics2D g2d) {
        // Fond gris foncé
        g2d.setColor(new Color(120, 120, 120));
        g2d.fill(forme);
        
        // Murs
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new Rectangle2D.Double(centre.x-RAYON/2, centre.y-RAYON/2, RAYON, RAYON));
    }

    private void dessinerEau(Graphics2D g2d) {
        // Fond bleu
        g2d.setColor(new Color(0, 100, 255));
        g2d.fill(forme);
        
        // Vagues
        g2d.setColor(new Color(0, 150, 255));
        for (int i = 0; i < 3; i++) {
            double angle = 2.0 * Math.PI / 3 * i;
            int x = (int)(centre.x + RAYON/2 * Math.cos(angle));
            int y = (int)(centre.y + RAYON/2 * Math.sin(angle));
            g2d.draw(new Ellipse2D.Double(x-3, y-3, 6, 6));
        }
    }

    private void dessinerPlaine(Graphics2D g2d) {
        // Fond vert clair
        g2d.setColor(new Color(150, 255, 150));
        g2d.fill(forme);
        
        // Herbe
        g2d.setColor(new Color(100, 200, 100));
        for (int i = 0; i < 4; i++) {
            double angle = 2.0 * Math.PI / 4 * i;
            int x = (int)(centre.x + RAYON/3 * Math.cos(angle));
            int y = (int)(centre.y + RAYON/3 * Math.sin(angle));
            g2d.draw(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }

    private void dessinerForet(Graphics2D g2d) {
        // Fond vert foncé
        g2d.setColor(new Color(0, 150, 0));
        g2d.fill(forme);
        
        // Arbres
        g2d.setColor(new Color(0, 100, 0));
        for (int i = 0; i < 3; i++) {
            double angle = 2.0 * Math.PI / 3 * i;
            int x = (int)(centre.x + RAYON/2 * Math.cos(angle));
            int y = (int)(centre.y + RAYON/2 * Math.sin(angle));
            g2d.fill(new Ellipse2D.Double(x-4, y-4, 8, 8));
        }
    }

    private void dessinerColline(Graphics2D g2d) {
        // Fond marron clair
        g2d.setColor(new Color(200, 150, 100));
        g2d.fill(forme);
        
        // Relief
        g2d.setColor(new Color(180, 130, 80));
        g2d.fill(new Ellipse2D.Double(centre.x-RAYON/2, centre.y-RAYON/2, RAYON, RAYON/2));
    }

    private void dessinerMontagne(Graphics2D g2d) {
        // Fond gris foncé
        g2d.setColor(new Color(100, 100, 100));
        g2d.fill(forme);
        
        // Sommet enneigé
        g2d.setColor(new Color(220, 220, 220));
        g2d.fill(new Ellipse2D.Double(centre.x-RAYON/3, centre.y-RAYON/3, RAYON/1.5, RAYON/1.5));
    }

    public void dessiner(Graphics2D g2d) {
        // Cette méthode n'est plus utilisée car on utilise l'image de fond
    }

    public boolean contient(Point point) {
        return forme.contains(point);
    }

    public Position getPosition() {
        return position;
    }

    public Point getCentre() {
        return centre;
    }

    public Terrain getTerrain() {
        return terrain;
    }
} 