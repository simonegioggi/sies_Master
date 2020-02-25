package siap.siep.penapecuniaria.action;

import f3b.web.IWebConstants;

public interface ICostantiPenaPecuniaria
{
  //========================================================================== 
  // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
  //========================================================================== 
  public static final String CAMPO_ID_RICHIESTA_CONVERSIONE      = "IdRichiestaConversione"; 
  public static final String CAMPO_ANNO_PARTITA                  = "AnnoPartita"; 
  public static final String CAMPO_NUM_PARTITA                   = "NumPartita"; 
  public static final String CAMPO_NUM_EX_CAMPIONE               = "NumExCampione"; 
  public static final String CAMPO_PROT_CIRCOSRIZIONE_DOGANALE   = "ProtCircosrizioneDoganale"; 
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE   = "CodTipoAutoritaEmittente"; 
  public static final String CAMPO_COD_LUOGO_EMITTENTE           = "CodLuogoEmittente"; 
  public static final String CAMPO_GIORNO_DATA_RICEZIONE_ATTO    = "GiornoDataRicezioneAtto"; 
  public static final String CAMPO_MESE_DATA_RICEZIONE_ATTO      = "MeseDataRicezioneAtto"; 
  public static final String CAMPO_ANNO_DATA_RICEZIONE_ATTO      = "AnnoDataRicezioneAtto"; 
  public static final String CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO   = "GiornoDataIscrizioneAtto"; 
  public static final String CAMPO_MESE_DATA_ISCRIZIONE_ATTO     = "MeseDataIscrizioneAtto"; 
  public static final String CAMPO_ANNO_DATA_ISCRIZIONE_ATTO     = "AnnoDataIscrizioneAtto"; 
  public static final String CAMPO_GIORNO_DATA_ESAZIONE          = "GiornoDataEsazione"; 
  public static final String CAMPO_MESE_DATA_ESAZIONE            = "MeseDataEsazione"; 
  public static final String CAMPO_ANNO_DATA_ESAZIONE            = "AnnoDataEsazione"; 
  public static final String CAMPO_IMPORTO_MULTA                 = "ImportoMulta"; 
  public static final String CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA = "GiornoDataPrescrizioneMulta"; 
  public static final String CAMPO_MESE_DATA_PRESCRIZIONE_MULTA  = "MeseDataPrescrizioneMulta"; 
  public static final String CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA  = "AnnoDataPrescrizioneMulta"; 
  public static final String CAMPO_FLAG_IMPRESCRITTIBILE_MULTA   = "FlagImprescrittibileMulta"; 
  public static final String CAMPO_IMPORTO_AMMENDA               = "ImportoAmmenda"; 
  public static final String CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA = "GiornoDataPrescrizioneAmmenda"; 
  public static final String CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA = "MeseDataPrescrizioneAmmenda"; 
  public static final String CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA = "AnnoDataPrescrizioneAmmenda"; 
  public static final String CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA = "FlagImprescrittibileAmmenda"; 
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP     = "FasSieIdFascicoloSiep"; 
  public static final String CAMPO_EVE_ID_EVENTO                 = "EveIdEvento"; 
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO     = "CodOperatoreInserimento"; 
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO       = "GiornoDataInserimento"; 
  public static final String CAMPO_MESE_DATA_INSERIMENTO         = "MeseDataInserimento"; 
  public static final String CAMPO_ANNO_DATA_INSERIMENTO         = "AnnoDataInserimento"; 
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO       = "CodUfficioInserimento"; 
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO   = "CodOperatoreAggiornamento"; 
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO     = "GiornoDataAggiornamento"; 
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO       = "MeseDataAggiornamento"; 
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO       = "AnnoDataAggiornamento"; 
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO     = "CodUfficioAggiornamento"; 
  public static final String CAMPO_COD_SEDE_UDS                  = "CodSedeUds"; 
  public static final String CAMPO_ANNO_PROVVEDIMENTO   	  		 = "AnnoProvvedimento"; 
  public static final String CAMPO_NUMERO_PROVVEDIMENTO          = "NumeroProvvedimento";
  public static final String CAMPO_NOTE									         = "Note";
  
  //================================================================= 
  // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
  // nelle classi del progetto 
  //================================================================= 
  public static final String FORM_NAME                 			 = "FormName"; 
  public static final String CAMPO_DURATA_ESITO_ANNI			 = "DurataAnni";
  public static final String CAMPO_DURATA_ESITO_MESI			 = "DurataMesi";
  public static final String CAMPO_DURATA_ESITO_GIORNI			 = "DurataGiorni";  
  public static final String CAMPO_NUMERO_RATE			 		= "NumeroRate";
  public static final String CAMPO_VALORE_RATA			 		= "ValoreRata"; 
  public static final String CAMPO_VALORE_RATA_I          = "ValoreRataI"; 
  public static final String CAMPO_VALORE_RATA_D          = "ValoreRataD"; 
  public static final String CAMPO_VALORE_ULTIMA_RATA			= "ValoreUltimaRata";
  public static final String CAMPO_VALORE_ULTIMA_RATA_I     = "ValoreUltimaRataI";
  public static final String CAMPO_VALORE_ULTIMA_RATA_D     = "ValoreUltimaRataD";
  
