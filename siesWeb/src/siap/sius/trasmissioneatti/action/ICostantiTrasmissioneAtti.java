package siap.sius.trasmissioneatti.action;

import f3b.web.IWebConstants;

public interface ICostantiTrasmissioneAtti
{
  public static final String CAMPO_ID_EVENTO                    = "IdEvento";
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE     = "GiornoTrasmissione";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE       = "MeseTrasmissione";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE       = "AnnoTrasmissione";
  public static final String CAMPO_COD_TIPO_UFFICIO             = "CodTipoUfficio";
  public static final String CAMPO_RICHIESTA_STAMPA             = "RichiestaStampa";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO         = "DescrComuneUfficio";
  public static final String CAMPO_NOTE                         = "Note";

  public static final String PG_LOAD_TRASMISSIONEATTI           = IWebConstants.ROOT_DIR + "files/siap/sius/trasmissioneatti/LoadTrasmissioneAtti.jsp";
  public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOSIUS  = IWebConstants.ROOT_DIR + "files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp";
  public static final String PG_DETTAGLIOTRASMISSIONEATTI       = IWebConstants.ROOT_DIR + "files/siap/sius/trasmissioneatti/DettaglioTrasmissioneAtti.jsp";
}

