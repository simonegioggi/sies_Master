package siap.siep.modulocumulo.action;

import f3b.web.IWebConstants;

public interface ICostantiRichiestePmInCumulo {
  public static final String CAMPO_PARENT_FORM_NAME = "ParentFormName";
  public static final String CAMPO_PARENT_FORM_TYPE = "ParentFormType";
  //
  public static final String FORM_TYPE_RIC_APP_BENEFICI = "RichiestaAppBenefici";
  
  public static final String FORM_TYPE_RIC_REV_BENEFICI   = "RichiestaRevBenefici";
  public static final String FORM_TYPE_RIC_REV_PENA_PRINC = "RichiestaRevPenaPrinc";
  public static final String FORM_TYPE_RIC_REV_SAN_SOST   = "RichiestaRevSanSost";
  
  public static final String FORM_TYPE_RIC_SOST_PA  = "RichiestaSostPA";
  public static final String FORM_TYPE_RIC_APPL_PA  = "RichiestaApplPA";
 
  public static final String FORM_TYPE_RIC_REV_TEST   = "RichiestaRevTEST";
 
  
  //========================================================================== 
  // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
  //========================================================================== 
  public static final String CAMPO_ID_RICHIESTE_PM_IN_CUMULO     = "IdRichiestePmInCumulo"; 
  public static final String CAMPO_COD_TIPO_RICHIESTA            = "CodTipoRichiesta"; 
  public static final String CAMPO_COD_TIPO_ANNOTAZIONE          = "CodTipoAnnotazione"; 
  
  public static final String CAMPO_FLAG_PIU_MENO_R               = "FlagPiuMenoR"; 
  public static final String CAMPO_NUM_ANNI_RECLUSIONE_R         = "NumAnniReclusioneR"; 
  public static final String CAMPO_NUM_MESI_RECLUSIONE_R         = "NumMesiReclusioneR"; 
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE_R       = "NumGiorniReclusioneR"; 
  public static final String CAMPO_IMPORTO_MULTA_R               = "ImportoMultaR"; 
  public static final String CAMPO_NUM_ANNI_ARRESTO_R            = "NumAnniArrestoR"; 
  public static final String CAMPO_NUM_MESI_ARRESTO_R            = "NumMesiArrestoR"; 
  public static final String CAMPO_NUM_GIORNI_ARRESTO_R          = "NumGiorniArrestoR"; 
  public static final String CAMPO_IMPORTO_AMMENDA_R             = "ImportoAmmendaR"; 
  
  public static final String CAMPO_FLAG_APP_PROVVISORIA          = "FlagAppProvvisoria"; 
  
  public static final String CAMPO_ANNO_DATA_COMMESSO_REATO       = "AnnoCommessoReato"; 
  public static final String CAMPO_MESE_DATA_COMMESSO_REATO       = "MeseCommessoReato"; 
  public static final String CAMPO_GIORNO_DATA_COMMESSO_REATO     = "GiornoCommessoReato"; 
  public static final String CAMPO_DATA_COMMESSO_REATO            = "DataCommessoReato"; 
  
  public static final String CAMPO_COD_TIPO_PENA_ACCESSORIA      = "CodTipoPenaAccessoria"; 
  public static final String CAMPO_COD_TIPO_DURATA_PA            = "CodTipoDurataPa"; 
  public static final String CAMPO_NUM_ANNI_PA                   = "NumAnniPa"; 
  public static final String CAMPO_NUM_MESI_PA                   = "NumMesiPa"; 
  public static final String CAMPO_NUM_GIORNI_PA                 = "NumGiorniPa"; 
  
  public static final String CAMPO_COD_FONTE                     = "CodFonte"; 
  public static final String CAMPO_ANNO_FONTE                    = "AnnoFonte"; 
  public static final String CAMPO_NUMERO_FONTE                  = "NumeroFonte"; 
  public static final String CAMPO_ARTICOLO                      = "Articolo"; 
  public static final String CAMPO_COD_SOTTONUMERAZIONE          = "CodSottonumerazione"; 
  public static final String CAMPO_COMMA                         = "Comma"; 
  public static final String CAMPO_LETTERA                       = "Lettera"; 
  public static final String CAMPO_NUMERO                        = "Numero"; 
  
