package siap.sius.remissionedebito.action;

import f3b.web.IWebConstants;

public interface ICostantiRemissioneDebito
{
  //========================================================================== 
  // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
  //========================================================================== 
  public static final String CAMPO_ID_RICHIESTA_REMISSIONE       = "IdRichiestaRemissione"; 
  public static final String CAMPO_ANNO_PARTITA                  = "AnnoPartita"; 
  public static final String CAMPO_NUM_PARTITA                   = "NumPartita"; 
  public static final String CAMPO_NUM_EX_CAMPIONE               = "NumExCampione"; 
  public static final String CAMPO_PROT_CIRCOSRIZIONE_DOGANALE   = "ProtCircosrizioneDoganale"; 
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE   = "CodTipoAutoritaEmittente"; 
  public static final String CAMPO_COD_LUOGO_EMITTENTE           = "CodLuogoEmittente"; 

  public static final String COD_TIPO_PROVVEDIMENTO           	 = "CodTipoProvvedimento"; 
  public static final String DATA_EMISSIONE           			 = "DataEmissione"; 
  public static final String CAMPO_GIORNO_DATA_EMISSIONE         = "GiornoDataEmissioneo"; 
  public static final String CAMPO_MESE_DATA_EMISSIONE           = "MeseDataEmissione"; 
  public static final String CAMPO_ANNO_DATA_EMISSIONE           = "AnnoDataEmissione"; 
  public static final String COD_AUTORITA_EMITTENTE_PROVV        = "CodAutoritaEmittenteProvv"; 
  public static final String COD_LUOGO_EMITTENTE_PROVV           = "CodLuogoEmittenteProvv"; 
  public static final String FLAG_SPESE_CARCERE           		 = "FlagSpeseCarcere"; 
  public static final String IMPORTO_SPESE_CARCERE               = "ImportoSpeseCarcere"; 
  public static final String FLAG_SPESE_PROCEDIMENTO             = "FlagSpeseProcedimento"; 
  public static final String IMPORTO_SPESE_PROCEDIMENTO          = "ImportoSpeseProcedimento"; 

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
  public static final String CAMPO_ANNO_PROVVEDIMENTO   	  	 = "AnnoProvvedimento"; 
  public static final String CAMPO_NUMERO_PROVVEDIMENTO          = "NumeroProvvedimento";
  public static final String CAMPO_NOTE									         = "Note";
  
  //================================================================= 
  // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
  // nelle classi del progetto 
  //================================================================= 
  public static final String FORM_NAME                 			 = "FormName"; 
  
  //========================================== 
  // Costanti che rappresentano le pagine jsp  
  //========================================== 
  public static final String PG_LOAD_RICERCARICHIESTAREMISSIONE    = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadRicercaRichiestaRemissione.jsp";
  public static final String PG_LOAD_DETTAGLIORICHIESTAREMISSIONE  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioRichiestaRemissione.jsp";
  public static final String PG_RICERCARICHIESTAREMISSIONE         = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/RicercaRichiestaRemissione.jsp";
  public static final String PG_LOAD_INSERISCIRICHIESTAREMISSIONE  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadInserisciRichiestaRemissione.jsp";
  public static final String PG_LOAD_INSERISCITRASMISSIONEREMISSIONE  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadInserisciTrasmissioneRemissione.jsp";
  public static final String PG_LOAD_CANCELLARICHIESTAREMISSIONE   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/CancellarichiestaRemissione.jsp";
  public static final String PG_LOAD_DETTAGLIO_RICHIESTA_REMISSIONE   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioRichiestaRemissione.jsp";
  public static final String PG_LOAD_MODIFICA_RICHIESTA_REMISSIONE   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadModificaRichiestaRemissione.jsp";
  public static final String PG_DETTAGLIO_TRASMISSIONE_REMISSIONE   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioTrasmissioneRemissione.jsp";
  public static final String PG_LOAD_INSERISCI_ANNOTAZIONE_PROVVEDIMENTO   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadInserisciAnnotazioneProvvedimento.jsp";
  public static final String PG_DETTAGLIO_INSERISCI_ANNOTAZIONI_PROVVEDIMENTO  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioInserisciAnnotazioneProvvedimento.jsp";
  public static final String PG_GRIGLIA_REMISSIONE = IWebConstants.ROOT_DIR + "/files/siap/sius/remissionedebito/GrigliaBottoniRemissione.jsp";
  public static final String PG_LOAD_RISCONTRO_TRASMISSIONE_REMISSIONE  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadRiscontroTrasmissioneRemissione.jsp";
  public static final String PG_LISTA_DOCUMENTI_SIUS_CP_PEC  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/ListaDocumentiSiusCPPec.jsp";
  public static final String PG_LOAD_INSERISCI_ANNULLA_PARTITA  = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/LoadInserisciAnnullaPartita.jsp";
  public static final String PG_LOAD_DETTAGLIO_ANNULLA_PARTITA   = IWebConstants.ROOT_DIR + "files/siap/sius/remissionedebito/DettaglioAnnullaPartitadiCredito.jsp";
}
