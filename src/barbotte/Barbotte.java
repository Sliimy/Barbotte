package barbotte;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import barbotte.DataBaseManager;
import barbotte.Student;
import barbotte.BarbotteUI;
import barbotte.School;
/**
 * @author Sébastien Lapeyre P2017/2018
 * @version V1
 * Cette classe contient le main de notre projet
 */
public class Barbotte {
	private final BarbotteUI barbotteUi;
	private PinManager pinManager;
	private Scanner sc;
	private final DataBaseManager dbm;
	private Student student;
	private School school;
	/**
	 * Constructeur
	 */
	public Barbotte() {
		barbotteUi = new BarbotteUI();
		pinManager=new PinManager();
		sc = new Scanner(System.in);
		// TODO verifier l'utilité de get instance
		dbm = DataBaseManager.getInstance();
		student = null;
		loadConfig();
	}
	/*
	 * Permet de charger les admins ainsi que le podium
	 */
	private void loadConfig() {
		Config.loadAdmins();
	}
	
	public void start () throws InterruptedException{
		while(true){			
			pinManager.lowPin(1);
			ArrayList<String> classement;
			classement=School.classement();
			if (classement!=null){
				barbotteUi.printMessage("Bienvenue au bar de l'Esisar !<br> Pose ton verre et scan ta carte AVE !",classement);
			} else {
				barbotteUi.printMessage("Bienvenue au bar de l'Esisar !<br> Pose ton verre et scan ta carte AVE !");
			}
			 
			// ********************************************
			student = readCard();
			if (student!=null){
				school = School.getSchool(student.getEtablissement());
				if((student.getDerniereVisite()+Config.DRINK_DELAY) < System.currentTimeMillis()){
					int res;
		            if (!Config.ADMIN.containsKey(student.getTagRFID())){
		            	barbotteUi.printMessage("Bonjour "+student.getPrenom()+"<br>("+student.getEtablissement()+")!<br>Tu as gagné "+student.getNbVerre()+" parties...<br>");
		            	res=startGame();
		            }else{
		            	affichageAdmin();
		            	res=startGame();
		            }
		            if (res==1){
						 barbotteUi.printMessage("Félicitation !! <br> Tu es maintenant à "+student.getNbVerre()+" partie(s) gagnée(s)<br> Ton école est à la place "+School.getClassement(school.getName()));
						 student.IncrNbVerre();
						 school.incNbWin();
						 pause2();
		            }else if(res==0) {
						 barbotteUi.printMessage("Raté ! <br>Retente ta chance plus tard!");
						 pause2();
		            }else if(res==-1){
						barbotteUi.printMessage("Met le verre dans le support !");
			 }
				}else{
					barbotteUi.printMessage("Attends un peu avant de rejouer");
				}
			}else {
				barbotteUi.printMessage("Carte non reconnue");
				pause();
			}
		}
	}
	/**
	 * récupere l'étudiant ave le scanner
	 * @return étudiant correspondant au RFID taggué
	 */
	private Student readCard() {
		long t = System.currentTimeMillis();
		Student s = null;
		while (sc.hasNextLine()) {
			if (System.currentTimeMillis() - t < 10) {
				sc.nextLine();
			} else {
				break;
			}
		}
		String tagRFID = sc.nextLine().toLowerCase();
		if (tagRFID.equals("quit")) {
			//TODO uncomment methode
			//quit();
		} else if (tagRFID.equals("pause")) {
			//barbotteUi.printMessage("La QuizBox est actuellement en pause!<br>Contacter un admin!");
			//pauseQuizBox();
		} else {
			s = Student.getStudent(tagRFID);
			if (s == null) { // si étudiant pas dans le hashmap on va chercher ses infos dans la Bdd
				s = dbm.getStudent(tagRFID);
				if (s != null) {
					Student.addStudent(s);
				}
			}
		}
		return s;
	}
	
	Boolean timerBool=false;
	Boolean timerBool2=false;
	Boolean timerBool3=false;
	private int startGame() throws InterruptedException{
		if(pinManager.readPin(4)){
			return -1;
		}
		System.out.println("Debut du jeu");
		timerBool=false;
		timerBool2=false;
		timerBool3=false;
		pinManager.highPin(1);
		System.out.println("PinGame 1");
		//TODO définir temps timer
		Timer timer1 = new Timer(30000, new ActionListener(){
			//System.out.println("Action listener");
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timerBool=true;

				System.out.println("Timer jeu saute");
			}
		});
		timer1.setRepeats(false);
		timer1.start();
		Boolean valueButton=pinManager.readPin(2);
		Boolean valueCaptor=pinManager.readPin(4);
		do{
			valueButton=pinManager.readPin(2);
			valueCaptor=pinManager.readPin(4);
			System.out.println("Value button :"+valueButton+ "ValueCaptor "+valueCaptor);
			Thread.sleep(100);
		}while (!timerBool  && !valueButton && !valueCaptor );
System.out.println("timerboo ="+timerBool+"  valueButoon ="+valueButton);
		if (timerBool){
			timerBool=false;
			System.out.println("PinGame 0");
			pinManager.lowPin(1);
			return 0;
		} else if(valueCaptor){
				System.out.println("Pas de verre");
				pinManager.lowPin(1);
			return -1;
		} else{
			timerBool=false;
			Timer timer2 = new Timer(15000, new ActionListener(){
				//System.out.println("Action listener");
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					timerBool2=true;
					System.out.println("Timer 2 jeu saute");
				}
			});
			timer2.setRepeats(false);
			timer2.start();
			//TODO Attendre résultat capteur sharp
			Boolean valueEndGame=pinManager.readPin(3);
			do{
				 valueEndGame=pinManager.readPin(3);
				valueCaptor=pinManager.readPin(4);
				System.out.println("Dans le 2eme while timerbool2 ="+timerBool2+"  valueEndGame ="+valueEndGame+ "ValueCaptor "+valueCaptor);
				Thread.sleep(100);
			}while (!timerBool2 && !valueEndGame && !valueCaptor );
			System.out.println("Sortie 2 eme while timerbool2 ="+timerBool2+"  valueEndGame ="+valueEndGame);
			//pinManager.lowPin(1);
			if (timerBool2){
				timerBool2=true;
				return 0;
			}else if(valueCaptor){
				System.out.println("Pas de verre");
				return -1;
			} else{
				Timer timer3 = new Timer(2000, new ActionListener(){
					//System.out.println("Action listener");
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						timerBool3=true;
						System.out.println("Timer 3 Victory saute");
						}
					});
				timer3.setRepeats(false);
				timer3.start();
				Boolean valueVictory=pinManager.readPin(5);
				do{
					valueVictory=pinManager.readPin(5);
					valueCaptor=pinManager.readPin(4);
					System.out.println("DAns le 3eme while timerbool3 ="+timerBool3+"  valueEndGame ="+valueEndGame+ "ValueCaptor "+valueCaptor);
					Thread.sleep(10);
				}while (!timerBool3 && valueVictory && !valueCaptor );
				pinManager.lowPin(1);
				System.out.println("Sortie 3 eme while timerbool3 ="+timerBool3+"  valueVictory ="+valueVictory);
				if (timerBool3){
					return 0;
				}else if (valueCaptor){
					return -1;
				}else{
					return 1;
				}
			}
		}
	}
