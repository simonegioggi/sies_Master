package siap.sige.aula.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiAula</p>
* <p>Description: Classe costanti di Aula</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface ICostantiAula
{
	 public static final String CAMPO_ID_AULA = "IdAula";
	 public static final String CAMPO_ID_SEZIONE = "IdSezione";
	 public static final String CAMPO_DESCRIZIONE_AULA = "DescrizioneAula";
	 public static final String CAMPO_DESCRIZIONE_STANZA = "DescrizioneStanza";
	 public static final String CAMPO_DESCRIZIONE_INGRESSO = "DescrizioneIngresso";
	 public static final String CAMPO_NUMERO_PIANO = "NumeroPiano";
	 public static final String CAMPO_FLAG_PREDEFINITA = "FlagPredefinita";

	 public static final String PG_BUTTONS_AULA         = IWebConstants.ROOT_DIR + "files/siap/sige/aula/buttonsAula.jsp";
	 public static final String PG_TOOLBAR_HEADER_AULA  = IWebConstants.ROOT_DIR + "files/siap/sige/aula/toolbar_header_aula.jsp";
	 
	 public static final String PG_LOAD_INSERISCI_AULA	= IWebConstants.ROOT_DIR + "files/siap/sige/aula/LoadInserisciAula.jsp";
	 public static final String PG_LOAD_RICERCA_AULA	= IWebConstants.ROOT_DIR + "files/siap/sige/aula/LoadRicercaAula.jsp";
	 public static final String PG_LOAD_DETTAGLIO_AULA  = IWebConstants.ROOT_DIR + "files/siap/sige/aula/DettaglioAula.jsp";
	 public static final String PG_RICERCA_AULA         = IWebConstants.ROOT_DIR + "files/siap/sige/aula/RicercaAula.jsp";
	 public static final String POPUP_RICERCA_AULA         = IWebConstants.ROOT_DIR + "files/siap/sige/aula/PopUpRicercaAula.jsp";
	 
}