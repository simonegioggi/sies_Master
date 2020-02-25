package siap.sico.magistrato.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMagistrato</p>
* <p>Description: Classe di costanti di Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiMagistrato
{
  public static final String CAMPO_COD_MAGISTRATO               = "CodMagistrato";
  public static final String CAMPO_COGNOME                      = "Cognome";
  public static final String CAMPO_NOME                         = "Nome";
  public static final String CAMPO_FLAG_STATO                   = "FlagStato";
  public static final String CAMPO_COD_UFFICIO_APPARTENENZA     = "CodUfficioAppartenenza";
  public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA  = "GiornoDataInizioValidita";
  public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA    = "MeseDataInizioValidita";
  public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA    = "AnnoDataInizioValidita";
  public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA    = "GiornoDataFineValidita";
  public static final String CAMPO_MESE_DATA_FINE_VALIDITA      = "MeseDataFineValidita";
  public static final String CAMPO_ANNO_DATA_FINE_VALIDITA      = "AnnoDataFineValidita";
  public static final String CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE  = "GiornoDataInizioAssegnazione";
  public static final String CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE    = "MeseDataInizioAssegnazione";
  public static final String CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE    = "AnnoDataInizioAssegnazione";
  public static final String CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE    = "GiornoDataFineAssegnazione";
  public static final String CAMPO_MESE_DATA_FINE_ASSEGNAZIONE      = "MeseDataFineAssegnazione";
  public static final String CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE      = "AnnoDataFineAssegnazione";  
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento";
  public static final String CAMPO_E_MAIL_UFFICIO               = "EMailUfficio";
  public static final String CAMPO_E_MAIL_PRIVATA               = "EMailPrivata";
  public static final String CAMPO_NUM_CELLULARE                = "NumCellulare";
  public static final String CAMPO_COD_MAGISTRATO_VECCHIO       = "CodMagistratoVecchio";

  public static final String PG_LOAD_RICERCAMAGISTRATO	  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadRicercaMagistrato.jsp";
  public static final String PG_LOAD_DETTAGLIOMAGISTRATO  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/DettaglioMagistrato.jsp";
  public static final String PG_RICERCAMAGISTRATO	  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/RicercaMagistrato.jsp";
  public static final String PG_LOAD_INSERISCIMAGISTRATO  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadInserisciMagistrato.jsp";
  public static final String PG_ELENCOMAGISTRATI          = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/ElencoMagistrati.jsp";
  public static final String PG_LOAD_RICERCA_MAGISTRATO_LISTA  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadRicercaMagistratoLista.jsp";
  public static final String PG_RICERCA_MAGISTRATO_LISTA  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/RicercaMagistratoLista.jsp";
  public static final String PG_LOAD_RICERCA_MAG  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadRicercaMag.jsp";
  public static final String PG_RICERCA_MAG  = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/RicercaMag.jsp";
  public static final String PG_LOAD_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadRicercaMagistratoAssegnazioneLista.jsp";
  public static final String PG_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/RicercaMagistratoAssegnazioneLista.jsp";
  
  public static final String PG_LOAD_RICERCA_MAGISTRATI_UFFICIO_POPUP = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/LoadRicercaMagistratoUfficioPopup.jsp";
  public static final String PG_LISTA_MAGISTRATI_UFFICIO = IWebConstants.ROOT_DIR + "files/siap/sico/magistrato/ListaMagistratiUfficio.jsp";
  
  public static final String CAMPO_COD_MAGISTRATO_ASS               = "CodMagistratoAss";
  public static final String CAMPO_COGNOME_ASS                      = "CognomeAss";
  public static final String CAMPO_NOME_ASS                         = "NomeAss";
  public static final String CAMPO_CHECK_MAGISTRATO_ASS	            = "CheckMagAss";
}