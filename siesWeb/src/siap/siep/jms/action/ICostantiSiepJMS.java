package siap.siep.jms.action;

import siap.jms.ICostantiJMS;
import f3b.web.IWebConstants;

public interface ICostantiSiepJMS extends ICostantiJMS
{
  final static String PG_LISTA_ESITI_RICERCA = IWebConstants.ROOT_DIR + "files/siap/siep/jms/ListaEsitiRicerca.jsp";
  final static String PG_LOAD_RICERCA_ESTESA_FASCICOLO_SIEP = IWebConstants.ROOT_DIR + "files/siap/siep/jms/LoadRicercaEstesaFascicoloSiep.jsp";

  final static String CAMPO_TIPO_UFFICIO = "TipoUfficio";
  final static String CAMPO_SEDE_UFFICIO = "SedeUfficio";
  final static String CAMPO_SEDE_UFFICIO_ACCORPATO = "SedeUfficioAccorpato";
  public static final String PG_LISTA_MESSAGGI_RICERCA_SPEDITI = IWebConstants.ROOT_DIR + "files/siap/jms/messaggio/ListaMessaggiRicercaSpediti.jsp";
  public static final String PG_LOAD_LISTAISTANZETRASMESSE = IWebConstants.ROOT_DIR + "files/siap/siep/jms/LoadListaIstanzeTrasmesse.jsp";  // STUB 10/05/2005
  public static final String PG_LISTAISTANZETRASMESSE = IWebConstants.ROOT_DIR + "files/siap/siep/jms/ListaIstanzeTrasmesse.jsp";  // STUB 10/05/2005
  public static final String PG_LOAD_LISTAPROVVEDIMENTITRASMESSI = IWebConstants.ROOT_DIR + "files/siap/siep/jms/LoadListaProvvedimentiTrasmessi.jsp";  // STUB 12/05/2005
  public static final String PG_LISTAPROVVEDIMENTITRASMESSI = IWebConstants.ROOT_DIR + "files/siap/siep/jms/ListaProvvedimentiTrasmessi.jsp";  // STUB 12/05/2005

}