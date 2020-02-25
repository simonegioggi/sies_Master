package siap.sico.jms.action;

import siap.jms.ICostantiJMS;
import f3b.web.IWebConstants;


public interface ICostantiSicoJMS extends ICostantiJMS
{
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO = "GiornoDataTrasmissioneInizio";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE_INIZIO = "MeseDataTrasmissioneInizio";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO = "AnnoDataTrasmissioneInizio";
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_FINE = "GiornoDataTrasmissioneFine";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE_FINE = "MeseDataTrasmissioneFine";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE_FINE = "AnnoDataTrasmissioneFine";
  public static final String CAMPO_COD_TIPO_OPERAZIONE = "CodTipoOperazione";
  public static final String CAMPO_DESC_TIPO_OPERAZIONE = "DescTipoOperazione";
  public static final String CAMPO_TIPO_ESITO = "TipoEsito";
  public static final String CAMPO_TIPO_UTENTE = "TipoUtente";
  public static final String CAMPO_COD_UTENTE = "CodUtente";
  public static final String CAMPO_TIPO_UFFICIO = "TipoUfficio";
  
  public static final String CAMPO_FLAG_VISTO = "FlagVisto";

  public static final String PG_DETTAGLIO_SOGGETTO_TROVATO = IWebConstants.ROOT_DIR + "files/siap/sico/jms/DettaglioMessaggioSoggettoTrovato.jsp";
  // STUB 31/05/2005.
  public static final String PG_DETTAGLIO_SOGGETTI_TROVATI = IWebConstants.ROOT_DIR + "files/siap/sico/jms/DettaglioMessaggioSoggettiTrovati.jsp";
  public static final String PG_LOAD_LISTAMESSAGGITRASMESSI = IWebConstants.ROOT_DIR + "files/siap/sico/jms/LoadListaMessaggiTrasmessi.jsp";
  public static final String PG_LOAD_LISTA_ESITI_RICERCA_FASC_ALTRE_BDI = IWebConstants.ROOT_DIR + "files/siap/sico/jms/LoadListaEsitiRicercaFascAltreBDI.jsp"; // STUB 18/03/2005
  public static final String PG_LOAD_LISTA_ESITI_RICERCA_SOGG_ALTRE_BDI = IWebConstants.ROOT_DIR + "files/siap/sico/jms/LoadListaEsitiRicercaSoggAltreBDI.jsp"; // STUB 13/06/2005
}