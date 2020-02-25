package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiComputiCumulo</p>
* <p>Description: Classe di costanti di ComputiCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


public interface ICostantiComputiCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_COMPUTI_CUMULO           = "IdComputiCumulo"; 
    public static final String CAMPO_COD_TIPO_ANNOTAZIONE        = "CodTipoAnnotazione"; 
    public static final String CAMPO_COD_CAUSALE_COMPUTO         = "CodCausaleComputo"; 
    public static final String CAMPO_FLAG_PIU_MENO               = "FlagPiuMeno"; 
    
    public static final String CAMPO_GIORNO_DATA_RECLUSIONE_DA   = "GiornoDataReclusioneDa"; 
    public static final String CAMPO_MESE_DATA_RECLUSIONE_DA     = "MeseDataReclusioneDa"; 
    public static final String CAMPO_ANNO_DATA_RECLUSIONE_DA     = "AnnoDataReclusioneDa"; 
    public static final String CAMPO_GIORNO_DATA_RECLUSIONE_A    = "GiornoDataReclusioneA"; 
    public static final String CAMPO_MESE_DATA_RECLUSIONE_A      = "MeseDataReclusioneA"; 
    public static final String CAMPO_ANNO_DATA_RECLUSIONE_A      = "AnnoDataReclusioneA"; 
    public static final String CAMPO_NUM_ANNI_RECLUSIONE         = "NumAnniReclusione"; 
    public static final String CAMPO_NUM_MESI_RECLUSIONE         = "NumMesiReclusione"; 
    public static final String CAMPO_NUM_GIORNI_RECLUSIONE       = "NumGiorniReclusione"; 
    public static final String CAMPO_IMPORTO_MULTA               = "ImportoMulta";

    public static final String CAMPO_GIORNO_DATA_ARRESTO_DA      = "GiornoDataArrestoDa"; 
    public static final String CAMPO_MESE_DATA_ARRESTO_DA        = "MeseDataArrestoDa"; 
    public static final String CAMPO_ANNO_DATA_ARRESTO_DA        = "AnnoDataArrestoDa"; 
    public static final String CAMPO_GIORNO_DATA_ARRESTO_A       = "GiornoDataArrestoA"; 
    public static final String CAMPO_MESE_DATA_ARRESTO_A         = "MeseDataArrestoA"; 
    public static final String CAMPO_ANNO_DATA_ARRESTO_A         = "AnnoDataArrestoA"; 
    public static final String CAMPO_NUM_ANNI_ARRESTO            = "NumAnniArresto"; 
    public static final String CAMPO_NUM_MESI_ARRESTO            = "NumMesiArresto"; 
    public static final String CAMPO_NUM_GIORNI_ARRESTO          = "NumGiorniArresto"; 
    public static final String CAMPO_IMPORTO_AMMENDA             = "ImportoAmmenda"; 
    
    public static final String CAMPO_NUM_GIORNI_MAP                 = "NumGiorniMap"; 
    public static final String CAMPO_COD_TIPO_MISURA                = "CodTipoMisura"; 
    public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione"; 
    public static final String CAMPO_ALTRO_LUOGO_DETENZIONE         = "AltroLuogoDetenzione";     
    
    public static final String CAMPO_COD_DPR                     = "CodDpr"; 
    public static final String CAMPO_NOTE                        = "NoteComputo"; 

    public static final String CAMPO_GIORNO_DATA_RICHIESTA      = "GiornoDataRichiesta"; 
    public static final String CAMPO_MESE_DATA_RICHIESTA        = "MeseDataRichiesta"; 
    public static final String CAMPO_ANNO_DATA_RICHIESTA        = "AnnoDataRichiesta"; 

    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO      = "TitIdTitoloCumulato"; 
    public static final String CAMPO_DAT_ID_DATI_FINALI_CUMULO   = "DatIdDatiFinaliCumulo"; 
    public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO  = "IstrIdIstruttoriaCumulo"; 

    public static final String CAMPO_STAT_ID_STATO_ESEC_TIT_CUM    = "StatIdStatoEsecTitCum"; 
    
    // Altre costanti
    public static final String CAMPO_DESCR_ISTITUTO_DETENZIONE 		= "DescrIstitutoDetenzione"; 
    public static final String CAMPO_COD_COMPUTO_MC         	= "CodComputoMisuraCautelare"; 
    public static final String CAMPO_COD_COMPUTO_DET         	= "CodComputoPenaDetentiva";
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 
    public static final String CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA   = "TdsGiornoDataReclusioneDa"; 
    public static final String CAMPO_TDS_MESE_DATA_RECLUSIONE_DA     = "TdsMeseDataReclusioneDa"; 
    public static final String CAMPO_TDS_ANNO_DATA_RECLUSIONE_DA     = "TdsAnnoDataReclusioneDa"; 
    
    public static final String CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A    = "TdsGiornoDataReclusioneA"; 
    public static final String CAMPO_TDS_MESE_DATA_RECLUSIONE_A      = "TdsMeseDataReclusioneA"; 
    public static final String CAMPO_TDS_ANNO_DATA_RECLUSIONE_A      = "TdsAnnoDataReclusioneA"; 
    
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    //========================================== 
    // PRESOFFERTO 
    //========================================== 
    public static final String PG_ELENCO_PRESOFFERTI_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoPresoffertiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_PRESOFFERTI_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPresoffertoCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_PRESOFFERTI_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPresoffertoCumulo.jsp";
    //========================================== 
    // ANNOTAZIONI PAGAMENTI PENE PECUNIARIE 
    //========================================== 
    public static final String PG_ELENCO_PAGAMENTI_PP_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoPagamentiPP.jsp";
    public static final String PG_LOAD_INSERISCI_PAGAMENTI_PP_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciPagamentoPP.jsp";
    public static final String PG_LOAD_DETTAGLIO_PAGAMENTI_PP_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioPagamentoPP.jsp";
    //========================================== 
    // PROVVEDIMENTI G.E. - AMNISTIA/INDULTO 
    //========================================== 
    public static final String PG_ELENCO_AMNISTIA_INDULTO_CUMULO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoAmnistieIndultiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_AMNISTIA_INDULTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciAmnistiaIndultoCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_AMNISTIA_INDULTO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioAmnistiaIndultoCumulo.jsp";
    //========================================================================= 
    // PROVVEDIMENTI G.E. - REVOCA SOSPENSIONE CONDIZIONALE / NON MENZIONE
    //======================================================================= 
    public static final String PG_ELENCO_REVOCA_BENEFICIO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoAnnotazioniRevocheBeneficiCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_REVOCA_BENEFICIO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciAnnotazioneRevocaBeneficioCumulo.jsp";
    public static final String PG_DETTAGLIO_REVOCA_BENEFICIO_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioAnnotazioneRevocaBeneficioCumulo.jsp";

    //========================================== 
    // PROVVEDIMENTI G.E. - DEPENALIZZAZIONE 
    //========================================== 
    public static final String PG_ELENCO_DEPENALIZZAZIONE_CUMULO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoDepenalizzazioniCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_DEPENALIZZAZIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDepenalizzazioneCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_DEPENALIZZAZIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDepenalizzazioneCumulo.jsp";
    //========================================== 
    // PROVVEDIMENTI G.E. - INCOSTITUZIONALITA' 
    //========================================== 
    public static final String PG_ELENCO_INCOSTITUZIONALITA_CUMULO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoIncostituzionalitaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_INCOSTITUZIONALITA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciIncostituzionalitaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_INCOSTITUZIONALITA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioIncostituzionalitaCumulo.jsp";
    //========================================== 
    // PROVVEDIMENTI G.E. - SOSPENSIONE 
    //========================================== 
    public static final String PG_ELENCO_SOSPENSIONE_CUMULO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoSospensioniCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_SOSPENSIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciSospensioneCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSospensioneCumulo.jsp";
    //========================================== 
    // PROVVEDIMENTI G.E. - INTERRUZIONE 
    //========================================== 
    public static final String PG_ELENCO_INTERRUZIONE_CUMULO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoInterruzioniCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_INTERRUZIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciInterruzioneCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_INTERRUZIONE_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioInterruzioneCumulo.jsp";

    //==========================================================
    // PROVVEDIMENTI SORV. - Concessione Misure Alternative  
    //==========================================================
    public static final String PG_ELENCO_CONC_MISUREALTERNATIVE_CUMULO         = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoConcMisureAlternativeCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_CONC_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciConcMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_CONC_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioConcMisuraAlternativaCumulo.jsp";

    public static final String PG_LOAD_RICERCA_CONC_MISURAALTERNATIVA_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaConcMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_CONC_MISURAALTERNATIVA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioConcMisuraAlternativaCumulo.jsp";   

    //==========================================================
    // PROVVEDIMENTI SORV. - Revoca Misure Alternative  
    //==========================================================
    public static final String PG_ELENCO_REVOCHE_MISUREALTERNATIVE_CUMULO        = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoRevocheMisureAlternativeCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_REVOCA_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRevocaMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_REVOCA_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRevocaMisuraAlternativaCumulo.jsp";

    public static final String PG_LOAD_RICERCA_REVOCA_MISURAALTERNATIVA_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaRevocaMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_REVOCA_MISURAALTERNATIVA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRevocaMisuraAlternativaCumulo.jsp";   

    //==========================================================
    // PROVVEDIMENTI SORV. - Sospensione Misure Alternative  
    //==========================================================
    public static final String PG_ELENCO_SOSP_MISUREALTERNATIVE_CUMULO        	 = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoSospMisureAlternativeCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_SOSP_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciSospMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_SOSP_MISURAALTERNATIVA_CUMULO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSospMisuraAlternativaCumulo.jsp";

    public static final String PG_LOAD_RICERCA_SOSP_MISURAALTERNATIVA_CUMULO   = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaSospMisuraAlternativaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_SOSP_MISURAALTERNATIVA_CUMULO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSospMisuraAlternativaCumulo.jsp";   

    //==========================================================
    // PROVVEDIMENTI SORV. - Sospensione Esecuzione pena  
    //==========================================================
    public static final String PG_ELENCO_SOSP_ESECUZIONEPENA_CUMULO			   	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoSospEsecuzionePenaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_SOSP_ESECUZIONEPENA_CUMULO 	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciSospEsecuzionePenaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_SOSP_ESECUZIONEPENA_CUMULO 	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSospEsecuzionePenaCumulo.jsp";

    public static final String PG_LOAD_RICERCA_SOSP_ESECUZIONEPENA_CUMULO   	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaSospEsecuzionePenaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_SOSP_ESECUZIONEPENA_CUMULO  	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioSospEsecuzionePenaCumulo.jsp";   

    //==========================================================
    // PROVVEDIMENTI SORV. - Differimento pena  
    //==========================================================
    public static final String PG_ELENCO_DIFFERIMENTOPENA_CUMULO			   	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoDifferimentoPenaCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_DIFFERIMENTOPENA_CUMULO 		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDifferimentoPenaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_DIFFERIMENTOPENA_CUMULO 		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDifferimentoPenaCumulo.jsp";

    public static final String PG_LOAD_RICERCA_DIFFERIMENTOPENA_CUMULO   		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaDifferimentoPenaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_DIFFERIMENTOPENA_CUMULO  		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDifferimentoPenaCumulo.jsp";   

    //==========================================================
    // PROVVEDIMENTI SORV. - Espulsione  
    //==========================================================
    public static final String PG_ELENCO_ESPULSIONE_CUMULO			   	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoEspulsioneCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_ESPULSIONE_CUMULO 		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciEspulsioneCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_ESPULSIONE_CUMULO 		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioEspulsioneCumulo.jsp";

    public static final String PG_LOAD_RICERCA_ESPULSIONE_CUMULO   		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadRicercaEspulsioneCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_ESPULSIONE_CUMULO  		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioEspulsioneCumulo.jsp";   

    //================================================ 
    // ANNOTAZIONI DI RIDETERMINAZIONE PENA PM - ALTRO
    //================================================ 
    public static final String PG_ELENCO_RIDET_PENA_PM_ALTRO     = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoRidetPenaPMAltroCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_RIDET_PENA_PM_ALTRO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRidetPenaPMAltroCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_RIDET_PENA_PM_ALTRO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRidetPenaPMAltroCumulo.jsp";
    
}