/* FICHIER MENUS.JAVA :
 *      - MENU PRINCIPAL
 *      - MENU JOUER
 *      - MENU AJOUTER_QUESTIONS
 *      - MENU AJOUTER_PERSONNAGE
 *      - MENU OPTIONS
 *      - MENU CRÉDITS
 *      - QUITTER
 * 
 *  DERNIÈRE MÀJ : 03/04/2018 par ROMAIN MONIER
 *  CRÉÉ PAR ROMAIN MONIER
 *  2017/2018
 * ------------------------------------------
 *  INFOS :
 *      - CLASSE DE VUE
 *      - Menus
 *      - Gestion des événements
 * ------------------------------------------
 */

package devinetor;
 
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.HashMap;

import javax.swing.ImageIcon;

import java.awt.event.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/** Menus
 * @author Romain MONIER
 */
public class Menus extends JFrame implements MouseListener, ActionListener
{
    private Bouton bouton_valider_perso_liste, bouton_jsp, bouton_oui, bouton_non, bouton_jouer, bouton_add_question, bouton_add_perso, bouton_options, bouton_credits, bouton_quitter, bouton_retour, bouton_son, bouton_musique, bouton_valider_question, bouton_valider_perso;
    private BaseDeDonnees bdd;
    private Audio musique;
    private JTextField texte_new_perso;
    private JTextArea texte_new_question, zone_question, nom_perso, zone_devinetor;
    private ImageIcon img_loading, img_musique_on, img_musique_on_ok, img_musique_off, img_musique_off_ok, img_son_on, img_son_on_ok, img_son_off, img_son_off_ok;
    private JLabel label_img_loading;
    private Point pos_img_loading, pos_jlist;
    private Timer chargement;
    private Thread thread;
    private JScrollPane scroll;
    private DefaultListModel<String> model_perso, model_question;
    private JList<String> jliste_perso, jliste_question;
    private HashMap<Integer, String> liste_perso, liste_question;
    private int current_id_question, result_req_ajout_perso;
    private boolean fin_analyse, pass_fin_analyse, add_perso_jouer, trouve;
    private String texte_id_final;
    
	/**
	 * Constructeur
     * @author Romain MONIER, Mathieu LAPARRA
     * @param bdd Adresse Base de Données
     * @param musique Audio
	*/
    public Menus(BaseDeDonnees bdd, Audio musique) throws IOException
    {
        this.setTitle("DEVINETOR");
        this.setSize(750, 900);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(ImageIO.read(Devinetor.getResourceStream("img/icones/icone.png")));
        
        this.bdd = bdd;
        
        this.musique = musique;
        
        if(Parametres.isMusicActive())
            musique.jouer();
        
        liste_question = bdd.recup_questions();
        liste_perso = bdd.recup_persos();
        
        Analyse.init(bdd, liste_perso, liste_question);
        
        add_perso_jouer = false;
        trouve = false;
    }
    
