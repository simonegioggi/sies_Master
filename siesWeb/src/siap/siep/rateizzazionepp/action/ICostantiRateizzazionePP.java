package siap.siep.rateizzazionepp.action;

import f3b.web.IWebConstants;

public interface ICostantiRateizzazionePP {

	// ==========================================================================
	// Costanti utilizzate nelle jsp e che rappresentano i campi della tabella
	// ==========================================================================
	public static final String CAMPO_ID_RATEIZZAZIONE_PP = "IdRateizzazionePP";
	public static final String CAMPO_IMPORTO_RATA = "ImportoRata";
	public static final String CAMPO_NUM_RATE = "NumeroRate";
	public static final String CAMPO_TIPO_RATEIZZAZIONE = "TipoRateizzazione";
	public static final String CAMPO_SCADENZA_GIORNI = "ScadenzaGiorni";

	public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
	public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";

	public static final String CAMPO_VALORE_IMPORTO_I = "ValoreImportoDaPagareI";
	public static final String CAMPO_VALORE_IMPORTO_D = "ValoreImportoDaPagareD";
	public static final String CAMPO_SCADENZA_GIORNI_RATA_UNICA = "ScadenzaGiorniRataUnica";

	public static final String CAMPO_VALORE_RATA_UNICA_I = "ValoreRataUnicaI";
	public static final String CAMPO_VALORE_RATA_UNICA_D = "ValoreRataUnicaD";

	public static final String CAMPO_VALORE_RATA_I = "ValoreRataI";
	public static final String CAMPO_VALORE_RATA_D = "ValoreRataD";

	public static final String TIPO_RATEIZZAZIONE_UNICA = "U";
	public static final String TIPO_RATEIZZAZIONE_RATEALE = "R";

	public static final int NUM_MAX_RATE = 10;

	// ==========================================
	// Costanti che rappresentano le pagine jsp
	// ==========================================
	public static final String PG_LOAD_INSERISCI_RATEIZZAZIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/rateizzazionepp/LoadInserisciRateizzazionePP.jsp";
	public static final String PG_LOAD_DETTAGLIO_RATEIZZAZIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/rateizzazionepp/DettaglioRateizzazionePP.jsp";

	// MEV_2023-33: aggiunte costanti
	public static final String PG_LOAD_INSERISCI_RIDETERMINAZIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/rateizzazionepp/LoadInserisciRideterminazionePP.jsp";
	public static final String PG_DETTAGLIO_RIDETERMINAZIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/rateizzazionepp/DettaglioRideterminazionePP.jsp";
	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoEmissioneProvvedimento";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseEmissioneProvvedimento";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoEmissioneProvvedimento";
	public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
	public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
	public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodiceTipoProvvedimento";
	public static final String CAMPO_SEDE_AUTORITA_PROVVEDIMENTO = "SedeAutoritaProvvedimento";
	public static final String CAMPO_COD_AUTORITA_PROVVEDIMENTO = "CodiceAutoritaProvvedimento";

}