  public static final String CAMPO_ANNO_CC                       = "AnnoCc"; 
  public static final String CAMPO_NUMERO_CC                     = "NumeroCc"; 
  public static final String CAMPO_GIORNO_DATA_CC                = "GiornoDataCc"; 
  public static final String CAMPO_MESE_DATA_CC                  = "MeseDataCc"; 
  public static final String CAMPO_ANNO_DATA_CC                  = "AnnoDataCc"; 
  
  public static final String CAMPO_COD_DPR                       = "CodDpr"; 
  
  public static final String CAMPO_MOTIVAZIONI                   = "Motivazioni"; 
  public static final String CAMPO_NOTE_RECLUSIONE               = "NoteReclusione"; 
  
  public static final String CAMPO_NUM_GIORNI_LA_REV             = "NumGiorniLARev"; 
  public static final String CAMPO_NUM_GIORNI_LS_REV             = "NumGiorniLSRev"; 
  public static final String CAMPO_NUM_GIORNI_LI_REV             = "NumGiorniLIRev"; 
  
   
  //Campi per lo scarico della decisione che non dovrebbe stare qui
  public static final String CAMPO_COD_UFFICIO_EMITTENTE         = "CodUfficioEmittente"; 
  public static final String CAMPO_COD_LUOGO_EMITTENTE           = "CodLuogoEmittente"; 
  public static final String CAMPO_GIORNO_DATA_EMISSIONE         = "GiornoDataEmissione"; 
  public static final String CAMPO_MESE_DATA_EMISSIONE           = "MeseDataEmissione"; 
  public static final String CAMPO_ANNO_DATA_EMISSIONE           = "AnnoDataEmissione"; 
  public static final String CAMPO_ANNO_PROVV                    = "AnnoProvv"; 
  public static final String CAMPO_NUMERO_PROVV                  = "NumeroProvv"; 
  public static final String CAMPO_FLAG_CONFORME                 = "FlagConforme"; 
  public static final String CAMPO_FLAG_PIU_MENO_D               = "FlagPiuMenoD"; 
  public static final String CAMPO_NUM_ANNI_RECLUSIONE_D         = "NumAnniReclusioneD"; 
  public static final String CAMPO_NUM_MESI_RECLUSIONE_D         = "NumMesiReclusioneD"; 
  public static final String CAMPO_NUM_GIORNI_RECLUSIONE_D       = "NumGiorniReclusioneD"; 
  public static final String CAMPO_IMPORTO_MULTA_D               = "ImportoMultaD"; 
  public static final String CAMPO_NUM_ANNI_ARRESTO_D            = "NumAnniArrestoD"; 
  public static final String CAMPO_NUM_MESI_ARRESTO_D            = "NumMesiArrestoD"; 
  public static final String CAMPO_NUM_GIORNI_ARRESTO_D          = "NumGiorniArrestoD"; 
  public static final String CAMPO_IMPORTO_AMMENDA_D             = "ImportoAmmendaD"; 
  public static final String CAMPO_GIORNO_DATA_EMISSIONE_D       = "GiornoDataEmissione"; 
  public static final String CAMPO_MESE_DATA_EMISSIONE_D         = "MeseDataEmissione"; 
  public static final String CAMPO_ANNO_DATA_EMISSIONE_D         = "AnnoDataEmissione"; 
  public static final String CAMPO_MOTIVAZIONI_D                 = "MotivazioniD"; 
  public static final String CAMPO_NUM_GIORNI_LA_REV_D           = "NumGiorniLARevD"; 
  public static final String CAMPO_NUM_GIORNI_LS_REV_D           = "NumGiorniLSRevD"; 
  public static final String CAMPO_NUM_GIORNI_LI_REV_D           = "NumGiorniLIRevD"; 
  //
  
  
  public static final String CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO    = "IstrIdIstruttoriaCumulo"; 
  public static final String CAMPO_TIT_ID_TITOLO_CUMULATO        = "TitIdTitoloCumulato"; 
  public static final String CAMPO_BEN_ID_BENEFICIO_CUMULO       = "BenIdBeneficioCumulo"; 
  public static final String CAMPO_TIT_ID_TITOLO_CUMULATO_REF    = "TitIdTitoloCumulatoRef"; 
  public static final String CAMPO_SAN_ID_SANZIONE_SOST_CUM      = "SanIdSanzioneSostCum"; 
  public static final String CAMPO_PEN_ID_PENA_ACCESSORIA_CUMULO = "PenIdPenaAccessoriaCumulo"; 

