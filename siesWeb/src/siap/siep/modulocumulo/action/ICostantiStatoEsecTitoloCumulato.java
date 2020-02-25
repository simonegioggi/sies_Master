package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiStatoEsecTitoloCumulato</p>
* <p>Description: Classe di costanti di StatoEsecTitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiStatoEsecTitoloCumulato {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO = "IdStatoEsecTitoloCumulato"; 
    public static final String CAMPO_COD_TIPO_EVENTO               = "CodTipoEvento"; 
    public static final String CAMPO_COD_TIPO_PROVVEDIMENTO        = "CodTipoProvvedimento"; 
    public static final String CAMPO_COD_MOTIVO                    = "CodMotivo"; 
    public static final String CAMPO_COD_MOTIVO_REVOCA             = "CodMotivoRevoca";
    public static final String CAMPO_COD_MOTIVO_REVOCA_PM          = "CodMotivoRevocaPm"; 
    public static final String CAMPO_COD_UFFICIO_EMITTENTE         = "CodUfficioEmittente"; 
    public static final String CAMPO_COD_LUOGO_EMITTENTE           = "CodLuogoEmittente"; 
    
    public static final String CAMPO_TIPO_UFFICIO_EMITTENTE         = "TipoUfficioEmittente"; 
    public static final String CAMPO_TIPO_AUTORITA_EMITTENTE        = "TipoAutoritaEmittente"; 
    public static final String CAMPO_DESC_LUOGO_EMITTENTE           = "DescLuogoEmittente"; 
    
    public static final String CAMPO_GIORNO_DATA_EMISSIONE         = "GiornoDataEmissione"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE           = "MeseDataEmissione"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE           = "AnnoDataEmissione"; 
    public static final String CAMPO_COD_ESITO                     = "CodEsito"; 
    public static final String CAMPO_COD_ESITO_TENORE              = "CodEsitoTenore"; 
    public static final String CAMPO_ANNO_PROCEDIMENTO             = "AnnoProcedimento"; 
    public static final String CAMPO_PROGR_PROCEDIMENTO            = "ProgrProcedimento"; 
    public static final String CAMPO_ANNO_PROVVEDIMENTO            = "AnnoProvvedimento"; 
    public static final String CAMPO_PROGR_PROVVEDIMENTO           = "ProgrProvvedimento"; 
    
    public static final String CAMPO_NOTE                          = "Note"; 
    
    public static final String CAMPO_COD_CONTENUTO_ISTANZA         = "CodContenutoIstanza"; 
    public static final String CAMPO_GIORNO_DATA_ISTANZA           = "GiornoDataIstanza"; 
    public static final String CAMPO_MESE_DATA_ISTANZA             = "MeseDataIstanza"; 
    public static final String CAMPO_ANNO_DATA_ISTANZA             = "AnnoDataIstanza"; 
    public static final String CAMPO_FLAG_ISTANZA_PRESDEP          = "FlagIstanzaPresdep"; 
    public static final String CAMPO_COD_STATO_ISTANZA             = "CodStatoIstanza"; 
    public static final String CAMPO_COD_TIPO_UFFICIO_DESTINATARIO = "CodTipoUfficioDestinatario"; 
    public static final String CAMPO_COD_LUOGO_DESTINATARIO        = "CodLuogoDestinatario"; 
    public static final String CAMPO_COD_UFFICIO_DESTINATARIO      = "CodUfficioDestinatario"; 
    public static final String CAMPO_GIORNO_DATA_TRASMISSIONE      = "GiornoDataTrasmissione"; 
    public static final String CAMPO_MESE_DATA_TRASMISSIONE        = "MeseDataTrasmissione"; 
    public static final String CAMPO_ANNO_DATA_TRASMISSIONE        = "AnnoDataTrasmissione";     
    
    public static final String CAMPO_FLAG_TIPO_SOSP                = "FlagTipoSosp"; 
    
    public static final String CAMPO_COD_TIPO_ISTANTE              = "CodTipoIstante"; 
    public static final String CAMPO_COD_TIPO_UFFICIO_ALTRO        = "CodTipoUfficioAltro"; 
    public static final String CAMPO_COD_TIPO_AUTORITA_ALTRO       = "CodTipoAutoritaAltro"; 
    public static final String CAMPO_COD_LUOGO_ALTRO               = "CodLuogoAltro"; 
    public static final String CAMPO_COD_UFFICIO_ALTRO             = "CodUfficioAltro"; 
    public static final String CAMPO_SEZIONE_ALTRO                 = "SezioneAltro"; 
    public static final String CAMPO_GIORNO_DATA_EMISSIONE_ALTRO   = "GiornoDataEmissioneAltro"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE_ALTRO     = "MeseDataEmissioneAltro"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE_ALTRO     = "AnnoDataEmissioneAltro"; 
    
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO        = "TitIdTitoloCumulato"; 
    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO    = "IstrIdIstruttoriaCumulo"; 
    public static final String CAMPO_FLAG_STATO                    = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA               = "MotivoModifica"; 
    public static final String CAMPO_ID_EVENTO_ORIGINE             = "IdEventoOrigine"; 
    public static final String CAMPO_EVE_ID_EVENTO_ORIGINE         = "EveIdEventoOrigine"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 
    public static final String CAMPO_TEXT_AREA_MOTIVO               			= "TextAreaMotivo"; 
    public static final String CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO   = "GiornoDataEmissioneProvvedimento"; 
    public static final String CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO     = "MeseDataEmissioneProvvedimento"; 
    public static final String CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO     = "AnnoDataEmissioneProvvedimento";
    public static final String CAMPO_CHECK_SOSPENSIONE                		= "CheckSospensione"; 
    public static final String CAMPO_CHECK_NON_MENZIONE                		= "CheckNonMenzione"; 
    
    //============================================================================
    // Attività del PM
    //============================================================================
    public static final String CAMPO_COD_ATTIVITA   = "codAttivita";
    public static final String ATTIVITA_PM_TIPO_SOSP  = "AttivitaPM_TIPO_SOSP";
    public static final String ATTIVITA_PM_SOSP_C5  = "AttivitaPM_SOSP_C5";
    public static final String ATTIVITA_PM_SOSP_78  = "AttivitaPM_SOSP_78";
    public static final String ATTIVITA_PM_SOSP_199 = "AttivitaPM_SOSP_199";
    public static final String ATTIVITA_PM_SOSP_C5_DESC  = "Sospensione esecuzione ex art. 656 c.p.p.";
    public static final String ATTIVITA_PM_SOSP_78_DESC  = "D.L. 78/2013";
    public static final String ATTIVITA_PM_SOSP_199_DESC = "Espiazione Presso il Domicilio (Legge n.199/2010)";
    
   
    public static final String PG_LOAD_ELENCO_SOSP_PM     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoDecretiSospPM.jsp";
    public static final String PG_LOAD_INSERISCI_SOSP_PM  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecretiSospPM.jsp";
    public static final String PG_LOAD_DETTAGLIO_SOSP_PM  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDecretiSospPM.jsp";

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
//    public static final String PG_LOAD_RICERCASTATOESECTITOLOCUMULATO  	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/LoadRicercaStatoEsecTitoloCumulato.jsp";
    public static final String PG_LOAD_DETTAGLIO_STATO_ESEC_TITOLO_CUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioStatoEsecTitoloCumulato.jsp";
//    public static final String PG_RICERCASTATOESECTITOLOCUMULATO       	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/RicercaStatoEsecTitoloCumulato.jsp";
//    public static final String PG_LOAD_INSERISCISTATOESECTITOLOCUMULATO	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/LoadInserisciStatoEsecTitoloCumulato.jsp";
//    public static final String PG_LOAD_CANCELLASTATOESECTITOLOCUMULATO 	= IWebConstants.JSP_DIR + "files/siap/siep/modulocumulo/DettaglioStatoEsecTitoloCumulato.jsp";
}