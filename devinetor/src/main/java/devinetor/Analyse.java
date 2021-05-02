/* FICHIER ANALYSE.JAVA :
 *      - L'ANALYSE PRINCIPALE QUI DETERMINE LES QUESTIONS A POSER ET LE PERSONNAGE
 *      - GERE LES STATS
 * 
 *  DERNIÈRE MÀJ : 03/04/2018 par ROMAIN MONIER
 *  CRÉÉ PAR ROMAIN MONIER
 *  2017/2018
 * ------------------------------------------
 *  INFOS :
 *      - CLASSE DE CONTROLE
 * ------------------------------------------
 */

package devinetor;
 
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/** Analyse
 * @author Romain MONIER, Alexandre LEBLOND
 */
public class Analyse
{   
    private static BaseDeDonnees s_bdd;
    private static HashMap<Integer, String> s_liste_perso, s_liste_question;
    
    private static int s_id_found, s_next_question_id, s_true_id_perso;
    private static boolean s_is_found;
    private static HashMap<Integer, Integer> s_liste_questions_reponses, s_verified_liste_questions_reponses, s_liste_question_stats;
    private static HashMap<Integer, HashMap<Integer, int[]>> s_stats;
    
    private static ArrayList<Integer> s_personnages_restants, s_liste_questions_posees;
    
	/**
	 * Initialisation de Analyse : tri des données si jamais fait (peut prendre du temps selon la taille des données)
     * @author Romain MONIER
     * @param bdd Base de données
     * @param liste_perso La liste de personnages
     * @param liste_question La liste de questions
	 */
    public static void init(BaseDeDonnees bdd, HashMap<Integer, String> liste_perso, HashMap<Integer, String> liste_question)
    {
        s_bdd = bdd;
        s_liste_perso = liste_perso;
        s_liste_question = liste_question;
        
        s_next_question_id = -1;
        s_id_found = -1;
        s_is_found = false;
        s_liste_questions_reponses = new HashMap<Integer, Integer>();
        s_verified_liste_questions_reponses = new HashMap<Integer, Integer>();
        s_personnages_restants = new ArrayList<Integer>();
        s_liste_questions_posees = new ArrayList<Integer>();
        
        s_stats = bdd.recup_stats();
    }
    
	/**
	 * Renvoi l'id de la prochaine meilleure question à poser en fonction des informations obtenues précédemment et recherche la meilleure première question à poser si on vient de l'appeler
     * @author Alexandre LEBLOND, Romain MONIER
     * @return id de la question
	 */
    public static int getNextQuestionID()
    {   
        if(s_next_question_id == -1) // recherche meilleure première question
        {
            int compteur = 0, question = -1;
            double prono = 0.0, meilleur_prono = 1.0;
            
            for(Map.Entry<Integer, String> e: s_liste_question.entrySet()){
                compteur = 0;
                for (Integer n: s_personnages_restants){
                    if (s_bdd.reponse(n, e.getKey()) == 1){
                        compteur++;
                    }
                }
                prono = Math.abs((double)((double)compteur / (double)s_personnages_restants.size() - 0.5)); // on détermine le pourcentage de perso qui ont répondu bon à la réponse.
                
                if (prono < meilleur_prono){ // on souhaite garder le pronostic le plus proche de 50% pour éliminer la moitié des candidats en liste.
                    meilleur_prono = prono;
                    question = e.getKey();
                }
            }
            s_next_question_id = question;
            s_liste_questions_posees.add(s_next_question_id);
        }
        
        return s_next_question_id;
    }
    
	/**
	 * Indique si le personnage a été trouvé par Analyse selon les probabilités définies
     * @author Romain MONIER
     * @return vrai ou faux
	 */
    public static boolean isFound()
    {        
        return s_is_found;
    }
    
	/**
	 * Renvoi l'id du personnage trouvé ou -1 s'il n'est pas encore trouvé
     * @author Romain MONIER
     * @return id du personnage (ou -1 si non trouvé)
	 */
    public static int getIDFound()
    {
        return s_id_found;
    }
    