  public static final String CAMPO_ID_TITOLO_SELEZIONATO   		 = "IdTitoloSelezionato"; 
  public static final String CAMPO_CHECK_SCELTA_TITOLI			 = "CheckSceltaTitoli";
  
  // Costanti per la form di Inserimento RICHIESTE_INVIATE
  public static final String CAMPO_ID_RICHIESTA_INVIATA_CUM   	 = "IdRichiestaInviataCum"; 
  public static final String CAMPO_ID_RICHIESTE_SELEZIONATE   	 = "IdRichiesteSelezionate"; 
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE      = "GiornoDataTrasmissione"; 
  public static final String CAMPO_MESE_DATA_TRASMISSIONE        = "MeseDataTrasmissione"; 
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE        = "AnnoDataTrasmissione"; 
  
  public static final String CAMPO_CONTENUTO			        = "Contenuto"; 
  public static final String CAMPO_MAGISTRATO			        = "Magistrato"; 
  
  public static final String CAMPO_UFFICIO_DEST         		= "CodUfficioDest"; 
  public static final String CAMPO_SEDE_UFFICIO_DEST         	= "CodLuogoDest"; 
  
  public static final String CAMPO_BLOB 			= "CampoBlob";
  public static final String CAMPO_VALIDA 			= "CampoValida";
  public static final String CAMPO_AZIONE_DETTAGLIO	= "AzioneDettaglio";
  
  // Costanti per la form di Inserimento REVOCA RICHIESTE
  public static final String CAMPO_ID_BENEFICIO_CUM_SEL_1     = "IdBeneficioCumSel1"; 
  public static final String CAMPO_ID_BENEFICIO_CUM_SEL_2     = "IdBeneficioCumSel2"; 
  public static final String CAMPO_ESTREMI_TITOLO_REVOCANTE   = "EstremiTitoloRevocante"; 
  
  public static final String CAMPO_DESCR_TIPO_PROVV 		  	= "DescrTipoProvvedimento"; 
  public static final String CAMPO_DESCR_TIPO_AUTO_EMITTENTE 	= "DescrTipoAutoritaEmittente"; 
  public static final String CAMPO_DESCR_LUOGO_AUTO_EMITTENTE 	= "DescrLuogoEmittente"; 
  public static final String CAMPO_DATA_PROVV 		  			= "DataProvvedimento"; 
  public static final String CAMPO_DATA_IRR 		  			= "DataIrrevocabilita"; 
  
  public static final String CAMPO_COD_ARTICOLO                 = "cODArticolo"; 
  public static final String CAMPO_COD_MOTIVO 		  			= "CodMotivo"; 
  
  public static final String CAMPO_ID_SANZIONE_SOST_SELEZIONATA = "IdSanzioneSostSelezionata";
  
  public static final String CAMPO_ID_TITOLO_PA_SELEZIONATI = "IdTitoloPASelezionati";
  public static final String CAMPO_ESTREMI_CONDONO_INDULTO  = "EstremiCondonoIndulto";
  public static final String CAMPO_ESTREMI_CONDONO_DPR      = "EstremiCondonoDpr";		  
  
  // Inserimento / Modifica decisioni del G.E.
  public static final String CAMPO_CHECK_REVOCA_BEN_1		 	 = "CheckRevocaBen1";
  public static final String CAMPO_CHECK_REVOCA_BEN_2		 	 = "CheckRevocaBen2";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO        = "CodTipoProvvedimento";
  
  // Applicazione Pena Accessoria
  public static final String CAMPO_ESTREMI_TITOLO_CUMULATO   = "EstremiTitoloCumulato"; 
  
  // Richieste sl GE: Revoca Benefici
  public static final String CAMPO_ID_TITOLO_BEN_STATOESEC_COMP 	= "IdTitoloBenStatoEsecComp";
  public static final String CAMPO_ID_STATOESEC_CUM_SEL 				= "IdStatoEsecCumSel";
  
  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_POPUP_LISTA_TITOLI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoTitoliPerSelezione.jsp";
  public static final String PG_POPUP_TEST = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoTitoliPerTest.jsp";
  public static final String PG_POPUP_LISTA_TITOLI_PA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadPopupElencoTitoliPerPA.jsp";
  public static final String PG_POPUP_PA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/ElencoTitoliPerTest.jsp";
  
  // Richieste al GE - Richieste Emesse
  public static final String PG_DETT_RICHIESTA_EMESSA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaDelPMEmessa.jsp";

