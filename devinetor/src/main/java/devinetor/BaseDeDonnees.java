/* FICHIER BASEDEDONNEES.JAVA :
 *      - FICHIER DE CONNEXION BDD & REQUETES
 * 
 *  DERNIÈRE MÀJ : 03/04/2018 par ROMAIN MONIER
 *  CRÉÉ PAR ROMAIN MONIER
 *  2017/2018
 * ------------------------------------------
 *  INFOS :
 *      - CLASSE DE MODELE
 *      - bdd R -> 0 = faux, 1 = vrai, 2 = jsp, -1 = null
 *      - pour l'ajout de perso, on créé la table de correspondance avec les questions et on met -1 dedans pour dire qu'on n'a pas encore eu de résultats, plus tard on rempliera en fonction des réponses
 * ------------------------------------------
 */

package devinetor;

import com.github.BenoitDuffez.ScriptRunner;

import java.io.InputStreamReader;
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** BaseDeDonnees
 * @author Romain MONIER
 */
public class BaseDeDonnees
{
    private Connection conn;
         
	/**
	 * Constructeur
     * @author Romain MONIER, Alexandre LEBLOND
     * @param adresseBD Adresse Base de Données
	*/
    public BaseDeDonnees(String adresseBD)
    {
        try // Tente de se connecter à la BDD
        {           
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(adresseBD);
            
            /*  CREATION DE LA BDD SI ELLE N'EXISTE PAS */
            
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res = conn.prepareStatement(""
                                                                + "CREATE TABLE IF NOT EXISTS Q ( \n"
                                                                + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                                                                + "    quest varchar(255) NOT NULL, \n"
                                                                + "    CONSTRAINT UC_quest UNIQUE (quest) \n"
                                                                + "    )"
                                                            + "");
            prep_res.executeUpdate();
            prep_res.close();
            prep_res = conn.prepareStatement(""
                                                                + "CREATE TABLE IF NOT EXISTS Item ( \n"
                                                                + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                                                                + "    nom varchar(255) NOT NULL, \n"
                                                                + "    CONSTRAINT UC_nom UNIQUE (nom) \n"
                                                                + "    )"
                                                            + "");
            prep_res.executeUpdate();
            prep_res.close();
            prep_res = conn.prepareStatement(""
                                                                + "CREATE TABLE IF NOT EXISTS R ( \n"
                                                                + "    id int(11) NOT NULL, \n"
                                                                + "    quest_id int(11), \n"
                                                                + "    rep int(11) NOT NULL, \n"
                                                                + "    PRIMARY KEY (id, quest_id) \n"
                                                                + "    )"
                                                            + "");
            prep_res.executeUpdate();
            prep_res.close();
            prep_res = conn.prepareStatement(""
                                                                + "CREATE TABLE IF NOT EXISTS Stats ( \n"
                                                                + "    id int(11) NOT NULL, \n"
                                                                + "    qid int(11) NOT NULL, \n"
                                                                + "    yes int(11), \n"
                                                                + "    no int(11), \n"
                                                                + "    PRIMARY KEY (id, qid) \n"
                                                                + "    )"
                                                            + "");
            prep_res.executeUpdate();
            prep_res.close();
            prep_res = conn.prepareStatement(""
                                                                + "CREATE TABLE IF NOT EXISTS Parametres ( \n"
                                                                + "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                                                                + "    musique tinyint(1), \n"
                                                                + "    son tinyint(1) \n"
                                                                + "    )"
                                                            + "");
            prep_res.executeUpdate();
            prep_res.close();
            prep_res = conn.prepareStatement("INSERT OR IGNORE INTO Parametres VALUES(1,1,1)");
            prep_res.executeUpdate();
            prep_res.close();
            
            stmt.close();
            
            /*  REMPLISSAGE DE LA BDD (si existait déjà, les entrées ne seront pas ajoutées grâce aux primary keys) [utilisation d'un script tierce pour lire un fichier sql]  */
            (new ScriptRunner(conn, false, false)).runScript(new BufferedReader(new InputStreamReader(Devinetor.getResourceStream("db/default.sql"))));
            
        }
        catch(Exception e) // Sinon erreur, fin du programme
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    
	/**
	 * Destructeur
     * @author Romain MONIER, Alexandre LEBLOND
	*/
    public void close() throws SQLException
    {
        conn.close();
    }
    
    //  METHODES DE RECUPERATION
    
	/**
	 * Exécute la requête, renvoi la réponse à la question
     * @author Romain MONIER, Alexandre LEBLOND
     * @param id_perso L'id du personnage
     * @param id_question L'id de la question
	*/
    public int reponse(int id_perso, int id_question)
    {                
        try
        {
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res = conn.prepareStatement("SELECT R.rep FROM R, Q, Item WHERE Item.id = R.id AND Q.id = R.quest_id AND R.id = ? AND Q.id = ?");
            
            prep_res.setInt(1, id_perso);
            prep_res.setInt(2, id_question);
            ResultSet res = prep_res.executeQuery();
            
            int valeur = -1;
            while(res.next()){
                valeur = res.getInt(1);
            }
            
            res.close();
            prep_res.close();
            stmt.close();
            
            if(valeur == 0 || valeur == 1)
                return valeur;
            
            return -1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Exécute la requête, renvoi les questions avec leur id (hashmap)
     * @author Romain MONIER
	*/
    public HashMap<Integer, String> recup_questions()
    {                
        try
        {
            HashMap<Integer, String> tab_questions = new HashMap<Integer, String>();
            
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res = conn.prepareStatement("SELECT Q.id, Q.quest FROM Q");
            
            ResultSet res = prep_res.executeQuery();
            while(res.next()){
                tab_questions.put(res.getInt(1), res.getString(2));
            }
            
            res.close();
            prep_res.close();
            stmt.close();
            
            return tab_questions;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
	/**
	 * Exécute la requête, renvoi les personnages avec leur id (hashmap)
     * @author Romain MONIER
	*/
    public HashMap<Integer, String> recup_persos()
    {                
        try
        {
            HashMap<Integer, String> tab_persos = new HashMap<Integer, String>();
            
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res = conn.prepareStatement("SELECT Item.id, Item.nom FROM Item");
            
            ResultSet res = prep_res.executeQuery();
            while(res.next())
            {
                tab_persos.put(res.getInt(1), res.getString(2));
            }
            
            res.close();
            prep_res.close();
            stmt.close();
            
            return tab_persos;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
	/**
	 * Exécute la requête, renvoi la valeur du paramètre audio
     * @author Romain MONIER
     * @param isMusic Modifie soit la musique soit le son
	*/
    public int recup_audio(boolean isMusic)
    {                
        try
        {
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res;
            
            if(isMusic)
                prep_res = conn.prepareStatement("SELECT Parametres.musique FROM Parametres");
            else
                prep_res = conn.prepareStatement("SELECT Parametres.son FROM Parametres");
            
            ResultSet res = prep_res.executeQuery();
            
            int valeur = -1;
            while(res.next())
            {
                valeur = res.getInt(1);
            }
            
            res.close();
            prep_res.close();
            stmt.close();
            
            return valeur;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Récupère les oui et non dans la BDD Stats
     * @author Romain MONIER
     * @return HashMap : id_question puis chaque perso avec valeur associée (case 0 = oui, case 1 = non)
	*/
    public HashMap<Integer, HashMap<Integer, int[]>> recup_stats()
    {
        try
        {
            Statement stmt = conn.createStatement();
            
            PreparedStatement prep_res;
            
            prep_res = conn.prepareStatement("SELECT Stats.* FROM Stats ORDER BY qid");
            
            ResultSet res = prep_res.executeQuery();
            
            HashMap<Integer, HashMap<Integer, int[]>> full_stats = new HashMap<Integer, HashMap<Integer, int[]>>();            
            HashMap<Integer, int[]> perso_rep = new HashMap<Integer, int[]>();
            
            int old_id_question = -1;
            boolean first_pass = true;
            while(res.next())
            {
                int id_perso = res.getInt(1);
                int id_question = res.getInt(2);
                int nb_oui = res.getInt(3);
                int nb_non = res.getInt(4);
                
                if(id_question != old_id_question)
                {
                    if(!first_pass){
                        full_stats.put(old_id_question, perso_rep);
                        perso_rep = new HashMap<Integer, int[]>();
                    }
                    first_pass = false;
                }
                perso_rep.put(id_perso, new int[]{nb_oui, nb_non});
                old_id_question = id_question;
            }
            
            if(perso_rep != null)
                full_stats.put(old_id_question, perso_rep);
            
            res.close();
            prep_res.close();
            stmt.close();
            
            return full_stats;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    //  METHODES D'AJOUT
    
	/**
	 * Exécute la requête, ajoute un personnage à la BDD et lie les réponses aux questions / personnage
     * @author Romain MONIER
     * @param nom Le nom du personnage
     * @param liste La liste actuelle
     * @param add_infos Booléen pour savoir si on a des réponses aux questions sur le personnage : si oui, on rempliera la table avec, sinon on met -1
	*/
    public int ajout_perso(String nom, HashMap<Integer, String> liste, boolean add_infos)
    {                
        try
        {
            Statement stmt = conn.createStatement();
            
            /*  AJOUT PERSO  */
            
            String req_perso = "INSERT INTO Item(nom) VALUES(?)";
            PreparedStatement prep_res_perso = conn.prepareStatement(req_perso, Statement.RETURN_GENERATED_KEYS);
            
            prep_res_perso.setString(1, nom);
            
            prep_res_perso.executeUpdate();
            
            /*  CREATION DES TABLES DE LIENS AVEC LES QUESTIONS  */
            
            ResultSet generatedKeys = prep_res_perso.getGeneratedKeys(); // on récupère l'id inséré précédemment
            generatedKeys.next();
            int last_id = generatedKeys.getInt(1);
            
            liste.put(last_id, nom);
            
            PreparedStatement prep_res_quest = conn.prepareStatement("SELECT Q.id FROM Q"); // Récupération du nombre de questions
            ResultSet res_quest = prep_res_quest.executeQuery();
            
            while(res_quest.next()) // on ajoute le lien avec les questions
            {
                int id_question = res_quest.getInt(1);
                
                HashMap<Integer, Integer> liste_questions_reponses = null;
                boolean question_posee = false;
                if(add_infos){
                    liste_questions_reponses = Analyse.getListeQuestionsReponses(); // On récupère la liste de questions / réponses de l'user pour ajouter des données au nouveau personnage
                    if(liste_questions_reponses.containsKey(id_question)) // Si la réponse en cours d'analyse a été posée
                        question_posee = true;
                }
                    
                String req_lien;
                if(add_infos && question_posee && liste_questions_reponses.get(id_question) != 2){
                    req_lien = "INSERT INTO R VALUES(?, ?, ?)";
                }
                else{
                    req_lien = "INSERT INTO R VALUES(?, ?, -1)";
                }
                PreparedStatement prep_res_lien = conn.prepareStatement(req_lien);
                
                prep_res_lien.setInt(1, last_id);
                prep_res_lien.setInt(2, id_question);
                if(add_infos && question_posee && liste_questions_reponses.get(id_question) != 2)
                    prep_res_lien.setInt(3, liste_questions_reponses.get(id_question)); // réponse à la question dont l'id est selectionné
                
                prep_res_lien.executeUpdate();
                
                prep_res_lien.close();
            }
            
            generatedKeys.close();
            res_quest.close();
            prep_res_perso.close();
            prep_res_quest.close();
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Exécute la requête, ajoute une question à la BDD
     * @author Romain MONIER
     * @param question La question
     * @param liste La liste actuelle
	*/
    public int ajout_question(String question, HashMap<Integer, String> liste)
    {                
        try
        {
            Statement stmt = conn.createStatement();
            
            /*  AJOUT QUESTION  */
            
            String req = "INSERT INTO Q(quest) VALUES(?)";
            PreparedStatement prep_res = conn.prepareStatement(req);
            
            prep_res.setString(1, question);
            
            prep_res.executeUpdate();
            
            /*  CREATION DES TABLES DE LIENS AVEC LES PERSONNAGES  */
            
            ResultSet generatedKeys = prep_res.getGeneratedKeys(); // on récupère l'id inséré précédemment
            generatedKeys.next();
            int last_id = generatedKeys.getInt(1);
            
            liste.put(last_id, question);
            
            PreparedStatement prep_res_perso = conn.prepareStatement("SELECT Item.id FROM Item"); // Récupération du nombre de personnages
            ResultSet res_perso = prep_res_perso.executeQuery();
            
            while(res_perso.next()) // on ajoute le lien avec les personnages
            {
                String req_lien = "INSERT INTO R VALUES(?, ?, -1)";
                PreparedStatement prep_res_lien = conn.prepareStatement(req_lien);
                
                prep_res_lien.setInt(1, res_perso.getInt(1));
                prep_res_lien.setInt(2, last_id);
                
                prep_res_lien.executeUpdate();
                
                prep_res_lien.close();
            }
            
            generatedKeys.close();
            res_perso.close();
            prep_res_perso.close();
            prep_res.close();
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Exécute la requête, ajoute les stats nécessaires à la BDD
     * @author Romain MONIER
     * @param id_question L'id de la question
     * @param id_perso L'id du perso
	*/
    public int add_stats(int id_question, int id_perso)
    {                
        try
        {
            Statement stmt = conn.createStatement();
            
            /*  AJOUT STATS  */
            
            String req = "INSERT OR IGNORE INTO Stats VALUES(?,?,0,0)";
            PreparedStatement prep_res = conn.prepareStatement(req);
            
            prep_res.setInt(1, id_perso);
            prep_res.setInt(2, id_question);
            
            prep_res.executeUpdate();
            
            prep_res.close();
            
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
     //  METHODES DE MISE A JOUR
    
	/**
	 * Exécute la requête, modifie les liens aux questions d'un personnage dans la BDD
     * @author Romain MONIER
     * @param nom Le nom du personnage
     * @param liste La liste actuelle
	*/
    public int maj_perso(String nom, HashMap<Integer, String> liste)
    {
        try
        {
            Statement stmt = conn.createStatement();
            int true_id = -1;
            
            /*  RECUP ID PERSO  */
            
            for (Integer id: liste.keySet()){
                if (liste.get(id).equals(nom)){
                    true_id = id;
                    break;
                }
            }
            
            /*  MISE A JOUR DE LA BDD */
            
            for(Map.Entry<Integer, Integer> entree: Analyse.getVerifiedListeQuestionsReponses().entrySet())
            {
                String req_maj = "UPDATE R SET rep=? WHERE id=? AND quest_id=?";
                PreparedStatement prep_res_maj = conn.prepareStatement(req_maj);
                
                prep_res_maj.setInt(1, entree.getValue()); // Analyse place la réponse
                prep_res_maj.setInt(2, true_id);
                prep_res_maj.setInt(3, entree.getKey()); // Analyse place l'id de la question qui correspond
                
                prep_res_maj.executeUpdate();
                    
                prep_res_maj.close();
            }
            
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Exécute la requête, modifie les paramètres audio dans la BDD
     * @author Romain MONIER
     * @param valeur Nouvelle valeur du paramètre
     * @param isMusic Modifie soit la musique soit le son
	*/
    public int maj_audio(boolean valeur, boolean isMusic)
    {
        try
        {
            Statement stmt = conn.createStatement();
            
            /*  MISE A JOUR DE LA BDD */
            
            String req_maj;
            
            if(isMusic)
                req_maj = "UPDATE Parametres SET musique=?";
            else
                req_maj = "UPDATE Parametres SET son=?";
                
            PreparedStatement prep_res_maj = conn.prepareStatement(req_maj);
            
            int valeur_int;
            if(valeur)
                valeur_int = 1;
            else
                valeur_int = 0;
            
            prep_res_maj.setInt(1, valeur_int);
            
            if(prep_res_maj.executeUpdate() == 0){ // si on a pas modifié de données => probleme
                prep_res_maj.close();
                stmt.close();
                return -3;
            }
            
            prep_res_maj.close();
        
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
    
	/**
	 * Exécute la requête, modifie les statistiques
     * @author Romain MONIER
     * @param id_perso L'id du personnage à modifier
     * @param id_question L'id de la question
     * @param rep Oui ou non
	*/
    public int maj_stats(int id_perso, int id_question, int rep)
    {
        try
        {
            Statement stmt = conn.createStatement();
            
            /*  MISE A JOUR DE LA BDD */
            
            String req_maj = "UPDATE Stats SET yes=yes+?, no=no+? WHERE id=? AND qid=?";
                
            PreparedStatement prep_res_maj = conn.prepareStatement(req_maj);
                
            if(rep == 1){ // si la réponse est oui, on ajoute un oui
                prep_res_maj.setInt(1, 1);
                prep_res_maj.setInt(2, 0);
            }
            else if(rep == 0){ // sinon un non
                prep_res_maj.setInt(1, 0);
                prep_res_maj.setInt(2, 1);
            }
            else if(rep == 2){ // sinon rien
                prep_res_maj.setInt(1, 0);
                prep_res_maj.setInt(2, 0);
            }
            prep_res_maj.setInt(3, id_perso);
            prep_res_maj.setInt(4, id_question);
            
            prep_res_maj.executeUpdate();
                
            prep_res_maj.close();
        
            stmt.close();
            
            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return -2;
        }
    }
}
