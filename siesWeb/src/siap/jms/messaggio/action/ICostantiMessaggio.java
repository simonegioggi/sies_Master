package siap.jms.messaggio.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMessaggio</p>
* <p>Description: Classe di costanti di Messaggio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiMessaggio
{
  public static final String CAMPO_ID_MESSAGGIO = "IdMessaggio";
  public static final String CAMPO_TIPO_MESSAGGIO = "CodTipoMessaggio";
  public static final String CAMPO_TIPO_OPERAZIONE = "CodTipoOperazione";
  public static final String CAMPO_COD_UFFICIO_MITTENTE = "CodUfficioMittente";
  public static final String CAMPO_COD_BDI_MITTENTE = "CodBdiMittente";
  public static final String CAMPO_COD_UFFICIO_DESTINATARIO = "CodUfficioDestinatario";
  public static final String CAMPO_COD_BDI_DESTINATARIA = "CodBdiDestinataria";
  public static final String CAMPO_DATA_INVIO = "DataInvio";
  public static final String CAMPO_GIORNO_DATA_INVIO = "GiornoDataInvio";
  public static final String CAMPO_MESE_DATA_INVIO = "MeseDataInvio";
  public static final String CAMPO_ANNO_DATA_INVIO = "AnnoDataInvio";
  public static final String CAMPO_GIORNO_DATA_ESITO = "GiornoDataEsito";
  public static final String CAMPO_MESE_DATA_ESITO = "MeseDataEsito";
  public static final String CAMPO_ANNO_DATA_ESITO = "AnnoDataEsito";
  public static final String CAMPO_CODICE_UTENTE_MITTENTE = "CodiceUtenteMittente";
  public static final String CAMPO_COD_ESITO = "CodEsito";
  public static final String CAMPO_BLOB_ESITO = "BlobEsito";
  public static final String CAMPO_FLAG_VISTO = "FlagVisto";
  public static final String CAMPO_CHIAVE_ANNO_CUMULANTE = "ChiaveAnnoCumulante";
  public static final String CAMPO_CHIAVE_PROGR_CUMULANTE = "ChiaveProgrCumulante";

  public static final String PG_LOAD_RICERCAMESSAGGIO	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/LoadRicercaMessaggio.jsp";
  public static final String PG_LOAD_DETTAGLIOMESSAGGIO	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/LoadRicercaMessaggio.jsp";
  public static final String PG_RICERCAMESSAGGIO	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/RicercaMessaggio.jsp";
  public static final String PG_LOAD_INSERISCIMESSAGGIO	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/LoadInserisciMessaggio.jsp";
  public static final String PG_LISTA_MESSAGGI_RICERCA_SPEDITI = IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicercaSpediti.jsp";
  public static final String PG_LISTA_MESSAGGI_RICERCA_SOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/jms/ListaMessaggiRicercaSoggetto.jsp";

  public static final String PG_LISTA_MESSAGGI_SPEDITI	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiSpediti.jsp";
  public static final String PG_LISTA_MESSAGGI_RICEVUTI	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicevuti.jsp";
  public static final String PG_LISTA_MESSAGGI_SPEDITI_SIUS	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiSpeditiSius.jsp";
  public static final String PG_LISTA_MESSAGGI_RICEVUTI_SIUS	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicevutiSius.jsp";
  public static final String PG_LISTA_MESSAGGI_RICEVUTI_PERDATE	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicevutiPerDate.jsp";
  public static final String PG_LISTA_MESSAGGI_TRASMESSI	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiTrasmessi.jsp";
  public static final String PG_LISTA_ESITI_RICERCA_FASC_ALTRE_BDI  = IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaEsitiRicercaFascAltreBDI.jsp";  // STUB 18/03/2005
  public static final String PG_BUTTONS_MESSAGGIO	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/buttonsMessaggi.jsp";  // STUB 16/05/2005
  public static final String PG_LISTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicevutiTrasmissioneCompetenza.jsp";
  public static final String PG_CONTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA = IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ContaMessaggiRicevutiTrasmissioneCompetenza.jsp";
  public static final String PG_LISTA_MESSAGGI_INCARICO_TRASMISSIONE_COMPETENZA	= IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiPresincaricoTrasmissioneCompetenza.jsp";

  
}