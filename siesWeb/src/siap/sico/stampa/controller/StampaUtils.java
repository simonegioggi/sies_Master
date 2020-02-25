package siap.sico.stampa.controller;

import siap.controller.SiapController;
import f3b.util.F3BException;


/**
 * <p>Title: StampaUtils</p>
 * <p>Description: Classe di utilità per la stampa dei template</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class StampaUtils extends SiapController
{
  public StampaUtils()
  {
  }

	/**
	* Il metodo restituisce il codice del fascicolo decodificato, compresivo della
	* stringa "R.E.S." O "P.T.", nel formato nuemero prot./lettera-numero
	* 
	* @param String aCodiceSiep 
	* @return String aCodiceOrigine
	*/
	public static String getCodiceOrigine(String aCodiceSiep) throws F3BException
	{
	  try
	  {
		  String oldCodice = new String(aCodiceSiep);
		  Integer intCodice;
		  int codAppo;
		  // Se la lunghezza del codice è minore uguale a 6 non deve essere trattato -> RES
		  if(aCodiceSiep.length()<=6 && !aCodiceSiep.startsWith("8")){
			  oldCodice = "R.E.S. " + aCodiceSiep;  
		  }
		  // Se la lunghezza è 6 (e inizia con 8) devo solamente sottrarre 800.000 -> PT 
		  if(aCodiceSiep.length()==8){
			  intCodice = new Integer(aCodiceSiep);			  
			  codAppo = intCodice.intValue() - 800000 ;
			  oldCodice = "P.T. " + codAppo;			  
		  }		  
		  if(aCodiceSiep.length()>8){
			  // isolo e tratto la sezione finale 
			  String uno = aCodiceSiep.substring(aCodiceSiep.length()-2, aCodiceSiep.length());
			  String due = aCodiceSiep.substring(aCodiceSiep.length()-4, aCodiceSiep.length()-2);
			  String tre = aCodiceSiep.substring(aCodiceSiep.length()-6, aCodiceSiep.length()-4);
			  
			  String stringaFinale = decodStr(uno) + decodStr(due) + decodStr(tre);
			  
			  // Se la lunghezza è 12 (e inizia con 9) devo togliere 
			  // le 6 cifre finali e trattarle per la decodifica (P.T.)
			  if(aCodiceSiep.length()==12 && aCodiceSiep.startsWith("9")){				  
				  intCodice = new Integer(aCodiceSiep.substring(0,  (aCodiceSiep.length()-6)));
				  codAppo = intCodice.intValue() - 900000 ;
				  oldCodice = "R.E.S. " + codAppo + "/" + stringaFinale;			  			  				  
			  }
			  // Se la lunghezza è 13 (e inizia con 1) devo togliere 
			  // le 6 cifre finali e trattarle per la decodifica (P.T.)
			  if(aCodiceSiep.length()==13 && aCodiceSiep.startsWith("1")){
				  intCodice = new Integer(aCodiceSiep.substring(0,  (aCodiceSiep.length()-6)));
				  codAppo = intCodice.intValue() - 1700000 ;
				  oldCodice = "P.T. " + codAppo + "/" + stringaFinale;
			  }			  
		  }
		/*  else{
			  oldCodice= aCodiceSiep;
		  }---non ci entrera' mai qui...*/
		  
		  return oldCodice;
	  }
	  catch (Exception ex)
	  {		  
	    throw new F3BException("Errore in: getCodiceOrigine()" + ex);
	  }	  
}

	/**
	 * Metodo per la conversione dei 6 numeri finali del codice SIEP
	 * nelle lettere originarie del codice R.E.S. o P.T.
	 * 
	 * @param aStr codice iniziale
	 * @return decStr stringa originaria
	 */
	private static String decodStr(String aStr){		
		String decStr="";
		int a = Integer.parseInt(aStr); 
		
		if(a>0 && a<27)
			decStr = Character.toString((char)(a+64));;
		
		if(a>30 && a<40)
			decStr = Character.toString((char)(a+18));;
		
		/* switch (a) {
		 	//stringhe
		 	case 1: decStr="A"; break;
		 	case 2: decStr="B"; break;
		 	case 3: decStr="C"; break;
		 	case 4: decStr="D"; break;
		 	case 5: decStr="E"; break;
		 	case 6: decStr="F"; break;
		 	case 7: decStr="G"; break;
		 	case 8: decStr="H"; break;
		 	case 9: decStr="I"; break;
		 	case 10: decStr="J"; break;
		 	case 11: decStr="K"; break;
		 	case 12: decStr="L"; break;
		 	case 13: decStr="M"; break;
		 	case 14: decStr="N"; break;
		 	case 15: decStr="O"; break;
		 	case 16: decStr="P"; break;
		 	case 17: decStr="Q"; break;
		 	case 18: decStr="R"; break;
		 	case 19: decStr="S"; break;
		 	case 20: decStr="T"; break;
		 	case 21: decStr="U"; break;
		 	case 22: decStr="V"; break;
		 	case 23: decStr="W"; break;
		 	case 24: decStr="X"; break;
		 	case 25: decStr="Y"; break;
		 	case 26: decStr="Z"; break;
		 	//nuemeri
		 	case 31: decStr="1"; break;
		 	case 32: decStr="2"; break;
		 	case 33: decStr="3"; break;
		 	case 34: decStr="4"; break;
		 	case 35: decStr="5"; break;
		 	case 36: decStr="6"; break;
		 	case 37: decStr="7"; break;
		 	case 38: decStr="8"; break;
		 	case 39: decStr="9"; break;
		 		 
		 	default: decStr="";
		 	break;
		 }*/
		
		return decStr;
	}
	

}