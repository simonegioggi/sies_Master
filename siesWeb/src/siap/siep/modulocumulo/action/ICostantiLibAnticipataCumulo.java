package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiLibAnticipataCumulo</p>
* <p>Description: Classe di costanti di LibAnticipataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiLibAnticipataCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_LIB_ANTICIPATA_CUMULO    	 = "IdLibAnticipataCumulo";
    public static final String CAMPO_ID_LIB_ANTICIPATA_SPE_CUMULO    = "IdLibAnticipataSpeCumulo";
    public static final String CAMPO_ID_LIB_ANTICIPATA_INT_CUMULO    = "IdLibAnticipataIntCumulo";
    public static final String CAMPO_COD_TIPO_LICENZA            = "CodTipoLicenza";
    
    public static final String CAMPO_RADIO_QUALE_LA           = "RadioQualeLa";
    
    public static final String CAMPO_NUM_GIORNI_LA            = "NumGiorniLa";
    public static final String CAMPO_NUM_GIORNI_LA_SPE        = "NumGiorniLaSpe";
    public static final String CAMPO_NUM_GIORNI_LA_INT        = "NumGiorniLaInt";
    
    public static final String CAMPO_SALVA_NUM_GIORNI_LA            = "SalvaNumGiorniLa";
    public static final String CAMPO_SALVA_NUM_GIORNI_LA_SPE        = "SalvaNumGiorniLaSpe";
    public static final String CAMPO_SALVA_NUM_GIORNI_LA_INT        = "SalvaNumGiorniLaInt";
    
    public static final String CAMPO_SOMMA_RISARC_DANNI          = "SommaRisarcDanni"; 
    public static final String CAMPO_FLAG_CONCESSO               = "FlagConcesso"; 
    public static final String CAMPO_TIPO_LA                     = "TipoLa"; 
    public static final String CAMPO_FLAG_ELABORATO              = "FlagElaborato"; 
    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO      = "TitIdTitoloCumulato"; 
    public static final String CAMPO_ID_LIBANTICIPATA_ORIGINE    = "IdLibanticipataOrigine";
    
    public static final String CAMPO_DATA_EMISSIONE 			 = "DataEmissione"; 
    public static final String CAMPO_COD_UFFICIO_EMITTENTE		 = "CodUfficioEmittente";
    public static final String CAMPO_COD_LUOGO_EMITTENTE		 = "CodLuogoEmittente";

    public static final String CAMPO_NUM_GIORNI_RISARCITORI      = "NumGiorniRisarcitori";
    public static final String CAMPO_INTERO_SOMMA_RISARC_DANNI   = "InteroSommaRisarcDanni"; 
    public static final String CAMPO_DECIMALE_SOMMA_RISARC_DANNI = "DecimaleSommaRisarcDanni"; 

    public static final String CAMPO_NUMERO_GIORNI      		 = "NumeroGiorni";
    
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_INSERISCI_LIB_ANTICIPATA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciLiberazioneAnticipataCumulo.jsp";
    public static final String PG_LOAD_MODIFICA_LIB_ANTICIPATA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModificaLiberazioneAnticipataCumulo.jsp";
    public static final String PG_LOAD_ELENCO_LIB_ANTICIPATA_CUMULO  	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoLiberazioneAnticipataCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_LIB_ANTICIPATA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioLiberazioneAnticipataCumulo.jsp";

    // Rimedi Risarcitori DL 2014/92
    public static final String PG_LOAD_ELENCO_RIMEDI_RISARCITORI_CUMULO  	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoRimediRisarcitoriDL201492Cumulo.jsp";
    public static final String PG_LOAD_INSERISCI_RIMEDI_RISARCITORI_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRimediRisarcitoriDL201492Cumulo.jsp";
    public static final String PG_LOAD_MODIFICA_RIMEDI_RISARCITORI_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModificaRimediRisarcitoriDL201492Cumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_RIMEDI_RISARCITORI_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRimediRisarcitoriDL201492Cumulo.jsp";
    
    // Scomputo Permessi
    public static final String PG_LOAD_ELENCO_SCOMPUTO_PERMESSI     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoScomputoPermessiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_SCOMPUTO_PERMESSI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciScomputoPermessiCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_SCOMPUTO_PERMESSI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioScomputoPermessiCumulo.jsp";

}