  // Richieste al GE - Applicazione Benefici 
  public static final String PG_POPUP_LISTA_TITOLI_APPLICAZIONE_BENEFICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/PopupTitoliPerRichiestaApplicazioneBenefici.jsp";
  public static final String PG_ELENCO_TITOLI_RICG_GE_BENEFICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaGEBenefici.jsp";
  
  public static final String PG_INS_RICH_GE_BENEFICI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGEBenefici.jsp";
  public static final String PG_MOD_RICH_GE_BENEFICI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaGEBenefici.jsp";
  public static final String PG_DETT_RICH_GE_BENEFICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGEBenefici.jsp";
//	Decisioni de GE
  public static final String PG_INSERIMENTO_DECISIONE_GE	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDecisioneDelGECumulo.jsp";
  public static final String PG_DETTAGLIO_DECISIONE_GE 		= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioDecisioneDelGECumulo.jsp";  
  
  //Richieste al GE - Revoca Benefici 
  public static final String PG_ELENCO_TITOLI_RIC_GE_REV_BENEFICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaGERevocaBenefici.jsp";
  public static final String PG_POPUP_ELENCO_TITOLI_REVOCANTI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadPopupElencoTitoliRevocaRichGE.jsp";
  public static final String PG_INS_RICH_GE_REV_BENEFICI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGERevocaBenefici.jsp";
  public static final String PG_MOD_RICH_GE_REV_BENEFICI  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaGERevocaBenefici.jsp";
  public static final String PG_DETT_RICH_GE_REV_BENEFICI = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGERevocaBenefici.jsp";
//	Decisioni de GE 
  public static final String PG_INS_DECISIONE_GE_REVOCA_BEN	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGERevocaBeneficioCumulo.jsp";
  
  //Richieste al GE - Revoca Pena Principale 
  public static final String PG_ELENCO_TITOLI_RICG_GE_REVOCA_PENA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaGERevocaPenaCum.jsp";
  public static final String PG_INS_RICH_GE_REV_PENA_PRINC  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGERevocaPenaPrinc.jsp";
  public static final String PG_MOD_RICH_GE_REVOCA_PENA_PRINC  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaGERevocaPenaPrinc.jsp";
  public static final String PG_DETT_RICH_GE_REV_PENA_PRINC = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGERevocaPenaPrincCum.jsp";
//	Decisioni de GE 
  public static final String PG_INSMOD_DEC_GE_REVOCA_PENA_PRINC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGERevocaPenaPrincCum.jsp";
  
  //Richieste al GE - Revoca Sanzione Sostitutiva
  public static final String PG_ELENCO_TITOLI_RICG_GE_REVOCA_SS_CUM = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaGERevocaSSCum.jsp";
  public static final String PG_INS_RICH_GE_REV_SAN_SOST  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGERevocaSS.jsp";
  public static final String PG_DETT_RICH_GE_REV_SAN_SOST = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGERevocaSSCum.jsp";
  
  //Richieste al GE - Sostituzione di PA
  public static final String PG_ELENCO_TITOLI_RICH_GE_SOST_PENA_ACC_CUM = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichGESostituzionePenaAccCum.jsp";
  public static final String PG_INS_RICH_GE_SOST_PA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGESostPA.jsp";
  public static final String PG_DETT_RICH_GE_SOST_PA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGESostPA.jsp";
//	Decisioni de GE 
  public static final String PG_INSMOD_DEC_GE_SOST_PENA_ACC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGESostPenaAcc.jsp";  

  //Richieste al GE - Revoca di PA
  public static final String PG_ELE_TITOLI_RICH_GE_REVOCA_PENA_ACC_CUM = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichGERevocaPenaAccCum.jsp";
  public static final String PG_INS_RICH_GE_REV_PA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGERevocaPA.jsp";
  public static final String PG_MOD_RICH_GE_REV_PA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaGERevocaPA.jsp";
  public static final String PG_DETT_RICH_GE_REVOCA_PA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGERevocaPA.jsp";
//	Decisioni de GE 
  public static final String PG_INSMOD_DEC_GE_REVOCA_PENA_ACC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGERevocaPenaAcc.jsp";   

  //Richieste al GE - Applicazione di PA
  public static final String PG_INS_RICH_GE_APP_PA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGEApplicaPA.jsp";
  public static final String PG_MOD_RICH_GE_APP_PA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaGEApplicaPA.jsp";
  public static final String PG_DETT_RICH_GE_APP_PA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGEApplicaPA.jsp";
//	Decisioni de GE 
  public static final String PG_INSMOD_DEC_GE_APPL_PENA_ACC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGEApplicaPenaAcc.jsp";

