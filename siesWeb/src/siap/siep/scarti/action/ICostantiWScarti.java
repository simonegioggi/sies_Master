package siap.siep.scarti.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiWScarti</p>
* <p>Description: Classe di costanti di WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiWScarti
{
		 public static final String CAMPO_ID_SCARTI = "IdScarti";
		 public static final String CAMPO_TABELLA = "Tabella";
		 public static final String CAMPO_ANN_RES = "AnnRes";
		 public static final String CAMPO_NUM_RES = "NumRes";
		 public static final String CAMPO_LET_RES = "LetRes";
		 public static final String CAMPO_CHIAVE_ALTERNATIVA = "ChiaveAlternativa";
		 public static final String CAMPO_NOTE_SCARTO = "NoteScarto";
		 public static final String CAMPO_CAUSA_SCARTO = "CausaScarto";
		 public static final String PG_LOAD_RICERCAWSCARTI	= IWebConstants.ROOT_DIR + "files/siap/siep/scarti/LoadRicercaWScarti.jsp";
		 public static final String PG_LOAD_DETTAGLIOWSCARTI	= IWebConstants.ROOT_DIR + "files/siap/siep/scarti/LoadRicercaWScarti.jsp";
		 public static final String PG_RICERCAWSCARTI	= IWebConstants.ROOT_DIR + "files/siap/siep/scarti/RicercaWScarti.jsp";
		 public static final String PG_LOAD_INSERISCIWSCARTI	= IWebConstants.ROOT_DIR + "files/siap/siep/scarti/LoadInserisciWScarti.jsp";
     public static final String PG_LISTA_WSCARTI = IWebConstants.ROOT_DIR + "files/siap/siep/scarti/ListaWScarti.jsp";
}