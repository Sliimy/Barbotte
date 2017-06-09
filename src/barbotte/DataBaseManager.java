package barbotte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import barbotte.Config;
import barbotte.Student;
/**
 * Classe permettant de gérer la base de données
 * @author sebastienlapeyre
 *
 */
public class DataBaseManager {

	private static Connection connection;
	/**
	 * Constructeur
	 */
	private DataBaseManager() {
		try {
			//probleme de librairie j'ai du aller chercher le jar
			Class.forName(Config.BDD_DRIVER);
			connection = DriverManager.getConnection("jdbc:mysql://localhost/challenge.db",
					"root", "azerty");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Récupère l'étudiant dans la base de donnée grâce à son code RFID
	 * @param tagRFID
	 * @return etudiant correspondant
	 */
	public Student getStudent(String tagRFID) {
		Student s = null;
		System.out.println("test");
		try {
			PreparedStatement q = connection
					.prepareStatement("SELECT code_rfid,prenom,s.nom,nbVerre"
							+" FROM correspondances_cartes c, etudiants e, structures s"
							+" WHERE c.code_rfid = ? AND c.code_barre = e.carte_id"
							+" AND e.structure_id = s.id");
					System.out.println("test 321");
			q.setString(1, tagRFID);
			ResultSet r = q.executeQuery();
			if (r.next()) {
				s = new Student(r.getString(1), r.getString(2),r.getString(3),r.getInt(4));
				System.out.println(s.getTagRFID() + "/" + s.getPrenom() + "/"
						+ s.getEtablissement() + "/" + s.getDerniereVisite()
						+ "/" + s.getNbVerre());
			}
		} catch (Exception e) {
					System.out.println("test46548");
			e.printStackTrace();
		}
		return s;
	}
	public static School getSchool(String name) {
		School scholl = null;
		try {
			PreparedStatement q = connection
					.prepareStatement("SELECT s.nom,s.nbWin"
							+" FROM structures s"
							+" WHERE s.nom = ?");
			q.setString(1, name);
			ResultSet r = q.executeQuery();
			if (r.next()) {
				scholl = new School(r.getString(1),r.getInt(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scholl;
	}
	
    public static void incNbVerre(String tagRFID,Integer nbVerre){
        tagRFID = tagRFID.toUpperCase();
        try{
            PreparedStatement q = connection.prepareStatement("UPDATE etudiants"
                    +" SET nbVerre= ? WHERE (SELECT etudiants.carte_id FROM correspondances_cartes"
            		+" WHERE correspondances_cartes.code_rfid=?"
                    +" AND correspondances_cartes.code_barre=etudiants.carte_id)");
            q.setInt(1, nbVerre);
            q.setString(2,tagRFID);
            q.executeUpdate();
            System.err.println(tagRFID);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void incNbWin(String school,Integer nbWin){
        //tagRFID = tagRFID.toUpperCase();
        try{
            PreparedStatement q = connection.prepareStatement("UPDATE structures"
                    +" SET nbWin= ? WHERE nom=?");
            q.setInt(1, nbWin);
            q.setString(2,school);
            q.executeUpdate();
            System.err.println(school);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	private static DataBaseManager dbm;

	public static DataBaseManager getInstance() {
		if (dbm == null) {
			dbm = new DataBaseManager();
		}
		return dbm;
	}
}
