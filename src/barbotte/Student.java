package barbotte;

import java.util.HashMap;
import barbotte.Config;
/**
 * Classe permettant de  gérer les étudiants
 * @author sebastienlapeyre
 *
 */
public class Student {
    private final String tagRFID;
    private final String prenom;
    private final String etablissement;
    private long derniereVisite;
    private int nbVerre;
    public static HashMap<String,Student> students = new HashMap<>();
    
    /**
     * Constructeur
     * @param tagRFID
     * @param prenom
     * @param etablissement
     * @return le student créé
     */
    public Student(String tagRFID, String prenom, String etablissement, int nbVerre) {
        this.tagRFID = tagRFID;
        this.prenom = prenom;
        this.etablissement = etablissement;
        this.derniereVisite = System.currentTimeMillis()-2*Config.DRINK_DELAY;
        this.nbVerre = nbVerre;
    }
    
    /**
     * Permet d'incrémenter le nombre de verre bu par l'étudiant
     */
    public void IncrNbVerre(){
        nbVerre++;
        DataBaseManager.incNbVerre(tagRFID,nbVerre);
    }

    public static Student getRecordman(){
        Student rec = null;
        for(Student s: students.values()){
            if(rec == null){
                rec = s;
            }else{
                if(rec.getNbVerre()<s.getNbVerre()){
                    rec = s;
                }
            }
        }
        return rec;
    }
    /**
     * Permet de récupérer un étudiant de la hashmap students
     * @param tagRFID
     * @return l'étudiant correspondant au tag RFID
     */
    public static Student getStudent(String tagRFID){
        if(students.containsKey(tagRFID)){
            return students.get(tagRFID);
        }
        return null;
    }
    /**
     * Ajoute un étudiant dans la hashmap students
     * @param student
     */
    public static void addStudent(Student student){
        students.put(student.getTagRFID(), student);
    }
    
    /**
     * 
     * @param tagRFID
     */
    public static void updateStudent(String tagRFID){
        Student s = students.get(tagRFID);
        s.setDerniereVisite(System.currentTimeMillis());
        s.IncrNbVerre();
    }
    
    // ***** Getter and setter *****
    
	public long getDerniereVisite() {
		return derniereVisite;
	}
	public void setDerniereVisite(long derniereVisite) {
		this.derniereVisite = derniereVisite;
	}
	public int getNbVerre() {
		return nbVerre;
	}
	public void setNbVerre(int nbVerre) {
		this.nbVerre = nbVerre;
	}
	public String getTagRFID() {
		return tagRFID;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getEtablissement() {
		return etablissement;
	}

}
