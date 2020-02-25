package siap.sige.curatore.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiCuratore</p>
* <p>Description: Classe di costanti di Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public interface ICostantiCuratore
{
	 public static final String CAMPO_ID_CURATORE = "IdCuratore";
	 public static final String CAMPO_COGNOME = "Cognome";
	 public static final String CAMPO_NOME = "Nome";
	 public static final String CAMPO_INDIRIZZO = "Indirizzo";
	 public static final String CAMPO_TELEFONO = "Telefono";
	 public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
	 public static final String CAMPO_EMAIL = "Email";
	 public static final String CAMPO_FAX = "Fax";
	 public static final String CAMPO_CELLULARE = "Cellulare";
	 public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
	 public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
	 public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
	 public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
	 public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
	 public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
	 public static final String CAMPO_FLAG_STATO = "FlagStato";
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
	 public static final String CAMPO_CODICE_FISCALE = "CodiceFiscale";

   public static final String PG_LOAD_RICERCACURATORE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/LoadRicercaCuratore.jsp";
	 public static final String PG_LOAD_DETTAGLIOCURATORE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/DettaglioCuratore.jsp";
	 public static final String PG_RICERCACURATORE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/RicercaCuratore.jsp";
	 public static final String PG_LOAD_INSERISCICURATORE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/LoadInserisciCuratore.jsp";
   public static final String PG_LOAD_RICERCA_CURATORE_LISTA	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/LoadRicercaCuratoreLista.jsp";
	 public static final String PG_RICERCA_CURATORE_LISTA	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/curatore/RicercaCuratoreLista.jsp";
}