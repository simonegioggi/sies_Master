package siap.sige.tenore.action;

/**
* <p>Title: ICostantiTenoreSige</p>
* <p>Description: Classe di costanti di TenoreSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;
public interface ICostantiTenoreSige
{
		 public static final String CAMPO_ID_TENORE_SIGE = "IdTenoreSige"; 
		 public static final String CAMPO_COD_OGGETTO_SIGE = "CodOggettoSige"; 
		 public static final String CAMPO_DESC_OGGETTO_SIGE = "DescOggettoSige"; 
		 public static final String CAMPO_COD_ESITO_TENORE_SIGE = "CodEsitoSige"; 
		 public static final String CAMPO_GIORNO_DATA = "GiornoData"; 
		 public static final String CAMPO_MESE_DATA = "MeseData"; 
		 public static final String CAMPO_ANNO_DATA = "AnnoData"; 
		 public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine"; 
		 public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine"; 
		 public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine"; 
		 public static final String CAMPO_RIC_SIG_ID_RICHIESTA_SIGE = "RicSigIdRichiestaSige"; 
		 public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep"; 
		 public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza";
		 public static final String CAMPO_DESC_SENTENZA = "DescSentenza";
		 public static final String CAMPO_REA_ID_REATO = "ReaIdReato"; 
		 public static final String CAMPO_DESC_REATO = "DescReato"; 
	  	 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento"; 
		 public static final String CAMPO_NOTE = "Note"; 
		 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento"; 
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento"; 
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento"; 
		 public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
		 public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento"; 
		 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento"; 
		 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento"; 
		 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento"; 
		 public static final String CAMPO_ID_TEN_SEN_REA = "IdTenSenRea"; 
		 public static final String CAMPO_COD_ESITO_TEN_SEN_REA = "CodEsito"; 
		 public static final String CAMPO_COD_TIPO_DATI_PROV = "CodTipoDatiProv";
		 public static final String CAMPO_ID_FASCICOLO_SIEP_SENTENZA = "IdFascicoloSiepSentenza";
		 public static final String PG_LOAD_RICERCATENORESIGE	= IWebConstants.ROOT_DIR + "jsp/files/siap/sige/tenore/LoadRicercaTenoreSige.jsp";
		 public static final String PG_LOAD_DETTAGLIOTENORISIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/DettaglioTenoriSige.jsp";
		 public static final String PG_LISTA_RICHIESTE_AL_GE= IWebConstants.ROOT_DIR + "files/siap/sige/richiesta/ListaRichiesteAlGE.jsp";
		 
		 public static final String BOTTONE_INSERISCI_OGGETTO	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/BottoneInserimento.jsp";
		 public static final String BOTTONE_CANCELLA_OGGETTO	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/BottoneCancellazione.jsp";
		 // MERGE v10 COLLAUDO: aggiunta nuova pagina
		 public static final String BOTTONE_CANCELLA_OGGETTO_LBG	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/BottoneCancellazioneLBG.jsp";
		 public static final String BOTTONE_MODIFICA_OGGETTO	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/BottoneModifica.jsp";
		 public static final String PG_RICERCATENORESIGE	= IWebConstants.ROOT_DIR + "jsp/files/siap/sige/tenore/RicercaTenoreSige.jsp";
		 public static final String PG_LOAD_INSERISCITENORESIGE	= IWebConstants.ROOT_DIR + "jsp/files/siap/sige/tenore/LoadInserisciTenoreSige.jsp";
		 public static final String PG_LOAD_DETTAGLIOTENORESIGE	= IWebConstants.ROOT_DIR + "";
		 public static final String PG_DETTAGLIO_TENORE_ESITO_SIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/DettaglioTenoreEsitoSige.jsp";
		 public static final String PG_INSERIMENTO_TENORE_ESITO_SIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/InserimentoEsitoOggetto.jsp";	

		 public static final String PG_INCLUDE_DETT_ANNOTAZIONE_MAN	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/IncDettAnnotazioneManuale.jsp";	
		 public static final String PG_INCLUDE_INC_ANNOTAZIONE_MAN	= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/IncAnnotazioniManualiBenefici.jsp";	

		 
		 public static final String CHECK_TIPO_ESITO = "chekEsito"; 
		 public static final String PG_BUTTONS_PROVVEDIMENTO_TENORI  = IWebConstants.ROOT_DIR + "files/siap/sige/tenore/buttonsProvvedimentoGestioneTenori.jsp";
		 public static final String PG_BUTTONS_PROVVEDIMENTO_TENORI_SINGOLI  = IWebConstants.ROOT_DIR + "files/siap/sige/tenore/buttonsProvvedimentoGestioneTenoriSingoli.jsp";
		 public static final String PG_BUTTONS_PROVVEDIMENTO_TENORI_MULTIPLI= IWebConstants.ROOT_DIR + "files/siap/sige/tenore/buttonsProvvedimentoGestioneTenoriMultipli.jsp";
		 
		 public static final String APPLICAZIONE_INDULTO  = "0091";
		 public static final String APPLICAZIONE_INDULTO_CONDIZIONATO  = "0093";
		 public static final String APPLICAZIONE_AMNISTIA  = "0090";
		 public static final String APPLICAZIONE_AMNISTIA_CONDIZIONATA  = "0092";

		 public static final String FLAG_INDULTO  = "FlagIndulto";
		 
		 public static final String COD_CONTENUTO  = "CodContenuto";
		 public static final String COD_CONTENUTO_SEL  = "CodContenutoSelected";
		 
}