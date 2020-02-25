package siap.sico.log.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiLogAttivita</p>
* <p>Description: Classe di costanti di LogAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiLogAttivita
{
		 public static final String CAMPO_RECORD = "Record"; 
		 public static final String CAMPO_COD_OPERATORE = "CodOperatore"; 
		 public static final String CAMPO_GIORNO_DATA = "GiornoData"; 
		 public static final String CAMPO_MESE_DATA = "MeseData"; 
		 public static final String CAMPO_ANNO_DATA = "AnnoData"; 
		 public static final String CAMPO_IP_UTENTE = "IpUtente"; 
		 public static final String CAMPO_AZIONE_CONTESTO_JAVA = "AzioneContestoJava"; 
		 public static final String PG_LOAD_RICERCALOGATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/sico/log/LoadRicercaLogAttivita.jsp";
		 public static final String PG_LOAD_DETTAGLIOLOGATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/sico/log/LoadRicercaLogAttivita.jsp";
		 public static final String PG_RICERCALOGATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/sico/log/RicercaLogAttivita.jsp";
		 public static final String PG_LOAD_INSERISCILOGATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/sico/log/LoadInserisciLogAttivita.jsp";
}