//	private int startGame() throws InterruptedException{
//		return 1;
//	}
	
	private void pause() {
		int sec = Config.MESSAGE_TIME;
		try {
			Thread.sleep(1000 * sec);
		} catch (Exception e) {
		}
	}
	private void pause2() {
		int sec = Config.MESSAGE_TIME;
		try {
			Thread.sleep(2000 * sec);
		} catch (Exception e) {
		}
	}

	private void affichageAdmin(){
		String urlImage = Config.ADMIN.get(student.getTagRFID());
		switch (student.getTagRFID()){
		case "0004CFCE8A774D80" : //torelli
			barbotteUi.printMessageAdmin("Petit cadeau :) <br>Utilises les boutons pour jouer",urlImage,0,0,700,700);
			break;
		case "000455C38A774D80" : //alexiaMuret
			barbotteUi.printMessageAdmin("Petit cadeau :) <br>Utilises les boutons pour jouer",urlImage,0,0,500,500);
			break;
		case "000411348A774D85" : //popoff
			barbotteUi.printMessageAdmin("Avec Rosinski ça marche de suite mieux !!!");
			break;
		case "00048C7C8A774D80" : //Celestin
			barbotteUi.printMessageAdmin("Avec Rosinski ça marche de suite mieux !!!");	
			break;
		case "000479908A774D80" : //tellier
			barbotteUi.printMessageAdmin("Super Taff respo-Challenge !! <br> Allez profites maintenant !!");
			break;
		case "0004C06D8A774D80" : // martin gab
			barbotteUi.printMessageAdmin("Réservé au pocesseurs de bracelet VIP !!");
			pause();
			break;
		}
		
	}
	/**
	 * Fonction main lancée lors de l'exécution du programme
	 * @param args
	 * @throws InterruptedException 
	 */
	public void testLogo(){
		ArrayList<String> liste= new ArrayList<>();
		liste.add(0, Config.IMAGE_ABIIS);
		liste.add(1, Config.IMAGE_AEM);
		liste.add(2, Config.IMAGE_AFESS);
		barbotteUi.printMessage("test", liste);
		pause();
		liste= new ArrayList<>();
		liste.add(0, Config.IMAGE_AIV);
		liste.add(1, Config.IMAGE_AVFI);
		liste.add(2, Config.IMAGE_BEC);
		barbotteUi.printMessage("test", liste);
		pause();
		liste= new ArrayList<>();
		liste.add(0, Config.IMAGE_CEDE);
		liste.add(1, Config.IMAGE_CELLVA);
		liste.add(2, Config.IMAGE_CEM);
		barbotteUi.printMessage("test", liste);
		pause();
		liste= new ArrayList<>();
		liste.add(0, Config.IMAGE_CECV);
		liste.add(1, Config.IMAGE_EGC);
		liste.add(2, Config.IMAGE_ESISAR);
		barbotteUi.printMessage("test", liste);
		pause();
		liste= new ArrayList<>();
		liste.add(0, Config.IMAGE_IFC);
		liste.add(1, Config.IMAGE_STAPS);
		liste.add(2, Config.IMAGE_UJF);
		barbotteUi.printMessage("test", liste);
		pause();
		
	}
	public static void main(String[] args) throws InterruptedException {
		Barbotte barbotte = new Barbotte();
		System.out.println("Debut");
		barbotte.start();
		
		/*************************** Test ******/
		//barbotte.testLogo();
//		barbotte.barbotteUi.printMessage("Raté ! <br>Retente ta chance plus tard!");
//		barbotte.student=new Student("azfdj", "lazkef", "ESISAR", 3);
//		barbotte.school = new School("ESISAR", 2);
//		barbotte.barbotteUi.printMessage("Félicitation !! <br> Tu es maintenant à "+barbotte.student.getNbVerre()+" partie(s) gagnée(s)<br> Ton école est "+School.getClassement(barbotte.school.getName())+"ème");
	}
}

