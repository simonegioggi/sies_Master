package siap.sico.assistentegiudiziario.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiAssistenteGiudiziario</p>
* <p>Description: Classe di costanti di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiAssistenteGiudiziario
{
		 public static final String CAMPO_ID_ASSISTENTE_GIUDIZIARIO = "IdAssistenteGiudiziario";
		 public static final String CAMPO_COGNOME = "Cognome";
		 public static final String CAMPO_NOME = "Nome";
		 public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
		 public static final String CAMPO_FLAG_STATO = "FlagStato";
		 public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
		 public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
		 public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
		 public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
		 public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
		 public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
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
		 public static final String PG_LOAD_RICERCAASSISTENTEGIUDIZIARIO	= IWebConstants.ROOT_DIR + "files/siap/sico/assistentegiudiziario/LoadRicercaAssistenteGiudiziario.jsp";
		 public static final String PG_LOAD_DETTAGLIOASSISTENTEGIUDIZIARIO	= IWebConstants.ROOT_DIR + "files/siap/sico/assistentegiudiziario/DettaglioAssistenteGiudiziario.jsp";
		 public static final String PG_RICERCAASSISTENTEGIUDIZIARIO	= IWebConstants.ROOT_DIR + "files/siap/sico/assistentegiudiziario/RicercaAssistenteGiudiziario.jsp";
		 public static final String PG_LOAD_INSERISCIASSISTENTEGIUDIZIARIO	= IWebConstants.ROOT_DIR + "files/siap/sico/assistentegiudiziario/LoadInserisciAssistenteGiudiziario.jsp";
}