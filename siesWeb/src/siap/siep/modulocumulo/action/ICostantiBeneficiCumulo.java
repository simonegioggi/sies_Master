package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiMisuraSicurezzaCumulo</p>
* <p>Description: Classe di costanti di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiBeneficiCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_BENEFICIO_CUMULO = "IdBeneficioCumulo";
    
    public static final String CAMPO_COD_NATURA_BENEFICIO = "CodNaturaBeneficio";
    public static final String CAMPO_COD_TIPO_BENEFICIO = "CodTipoBeneficio";
    public static final String CAMPO_COD_TIPO_SOSP_SUBORDINATA = "CodTipoSospSubordinata";
    public static final String CAMPO_COD_SOTTOTIPO_BENEFICIO = "CodSottotipoBeneficio";    
    
    // Indulto: Concessione e Revoca
    public static final String CAMPO_FLAG_SOSP_COND = "FlagSospCond"; 
    public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
    public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
    public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
    public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";
    public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
    public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
    public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
    public static final String CAMPO_IMPORTO_AMMENDA = "Importoammenda";
    
    public static final String CAMPO_COD_DPR = "CodDpr";  // Solo indulto Conessione e Revoca
    
    public static final String CAMPO_NOTE = "Note";

    public static final String CAMPO_NUM_ANNI_SOSPENSIONE = "NumAnniSospensione";
    // Sospenzione con Obbligo di prestazione
    public static final String CAMPO_NUM_MESI_PRESTAZIONE = "NumMesiPrestazione";
    public static final String CAMPO_NUM_GIORNI_PRESTAZIONE = "NumGiorniPrestazione";
    public static final String CAMPO_NUM_ORE_SETTIMANALI = "NumOreSettimanali";
    public static final String CAMPO_FLAG_FREQUENZA_SETTIMANALE = "FlagFrequenzaSettimanale";
    public static final String CAMPO_NUM_ANNI_ADEMPIMENTO   = "NumAnniAdempimento";
    public static final String CAMPO_NUM_MESI_ADEMPIMENTO   = "NumMesiAdempimento";
    public static final String CAMPO_NUM_GIORNI_ADEMPIMENTO = "NumGiorniAdempimento";    
    
//-----------------------------------------------------------------------------------------------------    
// Nel caso di Revoca sono i Dati del Titolo che ha concesso il beneficio che si sta revocando

    // CAMPO_RIF_ID_PROVVEDIMENTO contiene il Riferimento alla Sentenza di origine del Titolo_Cumulato di Concessione Beneficio
    public static final String CAMPO_RIF_ID_PROVVEDIMENTO 				= "RifIdProvvedimento";	
    public static final String CAMPO_RIF_COD_TIPO_PROVVEDIMENTO 		= "RifCodTipoProvvedimento";
    public static final String CAMPO_RIF_DATA_PROVVEDIMENTO 			= "RifDataProvvedimento";
    public static final String CAMPO_RIF_GIORNO_PROVVEDIMENTO 			= "RifGiornoProvvedimento";
    public static final String CAMPO_RIF_MESE_PROVVEDIMENTO 			= "RifMeseProvvedimento";
    public static final String CAMPO_RIF_ANNO_PROVVEDIMENTO 			= "RifAnnoProvvedimento";
    public static final String CAMPO_RIF_DATA_IRREVOCABILITA 			= "RifDataIrrevocabilita";
    public static final String CAMPO_RIF_GIORNO_IRREVOCABILITA 			= "RifGiornoIrrevocabilita";
    public static final String CAMPO_RIF_MESE_IRREVOCABILITA 			= "RifMeseIrrevocabilita";
    public static final String CAMPO_RIF_ANNO_IRREVOCABILITA 			= "RifAnnoIrrevocabilita";
    public static final String CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE 	= "RifCodTipoAutoritaEmittente";
    public static final String CAMPO_RIF_COD_LUOGO_EMITTENTE 			= "RifCodLuogoEmittente";
    public static final String CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE	= "RifNumSezioneAutoritaEmittente";
    public static final String CAMPO_RIF_ANNO_SENTENZA 					= "RifAnnoSentenza";
    public static final String CAMPO_RIF_NUMERO_SENTENZA 				= "RifNumeroSentenza";
//    
    // CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO contiene il Riferimento al Titolo_Cumulato di Concessione Beneficio_Cumulo
    public static final String CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO		= "TitIdTitoloCumuloCollegato";
    public static final String CAMPO_ID_BENEFICIO_DA_REVOCARE 			= "IdBeneficioDaRevocare";
//-----------------------------------------------------------------------------------------------------------------
    
    // Nel caso di Non Menzione Punta il record Pena Sospesa
    public static final String CAMPO_BEN_ID_BENEFICIO = "BenIdBeneficio";

    public static final String TIPO_FORM_BENEFICIO  = "tipoFormBeneficio";
    public static final String TIPO_FORM_SOSPENSIONE  = "01";
    public static final String TIPO_FORM_INDULTO      = "02";

    public static final String CAMPO_PROVENIENZA = "Provenienza";
    
    //
    public static final String COD_BENEFICIO_SOSPENSIONE  = "01";
    public static final String COD_BENEFICIO_NON_MENZIONE = "02";
    public static final String COD_BENEFICIO_INDULTO      = "03";
    public static final String COD_BENEFICIO_AMNISTIA     = "04";
    
    
    public static final String CAMPO_FLAG_NON_MENZIONE = "FlagNonMenzione";
    
    //
    public static final String CAMPO_MOTIVO_MODIFICA         = "MotivoModifica"; 
    public static final String CAMPO_FLAG_STATO              = "FlagStato"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO  = "TitIdTitoloCumulato"; 
    public static final String CAMPO_ID_BENEFICIO_ORIGINE    = "IdBeneficioOrigine";
    //
    // Deassocia il Titolo di concessione Beneficio
    public static final String CAMPO_DEASSOCIA_TITOLO        = "NO"; 

  
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_ELENCO_BENEFICI_CUMULO         		 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoBeneficiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_BENEFICI_CUMULO 		 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciBeneficioCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_BENEFICI_INDULTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciBeneficioIndultoCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_BENEFICIO_CUMULO 		 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioBeneficioCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_BENEFICIO_INDULTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioBeneficioIndultoCumulo.jsp";

    // REVOCHE
    public static final String PG_ELENCO_REVOCHE_BENEFICI_CUMULO        	 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoRevocheBeneficiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_REVOCA_BENEFICIO_CUMULO	 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRevocaBeneficioCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_REVOCA_BENEFICIO_INDULTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRevocaBeneficioIndultoCumulo.jsp";
    public static final String PG_DETTAGLIO_REVOCA_BENEFICI_CUMULO     		 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRevocaBeneficioCumulo.jsp";
    public static final String PG_DETTAGLIO_REVOCA_BENEFICI_INDULTO_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRevocaBeneficioIndultoCumulo.jsp";
    
    public static final String PG_POPUP_ELENCO_BENEFICI_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/PopupBeneficiConcessi.jsp";
    
    
}