package siap.sius.esperto.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiEsperto</p>
* <p>Description: Classe di costanti di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiEsperto
{
	 public static final String CAMPO_ID_ESPERTO = "IdEsperto";
	 public static final String CAMPO_COGNOME = "CognomeEsp";
	 public static final String CAMPO_NOME = "NomeEsp";
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

   public static final String PG_LOAD_RICERCAESPERTO	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/LoadRicercaEsperto.jsp";
	 public static final String PG_LOAD_DETTAGLIOESPERTO	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/DettaglioEsperto.jsp";
	 public static final String PG_RICERCAESPERTO	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/RicercaEsperto.jsp";
	 public static final String PG_LOAD_INSERISCIESPERTO	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/LoadInserisciEsperto.jsp";
   public static final String PG_LOAD_RICERCA_ESPERTO_LISTA	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/LoadRicercaEspertoLista.jsp";
	 public static final String PG_RICERCA_ESPERTO_LISTA	= 
     IWebConstants.ROOT_DIR + "files/siap/sius/esperto/RicercaEspertoLista.jsp";
}