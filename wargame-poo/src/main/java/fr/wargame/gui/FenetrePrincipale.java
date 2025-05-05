package fr.wargame.gui;

import fr.wargame.model.*;
import fr.wargame.ia.IAJoueur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class FenetrePrincipale extends JFrame {
    private static final String SAVE_DIR = "saves";
    private Partie partie;
    private PanneauJeu panneauJeu;
    private JLabel labelJoueur;
    private JLabel labelTour;
    private IAJoueur ia;
    private boolean modeIA;
    private JPanel panneauPrincipal;
    private CardLayout cardLayout;

    public FenetrePrincipale() {
        super("Wargame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Création du CardLayout pour gérer les différents écrans
        cardLayout = new CardLayout();
        panneauPrincipal = new JPanel(cardLayout);
        
        // Création du menu principal
        JPanel menuPanel = creerMenuPrincipal();
        panneauPrincipal.add(menuPanel, "MENU");
        
        // Ajout du panneau principal à la fenêtre
        add(panneauPrincipal);
        
        // Configuration de la fenêtre
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel creerMenuPrincipal() {
        JPanel menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/fond.png"));
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Titre stylisé
        JLabel titre = new JLabel("WARGAME", SwingConstants.CENTER);
        titre.setFont(new Font("Impact", Font.BOLD, 72));
        titre.setForeground(new Color(255, 215, 0)); // Or
        titre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 69, 19), 3), // Marron
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        menuPanel.add(titre, gbc);
        
        // Ajout des boutons du menu
        menuPanel.add(creerBoutonMenu("Nouvelle Partie", this::nouvellePartie), gbc);
        menuPanel.add(creerBoutonMenu("Charger Partie", this::chargerPartie), gbc);
        menuPanel.add(creerBoutonMenu("Règles", this::afficherRegles), gbc);
        menuPanel.add(creerBoutonMenu("Quitter", e -> System.exit(0)), gbc);
        
        return menuPanel;
    }

    private JButton creerBoutonMenu(String texte, ActionListener action) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fond du bouton
                g2d.setColor(new Color(70, 70, 70, 200)); // Gris semi-transparent
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Bordure
                g2d.setColor(new Color(255, 215, 0)); // Or
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Texte
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(texte)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(texte, x, y);
            }
        };
        
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setFont(new Font("Arial", Font.BOLD, 24));
        bouton.setPreferredSize(new Dimension(300, 50));
        bouton.addActionListener(action);
        
        // Effet de survol
        bouton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                bouton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bouton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                bouton.repaint();
            }
        });
        
        return bouton;
    }

    private void nouvellePartie(ActionEvent e) {
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

        // Création des composants du jeu
        JPanel jeuPanel = new JPanel(new GridBagLayout());
        this.panneauJeu = new PanneauJeu(partie);
        this.labelJoueur = new JLabel();
        this.labelTour = new JLabel();
        JButton boutonFinTour = new JButton("Fin du tour");
        JButton boutonMenu = new JButton("Retour au menu");
        JButton boutonSauvegarder = new JButton("Sauvegarder");

        // Centrage du plateau de jeu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        jeuPanel.add(panneauJeu, gbc);

        // Panel d'informations
        JPanel panneauInfo = new JPanel();
        panneauInfo.setLayout(new FlowLayout());
        panneauInfo.add(labelJoueur);
        panneauInfo.add(labelTour);
        panneauInfo.add(boutonFinTour);
        panneauInfo.add(boutonMenu);
        panneauInfo.add(boutonSauvegarder);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.PAGE_END;
        jeuPanel.add(panneauInfo, gbc);

        // Configuration des actions
        boutonFinTour.addActionListener(this::finDeTour);
        boutonMenu.addActionListener(evt -> cardLayout.show(panneauPrincipal, "MENU"));
        boutonSauvegarder.addActionListener(this::sauvegarderPartie);

        // Mise à jour des labels
        mettreAJourLabels();

        // Ajout du panneau de jeu
        panneauPrincipal.add(jeuPanel, "JEU");
        cardLayout.show(panneauPrincipal, "JEU");
    }

    private void afficherRegles(ActionEvent e) {
        String regles = "Règles du Wargame :\n\n" +
            "1. Chaque unité peut se déplacer selon ses points de mouvement\n" +
            "2. Le coût de déplacement dépend du terrain\n" +
            "3. Une unité peut attaquer une unité ennemie adjacente\n" +
            "4. Les dégâts dépendent de l'attaque, de la défense et du terrain\n" +
            "5. Une unité qui ne bouge pas récupère 10% de ses PV\n" +
            "6. La partie se termine quand toutes les unités d'un joueur sont détruites";
        
        JOptionPane.showMessageDialog(this, regles, "Règles du Jeu", JOptionPane.INFORMATION_MESSAGE);
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
            String message;
            if (gagnant == 0) {
                message = "Match nul : aucune unité survivante à la fin du tour " + Partie.TOUR_MAX + ".";
            } else if (partie.getTour() > Partie.TOUR_MAX) {
                if (gagnant == 1) {
                    message = "Victoire du défenseur (Joueur 1) : vous avez résisté jusqu'au tour " + Partie.TOUR_MAX + " !";
                } else {
                    message = "Victoire de l'attaquant (Joueur 2) : le défenseur a été éliminé au dernier tour.";
                }
            } else {
                if (gagnant == 1) {
                    message = "Victoire du défenseur (Joueur 1) : l'armée adverse a été détruite !";
                } else {
                    message = "Victoire de l'attaquant (Joueur 2) : l'armée adverse a été détruite !";
                }
            }
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
                    String message;
                    if (gagnant == 0) {
                        message = "Match nul : aucune unité survivante à la fin du tour " + Partie.TOUR_MAX + ".";
                    } else if (partie.getTour() > Partie.TOUR_MAX) {
                        if (gagnant == 1) {
                            message = "Victoire du défenseur (Joueur 1) : vous avez résisté jusqu'au tour " + Partie.TOUR_MAX + " !";
                        } else {
                            message = "Victoire de l'attaquant (Joueur 2) : le défenseur a été éliminé au dernier tour.";
                        }
                    } else {
                        if (gagnant == 1) {
                            message = "Victoire du défenseur (Joueur 1) : l'armée adverse a été détruite !";
                        } else {
                            message = "Victoire de l'attaquant (Joueur 2) : l'armée adverse a été détruite !";
                        }
                    }
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

    private void sauvegarderPartie(ActionEvent e) {
        // Création du dossier de sauvegarde s'il n'existe pas
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        // Création du nom de fichier avec la date et l'heure
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTime = dateFormat.format(new Date());
        
        // Boîte de dialogue pour le nom de la sauvegarde
        JTextField nomSauvegarde = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nom de la sauvegarde :"));
        panel.add(nomSauvegarde);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Sauvegarder la partie",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nom = nomSauvegarde.getText().trim();
            if (nom.isEmpty()) {
                nom = "Sauvegarde";
            }
            
            // Nettoyage du nom pour éviter les caractères spéciaux
            nom = nom.replaceAll("[^a-zA-Z0-9\\s]", "_");
            
            String fileName = nom + "_" + dateTime + ".sav";
            String filePath = SAVE_DIR + File.separator + fileName;
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(partie);
                oos.writeBoolean(modeIA);
                JOptionPane.showMessageDialog(this, "Partie sauvegardée avec succès !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chargerPartie(ActionEvent e) {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists() || !saveDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Aucune sauvegarde trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupération des fichiers de sauvegarde
        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".sav"));
        if (saveFiles == null || saveFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "Aucune sauvegarde trouvée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Création de la liste des sauvegardes avec leurs dates
        List<String> saveNames = new ArrayList<>();
        for (File file : saveFiles) {
            String fileName = file.getName();
            // Extraction du nom personnalisé (tout ce qui est avant le dernier underscore)
            String customName = fileName.substring(0, fileName.lastIndexOf('_'));
            saveNames.add(customName);
        }

        // Création de la boîte de dialogue de sélection
        JComboBox<String> saveComboBox = new JComboBox<>(saveNames.toArray(new String[0]));
        JPanel panel = new JPanel();
        panel.add(new JLabel("Choisissez une sauvegarde :"));
        panel.add(saveComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Charger une partie",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedSave = (String) saveComboBox.getSelectedItem();
            // Trouver le fichier correspondant au nom sélectionné
            File selectedFile = null;
            for (File file : saveFiles) {
                if (file.getName().startsWith(selectedSave + "_")) {
                    selectedFile = file;
                    break;
                }
            }
            
            if (selectedFile != null) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                    // Charger la partie et le mode IA
                    this.partie = (Partie) ois.readObject();
                    this.modeIA = ois.readBoolean();
                    this.ia = modeIA ? new IAJoueur(partie, 2) : null;

                    // Mettre à jour le panneau de jeu existant
                    panneauJeu.setPartie(partie);
                    panneauJeu.repaint();

                    // Mettre à jour les labels
                    mettreAJourLabels();

                    // Passer à l'écran de jeu
                    cardLayout.show(panneauPrincipal, "JEU");

                    JOptionPane.showMessageDialog(this, "Partie chargée avec succès !", "Chargement", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
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