  public static final String CAMPO_GIORNO_DATA_ANNULLA          = "GiornoDataAnnulla"; 
  public static final String CAMPO_MESE_DATA_ANNULLA            = "MeseDataAnnulla"; 
  public static final String CAMPO_ANNO_DATA_ANNULLA            = "AnnoDataAnnulla";   
  
  public static final String CAMPO_DURATA_ESITO_ANNI_LIB			 = "DurataAnniLib";
  public static final String CAMPO_DURATA_ESITO_MESI_LIB			 = "DurataMesiLib";
  public static final String CAMPO_DURATA_ESITO_GIORNI_LIB			 = "DurataGiorniLib";
  public static final String CAMPO_DURATA_ESITO_ANNI_DIFF			 = "DurataAnniDiff";
  public static final String CAMPO_DURATA_ESITO_MESI_DIFF			 = "DurataMesiDiff";
  public static final String CAMPO_DURATA_ESITO_GIORNI_DIFF			 = "DurataGiorniDiff";
  public static final String CAMPO_DURATA_ESITO_ANNI_LAV			 = "DurataAnniLav";
  public static final String CAMPO_DURATA_ESITO_MESI_LAV			 = "DurataMesiLav";
  public static final String CAMPO_DURATA_ESITO_GIORNI_LAV			 = "DurataGiorniLav";
  public static final String CAMPO_GIORNO_DATA_INIZIO_PAGA          = "GiornoDataInizioPaga"; 
  public static final String CAMPO_MESE_DATA_INIZIO_PAGA            = "MeseDataInizioPaga"; 
  public static final String CAMPO_ANNO_DATA_INIZIO_PAGA            = "AnnoDataInizioPaga";
  public static final String CAMPO_GIORNI_INIZIO_PAGA            	= "GiorniInizioPaga";
  
  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_LOAD_RICERCARICHIESTACONVERSIONE    = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadRicercaRichiestaConversione.jsp";
  public static final String PG_LOAD_DETTAGLIORICHIESTACONVERSIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/DettaglioRichiestaConversione.jsp";
  public static final String PG_RICERCARICHIESTACONVERSIONE         = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/RicercaRichiestaConversione.jsp";
  public static final String PG_LOAD_INSERISCIRICHIESTACONVERSIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadInserisciRichiestaConversione.jsp";
  public static final String PG_LOAD_INSERISCITRASMISSIONECONVERSIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadInserisciTrasmissioneConversione.jsp";
  public static final String PG_LOAD_CANCELLARICHIESTACONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/CancellarichiestaConversione.jsp";
  public static final String PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/DettaglioRichiestaConversione.jsp";
  public static final String PG_LOAD_MODIFICA_RICHIESTA_CONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadModificaRichiestaConversione.jsp";
  public static final String PG_DETTAGLIO_TRASMISSIONE_CONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/DettaglioTrasmissioneConversione.jsp";
  public static final String PG_LOAD_INSERISCI_ANNOTAZIONE_PROVVEDIMENTO   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadInserisciAnnotazioneProvvedimento.jsp";
  public static final String PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/DettaglioInserisciAnnotazioneProvvedimento.jsp";
  public static final String PG_GRIGLIA_CONVERSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/penapecuniaria/GrigliaBottoniConversione.jsp";
  public static final String PG_LOAD_RISCONTRO_TRASMISSIONE_CONVERSIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadRiscontroTrasmissioneConversione.jsp";
  public static final String PG_LISTA_DOCUMENTI_SIUS_CP_PEC  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/ListaDocumentiSiusCPPec.jsp";
  public static final String PG_LOAD_INSERISCI_ANNULLA_PARTITA  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadInserisciAnnullaPartita.jsp";
  public static final String PG_LOAD_DETTAGLIO_ANNULLA_PARTITA   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/DettaglioAnnullaPartitadiCredito.jsp";
  public static final String PG_LISTA_DOCUMENTI_SIUS_CP_PEC_EPS  = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/ListaDocumentiSiusCPPecEPS.jsp";
  public static final String PG_LOAD_INSERISCI_ANNOTAZIONI_REVOCA_CONV_SANZ_SOST	= IWebConstants.ROOT_DIR + "/files/siap/siep/penapecuniaria/LoadAnnotazioneRevocaConversioneSanzSost.jsp";
  public static final String PG_DETTAGLIO_ANNOTAZIONE_REVOCA_CONV_SANZ_SOST	= IWebConstants.ROOT_DIR + "/files/siap/siep/penapecuniaria/DettaglioAnnotazioneRevocaConversioneSanzSost.jsp";
  public static final String PG_LOAD_RIDETPENA_REVOCA_SSPP   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadRideterminazionePenaRevocaSSPP.jsp";
  public static final String PG_LOAD_DETTAGLIO_RIDETPENA_REVOCA_SSPP   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadDettaglioRideterminazionePenaRevocaSSPP.jsp";
}

