package barbotte;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.util.ArrayList;

import barbotte.Config;

/**
 * gere l'interface graphique
 * @author sebastienlapeyre
 *
 */
public class BarbotteUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel display;
	private JLabel message;
    private JLabel image1;
    private ImageIcon icone1;
    private JLabel image2;
    private ImageIcon icone2;
    private JLabel image3;
    private ImageIcon icone3;
    private JLabel image4;
    private ImageIcon icone4;
    
    /**
     * Constructeur
     */
	public BarbotteUI() {
		try {
			initComponents();
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur: la création de la fenêtre a échoué:"
					+ e.getMessage());
		}
	}
	/**
	 * Crée la fenêtre de l'application et initie les composants
	 * On peut ici changer l'image de fond ainsi que la police d'écriture
	 * @throws Exception
	 */
	private void initComponents() throws Exception {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(
				"ressources/DIOGENES.ttf"));
		GraphicsEnvironment genv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		genv.registerFont(font);
		font = font.deriveFont(35f);

		display = setBackgroundImage(this, new File(Config.BACKGROUND_IMAGE));

		message = new JLabel("", SwingConstants.CENTER);
		message.setFont(font);
		
		display.add(message);
		display.setLayout(null);

		message.setLocation(600, 170);
		message.setSize(550, 240);
		
		setBounds(0, 0, 1290,1024);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	/**
	 * affiche seulement le texte passé en argument
	 * @param str
	 */
	public void printMessage(String str) {
		message.setText("<html>" + str + "</html>");
	}
	/**
	 * Affiche le texte passé en argument et le podium des écoles
	 * @param str
	 * @param podium
	 */
    public void printMessage(String str, ArrayList<String> podium){
    	//permet d'enlever les anciennes images
    	if (image1!=null){
    		image1.setBounds(0,0,0,0);
    	}
    	if (image2!=null){
    		image2.setBounds(0,0,0,0);
    	}
    	if (image3!=null){
    		image3.setBounds(0,0,0,0);
    	}
    	if (image4!=null){
    		image4.setBounds(0,0,0,0);
    	}
    	switch (podium.size()){
    	case 1 :
    		System.out.println("cas 1 : "+podium.get(0));
    		icone1 = new ImageIcon(podium.get(0));
			image1 = new JLabel(icone1, JLabel.CENTER);
		    display.add(image1);
	    	//TODO : changer affixes
		    image1.setBounds(810,400,300,300);
	    	break;
    	case 2:
    		System.out.println("cas 2 : "+podium);
    		icone1 = new ImageIcon(podium.get(0));
			image1 = new JLabel(icone1, JLabel.CENTER);
		    display.add(image1);
	    	image1.setBounds(810,400,300,300);
		    icone2 = new ImageIcon(podium.get(1));
			image2 = new JLabel(icone2, JLabel.CENTER);
		    display.add(image2);
		    image2.setBounds(570,570,300,300);
	    	break;
    	case 3:
	    	//logo sur podium
    		System.out.println("test case 3"+podium);
    		icone1 = new ImageIcon(podium.get(0));
			image1 = new JLabel(icone1, JLabel.CENTER);
		    display.add(image1);
	    	image1.setBounds(810,400,300,300);
		    icone2 = new ImageIcon(podium.get(1));
			image2 = new JLabel(icone2, JLabel.CENTER);
		    display.add(image2);
		    image2.setBounds(570,570,300,300);
		    icone3 = new ImageIcon(podium.get(2));
			image3 = new JLabel(icone3, JLabel.CENTER);
		    display.add(image3);
	    	image3.setBounds(1030,610,300,300);
	    	break;
    	}
		//icone4 = new ImageIcon(Config.IMAGE_PODIUM);
		//image4 = new JLabel(icone4, JLabel.CENTER);
	//display.add(image4);
    	//image4.setBounds(600,-20,500,500);
        message.setText("<html>"+str+"</html>");
    }
    public void printMessageAdmin (String txt,String urlImage,int posX,int posY, int largeur,int hauteur){
		icone4 = new ImageIcon(urlImage);
		image4 = new JLabel(icone4, JLabel.CENTER);
    	display.add(image4);
    	image4.setBounds(posX,posY,largeur,hauteur);
    	message.setText("<html>"+txt+"</html>");
    }

	public static JPanel setBackgroundImage(JFrame frame, final File img) throws IOException
    {
            JPanel panel = new JPanel()
            {
                    private static final long serialVersionUID = 1;

                    private BufferedImage buf = ImageIO.read(img);

                    @Override
                    protected void paintComponent(Graphics g)
                    {
                            super.paintComponent(g);
                            g.drawImage(buf, 0,0, null);
                    }
            };

            frame.setContentPane(panel);

            return panel;
    }
}
