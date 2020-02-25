package siap.web;

import f3b.web.IWebConstants;

public interface ISIAPCostantiWeb extends IWebConstants
{
  public static final String PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO = ROOT_DIR + "files/siap/siep/fascicolo/ToolbarFascicolo.jsp";
  public static final String PG_TOOLBAR_GESTIONE_REGISTRO_ISTANZE = ROOT_DIR + "files/siap/siep/fascicolo/ToolbarRegistroIstanze.jsp";
  
  public static final String PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO = ROOT_DIR + "files/siap/siep/fascicolo/ButtonsFascicolo.jsp";
  public static final String PG_TOOLBAR_REATO = ROOT_DIR + "files/siap/siep/reato/ToolbarReato.jsp";
  public static final String PG_TOOLBAR_REATO_ONLYCOMBO  = ROOT_DIR + "files/siap/siep/reato/ToolbarReatoOnlyCombo.jsp";
  public static final String PG_TOOLBAR_PENA_COMPLESSIVA = ROOT_DIR + "files/siap/siep/penacomplessiva/ToolbarPenaComplessiva.jsp";
  public static final String PG_TOOLBAR_PENA_COMPLESSIVA_CUM = ROOT_DIR + "files/siap/siep/modulocumulo/ToolbarPenaComplessivaCumulo.jsp";
  public static final String PG_TOOLBAR_ISTANZA = ROOT_DIR + "files/siap/siep/istanza/toolbarIstanza.jsp";
  public static final String PG_TOOLBAR_NUOVA_ISTANZA = ROOT_DIR + "files/siap/siep/nuovaistanza/toolbarNuovaIstanza.jsp";
  public static final String PG_INCLUDE_UPLOAD_DOCUMENT = ROOT_DIR +"files/siap/sico/evento/IncludeUploadDocument.jsp";

  public static final String PG_BUTTONS_MISURA_SICUREZZA = ROOT_DIR + "files/siap/siep/misurasicurezza/ButtonsMisuraSicurezza.jsp";

  //GDV 11-11-2004
  //Portata dalla procedura vecchia gestisciUploadStampa.js alla nuova versione
  public static final String JS_CONTROL_UPLOAD     = JS_DIR + "gestisciUploadStampa2.js";
  public static final String JS_CONTROL_UPLOAD_NEW = JS_DIR + "gestisciUploadStampa2.js";

  public static final String PG_GRIGLIA_BOTTONI_SIEP = ROOT_DIR + "files/siap/siep/web/GrigliaBottoni.jsp";
  public static final String PG_GRIGLIA_BOTTONI_SIEP_SEN_DET_FASC = ROOT_DIR + "files/siap/siep/web/GrigliaBottoniSenzaDettFasc.jsp";
  public static final String CAMPO_AZIONE_CHIAMANTE = "AzioneChiamante";
  public static final String PG_PAGINA_VUOTA = JS_DIR + "blankGray.htm";
  public static final String PG_UNDER_CONSTRUCTION  = ROOT_DIR + "files/siap/UnderConstruction.jsp";

  //GDV 11-11-2004
  //Aggiunta la jsp per la gestione stampa ButtonsStampaSiep
  public static final String PG_BUTTONS_STAMPA_SIUS = ROOT_DIR + "files/siap/sico/security/BottoneStampaSius.jsp";
  public static final String PG_BUTTONS_STAMPA_SIEP = ROOT_DIR + "files/siap/sico/security/ButtonsStampaSiep.jsp";
  public static final String PG_BUTTONS_STAMPA_SIGE = ROOT_DIR + "files/siap/sico/security/BottoneStampaSige.jsp";
  public static final String PG_BUTTONS_RICERCHE_STAMPA_SIEP = ROOT_DIR + "files/siap/sico/security/ButtonsRicercheStampaSiep.jsp";
  public static final String PG_BUTTONS_TOOLBAR_STAMPA_SIEP = ROOT_DIR + "files/siap/sico/security/ButtonsToolBarStampaSiep.jsp";
  public static final String PG_STAMPA = ROOT_DIR + "files/Stampa.jsp";
  public static final String PG_STATISTICA = ROOT_DIR + "files/Statistica.jsp";
  public static final String CAMPI_VALIDA_UPLOAD = ROOT_DIR + "files/siap/sico/security/CampiValidaUpload.jsp";
  public static final String DIV_FORM_UPLOAD = ROOT_DIR + "files/siap/sico/security/DivFormUpload.jsp";
  public static final String PG_BUTTONS_MOD_CANC = ROOT_DIR + "files/siap/sico/security/BottoniModificaCancella.jsp";
  
  //Fabio 25-10-2007
  public static final String PG_BUTTONS_STAMPA_PDF_SIEP = ROOT_DIR + "files/siap/sico/security/ButtonsStampaPdfSiep.jsp";

  public static final String PG_BUTTONS_CANCELLA_SIEP = ROOT_DIR + "files/siap/sico/security/ButtonsCancellaSiep.jsp";
  
}
