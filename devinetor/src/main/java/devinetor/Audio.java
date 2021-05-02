/* FICHIER AUDIO.JAVA :
 *      - JOUER LA MUSIQUE ET LES SONS
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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.File;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Objects;

/** Audio
 * @author Romain MONIER
 */
public class Audio
{
    private AudioInputStream musique;
    private Clip clip;
    private boolean isMusic;
    
	/**
	 * Constructeur
     * @author Romain MONIER
     * @param chemin_musique chemin vers la musique
     * @param isMusic booléen si musique ou son
	*/
    public Audio(String chemin_musique, boolean isMusic) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        musique = AudioSystem.getAudioInputStream(new BufferedInputStream(Devinetor.getResourceStream(chemin_musique)));
        clip = AudioSystem.getClip();
        clip.open(musique);
        
        this.isMusic = isMusic;
    }
    
    /**
     * Joue la musique / son
     * @author Romain MONIER
     */
    public void jouer()
    {
        clip.setMicrosecondPosition(0);
        if(isMusic) // musique
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else // son
            clip.start();
    }
    
    /**
     * Stoppe la musique / son
     * @author Romain MONIER
     */
    public void stop()
    {
        clip.stop();
    }
}
