package siap.siep.richiesta.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import f3b.web.IWebConstants;
import siap.siep.web.ISIEPCostantiWeb;

public interface ICostantiRichiesta extends ISIEPCostantiWeb {

  public static final String SEDE_SIEP                  = "SedeSiep";
  public static final String AUTORITA_DESTINATARIO      = "AutoritaDestinatario";
  public static final String AUTORITA_SEDE              = "AutoritaSede";
  public static final String CAMPO_LIBERO               = "CampoLibero";
  public static final String CAMPO_DATA_GG_EMISSIONE    = "DataGGEmissione";
  public static final String CAMPO_DATA_MM_EMISSIONE    = "DataMMEmissione";
  public static final String CAMPO_DATA_AAAA_EMISSIONE  = "DataAAAAEmissione";
  public static final String CAMPO_TDS                  = "TDS";
  public static final String CAMPO_NOTE_TDS             = "NoteTDS";
  public static final String CAMPO_SEDE_TDS             = "SedeTDS";
  public static final String CAMPO_COD_UFFICIO          = "TDS";
  public static final String CAMPO_NOTE_UFFICIO         = "NoteTDS";
  public static final String CAMPO_SEDE_UFFICIO         = "SedeTDS";
  public static final String CAMPO_COD_AUTORITA         = "Autorita";
  public static final String CAMPO_NOTE_AUTORITA        = "NoteAutorita";
  public static final String CAMPO_SEDE_AUTORITA        = "SedeAutorita";
  public static final String CAMPO_FLAG_PIU_MENO        = "FlagPiuMeno";
  public static final String CAMPO_FLAG_APP_PROVVISORIA = "FlagAppProvvisoria";
  public static final String CAMPO_COD_TIPO_ANNOTAZIONE = "CodTipoAnnotazione";
  public static final String CAMPO_GIORNO_DATA_RICHIESTA = "GiornoDataRichiesta";
  public static final String CAMPO_MESE_DATA_RICHIESTA = "MeseDataRichiesta";
  public static final String CAMPO_ANNO_DATA_RICHIESTA = "AnnoDataRichiesta";
  public static final String CAMPO_SEDE_UFFICIO_EMISSIONEPROVV = "SedeUffEmitt";
  public static final String CAMPO_COD_UFFICIO_EMISSIONEPROVV = "CodUffEmitt";
  public static final String CAMPO_SEZ_UFFICIO_EMISSIONEPROVV = "SezUffEmitt";
  public static final String CAMPO_COD_TIPO_ATTO = "CodTipoAtto";
  public static final String CAMPO_OGGETTO_ATTO = "OggettoAtto";

	public static final String PG_LOAD_RICHIESTA_STAMPA = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRichiestaStampa.jsp";
	public static final String PG_RICHIESTA_STAMPA = ROOT_DIR
			+ "files/siap/siep/richiesta/RichiestaStampa.jsp";
	public static final String PG_LOAD_INSERISCI_RICHIESTA_GE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichiestaGE.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_GE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichiestaGE.jsp";

	public static final String PG_LOAD_INSERISCI_RICHIESTA_MS = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichiestaMS.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_MS = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichiestaMS.jsp";
	public static final String PG_LOAD_INSERISCI_RICHIESTA_MS_ESP_PENA_LIB_COND = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichiestaMSEspPenaLibCond.jsp";
	public static final String PG_LOAD_INSERISCI_RICHIESTA_MS_ESP_PENA_AFF_PROVA = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichiestaMSEspPenaAffProva.jsp";

	public static final String PG_LOAD_RICHIESTA_AMNISTIA_INDULTO = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRichiestaAmnistiaIndulto.jsp";
	public static final String PG_LOAD_EMISSIONE_CONCESSIONI = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciEmissioneComunicazioniConcesse.jsp";

	public static final String PG_LOAD_RICHIESTA_INCOSTITUZIONALITA = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRichiestaIncostituzionalita.jsp";
	public static final String PG_LOAD_RICHIESTA_DEPENALIZZAZIONE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRichiestaDepenalizzazione.jsp";

	public static final String PG_LOAD_EMISSIONE_REVOCHE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciEmissioneComunicazioniRevocate.jsp";
	public static final String PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_CONCESSE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioEmissioneComunicazioniConcesse.jsp";
	public static final String PG_DETTAGLIO_EMISSIONE_COMUNICAZIONI_REVOCATE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioEmissioneComunicazioniRevocate.jsp";
	public static final String PG_LOAD_RICHIESTA_ACC_DATA_REATO = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichAccDataReato.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_ACC_DATA_REATO = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichiestaAccDataReato.jsp";
	public static final String PG_LOAD_RICHIESTA_CON_CODICE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichcConCodice.jsp";
	public static final String PG_DETTAGLIO_RICHIESTe_CON_CODICE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichiesteConCodice.jsp";
	public static final String PG_LOAD_RICHIESTA_DET_PEN_ABO_REATO = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichDetPenAboReato.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_DET_PEN_ABO_REATO = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichDetPenAboReato.jsp";
	public static final String PG_LOAD_RICHIESTA_DEP_O_INCOST = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichDepenIncost.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_DEP_O_INCONST = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichDepIncost.jsp";