  //Richieste al GE - Altre Richieste
  public static final String PG_INS_RICH_GE_ALTRO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaGEAltro.jsp";
  public static final String PG_DETT_RICH_GE_ALTRO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaGEAltro.jsp";
//	Decisioni de GE 
  public static final String PG_INSMOD_DEC_GE_ALTRE_RIC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDelGEAltreRic.jsp";  


  //============================================================================
  //  Richiesta alla Sorveglianza
  //============================================================================
  // Richieste alla SORV - Revoca Liberazione Anticipata
  public static final String PG_INS_RICH_SORV_REV_LA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRichiestaSORVRevocaLA.jsp";
  public static final String PG_MOD_RICH_SORV_REV_LA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaSORVRevocaLA.jsp";
  public static final String PG_DETT_RICH_SORV_REV_LA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaSORVRevocaLA.jsp";
  public static final String PG_ELENCO_TITOLI_RIC_SORV_REV_LA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaSORVRevLA.jsp";
  public static final String PG_DETT_RICHIESTA_SORV_EMESSA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaDelPMallaSORVEmessa.jsp";
  public static final String PG_INSERIMENTO_DECISIONE_SORV	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciDecisioneDellaSORVCumulo.jsp";

  // Richieste alla SORV - Unificazione Misure di Sicurezza
  public static final String PG_ELENCO_TITOLI_RIC_SORV_UNIF_MIS_SIC = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaSORVUnifMisSic.jsp";
  public static final String PG_INS_RICH_SORV_UNIFICA_MS  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRichiestaSORVUnificaMS.jsp";
  public static final String PG_MOD_RICH_SORV_UNIFICA_MS  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadModRichiestaSORVUnificaMS.jsp";
  public static final String PG_DETT_RICH_SORV_UNIFICA_MS = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaSORVUnificaMS.jsp";
  public static final String CAMPO_ID_MIS_SIC_CUM_SEL    = "IdMisSicCumSel1"; 
  
  // Richieste alla SORV - Revoca Misura Alternativa
  public static final String CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO = "IdTitoloMisAltSelezionato";
  public static final String CAMPO_DATA_REVOCA 			= "DataRevoca";
  public static final String CAMPO_GIORNO_DATA_REVOCA 	= "GiornoDataRevoca";
  public static final String CAMPO_MESE_DATA_REVOCA 	= "MeseDataRevoca";
  public static final String CAMPO_ANNO_DATA_REVOCA 	= "AnnoDataRevoca";
  public static final String PG_ELENCO_TITOLI_RICH_SORV_REVOCA_MA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadElencoTitoliRichiestaSORVRevocaMA.jsp";
  public static final String PG_INS_RICH_SORV_REV_MA  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInserisciRichiestaSORVRevocaMA.jsp";
  public static final String PG_DETT_RICH_SORV_REV_MA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaSORVRevocaMA.jsp";
  //	Decisioni dela SORV 
  public static final String PG_INSMOD_DEC_SORV_REVOCA_MA = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDellaSORVRevocaMA.jsp";

  // Richieste alla SORV - Unificazione Misure di Sicurezza
  public static final String CAMPO_NUM_ANNI_MS            = "NumAnniMS"; 
  public static final String CAMPO_NUM_MESI_MS            = "NumMesiMS"; 
  public static final String CAMPO_NUM_GIORNI_MS          = "NumGiorniMS"; 
  
  //Richieste alla SORV - Altre Richieste
  public static final String PG_INS_RICH_SORV_ALTRO  = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsRichiestaSORVAltro.jsp";
  public static final String PG_DETT_RICH_SORV_ALTRO = IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/DettaglioRichiestaSORVAltro.jsp";
  //	Decisioni dela SORV 
  public static final String PG_INSMOD_DEC_SORV_ALTRE_RIC	= IWebConstants.ROOT_DIR  + "files/siap/siep/modulocumulo/LoadInsDecisioneDellaSORVAltreRic.jsp";  
  //======================================================= 
  // Costanti che rappresentano codifiche  
  //======================================================= 
  public static final String RICHIESTA_AL_GE = "01";
  public static final String RICHIESTA_ALLA_SORV = "02";
  
}
