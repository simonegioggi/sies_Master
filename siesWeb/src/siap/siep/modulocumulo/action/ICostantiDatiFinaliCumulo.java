package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiDatiFinaliCumulato</p>
* <p>Description: Classe di costanti di ICostantiDatiFinaliCumulo</p>
*/

import f3b.web.IWebConstants;

public interface ICostantiDatiFinaliCumulo {
  
  public static final String CAMPO_ID_DATI_FINALI_CUMULO       = "IdDatiFinaliCumulo"; 
  public static final String CAMPO_TIPO_UFFICIO_EMISSIONE      = "TipoUfficioEmissione"; 
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO   = "GiornoDataProvvedimento"; 
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO     = "MeseDataProvvedimento"; 
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO     = "AnnoDataProvvedimento"; 
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO      = "CodTipoProvvedimento"; 
  public static final String CAMPO_ANNO_PROVVEDIMENTO          = "AnnoProvvedimento"; 
  public static final String CAMPO_NUMERO_PROVVEDIMENTO        = "NumeroProvvedimento"; 
  public static final String CAMPO_COD_TIPO_UFFICIO_EMITTENTE  = "CodTipoUfficioEmittente"; 
  public static final String CAMPO_COD_LUOGO_UFFICIO_EMITTENTE = "CodLuogoUfficioEmittente"; 
  public static final String CAMPO_DESCR_LUOGO_UFFICIO_EMITTENTE = "DescrLuogoUfficioEmittente";   
  public static final String CAMPO_SEZIONE_UFFICIO_EMITTENTE   = "SezioneUfficioEmittente"; 
  
  public static final String CAMPO_FLAG_CREA_FASCICOLO_MS       = "FlagCreaFascicoloMs"; 
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS = "FasSieIdFascicoloSiepMs"; 
  
  public static final String CAMPO_EVE_ID_EVENTO               = "EveIdEvento"; 
  public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO  = "IstrIdIstruttoriaCumulo"; 
  
  public static final String CAMPO_MOTIVO_PROVV  = "CodMotivoProvv"; 

  //============================================================================
  //
  //============================================================================
  public static final String PG_LOAD_INSERISCI_DATI_FINALI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDatiFinali.jsp";
  public static final String PG_LOAD_DETTAGLIO_DATI_FINALI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDatiFinali.jsp";

  public static final String PG_LOAD_INSERISCI_PENE_RIDETERMINATE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPeneRideterminate.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENE_RIDETERMINATE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPeneRideterminate.jsp";
  
  public static final String PG_LOAD_INSERISCI_RICHIESTE_GE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRichiesteComputiDatiFinali.jsp";
  public static final String PG_LOAD_DETTAGLIO_RICHIESTE_GE = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiesteComputiDatiFinali.jsp";
  
  public static final String PG_LOAD_INSERISCI_POSIZIONE_GIURIDICA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPosGiuridicaCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIO_POSIZIONE_GIURIDICA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPosGiuridicaCumulo.jsp";

  public static final String PG_LOAD_DETTAGLIO_PENA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPenaCumulo.jsp";

  public static final String PG_LOAD_INSERISCI_PROVVEDIMENTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciProvvedimentoCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIO_PROVVEDIMENTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioProvvedimentoCumulo.jsp";
  
}
