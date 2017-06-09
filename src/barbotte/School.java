package barbotte;

import java.util.ArrayList;
import java.util.HashMap;

public class School {
	private final String name;
	private final String URLImage;
	int nbWin;
	
	public static HashMap<String,Integer> schools = new HashMap<>();
	
	public School (String name, int nbWin){
		this.name=name;
		this.URLImage=getURLImage(name);
		this.nbWin=nbWin;
	}
	
    public void addSchool(){
        schools.put(this.name, this.nbWin);
    }
    
	public static School getSchool(String school){
		School res;
		if (schools.containsKey(school)){
			System.out.println("1");
			res=new School(school,schools.get(school));
		}else {
			System.out.println("2");
			// TODO : chercher dans la BDD
			res =DataBaseManager.getSchool(school);
			schools.put(res.getName(), res.getNbWin());
		}
		return res;
	}
	/**
	 * Relie le nom d'une école avec le chemin du logo de l'école
	 * @param name
	 * @return URL du logo de l'école
	 */
//	private static String getURLImage(String name){
//	       String res = null;
//	        switch (name) {
//	            case "ABIIS - CREST":
//	                res = Config.IMAGE_ABIIS;
//	                break;
//	            case "AEM - LYCEE MONTPLAISIR":
//	                res = Config.IMAGE_AEM;
//	                break;
//	            case "AFESS - ESSSE":
//	                res = Config.IMAGE_AFESS;
//	                break;
//	            case "AIV - IUT":
//	                res = Config.IMAGE_AIV;
//	                break;
//	            case "ARDE":
//	                res = Config.IMAGE_ARDE;
//	                break;
//	            case "AVFI - IFSI":
//	                res = Config.IMAGE_AVFI;
//	                break;
//	            case "BEC - BRIFFAUT":
//	                res = Config.IMAGE_BEC;
//	                break;
//	            case "CEDE - DROIT ECO":
//	                res = Config.IMAGE_CEDE;
//	                break;
//	            case "CELLVA - LANGUES ET LETTRES":
//	                res = Config.IMAGE_CELLVA;
//	                break;
//	            case "CEM - MAESTRIS":
//	                res = Config.IMAGE_CEM;
//	                break;
//	            case "GERON TAUPIN - CAMILLE VERNET":
//	                res = Config.IMAGE_CV;
//	                break;
//	            case "BDE EGC - EGC":
//	                res = Config.IMAGE_EGC;
//	                break;
//	            case "CERCLE ESISAR - ESISAR":
//	                res = Config.IMAGE_ESISAR;
//	                break;
//	            case "AE IFC - IFC":
//	                res = Config.IMAGE_IFC;
//	                break;
//	            case "BDE STAPS - STAPS":
//	                res = Config.IMAGE_STAPS;
//	                break;
//	            case "ADSV - UJF":
//	                res = Config.IMAGE_UJF;
//	                break;
//	        }
//	        return res;
//	}
	private static String getURLImage(String name){
	       String res = null;
	        switch (name) {
	            case "ABIIS":
	                res = Config.IMAGE_ABIIS;
	                break;
	            case "AEM":
	                res = Config.IMAGE_AEM;
	                break;
	            case "AFESS":
	                res = Config.IMAGE_AFESS;
	                break;
	            case "AIV":
	                res = Config.IMAGE_AIV;
	                break;
	            case "AVFI":
	                res = Config.IMAGE_AVFI;
	                break;
	            case "BEC":
	                res = Config.IMAGE_BEC;
	                break;
	            case "CEDE":
	                res = Config.IMAGE_CEDE;
	                break;
	            case "CELLVA":
	                res = Config.IMAGE_CELLVA;
	                break;
	            case "CEM":
	                res = Config.IMAGE_CEM;
	                break;
	            case "CECV":
	                res = Config.IMAGE_CECV;
	                break;
	            case "EGC":
	                res = Config.IMAGE_EGC;
	                break;
	            case "ESISAR":
	                res = Config.IMAGE_ESISAR;
	                break;
	            case "IFC":
	                res = Config.IMAGE_IFC;
	                break;
	            case "STAPS":
	                res = Config.IMAGE_STAPS;
	                break;
	            case "ADSV":
	                res = Config.IMAGE_UJF;
	                break;
	        }
	        return res;
	}

	
	/**
	 * Donne le classement d'une école 
	 * Attention plusieurs écoles peuvent avoir le même classement si elles ont le même nombre de point
	 * @param ecole
	 * @return
	 */
    public static int getClassement(String ecole ){
        int res = 1;
        for(int a: schools.values()){
            if(schools.get(ecole) < a){
                res++;
            }
        }
    	System.out.println("res :"+res+" ecole : "+ecole);
        return res;
    }
	/**
	 * Permet de récupérer le podium des 3 meilleurs écoles 
	 * dans notre cas celle qui ont le plus gagné de point
	 * La fonction permet que le logo de l'Esisar puisse apparaitre en priorité si égalité avec d'autres écoles
	 * @return
	 */
    public static ArrayList<String> classement(){
    	ArrayList<String> clas = new ArrayList<>();
    	int a=0;
    	if (schools.size()==0){
        	System.out.println("school size 0");
    		return null;
    	} else {
	     	for (int i=0 ; clas.size()<=3 && schools.size()>i;i++){
	        	System.out.println("i :"+i);
	            // Condition pour qu le logo de l'esisar apparaisse en cas d'égalité
	     		if (schools.containsKey("CERCLE ESISAR - ESISAR")){
		            if (getClassement("CERCLE ESISAR - ESISAR")==i+1){
		                clas.add(a,getURLImage("CERCLE ESISAR - ESISAR") );
		                a++;
		                if (a==3){
		                	return clas;
		                }
		            }
	     		}
				for(String ecole : schools.keySet()){
	
					if (getClassement(ecole)==i+1 && !ecole.equals("CERCLE ESISAR - ESISAR")){
						clas.add(a, getURLImage(ecole));
						a++;
		                if (a==3){
		                	return clas;
		                }						
					}
				}
	    	}
    	}
    	return clas;
    }
    /**
     * Augmente l'attribut NbWin de l'école et sauvegarde cette nouvelle valeur dans la Hashmap schools et dans la bas de données
     */
    public void incNbWin(){
    	this.nbWin++;
    	schools.replace(name, nbWin);
    	//TODO : implémenter la BDD
    	DataBaseManager.incNbWin(name, nbWin);
    }

	public int getNbWin() {
		return nbWin;
	}

	public void setNbWin(int nbWin) {
		this.nbWin = nbWin;
	}

	public static HashMap<String, Integer> getSchools() {
		return schools;
	}

	public static void setSchools(HashMap<String, Integer> schools) {
		School.schools = schools;
	}

	public String getName() {
		return name;
	}
    
    
}