	public static final String PG_LOAD_RICHIESTA_COMUNICAZIONE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciRichestaComunicazione.jsp";
	public static final String PG_DETTAGLIO_RICHIESTA_COMUNICAZIONE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRichiestaGenerica.jsp";
	public static final String PG_LOAD_GRIGLIA_SELEZIONE_TIPO_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadGrigliaSelezioneTipoTrasmissione.jsp";
	public static final String PG_LOAD_RICHIESTA_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciTrasmissioneComp.jsp";
	public static final String PG_LOAD_RICERCA_DISTRETTO_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadGrigliaSelezioneTipoTrasmissione.jsp";
	public static final String PG_LOAD_RICERCA_TITOLO_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRicercaFascicoloTrasmissioneComp.jsp";
	public static final String PG_LOAD_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciTrasmissioneCompetenza.jsp";
	public static final String PG_DETTAGLIO_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioTrasmissioneCompetenza.jsp";
	public static final String PG_LOAD_MODIFICA_TRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadModificaTrasmissioneCompetenza.jsp";
	public static final String PG_LOAD_TRASFERISCI_TRASMISSIONE_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/richiesta/LoadTrasferisciTrasmissioneCompetenza.jsp";
	public static final String PG_LOAD_RICERCA_ATTI_IN_CARICO_COMPETENZA = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRicercaAttiCompetenzaPresiCarico.jsp";
	public static final String PG_LOAD_RITRASMISSIONE_COMP = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadRitrasmissioneCompetenza.jsp";
	public static final String PG_LOAD_RISCONTRO_SOLLECITI = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadElencoRiscontroTrasmissioniSolleciti.jsp";
	public static final String PG_DETTAGLIO_RISCONTRO_TRASMISSIONE_COMPETENZA = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioRiscontroTrasmissioneCompetenza.jsp";
// Rigetto Richiesta
  public static final String PG_LOAD_RIGETTO_RICHIESTA_ATTI_TRASM_COMP      = ROOT_DIR + "files/siap/siep/richiesta/LoadInserisciRigettoRichiestaAtti.jsp";
  public static final String PG_DETTAGLIO_RIGETTO_RICHIESTA_ATTI_TRASM_COMP = ROOT_DIR + "files/siap/siep/richiesta/DettaglioRigettoRichiestaAtti.jsp";
  public static final String PG_LOAD_TRASFERISCI_RIGETTO_RICHIESTA_ATTI = IWebConstants.ROOT_DIR + "files/siap/siep/richiesta/LoadTrasferisciRigettoRichiestaAtti.jsp";
  
// Decreto 146/2013
	public static final String PG_LOAD_TRASM_ATTI_51_BIS = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadTrasmissioneAttiExArt51Bis.jsp";
	public static final String PG_LOAD_CONFERMA_TRASM_ATTI_51_BIS = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadConfermaTrasmissioneAttiExArt51Bis.jsp";

	// public static final String PG_LOAD_RISCONTRO_SOLLECITI = ROOT_DIR +
	// "files/siap/siep/richiesta/LoadElencoRiscontroTrasmissioniSolleciti.jsp";
	// public static final String PG_DETTAGLIO_RISCONTRO_TRASMISSIONE_COMPETENZA = ROOT_DIR +
	// "files/siap/siep/richiesta/DettaglioRiscontroTrasmissioneCompetenza.jsp";
  
  
  // ORDINE SCARCERAZIONE PROVVISORIO
	public static final String PG_LOAD_ORDINE_SCARCERAZIONE_PROVV = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciOrdineScarcerazioneProvv.jsp";
	public static final String PG_DETTAGLIO_ORDINE_SCARCERAZIONE_PROVV = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioOrdineScarcerazioneProvv.jsp";

  //RICHIESTA ESITO ESPULSIONE
	public static final String PG_LOAD_ESITO_ESPULSIONE = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadInserisciEsistoEspulsione.jsp";
	public static final String PG_DETTAGLIO_ESITO_ESPULSIONE = ROOT_DIR
			+ "files/siap/siep/richiesta/DettaglioEsitoEspulsione.jsp";
    
  //TEMPLATE
  public static final String TEMPLATE_CANCELLAZIONE_FALSITA = "RICGE05";

  //MS
  public static final String TEMPLATE_ESITO_ESP_PENA = "RICMDS01";
  public static final String TEMPLATE_EST_LIB_COND = "RICMDS02";
  public static final String TEMPLATE_EST_AFF_PROVA = "RICMDS03";
  public static final String TEMPLATE_ESEC_SANS_SOST = "RICMDS04";
  public static final String TEMPLATE_DECL_INAMM = "RICMDS05";

  // * TIPI RICHIESTE *
  public static final String AMNISTIA            = "003";
  public static final String INDULTO             = "002";
  public static final String INCOSTITUZIONALITA  = "013";
  public static final String DEPENALIZZAZIONE    = "004";

  // NEV 37 Depenalizzazione - inizio
  public static final String ILLECITO_AMMINISTRATIVO   = "017";
  // NEV 37 Depenalizzazione - Fine
  // CAMPO NOME DEL CAMPO CHE VIENE MESSO TEMPORANEAMENTE IN SESSIONE
  public static final String FIELD_TEMP_ID_EVENTO_SOLLECITO = "FieldTempIdEveSoll";
  
	// MEV_39: aggiunta costante per trasmissione richiesta generica
	public static final String PG_LOAD_TRASM_RICH_GEN = ROOT_DIR
			+ "files/siap/siep/richiesta/LoadTrasmissioneRichiestaGenerica.jsp";

}