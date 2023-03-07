package siap.siep.rateizzazionepp.action;

import f3b.web.IWebConstants;

public interface ICostantiRateizzazionePP {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_RATEIZZAZIONE_PP = "IdRateizzazionePP"; 
    public static final String CAMPO_IMPORTO_RATA        = "ImportoRata"; 
    public static final String CAMPO_NUM_RATE            = "NumeroRate"; 
    public static final String CAMPO_TIPO_RATEIZZAZIONE  = "TipoRateizzazione"; 
    public static final String CAMPO_SCADENZA_GIORNI     = "ScadenzaGiorni"; 
    
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep"; 
    public static final String CAMPO_EVE_ID_EVENTO             = "EveIdEvento";    

    
    public static final String CAMPO_VALORE_IMPORTO_I = "ValoreImportoDaPagareI"; 
    public static final String CAMPO_VALORE_IMPORTO_D = "ValoreImportoDaPagareD"; 
    public static final String CAMPO_SCADENZA_GIORNI_RATA_UNICA = "ScadenzaGiorniRataUnica";    
    
    public static final String CAMPO_VALORE_RATA_UNICA_I = "ValoreRataUnicaI"; 
    public static final String CAMPO_VALORE_RATA_UNICA_D = "ValoreRataUnicaD"; 
   
    public static final String CAMPO_VALORE_RATA_I = "ValoreRataI"; 
    public static final String CAMPO_VALORE_RATA_D = "ValoreRataD";
    
    public static final String TIPO_RATEIZZAZIONE_UNICA   = "U";
    public static final String TIPO_RATEIZZAZIONE_RATEALE = "R";
    
    public static final int NUM_MAX_RATE = 10;
    
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_INSERISCI_RATEIZZAZIONE_PP  = IWebConstants.ROOT_DIR + "files/siap/siep/rateizzazionepp/LoadInserisciRateizzazionePP.jsp";
    public static final String PG_LOAD_DETTAGLIO_RATEIZZAZIONE_PP  = IWebConstants.ROOT_DIR + "files/siap/siep/rateizzazionepp/DettaglioRateizzazionePP.jsp";

}
