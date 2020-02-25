package siap.siepe.relazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRelazione</p>
* <p>Description: Classe di costanti di Relazione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiRelazione
{
  public static final String CAMPO_ID_RELAZIONE = "IdRelazione";
  public static final String CAMPO_ATT_ID_ATTIVITA = "AttIdAttivita";
  public static final String CAMPO_RIC_ID_RICHIESTA = "RicIdRichiesta";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_DOC_BLOB = "DocBlob";
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
  public static final String CAMPO_FLAG_DOCUMENTO_REGISTRATO = "FlagDocumentoRegistrato";
  public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";

  
  public static final String PG_LOAD_RICERCARELAZIONE = IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/LoadRicercaRelazione.jsp";
  public static final String PG_DETTAGLIO_RELAZIONE = IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/DettaglioRelazione.jsp";
  public static final String PG_TRASFERISCI_RELAZIONE = IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/TrasferisciRelazione.jsp";
  public static final String PG_RICERCARELAZIONE = IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/RicercaRelazione.jsp";
  public static final String PG_ELENCO_RELAZIONI = IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/ElencoRelazioni.jsp";
  public static final String PG_LOAD_INSERISCI_RELAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/LoadInserisciRelazione.jsp";
  public static final String PG_BUTTON_INSERISCIRELAZIONE	= IWebConstants.ROOT_DIR + "files/siap/siepe/relazione/ButtonInserisciRelazione.jsp";
}