	/**
	 * Menu principal
     * @author Romain MONIER
	*/
    public void principal() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        ImageIcon img_principal, img_titre;
        JLabel label_img_principal, label_img_titre;
        Point pos_img_principal, pos_img_titre;
        
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/principal.jpg")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_titre = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/titre.png")));
        label_img_titre = new JLabel(img_titre);
        
        pos_img_titre = new Point((cont_full.getWidth() / 2) - (img_titre.getIconWidth() / 2),0);
        label_img_titre.setBounds(pos_img_titre.x, pos_img_titre.y, img_titre.getIconWidth(), img_titre.getIconHeight());
        
        bouton_jouer = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_jouer.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_jouer_ok.gif"))), new Point(50, 200));
        
        bouton_add_perso = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_add_perso.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_add_perso_ok.gif"))), new Point(50, 310));
        
        bouton_add_question = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_add_question.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_add_question_ok.gif"))), new Point(50, 420));
        
        bouton_options = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_options.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_options_ok.gif"))), new Point(50, 530));
        
        bouton_credits = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_credits.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_credits_ok.gif"))), new Point(50, 640));
        
        bouton_quitter = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_quitter.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_quitter_ok.gif"))), new Point(50, 750));
        
        /*  AJOUT DES LISTENERS */
        
        bouton_quitter.addMouseListener(this);
        bouton_credits.addMouseListener(this);
        bouton_options.addMouseListener(this);
        bouton_add_perso.addMouseListener(this);
        bouton_add_question.addMouseListener(this);
        bouton_jouer.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        bouton_quitter.setOpaque(false);
        bouton_credits.setOpaque(false);
        bouton_options.setOpaque(false);
        bouton_add_perso.setOpaque(false);
        bouton_add_question.setOpaque(false);
        bouton_jouer.setOpaque(false);
        
        cont_front.add(bouton_quitter);
        cont_front.add(bouton_credits);
        cont_front.add(bouton_options);
        cont_front.add(bouton_add_perso);
        cont_front.add(bouton_add_question);
        cont_front.add(bouton_jouer);
        cont_front.add(label_img_titre);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
	/**
	 * Menu Jouer
     * @author Romain MONIER
	*/
    public void jouer() throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException
    {
        ImageIcon img_principal, img_devinetor;
        JLabel label_img_principal, label_img_devinetor;
        Point pos_img_principal, pos_img_devinetor, pos_zone_question, pos_bouton_oui, pos_bouton_non, pos_bouton_jsp, pos_nom_perso, pos_zone_devinetor;
        Font font = Font.createFont(Font.TRUETYPE_FONT, Devinetor.getResourceStream("font/FUTURA MEDIUM BT.TTF")).deriveFont(Font.PLAIN, 40);
        Font font_big = font.deriveFont(Font.BOLD, 80);
        Font font_little = font.deriveFont(Font.PLAIN, 20);
        
        fin_analyse = false;
        pass_fin_analyse = false;
        trouve = false;
        add_perso_jouer = true; // savoir si on va pouvoir remplir les réponses aux questions si on ajoute un perso a la fin
        
        Analyse.reset();
        texte_id_final = "";
        
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/jouer.jpg")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_devinetor = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/devinetor.png")));
        label_img_devinetor = new JLabel(img_devinetor);
        
        pos_img_devinetor = new Point(0, 50);
        label_img_devinetor.setBounds(pos_img_devinetor.x, pos_img_devinetor.y, img_devinetor.getIconWidth(), img_devinetor.getIconHeight());
        
        bouton_retour = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_fleche.png"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_fleche_ok.png"))), new Point(0, 0));
        
        pos_bouton_oui = new Point(50, 650);
        pos_bouton_non = new Point(415, 650);
        pos_bouton_jsp = new Point(165, 750);
        
        bouton_oui = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_oui.png"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_oui_ok.png"))), pos_bouton_oui);
        bouton_non = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_non.png"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_non_ok.png"))), pos_bouton_non);
        bouton_jsp = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_jsp.png"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_jsp_ok.png"))), pos_bouton_jsp);
        bouton_jsp.setSonClick(new Audio("audio/snd/ah.wav", false));
        
        /*  ------------------ CHARGEMENT DES ELEMENTS D'AJOUT PERSONNAGE POUR LE CAS OU LE PERSO N'EST PAS TROUVE ------------------------ */
        
        img_loading = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/icones/loading.gif")));
        label_img_loading = new JLabel(img_loading);
        label_img_loading.setVisible(false);
        label_img_loading.setOpaque(false);
        scroll = new JScrollPane();
        
        pos_img_loading = new Point((cont_full.getWidth() / 2) - (img_loading.getIconWidth() / 2) - 15, (cont_full.getHeight() / 2));
        label_img_loading.setBounds(pos_img_loading.x, pos_img_loading.y, img_loading.getIconWidth(), img_loading.getIconHeight());
        
        bouton_valider_perso = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider_ok.gif"))), new Point(225, 450));
        bouton_valider_perso.setVisible(false);
        bouton_valider_perso_liste = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider_ok.gif"))), new Point(225, 750));
        bouton_valider_perso_liste.setVisible(false);
        
        /*  AJOUT DE LA ZONE DE TEXTE   */
        
        texte_new_perso = new JTextField();
        texte_new_perso.setBackground(new Color(210,180,140));
        texte_new_perso.setBounds(220, 350, 300, 100);
        texte_new_perso.setVisible(false);
        
        /*  AJOUT DE LA LISTE DE QUESTIONS  */
        
        model_perso = new DefaultListModel<String>();
        for(String val: liste_perso.values())
            model_perso.addElement(val);
        jliste_perso = new JList<String>(model_perso);
        jliste_perso.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jliste_perso.setLayoutOrientation(JList.VERTICAL);
        
        scroll.setViewportView(jliste_perso);
        pos_jlist = new Point(220, (cont_full.getHeight() / 2) - (scroll.getHeight()) + 150);
        
        scroll.setBounds(pos_jlist.x, pos_jlist.y, 300, 130);
        scroll.setVisible(false);
        
        /*  AJOUT DES LISTENERS  */
        
        bouton_valider_perso.addMouseListener(this);
        bouton_valider_perso_liste.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        texte_new_perso.setOpaque(true);
        texte_new_perso.setFont(font);
        bouton_valider_perso.setOpaque(false);
        bouton_valider_perso_liste.setOpaque(false);
        
        /* --------------------------------------------------------------------------------------------------------------------------------------------------- */
        
        /*  AFFICHAGE PERSONNAGE */
        
        nom_perso = new JTextArea();
        nom_perso.setFont(font_big);
        nom_perso.setWrapStyleWord(true);
        nom_perso.setLineWrap(true);
        nom_perso.setEditable(false);
        nom_perso.setFocusable(false);
        nom_perso.setForeground(Color.BLACK);
        nom_perso.setBackground(new Color(210,180,140));
        pos_nom_perso = new Point(20, 350);
        nom_perso.setBounds(pos_nom_perso.x, pos_nom_perso.y, 700, 300);
        
        /*  AFFICHAGE QUESTIONS */
        
        zone_question = new JTextArea();
        zone_question.setFont(font);
        zone_question.setWrapStyleWord(true);
        zone_question.setLineWrap(true);
        zone_question.setEditable(false);
        zone_question.setFocusable(false);
        zone_question.setForeground(Color.BLACK);
        zone_question.setBackground(new Color(210,180,140));
        pos_zone_question = new Point(20, 350);
        zone_question.setBounds(pos_zone_question.x, pos_zone_question.y, 700, 300);
        
        current_id_question = Analyse.getNextQuestionID(); // ANALYSE DE LA PREMIERE QUESTION A POSER
        zone_question.setText(liste_question.get(current_id_question));
            
        /*  AFFICHAGE TEXTE DEVINETOR */
        
        zone_devinetor = new JTextArea("Salut ! Je suis Devinetor et mon objectif est de trouver à qui / quoi tu penses ! Il suffit de répondre aux questions ! C'est parti !");
        zone_devinetor.setFont(font_little);
        zone_devinetor.setWrapStyleWord(true);
        zone_devinetor.setLineWrap(true);
        zone_devinetor.setEditable(false);
        zone_devinetor.setFocusable(false);
        zone_devinetor.setForeground(Color.BLACK);
        pos_zone_devinetor = new Point(365, 88);
        zone_devinetor.setBounds(pos_zone_devinetor.x, pos_zone_devinetor.y, 300, 120);

        /*  AJOUT DES LISTENERS */
        
        bouton_retour.addMouseListener(this);
        bouton_oui.addMouseListener(this);
        bouton_non.addMouseListener(this);
        bouton_jsp.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        bouton_retour.setOpaque(false);
        bouton_oui.setOpaque(false);
        bouton_non.setOpaque(false);
        bouton_jsp.setOpaque(false);
        zone_question.setOpaque(true);
        nom_perso.setOpaque(true);
        zone_devinetor.setOpaque(false);
        
        nom_perso.setVisible(false);
        
        cont_front.add(label_img_loading);
        cont_front.add(bouton_retour);
        cont_front.add(bouton_jsp);
        cont_front.add(bouton_oui);
        cont_front.add(bouton_non);
        cont_front.add(nom_perso);
        cont_front.add(scroll);
        cont_front.add(texte_new_perso);
        cont_front.add(bouton_valider_perso);
        cont_front.add(bouton_valider_perso_liste);
        cont_front.add(zone_question);
        cont_front.add(zone_devinetor);
        cont_front.add(label_img_devinetor);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
	/**
	 * Menu Ajouter Question
     * @author Romain MONIER, Mathieu LAPARRA
	*/
    public void ajouter_question() throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException
    {
        ImageIcon img_principal, img_titre;
        JLabel label_img_principal, label_img_titre;
        Point pos_img_principal, pos_img_titre;
        Font font = Font.createFont(Font.TRUETYPE_FONT, Devinetor.getResourceStream("font/FUTURA MEDIUM BT.TTF")).deriveFont(Font.BOLD, 20);;
        scroll = new JScrollPane();
        trouve = false;
		
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/add_question.jpg")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_titre = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/titre_add_question.png")));
        label_img_titre = new JLabel(img_titre);
        
        pos_img_titre = new Point((cont_full.getWidth() / 2) - (img_titre.getIconWidth() / 2),0);
        label_img_titre.setBounds(pos_img_titre.x, pos_img_titre.y, img_titre.getIconWidth(), img_titre.getIconHeight());
        
        img_loading = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/icones/loading.gif")));
        label_img_loading = new JLabel(img_loading);
        label_img_loading.setVisible(false);
        label_img_loading.setOpaque(false);
        
        pos_img_loading = new Point((cont_full.getWidth() / 2) - (img_loading.getIconWidth() / 2) - 15, (cont_full.getHeight() / 2));
        label_img_loading.setBounds(pos_img_loading.x, pos_img_loading.y, img_loading.getIconWidth(), img_loading.getIconHeight());
        
        bouton_valider_question = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider_ok.gif"))), new Point(225, 300));
        
        bouton_retour = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_ok.gif"))), new Point(225, 750));
        
        /*  AJOUT DE LA ZONE DE TEXTE   */
        
        texte_new_question = new JTextArea();  
        texte_new_question.setLineWrap(true);
        texte_new_question.setBackground(new Color(210,180,140));
        texte_new_question.setBounds(220, 200, 300, 100);
        
        /*  AJOUT DE LA LISTE DE QUESTIONS  */
        
        model_question = new DefaultListModel<String>();
        for(String val: liste_question.values())
            model_question.addElement(val);
        jliste_question = new JList<String>(model_question);
        jliste_question.setLayoutOrientation(JList.VERTICAL);
        
        scroll.setViewportView(jliste_question);
        pos_jlist = new Point(220, (cont_full.getHeight() / 2) - (scroll.getHeight()));
        
        scroll.setBounds(pos_jlist.x, pos_jlist.y, 300, 280);
        
        /*  AJOUT DES LISTENERS */
        
        bouton_valider_question.addMouseListener(this);
        bouton_retour.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        texte_new_question.setOpaque(true);
        texte_new_question.setFont(font);
        bouton_valider_question.setOpaque(false);
        bouton_retour.setOpaque(false);
        
        cont_front.add(label_img_loading);
        cont_front.add(scroll);
        cont_front.add(texte_new_question);
        cont_front.add(bouton_valider_question);
        cont_front.add(bouton_retour);
        cont_front.add(label_img_titre);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
	/**
	 * Menu Ajouter Personnage
     * @author Romain MONIER, Mathieu LAPARRA
	*/
    public void ajouter_personnage() throws UnsupportedAudioFileException, IOException, LineUnavailableException, FontFormatException
    {
        ImageIcon img_principal, img_titre;
        JLabel label_img_principal, label_img_titre;
        Point pos_img_principal, pos_img_titre;
        Font font = Font.createFont(Font.TRUETYPE_FONT, Devinetor.getResourceStream("font/FUTURA MEDIUM BT.TTF")).deriveFont(Font.BOLD, 40);
        scroll = new JScrollPane();
        
        trouve = false;
        add_perso_jouer = false; // savoir si on va pouvoir remplir les réponses aux questions si on ajoute un perso a la fin (en l'occurrence ici false car on n'est pas dans jouer)
        
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/add_perso.jpg")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_titre = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/titre_add_perso.png")));
        label_img_titre = new JLabel(img_titre);
        
        pos_img_titre = new Point((cont_full.getWidth() / 2) - (img_titre.getIconWidth() / 2),0);
        label_img_titre.setBounds(pos_img_titre.x, pos_img_titre.y, img_titre.getIconWidth(), img_titre.getIconHeight());
        
        img_loading = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/icones/loading.gif")));
        label_img_loading = new JLabel(img_loading);
        label_img_loading.setVisible(false);
        label_img_loading.setOpaque(false);
        
        pos_img_loading = new Point((cont_full.getWidth() / 2) - (img_loading.getIconWidth() / 2) - 15, (cont_full.getHeight() / 2));
        label_img_loading.setBounds(pos_img_loading.x, pos_img_loading.y, img_loading.getIconWidth(), img_loading.getIconHeight());
        
        bouton_valider_perso = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_valider_ok.gif"))), new Point(225, 300));
        
        bouton_retour = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_ok.gif"))), new Point(225, 750));
        
        /*  AJOUT DE LA ZONE DE TEXTE   */
        
        texte_new_perso = new JTextField();
        texte_new_perso.setBackground(new Color(210,180,140));
        texte_new_perso.setBounds(220, 200, 300, 100);
        
        /*  AJOUT DE LA LISTE DE QUESTIONS  */
        
        model_perso = new DefaultListModel<String>();
        for(String val: liste_perso.values())
            model_perso.addElement(val);
        jliste_perso = new JList<String>(model_perso);
        jliste_perso.setLayoutOrientation(JList.VERTICAL);
        
        scroll.setViewportView(jliste_perso);
        pos_jlist = new Point(220, (cont_full.getHeight() / 2) - (scroll.getHeight()));
        
        scroll.setBounds(pos_jlist.x, pos_jlist.y, 300, 280);
        
        /*  AJOUT DES LISTENERS */
        
        bouton_valider_perso.addMouseListener(this);
        bouton_retour.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        texte_new_perso.setOpaque(true);
        texte_new_perso.setFont(font);
        bouton_valider_perso.setOpaque(false);
        bouton_retour.setOpaque(false);
        
        cont_front.add(label_img_loading);
        cont_front.add(scroll);
        cont_front.add(texte_new_perso);
        cont_front.add(bouton_valider_perso);
        cont_front.add(bouton_retour);
        cont_front.add(label_img_titre);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
	/**
	 * Menu Crédits
     * @author Romain MONIER, Mathieu LAPARRA
	*/
    public void credits() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        ImageIcon img_principal, img_titre;
        JLabel label_img_principal, label_img_titre;
        Point pos_img_principal, pos_img_titre;
        
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/credits.jpg")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_titre = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/titre_credits.png")));
        label_img_titre = new JLabel(img_titre);
        
        pos_img_titre = new Point((cont_full.getWidth() / 2) - (img_titre.getIconWidth() / 2),0);
        label_img_titre.setBounds(pos_img_titre.x, pos_img_titre.y, img_titre.getIconWidth(), img_titre.getIconHeight());
        
        bouton_retour = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_ok.gif"))), new Point(50, 750));
        
        /*  AJOUT DES LISTENERS */
        
        bouton_retour.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        bouton_retour.setOpaque(false);
        
        cont_front.add(bouton_retour);
        cont_front.add(label_img_titre);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
	/**
	 * Menu Options
     * @author Romain MONIER, Mathieu LAPARRA
	*/
    public void options() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        ImageIcon img_principal, img_titre;
        JLabel label_img_principal, label_img_titre;
        Point pos_img_principal, pos_img_titre;
        
        /*  MISE EN PLACE DES CONTAINERS    */
        
        JLayeredPane cont_full = new JLayeredPane();
        JLayeredPane cont_front = new JLayeredPane();
        
        /*  CHARGEMENT DES IMAGES AVEC LEUR POSITION   */
        
        img_principal = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/options.png")));
        label_img_principal = new JLabel(img_principal);
        
        pos_img_principal = new Point(0,0);
        
        cont_full.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        cont_front.setBounds(pos_img_principal.x, pos_img_principal.y, this.getWidth(), this.getHeight());
        
        label_img_principal.setBounds(pos_img_principal.x, pos_img_principal.y, img_principal.getIconWidth(), img_principal.getIconHeight());
        
        img_titre = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/backgrounds/titre_options.png")));
        label_img_titre = new JLabel(img_titre);
        
        pos_img_titre = new Point((cont_full.getWidth() / 2) - (img_titre.getIconWidth() / 2),0);
        label_img_titre.setBounds(pos_img_titre.x, pos_img_titre.y, img_titre.getIconWidth(), img_titre.getIconHeight());
        
        img_musique_on = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_musique_on.gif")));
        img_musique_on_ok = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_musique_on_ok.gif")));
        img_musique_off = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_musique_off.gif")));
        img_musique_off_ok = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_musique_off_ok.gif")));
        img_son_on = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_son_on.gif")));
        img_son_on_ok = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_son_on_ok.gif")));
        img_son_off = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_son_off.gif")));
        img_son_off_ok = new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_son_off_ok.gif")));
        
        if(Parametres.isMusicActive())
            bouton_musique = new Bouton(img_musique_on, img_musique_on_ok, new Point(50, 200));
        else
            bouton_musique = new Bouton(img_musique_off, img_musique_off_ok, new Point(50, 200));
        
        if(Parametres.isSoundActive())
            bouton_son = new Bouton(img_son_on, img_son_on_ok, new Point(50, 310));
        else
            bouton_son = new Bouton(img_son_off, img_son_off_ok, new Point(50, 310));
        
        bouton_retour = new Bouton(new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour.gif"))), new ImageIcon(ImageIO.read(Devinetor.getResourceStream("img/boutons/bouton_retour_ok.gif"))), new Point(50, 750));
        
        /*  AJOUT DES LISTENERS */
        
        bouton_musique.addMouseListener(this);
        bouton_son.addMouseListener(this);
        bouton_retour.addMouseListener(this);
        
        /*  EMBRIQUER & PLACER LES CONTAINERS    */  
        
        bouton_musique.setOpaque(false);
        bouton_son.setOpaque(false);
        bouton_retour.setOpaque(false);
        
        cont_front.add(bouton_musique);
        cont_front.add(bouton_son);
        cont_front.add(bouton_retour);
        cont_front.add(label_img_titre);
        
        cont_full.add(cont_front, 2);
        cont_full.add(label_img_principal, 1);
        
        cont_full.setLocation(pos_img_principal);
        
        this.setContentPane(cont_full);
    }
    
    /*  EVENEMENTS  */
    
	/**
	 * Event click
     * @author Romain MONIER
     * @param event l'event
	*/
    public void mouseClicked(MouseEvent event)
    {
        try
        {
            if(SwingUtilities.isLeftMouseButton(event))
            {
                /*  MENUS GENERAUX  */
                
                if(event.getSource() == bouton_quitter){
                    bdd.close();
                    System.exit(0);
                }
                else if(event.getSource() == bouton_credits){
                    this.credits();
                }
                else if(event.getSource() == bouton_options){
                    this.options();
                }
                else if(event.getSource() == bouton_add_perso){
                    this.ajouter_personnage();
                }
                else if(event.getSource() == bouton_add_question){
                    this.ajouter_question();
                }
                else if(event.getSource() == bouton_jouer){
                    this.jouer();
                }
                
                else if(event.getSource() == bouton_son)
                {
                    Parametres.setSon(!Parametres.isSoundActive());
                    
                    if(Parametres.isSoundActive())
                        bouton_son.setImage(img_son_on, img_son_on_ok);
                    else
                        bouton_son.setImage(img_son_off, img_son_off_ok);
                }
                
                else if(event.getSource() == bouton_musique)
                {
                    Parametres.setMusique(!Parametres.isMusicActive(), musique);
                    if(Parametres.isMusicActive())
                        bouton_musique.setImage(img_musique_on, img_musique_on_ok);
                    else
                        bouton_musique.setImage(img_musique_off, img_musique_off_ok);
                    
                }
                
                else if(event.getSource() == bouton_valider_question)
                {
                    String texte = texte_new_question.getText();
                    label_img_loading.setVisible(true);
                    bouton_retour.setVisible(false);
                    bouton_valider_question.setVisible(false);
                    scroll.setVisible(false);
                    texte_new_question.setVisible(false);
                    chargement = new Timer(50, this);
                    chargement.start();
                    
                    model_question.addElement(texte);
                    thread = new Thread(() -> bdd.ajout_question(texte, liste_question)); // on envoi l'appel en background pour ne pas bloquer le thread principal
                    thread.start();
                }
                
                else if(event.getSource() == bouton_valider_perso)
                {
                    label_img_loading.setVisible(true);
                    bouton_retour.setVisible(false);
                    String texte;
                    if(add_perso_jouer){ // si on est dans jouer
                        texte_id_final = texte_new_perso.getText();
                        texte = texte_id_final;
                        zone_devinetor.setText("J'ajoute ça à ma base de connaissances...");
                        bouton_valider_perso_liste.setVisible(false);
                    }
                    else{
                        texte = texte_new_perso.getText();
                    }
                    bouton_valider_perso.setVisible(false);
                    texte_new_perso.setVisible(false);
                    scroll.setVisible(false);
                    chargement = new Timer(50, this);
                    chargement.start();
                    
                    model_perso.addElement(texte);
                    thread = new Thread(() -> result_req_ajout_perso = bdd.ajout_perso(texte, liste_perso, add_perso_jouer)); // on envoi l'appel en background pour ne pas bloquer le thread principal
                    thread.start();
                }
                
                else if(event.getSource() == bouton_valider_perso_liste)
                {
                    zone_devinetor.setText("J'ajoute ça à ma base de connaissances...");
                    texte_id_final = jliste_perso.getSelectedValue();
                    label_img_loading.setVisible(true);
                    bouton_retour.setVisible(false);
                    bouton_valider_perso.setVisible(false);
                    bouton_valider_perso_liste.setVisible(false);
                    texte_new_perso.setVisible(false);
                    scroll.setVisible(false);
                    chargement = new Timer(50, this);
                    chargement.start();
                    
                    thread = new Thread(() -> result_req_ajout_perso = bdd.maj_perso(texte_id_final, liste_perso)); // on envoi l'appel en background pour ne pas bloquer le thread principal
                    thread.start();
                }
                
                else if(event.getSource() == bouton_retour){
                    this.principal();
                }
                
                /*  MENU JOUER  */
                
                else if(event.getSource() == bouton_oui || event.getSource() == bouton_non || event.getSource() == bouton_jsp)
                {
                    if(event.getSource() == bouton_oui)
                    {
                        if(fin_analyse) // on appelle analyse pour faire le bilan
                        {
                            trouve = true;
                            Analyse.resultat(nom_perso.getText()); // on envoit le nom à réultat pour qu'il update les stats
                            zone_devinetor.setText("Super ! Je suis imbattable !");
                            bouton_oui.setVisible(false);
                            bouton_non.setVisible(false);
                            label_img_loading.setVisible(true);
                            bouton_retour.setVisible(false);
                            chargement = new Timer(50, this);
                            chargement.start();
                            
                            thread = new Thread(() -> bdd.maj_perso(nom_perso.getText(), liste_perso)); // on envoi l'appel en background pour ne pas bloquer le thread principal
                            thread.start();
                        }
                        else{ // ou on envoi la réponse précédente
                            Analyse.sendReponse(current_id_question, 1);
                        }
                    }
                    else if(event.getSource() == bouton_non)
                    {
                        if(fin_analyse) // on appelle analyse pour faire le bilan
                        {
                            zone_devinetor.setText("Ah mince, tu m'as eu ! À qui pensais-tu ? Si tu ne le trouves pas dans la liste, ajoute-le !");
                            bouton_oui.setVisible(false);
                            bouton_non.setVisible(false);
                            nom_perso.setVisible(false);
                            bouton_valider_perso.setVisible(true);
                            bouton_valider_perso_liste.setVisible(true);
                            scroll.setVisible(true);
                            texte_new_perso.setVisible(true);
                        }
                        else{ // ou on envoi la réponse précédente
                            Analyse.sendReponse(current_id_question, 0);
                        }
                    }
                    else if(event.getSource() == bouton_jsp)
                    {
                        if(!fin_analyse)
                            Analyse.sendReponse(current_id_question, 2);
                    }
                    
                    // Analyse -> dis si pense avoir trouvé le perso et sinon met la prochaine question
                    if(!fin_analyse)
                        fin_analyse = Analyse.isFound();
                    if(fin_analyse && !pass_fin_analyse)
                    {
                        zone_devinetor.setText("Je pense avoir trouvé de quoi tu veux parler... Est-ce que j'ai bon ?");
                        nom_perso.setText(liste_perso.get(Analyse.getIDFound()));
                        zone_question.setVisible(false);
                        nom_perso.setVisible(true);
                        bouton_jsp.setVisible(false);
                        pass_fin_analyse = true;
                    }
                    else if(!pass_fin_analyse)
                    {
                        current_id_question = Analyse.getNextQuestionID();
                        zone_question.setText(liste_question.get(current_id_question));
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
	/**
	 * Event Timer
     * @author Romain MONIER
     * @param event l'event
	*/
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == chargement)
        {
            if(!thread.isAlive()) // Réaffichage des boutons si l'ajout est terminé
            {
                chargement.stop();
                label_img_loading.setVisible(false);
                bouton_retour.setVisible(true);
                
                if(!trouve) // Si on n'a pas trouvé le perso, on doit ré afficher plus de choses
                {
                    if(add_perso_jouer)
                        bouton_valider_perso_liste.setVisible(true);
                    scroll.setVisible(true);
                    if(bouton_valider_perso != null){
                        bouton_valider_perso.setVisible(true);
                        texte_new_perso.setVisible(true);
                    }
                    if(bouton_valider_question != null){
                        bouton_valider_question.setVisible(true);
                        texte_new_question.setVisible(true);
                    }
                    
                    if(add_perso_jouer && result_req_ajout_perso == 1) // On considère que l'ajout est terminé seulement si l'appel aux méthodes de la BDD retourne que tout s'est bien passé
                    {
                        Analyse.resultat(texte_id_final); // on envoit le nom à réultat pour qu'il update les stats
                        bouton_valider_perso_liste.setVisible(false);
                        scroll.setVisible(false);
                        bouton_valider_perso.setVisible(false);
                        texte_new_perso.setVisible(false);
                        zone_devinetor.setText("Super ! Pour ton information, j'apprends de mes erreurs... La prochaine fois je trouverai !");
                    }
                    else if(add_perso_jouer && result_req_ajout_perso != 1){
                        zone_devinetor.setText("Entre un nom valide ! Vérifie aussi qu'il n'est pas déjà dans la liste ! À qui pensais-tu ?");
                    }
                }
            }
        }
    }
    
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mousePressed(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}
}
