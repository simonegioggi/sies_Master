package siap.sius.iscrizioneprocedimento.action;

import f3b.web.IWebConstants;

public interface ICostantiIscrProcedimento
{
  public static final String CAMPO_ID_EVENTO                    = "IdEvento";
  public static final String CAMPO_GIORNO_DATA_ATTO             = "GiornoDataAtto";
  public static final String CAMPO_MESE_DATA_ATTO               = "MeseDataAtto";
  public static final String CAMPO_ANNO_DATA_ATTO               = "AnnoDataAtto";
  public static final String CAMPO_COD_TIPO_UFFICIO             = "CodTipoUfficio";
  public static final String CAMPO_RICHIESTA_STAMPA             = "RichiestaStampa";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO         = "DescrComuneUfficio";
  public static final String CAMPO_NOTE                         = "Note";

  public static final String CAMPO_COD_TIPO_ATTO                = "CodTipoAtto";
  public static final String CAMPO_DESCR_TIPO_ATTO              = "DescrTipoAtto";
  public static final String CAMPO_COD_MITTENTE_ATTO            = "CodMittenteAtto";
  public static final String CAMPO_DESCR_MITTENTE_ATTO          = "DescrMittenteAtto";
  public static final String CAMPO_COD_SEDE_MITTENTE            = "CodSedeMittente";
  public static final String CAMPO_DESCR_SEDE_MITTENTE          = "DescrSedeMittente";

  public static final String CAMPO_COD_CONTENUTO                = "CodContenuto";
  public static final String CAMPO_DESCR_CONTENUTO              = "DescrContenuto";
  public static final String CAMPO_COD_OGGETTO                  = "CodOggetto";
  public static final String CAMPO_DESCR_OGGETTO                = "DescrOggetto";
  public static final String CAMPO_GIORNO_DATA_ARRIVO           = "GiornoDataArrivo";
  public static final String CAMPO_MESE_DATA_ARRIVO             = "MeseDataArrivo";
  public static final String CAMPO_ANNO_DATA_ARRIVO             = "AnnoDataArrivo";

  public static final String PG_LOAD_ISCRPROCEDIMENTO           = IWebConstants.ROOT_DIR + "files/siap/sius/iscrizioneprocedimento/LoadIscrProcedimento.jsp";
  public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOSIUS  = IWebConstants.ROOT_DIR + "files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp";
  public static final String PG_LOAD_SINTESI_SOGGETTO_SENTENZA  = IWebConstants.ROOT_DIR + "files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp";

  public static final String PG_LOAD_ISCRPROCEDIMENTOUDS        = IWebConstants.ROOT_DIR + "files/siap/sius/iscrizioneprocedimento/LoadIscrProcedimentoUDS.jsp";
}