	/**
	 * Renvoi une hashmap d'id associant les questions aux réponses actuelles de l'utilisateur sans vérification de leur concordance
     * @author Romain MONIER
     * @return hashmap d'id associant les questions aux réponses actuelles de l'utilisateur
	 */
    public static HashMap<Integer, Integer> getListeQuestionsReponses()
    {
        return s_liste_questions_reponses;
    }
    
	/**
	 * Renvoi une hashmap d'id associant les questions aux réponses actuelles de l'utilisateur qui semblent concorder avec les valeurs habituelles (Stats) (si on a pas d'entrée ou -1 pour le personnage en cours c'est qu'il est nouveau, on considère que toutes les reponses sont bonnes
     * @author Romain MONIER
     * @return hashmap d'id associant les questions aux réponses actuelles de l'utilisateur
	 */
    public static HashMap<Integer, Integer> getVerifiedListeQuestionsReponses()
    {
        for(Map.Entry<Integer, Integer> entree: s_liste_questions_reponses.entrySet())
        {
            if(s_stats.containsKey(entree.getKey()) && s_stats.get(entree.getKey()).containsKey(s_true_id_perso) && s_bdd.reponse(s_true_id_perso, entree.getKey()) != -1)
            {
                if((s_stats.get(entree.getKey()).get(s_true_id_perso)[1] >= s_stats.get(entree.getKey()).get(s_true_id_perso)[0] && entree.getValue() == 1) || (s_stats.get(entree.getKey()).get(s_true_id_perso)[1] <= s_stats.get(entree.getKey()).get(s_true_id_perso)[0] && entree.getValue() == 0)) // si réponse oui et que plus de oui que de non on renvoi ça, sinon non et inversement
                    s_verified_liste_questions_reponses.put(entree.getKey(), entree.getValue());
            }
            else if(entree.getValue() != 2)
                s_verified_liste_questions_reponses.put(entree.getKey(), entree.getValue());
        }
        
        return s_verified_liste_questions_reponses;
    }
    
	/**
	 * Récupère le vrai personnage et appelle la BDD pour modifier les valeurs des statistiques
     * @author Romain MONIER
     * @param true_nom_perso ID du vrai personnage
	 */
    public static void resultat(String true_nom_perso)
    {
        for (Integer id: s_liste_perso.keySet()){
            if (s_liste_perso.get(id).equals(true_nom_perso)){
                s_true_id_perso = id;
                break;
            }
        }
        
        for(Map.Entry<Integer, Integer> entree: s_liste_questions_reponses.entrySet())
        {
            if(!s_stats.containsKey(entree.getKey())) // si n'existe pas dans la table stats, on l'ajoute
            {
                s_bdd.add_stats(entree.getKey(), s_true_id_perso);
                HashMap<Integer, int[]> map_temp = new HashMap<Integer, int[]>();
                map_temp.put(s_true_id_perso, new int[]{0,0});
                s_stats.put(entree.getKey(), map_temp);
            }
            else if(!s_stats.get(entree.getKey()).containsKey(s_true_id_perso)) // de même ici
            {
                s_bdd.add_stats(entree.getKey(), s_true_id_perso);
                s_stats.get(entree.getKey()).put(s_true_id_perso, new int[]{0,0});
            }
            
            s_bdd.maj_stats(s_true_id_perso, entree.getKey(), entree.getValue()); // mise a jour de la bdd
            
            if(entree.getValue() == 0)
                s_stats.get(entree.getKey()).get(s_true_id_perso)[0] += 1;
            else if(entree.getValue() == 1)
                s_stats.get(entree.getKey()).get(s_true_id_perso)[1] += 1;
        }
    }
    
