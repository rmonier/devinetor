/* FICHIER BOUTON.JAVA :
 *      - LES BOUTONS
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

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Point;

import java.awt.event.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/** Bouton
 * @author Romain MONIER
 */
public class Bouton extends JPanel implements MouseListener
{
    private JPanel cont_bouton;
    private ImageIcon img_bouton, img_bouton_ok;
    private JLabel label_img_bouton, label_img_bouton_ok;
    private Point pos_img_bouton;
    private Audio son_slide, son_click;
    
    /** Constructeur
     * @author Romain MONIER
     * @param img_bouton L'image du bouton normal
     * @param img_bouton_ok L'image du bouton scintillant
     * @param pos Position du bouton
     */
    public Bouton(ImageIcon img_bouton, ImageIcon img_bouton_ok, Point pos) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        son_slide = new Audio("audio/snd/slide.wav", false);
        son_click = new Audio("audio/snd/click.wav", false);
        
        this.img_bouton = img_bouton;
        this.img_bouton_ok = img_bouton_ok;
        this.pos_img_bouton = pos;
        
        this.label_img_bouton = new JLabel(img_bouton);
        this.label_img_bouton_ok = new JLabel(img_bouton_ok);
        
        this.add(label_img_bouton);
        
        this.addMouseListener(this);
        
        this.setBounds(pos_img_bouton.x, pos_img_bouton.y, img_bouton.getIconWidth(), img_bouton.getIconHeight());
    }
    
    /** Change les sprites du bouton
     * @author Romain MONIER
     * @param img_bouton L'image du bouton normal
     * @param img_bouton_ok L'image du bouton scintillant
     */
    public void setImage(ImageIcon img_bouton, ImageIcon img_bouton_ok)
    {
        this.remove(label_img_bouton);
        this.remove(label_img_bouton_ok);
        
        this.img_bouton = img_bouton;
        this.img_bouton_ok = img_bouton_ok;
        this.label_img_bouton = new JLabel(img_bouton);
        this.label_img_bouton_ok = new JLabel(img_bouton_ok);
        
        this.add(label_img_bouton);
        this.revalidate();
        repaint();
    }
    
    /** Change le son du click
     * @author Romain MONIER
     * @param son Le nouvel audio
     */
    public void setSonClick(Audio son)
    {
        son_click = son;
    }
    
    /*  EVENEMENTS  */
    
	/**
	 * Event click
     * @author Romain MONIER
     * @param event l'event
	*/
    public void mouseClicked(MouseEvent event)
    {
        if(SwingUtilities.isLeftMouseButton(event))
        {
            if(Parametres.isSoundActive())
                son_click.jouer();
        }
    }
    
	/**
	 * Event entrée zone
     * @author Romain MONIER
     * @param event l'event
	*/
    public void mouseEntered(MouseEvent event)
    {
        if(Parametres.isSoundActive())
            son_slide.jouer();
            
        this.remove(label_img_bouton);
        this.add(label_img_bouton_ok);
        this.revalidate();
        repaint();
    }
    
	/**
	 * Event sortie zone
     * @author Romain MONIER
     * @param event l'event
	*/
    public void mouseExited(MouseEvent event)
    {
        this.remove(label_img_bouton_ok);
        this.add(label_img_bouton);
        this.revalidate();
        repaint();
    }
    public void mousePressed(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}
}


