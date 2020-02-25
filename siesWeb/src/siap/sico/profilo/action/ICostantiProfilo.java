package siap.sico.profilo.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiProfilo</p>
* <p>Description: Classe di costanti di Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiProfilo
{
		 public static final String CAMPO_COD_PROFILO = "CodProfilo";
		 public static final String CAMPO_DESCRIZIONE = "Descrizione";
		 public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
		 public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
		 public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
		 public static final String PG_LOAD_RICERCAPROFILO	= IWebConstants.ROOT_DIR + "files/siap/sico/profilo/LoadRicercaProfilo.jsp";
		 public static final String PG_LOAD_DETTAGLIOPROFILO	= IWebConstants.ROOT_DIR + "files/siap/sico/profilo/LoadRicercaProfilo.jsp";
		 public static final String PG_RICERCAPROFILO	= IWebConstants.ROOT_DIR + "files/siap/sico/profilo/RicercaProfilo.jsp";
		 public static final String PG_LOAD_INSERISCIPROFILO	= IWebConstants.ROOT_DIR + "files/siap/sico/profilo/LoadInserisciProfilo.jsp";
                 public static final String PG_LISTA_PROFILI	= IWebConstants.ROOT_DIR + "files/siap/sico/profilo/ListaProfili.jsp";
}