	/**
	 * Prend en compte la réponse de l'utilisateur à la question posée et analyse quel pourrait être le personnage tout en calculant quelle est la meilleure prochaine question à poser
     * @author Alexandre LEBLOND, Romain MONIER
     * @param id_question id de la question
     * @param reponse réponse de l'utilisateur à la question
	 */
    public static void sendReponse(int id_question, int reponse)
    {
        s_liste_questions_reponses.put(id_question, reponse);
        
		int compteur_oui = 0, question = -1, compteur_non = 0;
		double prono = 0.0, prono2 = 0.0, meilleur_prono = 2.0;
        
        ArrayList<Integer> copie_perso_restants = new ArrayList<Integer>();
        
        for (Integer n: s_personnages_restants)
        {
            copie_perso_restants.add(n);
        }
        
        for (Integer n: copie_perso_restants){ // ca va enlever de la liste des persos tous ceux qui ne correpondent pas à la question.
			if (reponse != 2 && s_bdd.reponse(n, id_question) != reponse && s_personnages_restants.size() > 1){
				s_personnages_restants.remove(n);
			}
			else if (reponse != 2 && s_bdd.reponse(n, id_question) == -1 && s_personnages_restants.size() > 1){
				if (Math.random() < 0.5){ // aléatoirement, on choisit ou non de laisser dans la liste les persos qui n'ont pas de réponse définie
					s_personnages_restants.remove(n);
				}
			}
		}
        
		if (s_personnages_restants.size() == 1){ // verifie si dans la liste restante, il n'y en a plus q'un seul
			s_id_found = s_personnages_restants.get(0);
			s_is_found = true;
		}
        else // sinon on cherche la question la plus pertinente
        {
            boolean no_more_quest = true;
            
            if(Math.random() < 0.1) // aléatoirement on peut choisir une question sans données pour la tester et peut etre la remplir plus tard
            {
                for(Map.Entry<Integer, String> e: s_liste_question.entrySet())
                {
                    if(!s_liste_questions_posees.contains(e.getKey()))
                    {
                        no_more_quest = false;
                        for (Map.Entry<Integer, String> e2: s_liste_perso.entrySet()){
                            reponse = s_bdd.reponse(e2.getKey(), e.getKey());
                            if(reponse == -1){
                                question = e.getKey();
                                break;
                            }
                        }
                    }
                }
            }
            
            if(question == -1)
            {
                for(Map.Entry<Integer, String> e: s_liste_question.entrySet())
                {     
                    if(!s_liste_questions_posees.contains(e.getKey()))
                    {
                        no_more_quest = false;
                        compteur_oui = 0;
                        compteur_non = 0;
                        for (Integer n: s_personnages_restants){
                            reponse = s_bdd.reponse(n, e.getKey());
                            if (reponse == 1){
                                compteur_oui++;
                            }
                            else if (reponse == 0){
                                compteur_non++;
                            }
                        }
                        prono = Math.abs((double)((double)compteur_oui / (double)s_personnages_restants.size() - 0.5)); // on détermine le pourcentage de perso qui ont répondu bon à la réponse.
                        prono2 = Math.abs((double)((double)compteur_non / (double)s_personnages_restants.size() - 0.5)); // on détermine le pourcentage de perso qui ont répondu mauvais à la réponse.
                        
                        if (prono < meilleur_prono){ // on souhaite garder le pronostic le plus proche de 50% pour éliminer presque la moitié des candidats en liste pour être le plus efficace.
                            meilleur_prono = prono;
                            question = e.getKey();
                        }
                        if (prono2 < meilleur_prono){ 
                            meilleur_prono = prono2;
                            question = e.getKey();
                        }
                    }
                }
            }
                    
            if(no_more_quest){ // si plus de questions dispo, on choisit aléatoirement parmi les perso restants
                s_id_found = s_personnages_restants.get((int)(Math.random() * (s_personnages_restants.size()-1)));
                s_is_found = true;
            }
            s_next_question_id = question;
            s_liste_questions_posees.add(s_next_question_id);
        }
    }
    
	/**
	 * Réinitialise les attributs statiques de Analyse utilisés pour la session de recherche précédante : à appeller avant chaque nouelle Analyse
     * @author Romain MONIER
	 */
    public static void reset()
    {        
        /*  REINITIALISATION DES ATTRIBUTS  */
        
        s_id_found = -1;
        s_is_found = false;
        s_next_question_id = -1;
        s_liste_questions_reponses = new HashMap<Integer, Integer>();
        s_verified_liste_questions_reponses = new HashMap<Integer, Integer>();
        s_personnages_restants = new ArrayList<Integer>();
        s_liste_questions_posees = new ArrayList<Integer>();
        
        for (Map.Entry<Integer, String> e: s_liste_perso.entrySet()){
			s_personnages_restants.add(e.getKey());
		}
    }
}
