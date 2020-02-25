package siap.siep.posizionemateriale.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPosizioneMateriale</p>
* <p>Description: Classe di costanti di PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPosizioneMateriale
{
	 public static final String CAMPO_COD_POSIZIONE_MATERIALE = "CodPosizioneMateriale";
	 public static final String CAMPO_COD_UFFICIO = "CodUfficio";
	 public static final String CAMPO_DESC_POSIZIONE_MATERIALE = "DescPosizioneMateriale";
	 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	 public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	 public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
   public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
   public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
   public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
   public static final String CAMPO_FILTRO_DATA = "FiltroData";
	 
   public static final String PG_LOAD_RICERCAPOSIZIONEMATERIALE	= IWebConstants.ROOT_DIR + "files/siap/siep/posizionemateriale/LoadRicercaPosizioneMateriale.jsp";
	 public static final String PG_LOAD_DETTAGLIOPOSIZIONEMATERIALE	= IWebConstants.ROOT_DIR + "files/siap/siep/posizionemateriale/DettaglioPosizioneMateriale.jsp";
	 public static final String PG_RICERCAPOSIZIONEMATERIALE	= IWebConstants.ROOT_DIR + "files/siap/siep/posizionemateriale/RicercaPosizioneMateriale.jsp";
	 public static final String PG_LOAD_INSERISCIPOSIZIONEMATERIALE	= IWebConstants.ROOT_DIR + "files/siap/siep/posizionemateriale/LoadInserisciPosizioneMateriale.jsp";
   public static final String PG_TOOLBAR_HEADER  = IWebConstants.ROOT_DIR + "files/siap/siep/posizionemateriale/toolbar_header.jsp";
}