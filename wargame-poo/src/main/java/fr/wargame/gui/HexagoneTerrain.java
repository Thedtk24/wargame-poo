package fr.wargame.gui;

import fr.wargame.model.Position;
import fr.wargame.model.Terrain;

import java.awt.*;
import java.awt.geom.Path2D;

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

    public void dessiner(Graphics2D g2d) {
        // Couleur de remplissage selon le type de terrain
        g2d.setColor(getCouleurTerrain());
        g2d.fill(forme);
        
        // Bordure
        g2d.setColor(Color.BLACK);
        g2d.draw(forme);
    }

    private Color getCouleurTerrain() {
        return switch (terrain) {
            case VILLAGE -> new Color(200, 200, 200);
            case FORTERESSE -> new Color(150, 150, 150);
            case EAU_PROFONDE -> new Color(0, 100, 255);
            case PLAINE -> new Color(150, 255, 150);
            case FORET -> new Color(0, 150, 0);
            case COLLINE -> new Color(200, 150, 100);
            case MONTAGNE -> new Color(100, 100, 100);
        };
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