package siap.sius.remissionedebito.action;

import f3b.web.IWebConstants;

public interface ICostantiSiusRemissioneDebito extends ICostantiRemissioneDebito
{
  //========================================================================== 
  // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
  //========================================================================== 
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA         = "GiornoDataIrrevocabilita"; 
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA           = "MeseDataIrrevocabilita"; 
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA           = "AnnoDataIrrevocabilita"; 
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS 					= "FasSiuIdFascicoloSius"; 
  public static final String CAMPO_NUM_GIORNI_SS 											= "NumGiorniSS";
  public static final String CAMPO_NUM_MESI_SS 												= "NumMesiSS";
  public static final String CAMPO_NUM_ANNI_SS												= "NumAnniSS";
  public static final String CAMPO_INTERO_IMPORTO_MULTA								= "InteroImportoMulta";
  public static final String CAMPO_DECIMALE_IMPORTO_MULTA							= "DecimaleImportoMulta";
  public static final String CAMPO_INTERO_IMPORTOFINALE_MULTA					= "InteroImportoFinaleMulta";
  public static final String CAMPO_DECIMALE_IMPORTOFINALE_MULTA				= "DecimaleImportoFinaleMulta";
  public static final String CAMPO_GIORNO_DATA_TERMINE_PAG						= "GiornoDataTerminePag"; 
  public static final String CAMPO_MESE_DATA_TERMINE_PAG		          = "MeseDataTerminePag"; 
  public static final String CAMPO_ANNO_DATA_TERMINE_PAG	            = "AnnoDataTerminePag"; 
  public static final String CAMPO_NUM_GIORNI_PER_PAGAMENTO						= "NumGiorniPerPagamento";
  public static final String CAMPO_FLAG_TIPO_SANZIONE									= "FlagTipoSanzione";

  //================================================================= 
  // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
  // nelle classi del progetto 
  //================================================================= 
  //public static final String CAMPO_ANNO_DATA_ANNULLA            = "AnnoDataAnnulla";   
  
  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_RICERCA_SIUS_RICHIESTAREMISSIONE  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/RicercaSiusRichiestaRemissione.jsp";
  public static final String PG_LOAD_DETTAGLIO_RICHIESTA_REMISSIONE_DEBITO  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioRichiestaRemissioneDebito.jsp";
  public static final String PG_LOAD_INSERISCI_RICHIESTAREMISSIONE_DEBITO  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadInserisciRichiestaRemissioneDebito.jsp";
  public static final String PG_LOAD_MODIFICA_RICHIESTA_REMISSIONE_DEBITO   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadModificaRichiestaRemissioneDebito.jsp";
}