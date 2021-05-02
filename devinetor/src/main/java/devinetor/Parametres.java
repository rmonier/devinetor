/* FICHIER PARAMETRES.JAVA :
 *      - PARAMETRAGE SONS
 *
 *  DERNIÈRE MÀJ : 02/04/2018 par ROMAIN MONIER
 *  CRÉÉ PAR ROMAIN MONIER
 *  2017/2018
 * ------------------------------------------
 *  INFOS :
 *      - 
 * ------------------------------------------
 */

package devinetor;

/** Réglages dans options
 * @author Romain MONIER
 */
public class Parametres
{
	/*	ATTRIBUTS STATIQUES */
	
	private static boolean s_son, s_musique;
	private static BaseDeDonnees s_bdd;
    
    /*  INITIALISATION  */
    
	/**
     * Initialise Parametres en récupérant les valeurs enregistrées
     * @author Romain MONIER, Émilien VINET
     * @param bdd La base de données
     */
    public static void init(BaseDeDonnees bdd)
    {
        s_bdd = bdd;
        
        /*  MUSIQUE  */
        
        int musique = s_bdd.recup_audio(true);
        
        if(musique == 1)
            s_musique = true;
        else if(musique == 0)
            s_musique = false;
        else{
            s_musique = true;
        }
            
        /*  SONS  */
        
        int son = s_bdd.recup_audio(false);
        
        if(son == 1)
            s_son = true;
        else if(son == 0)
            s_son = false;
        else{
            s_son = true;
        }
    }
	
	/*	GETTERS	*/
	
	/**
     * Vérifie si la musique est activée 
     * @author Romain MONIER, Émilien VINET
     * @return Vrai si elle est activée
     */
	public static boolean isMusicActive() 
	{
		return s_musique;
	}
	
	/**
     * Vérifie si les sons sur les boutons sont activés 
     * @author Romain MONIER, Émilien VINET
     * @return Vrai s'ils sont activés
     */
	public static boolean isSoundActive()
	{
		return s_son;
	}
	
	/*	SETTERS	*/
	
	/**
     * Remet la musique ou la désactive
     * @author Romain MONIER, Émilien VINET
     * @param musique jouer ou non
     * @param mus la musique qu'on veut jouer
     */
	public static void setMusique(boolean musique, Audio mus)
	{
		s_musique = musique;
		
		if (!s_musique){ 	
            mus.stop();
		}
		else{ 	
            mus.jouer();
		}
        
        s_bdd.maj_audio(s_musique, true);
	}
	
	/**
     * Coupe le son ou l'active
     * @author Romain MONIER, Émilien VINET
     * @param son ON/OFF
     */
	public static void setSon(boolean son)
	{
		s_son = son;
        s_bdd.maj_audio(s_son, false);
	}
}
