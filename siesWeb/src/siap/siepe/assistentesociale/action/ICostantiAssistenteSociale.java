package siap.siepe.assistentesociale.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiAssistenteSociale</p>
* <p>Description: Classe di costanti di Assistente Sociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiAssistenteSociale
{
  public static final String CAMPO_ID_ASSISTENTE_SOCIALE = "IdAssistenteSociale";
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

  public static final String PG_LOAD_RICERCAASSISTENTESOCIALE	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/LoadRicercaAssistenteSociale.jsp";
  public static final String PG_LOAD_DETTAGLIOASSISTENTESOCIALE	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/DettaglioAssistenteSociale.jsp";
  public static final String PG_RICERCAASSISTENTESOCIALE	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/RicercaAssistenteSociale.jsp";
  public static final String PG_LOAD_INSERISCIASSISTENTESOCIALE	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/LoadInserisciAssistenteSociale.jsp";
  public static final String PG_LOAD_RICERCA_ASSISTENTESOCIALE_LISTA	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/LoadRicercaAssistenteSocialeLista.jsp";
  public static final String PG_RICERCA_ASSISTENTESOCIALE_LISTA	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesociale/RicercaAssistenteSocialeLista.jsp";
}
