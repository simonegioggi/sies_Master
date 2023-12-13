package siap.siep.pagoPA.action;

import f3b.web.IWebConstants;

public interface ICostantiErroriSiesPagopa {
  
  public static final String CAMPO_ID_ERRORI_SIES_PAGOPA = "IdErroriSiesPagopa";
  
  public static final String CAMPO_GIORNO_DATA_DAL = "GiornoDataDal";
  public static final String CAMPO_MESE_DATA_DAL = "MeseDataDal";
  public static final String CAMPO_ANNO_DATA_DAL= "AnnoDataDal";
  
  public static final String CAMPO_GIORNO_DATA_AL = "GiornoDataAl";
  public static final String CAMPO_MESE_DATA_AL = "MeseDataAl";
  public static final String CAMPO_ANNO_DATA_AL= "AnnoDataAl";
  
  public static final String CAMPO_COD_UTENTE  = "CodUtente";
  public static final String CAMPO_TIPO_UTENTE = "TipoUtente";
  
  public static final String CAMPO_TIPO_UTENTE_TUTTI = "TipoUtenteTutti";
  public static final String CAMPO_TIPO_UTENTE_CODICE = "TipoUtenteCodice";
  
  public static final String PG_LOAD_VERIFICA_ERRORI= IWebConstants.ROOT_DIR
      + "files/siap/siep/pagoPA/LoadRicercaErroriPagoPA.jsp";
  public static final String PG_ESITO_VERIFICA_ERRORI= IWebConstants.ROOT_DIR
      + "files/siap/siep/pagoPA/EsitoRicercaErroriPagoPA.jsp"; 
  
  public static final String DESC_FUNZIONE_RICHIESTA = "Richiesta Bollettini PagoPA";
  public static final String DESC_FUNZIONE_VERIFICA  = "Verifica stato bollettino su PagoPA";
}
