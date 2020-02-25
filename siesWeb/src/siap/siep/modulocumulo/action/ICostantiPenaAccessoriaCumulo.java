package siap.siep.modulocumulo.action;


import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaAccessoriaCumulo</p>
* <p>Description: Classe di costanti di PenaAccessoriaCumulo</p>
*/

public interface ICostantiPenaAccessoriaCumulo
{
  public static final String CAMPO_ID_PENA_ACCESSORIA_CUMULO = "IdPenaAccessoriaCumulo";
  public static final String CAMPO_COD_TIPO_PENA_ACCESSORIA = "CodTipoPenaAccessoria";
  public static final String CAMPO_DESCR_TIPO_PENA_ACCESSORIA = "DescrTipoPenaAccessoria";
  public static final String CAMPO_DESCR_ALTRE_PA = "DescrAltrePA";
 
  public static final String CAMPO_DURATA = "Durata";
  public static final String CAMPO_NUM_ANNI = "NumAnni";
  public static final String CAMPO_NUM_MESI = "NumMesi";
  public static final String CAMPO_NUM_GIORNI = "NumGiorni";
  public static final String CAMPO_NOTE = "Note";
  
  // Estremi Ordinanza
  public static final String CAMPO_ANNO_ORDINANZA_GE = "AnnoOrdinanzaGE";
  public static final String CAMPO_NUMERO_ORDINANZA_GE = "NumeroOrdinanzaGE";
  public static final String CAMPO_GIORNO_DATA_ORDINANZA_GE = "GiornoDataOrdinanzaGE";
  public static final String CAMPO_MESE_DATA_ORDINANZA_GE = "MeseDataOrdinanzaGE";
  public static final String CAMPO_ANNO_DATA_ORDINANZA_GE = "AnnoDataOrdinanzaGE";
  public static final String CAMPO_COD_TIPO_UFFICIO_ORDINANZA_GE = "CodTipoUfficioOrdinanzaGE";
  public static final String CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_GE = "CodLuogoUfficioOrdinanzaGE";

  // Comunicazione
  public static final String CAMPO_FLAG_COMUNICAZIONE        = "FlagComunicazione";
  public static final String CAMPO_GIORNO_DATA_COMUNICAZIONE = "GiornoDataComunicazione";
  public static final String CAMPO_MESE_DATA_COMUNICAZIONE   = "MeseDataComunicazione";
  public static final String CAMPO_ANNO_DATA_COMUNICAZIONE   = "AnnoDataComunicazione";


  //
  public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
  public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
  public static final String CAMPO_TIT_ID_TITOLO_CUMULATO      = "TitIdTitoloCumulato"; 
  public static final String CAMPO_ID_PENA_ACCESSORIA_ORIGINE  = "IdPenaAccessoriaOrigine"; 
  
  public static final String CAMPO_FLAG_DATI_FINALI       = "flagDatiFinali"; 
  
  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_ELENCO_PENEACCESSORIE_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoPeneAccessorieCumulo.jsp";
  public static final String PG_LOAD_INSERISCI_PENEACCESSORIE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPenaAccessoriaCumulo.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENEACCESSORIE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPenaAccessoriaCumulo.jsp";

//  public static final String PG_LOAD_RICERCAMISURASICUREZZACUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaMisuraSicurezzaCumulo.jsp";
//  public static final String PG_LOAD_CANCELLAMISURASICUREZZACUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioMisuraSicurezzaCumulo.jsp";

}
