package siap.siep.sentenzariunita.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiSentenzaRiunita</p>
* <p>Description: Classe di costanti di SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiSentenzaRiunita
{
		 public static final String CAMPO_ID_SENTENZA_RIUNITA = "IdSentenzaRiunita";
		 public static final String CAMPO_GIORNO_DATA_SENTENZA = "GiornoDataSentenza";
		 public static final String CAMPO_MESE_DATA_SENTENZA = "MeseDataSentenza";
		 public static final String CAMPO_ANNO_DATA_SENTENZA = "AnnoDataSentenza";
		 public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza";
		 public static final String CAMPO_NUMERO_SENTENZA = "NumeroSentenza";
		 public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
		 public static final String CAMPO_COD_AUTORITA_EMITTENTE = "CodAutoritaEmittente";
		 public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
		 public static final String CAMPO_SEZIONE_AUTORITA_EMITTENTE = "SezioneAutoritaEmittente";
		 public static final String CAMPO_ANNO_REGE_PM = "AnnoRegePm";
		 public static final String CAMPO_NUMERO_REGE_PM = "NumeroRegePm";
		 public static final String CAMPO_ANNO_REGE_GIP = "AnnoRegeGip";
		 public static final String CAMPO_NUMERO_REGE_GIP = "NumeroRegeGip";
		 public static final String CAMPO_ANNO_REGE_DIB = "AnnoRegeDib";
		 public static final String CAMPO_NUMERO_REGE_DIB = "NumeroRegeDib";
		 public static final String CAMPO_ANNO_REGE_CAS = "AnnoRegeCas";
		 public static final String CAMPO_NUMERO_REGE_CAS = "NumeroRegeCas";
		 public static final String CAMPO_ANNO_REGE_DIB_CAS = "AnnoRegeDibCas";
		 public static final String CAMPO_NUMERO_REGE_DIB_CAS = "NumeroRegeDibCas";

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
		 public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza";

		 // modifica Marzo 2010 
		 public static final String CAMPO_SEDE_NOTIZIA_REATO			= "SedeNotiziaReato";		 
		 // fine modifica Marzo 2010 
		 
		 public static final String PG_LOAD_RICERCASENTENZARIUNITA	= IWebConstants.ROOT_DIR + "files/siap/siep/sentenzariunita/LoadRicercaSentenzaRiunita.jsp";
		 public static final String PG_LOAD_DETTAGLIOSENTENZARIUNITA	= IWebConstants.ROOT_DIR + "files/siap/siep/sentenzariunita/DettaglioSentenzaRiunita.jsp";
		 public static final String PG_RICERCASENTENZARIUNITA	= IWebConstants.ROOT_DIR + "files/siap/siep/sentenzariunita/RicercaSentenzaRiunita.jsp";
		 public static final String PG_LOAD_INSERISCISENTENZARIUNITA	= IWebConstants.ROOT_DIR + "files/siap/siep/sentenzariunita/LoadInserisciSentenzaRiunita.jsp";

		 public static final String PG_RICERCASENTENZARIUNITAFASCICOLO	= IWebConstants.ROOT_DIR + "files/siap/siep/sentenzariunita/RicercaSentenzaRiunitaFascicolo.jsp";
}