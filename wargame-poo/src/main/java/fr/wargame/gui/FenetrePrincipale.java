package fr.wargame.gui;

import fr.wargame.model.*;
import fr.wargame.ia.IAJoueur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FenetrePrincipale extends JFrame {
    private final Partie partie;
    private final PanneauJeu panneauJeu;
    private final JLabel labelJoueur;
    private final JLabel labelTour;
    private final IAJoueur ia;
    private final boolean modeIA;

    public FenetrePrincipale() {
        super("Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Demander si on veut jouer contre l'IA
        this.modeIA = JOptionPane.showConfirmDialog(null,
            "Voulez-vous jouer contre l'ordinateur ?",
            "Mode de jeu",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        // Création d'une nouvelle partie
        Carte carte = new Carte(10, 8);
        this.partie = new Partie(carte);
        this.ia = modeIA ? new IAJoueur(partie, 2) : null;
        initialiserPartie();

        // Création des composants
        this.panneauJeu = new PanneauJeu(partie);
        this.labelJoueur = new JLabel();
        this.labelTour = new JLabel();
        JButton boutonFinTour = new JButton("Fin du tour");

        // Configuration du layout
        setLayout(new BorderLayout());
        add(panneauJeu, BorderLayout.CENTER);

        // Panel d'informations
        JPanel panneauInfo = new JPanel();
        panneauInfo.setLayout(new FlowLayout());
        panneauInfo.add(labelJoueur);
        panneauInfo.add(labelTour);
        panneauInfo.add(boutonFinTour);
        add(panneauInfo, BorderLayout.SOUTH);

        // Configuration des actions
        boutonFinTour.addActionListener(this::finDeTour);

        // Mise à jour des labels
        mettreAJourLabels();

        // Finalisation de la fenêtre
        pack();
        setLocationRelativeTo(null);
    }

    private void initialiserPartie() {
        // Initialisation des terrains
        for (int y = 0; y < partie.getCarte().getHauteur(); y++) {
            for (int x = 0; x < partie.getCarte().getLargeur(); x++) {
                Position pos = new Position(x, y);
                // Placement aléatoire des terrains
                Terrain terrain = Terrain.values()[(int)(Math.random() * Terrain.values().length)];
                partie.getCarte().setTerrain(pos, terrain);
            }
        }

        // Placement des unités du joueur 1
        placerUnite(new Position(1, 1), TypeUnite.INFANTERIE, 1);
        placerUnite(new Position(2, 1), TypeUnite.ARCHER, 1);
        placerUnite(new Position(1, 2), TypeUnite.CAVALERIE, 1);
        placerUnite(new Position(2, 2), TypeUnite.MAGE, 1);
        placerUnite(new Position(1, 3), TypeUnite.INFANTERIE_LOURDE, 1);

        // Placement des unités du joueur 2
        placerUnite(new Position(8, 6), TypeUnite.INFANTERIE, 2);
        placerUnite(new Position(7, 6), TypeUnite.ARCHER, 2);
        placerUnite(new Position(8, 5), TypeUnite.CAVALERIE, 2);
        placerUnite(new Position(7, 5), TypeUnite.MAGE, 2);
        placerUnite(new Position(8, 4), TypeUnite.INFANTERIE_LOURDE, 2);
    }

    private void placerUnite(Position position, TypeUnite type, int joueur) {
        Unite unite = new Unite(type, position, joueur);
        partie.ajouterUnite(unite);
    }

    private void finDeTour(ActionEvent e) {
        partie.finDeTour();
        mettreAJourLabels();
        panneauJeu.repaint();

        if (partie.partieTerminee()) {
            int gagnant = partie.getJoueurGagnant();
            String message = "Partie terminée ! " + 
                (modeIA ? (gagnant == 1 ? "Vous avez gagné !" : "L'ordinateur a gagné !") 
                       : ("Joueur " + gagnant + " gagne !"));
            JOptionPane.showMessageDialog(this, message, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        // Si c'est au tour de l'IA
        if (modeIA && partie.getJoueurCourant() == 2) {
            // Petite pause pour que le joueur puisse voir ce qui se passe
            Timer timer = new Timer(500, evt -> {
                ia.jouerTour();
                partie.finDeTour();
                mettreAJourLabels();
                panneauJeu.repaint();

                if (partie.partieTerminee()) {
                    int gagnant = partie.getJoueurGagnant();
                    String message = "Partie terminée ! " + 
                        (gagnant == 1 ? "Vous avez gagné !" : "L'ordinateur a gagné !");
                    JOptionPane.showMessageDialog(this, message, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void mettreAJourLabels() {
        if (modeIA) {
            labelJoueur.setText(partie.getJoueurCourant() == 1 ? "À vous de jouer" : "Tour de l'ordinateur");
        } else {
            labelJoueur.setText("Joueur " + partie.getJoueurCourant());
        }
        labelTour.setText("Tour " + partie.getTour());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FenetrePrincipale().setVisible(true);
        });
    }
} 