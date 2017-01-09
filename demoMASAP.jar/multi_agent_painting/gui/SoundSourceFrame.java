package multi_agent_painting.gui;

import java.awt.FileDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

/**@author LEFOULON Ludovic
 * 
 */
public class SoundSourceFrame extends JDialog{

private static final long serialVersionUID = -8916818603396992798L;
	
	private static int nbApparition = 0;
	private boolean apparitionBeforePainting = true;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private final JRadioButton mp3RadioButton = new JRadioButton();
	private final JRadioButton microRadioButton = new JRadioButton();
	private final JButton validerButton = new JButton();
	private File song = null; 	//fichier destiné a etre joué pendantl'execution du programme
	private GUI gui = null;		//reference sur la fenetre principale pour la faire apparaitre après le choix de la provenance du son
	//playing : true , recording : false
	private boolean playingOrRecording = true;
	
	
	//Initialisation de la fenetre de choix de la provenance du son
	//Positionnement des boutons radios et du bouton de validation
	//Définition du comportement de fermeture de la fenetre de dialog
	// DISPOSE_ON_CLOSE => si la fenetre est fermer avec la crois l'application se termine
	public SoundSourceFrame(GUI _gui) {
		super();
		setBounds(100, 100, 400, 115);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		gui = _gui;
		try {
			buildDialog();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	//Construction du corps de la fenetre définie ci dessus
	private void buildDialog() throws Exception {
		getContentPane().setLayout(null);
		setResizable(false);
		setTitle("Choisissez la provenance du son");
		
		getContentPane().add(mp3RadioButton);
		mp3RadioButton.addMouseListener(new Mp3RadioButtonMouseListener());
		mp3RadioButton.setText("A partir d'un mp3");
		mp3RadioButton.setBounds(10, 10, 129, 24);
		mp3RadioButton.setSelected(true);
		
		getContentPane().add(microRadioButton);
		microRadioButton.addMouseListener(new MicroRadioButtonMouseListener());
		microRadioButton.setText("A partir d'un microphone");
		microRadioButton.setBounds(208, 10, 176, 24);
		
		buttonGroup.add(mp3RadioButton);
		buttonGroup.add(microRadioButton);
		
		getContentPane().add(validerButton);
		validerButton.addMouseListener(new ValiderButtonMouseListener());
		validerButton.setText("Valider");
		validerButton.setBounds(144, 47, 106, 26);
	}
	
	//Definition des listener sur les boutons radio et sur le bouton de validation
	private class Mp3RadioButtonMouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
			mp3RadioButton_mousePressed(e);
		}
	}
	private class MicroRadioButtonMouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
			microRadioButton_mousePressed(e);
		}
	}
	private class ValiderButtonMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			validerButton_mouseClicked(e);
		}
	}
	
	//e.getButton() == 1 : On effectue un clique gauche sur l'élément
	protected void mp3RadioButton_mousePressed(MouseEvent e) {
		if(e.getButton() == 1){
			playingOrRecording = true;
		}
		
	}
	protected void microRadioButton_mousePressed(MouseEvent e) {
		if(e.getButton() == 1){
			playingOrRecording = false;
		}
	}
	
	//Cette méthode vérifie si ,dans le cas où l'on a choisi d'importer un son, on a pas
	// tout simplement appuyé sur annuler sans charger de fichier.
	// (Cas 1) Si un fichier es chargé on fait disparaitre la fenetre et on fait apparaitre la fenetre principale
	// (Cas 2) sinon on laisse la fenetre ouverte tant qu'un fichier n'a pas été chargé
	// Dans le cas où le microphone a été choisi on fait applique le comportement 1
	//nb Apparition est défini pour la réapparition de la fentre dans le cas où l'on veuille appliquer un
	// nouvelle extrait au programme
	protected void validerButton_mouseClicked(MouseEvent e) {
		this.apparitionBeforePainting = true;
		int previousNbApparition = this.nbApparition; //permet de garder l'ancienne valeur du nombre d'apparition de la fenetre afin d'appeller la bonne méthode de GUI ensuite
		if(e.getButton() == 1){
			if(playingOrRecording){
				FileDialog fileDialog = new FileDialog(this);
				fileDialog.setFile("*.mp3");
				fileDialog.setVisible(true);
				String fileDirectoryPath = fileDialog.getDirectory();
				String fileName = fileDialog.getFile();
				
				if(fileDirectoryPath != null && fileName != null){
					song = new File(fileDirectoryPath+fileName);
					if(nbApparition == 0){
						gui.completeGUIStructure(playingOrRecording,song);
					}
					else{
						gui.initSoundSource(playingOrRecording,song);
					}
					this.setVisible(false);
					gui.setVisible(true);
					++nbApparition;
				}
				else{
					this.nbApparition = previousNbApparition;
				}
			}
			else{
				this.setVisible(false);
				if(nbApparition ==0){
					gui.completeGUIStructure(playingOrRecording,song);//la fenetre est construite pour la premier fois donc on appele les méthodes init etc...
				}
				else{
					gui.goSplasModeFromSoundSourceFrame(playingOrRecording,song);//la fenetre est déjà construite on appelle le splash mode
				}
				
				gui.setVisible(true);
			}
		}
	}
	
	public boolean getApparitionBeforePainting(){
		return this.apparitionBeforePainting;
	}
	
	public void setApparitionBeforePainting(){
		this.apparitionBeforePainting = false;
	}
}