package siap.siep.refertoscarcerazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRefertoScarcerazione</p>
* <p>Description: Classe di costanti di RefertoScarcerazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRefertoScarcerazione
{
		 public static final String CAMPO_ID_REFERTO_SCARCERAZIONE = "IdRefertoScarcerazione";
		 public static final String CAMPO_ANNO_NOTA = "AnnoNota";
		 public static final String CAMPO_NUM_NOTA = "NumNota";
		 public static final String CAMPO_GIORNO_DATA_NOTA = "GiornoDataNota";
		 public static final String CAMPO_MESE_DATA_NOTA = "MeseDataNota";
		 public static final String CAMPO_ANNO_DATA_NOTA = "AnnoDataNota";
		 public static final String CAMPO_GIORNO_DATA_SCARCERAZIONE = "GiornoDataScarcerazione";
		 public static final String CAMPO_MESE_DATA_SCARCERAZIONE = "MeseDataScarcerazione";
		 public static final String CAMPO_ANNO_DATA_SCARCERAZIONE = "AnnoDataScarcerazione";
		 public static final String CAMPO_NOTE = "CampoNote";
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
		 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
		 public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IsDetIdIstitutoDetenzione";

		 public static final String PG_LOAD_RICERCAREFERTOSCARCERAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadRicercaRefertoScarcerazione.jsp";
		 public static final String PG_LOAD_DETTAGLIOREFERTOSCARCERAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadRicercaRefertoScarcerazione.jsp";
		 public static final String PG_RICERCAREFERTOSCARCERAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/RicercaRefertoScarcerazione.jsp";
		 public static final String PG_LOAD_INSERISCIREFERTOSCARCERAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/refertoscarcerazione/LoadInserisciRefertoScarcerazione.jsp";
}