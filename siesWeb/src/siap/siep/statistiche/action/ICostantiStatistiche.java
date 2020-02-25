package siap.siep.statistiche.action;

import f3b.web.IWebConstants;

public interface ICostantiStatistiche {

	public static final String CAMPO_TIPO_RICERCA = "TipoRicerca"; // Ricerca Avanzata o Base
	public static final String CAMPO_TIPO_INTERVALLO = "TipoIntervallo";
	public static final String CAMPO_STATO_VALIDAZIONE = "StatoValidazione";

	public static final String DIV_RICERCHE_ORDINANZE_BASE = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/DivRicercaOrdinanzeBase.jsp";
	public static final String DIV_RICERCHE_ORDINANZE_AVANZATA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/DivRicercaOrdinanzeAvanzata.jsp";

	// Valori per la modalità di ricerca dei Fogli Complementari
	public static final String RICERCA_FOGLIO_COMPLEMENTARE = "RF";
	public static final String RICERCA_FC_TRASMESSI = "FCT";
	public static final String RICERCA_FC_DA_TRASMETTERE = "FCNT";
	public static final String RICERCA_FC_TRASMESSI_CON_ERRORE = "FCTE";
	public static final String RICERCA_FC_ISCRITTI_MANUALMENTE = "FCIM";
	public static final String RICERCA_ORDINANZE_PRIVE_DI_FC = "OPFC";

	public static final String RICERCA_ORDINANZA_JS = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/RicercaOrdinanze.js";

	public static final String PG_LOAD_GRIGLIA_RICERCHE_STAT = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/GrigliaRicercheSiepStatistiche.jsp";
	public static final String PG_LOAD_RICERCA_FC_TRASMESSI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/LoadRicercaFCTrasmessi.jsp";
	public static final String PG_LOAD_RICERCA_FC_DA_TRASMETTERE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/LoadRicercaFCDaTrasmettere.jsp";
	public static final String PG_LOAD_RICERCA_STATISTICHE_FC = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/LoadRicercaStatisticheFogliComp.jsp";
	public static final String PG_STATISTICHE_FOGLI_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/StatisticheFogliComplementari.jsp";

	public static final String DIV_RICERCA_FC_TRASMESSI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/DivRicercaFCTrasmessi.jsp";
	public static final String DIV_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/DivRicercaOrdinanzePriveDiFC.jsp";
	public static final String PG_ELENCO_FOGLI_COMPLEMENTARI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/ElencoFogliComplementari.jsp";
	public static final String PG_ELENCO_FC_DA_TRASMETTERE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/ElencoFCDaTrasmettere.jsp";
	public static final String PG_LOAD_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/statistiche/LoadRicercaOrdinanzePriveDiFC.jsp";
	public static final String RICERCA_JS = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/Ricerca.js";

	public static final String PG_LOAD_RICERCA_PER_ESTREMI_FC = IWebConstants.ROOT_DIR
			+ "files/siap/siep/statistiche/LoadRicercaProcedimentiPerEstremiFC.jsp";

	public static final String CAMPO_GIORNO_DATA_EMISSIONE_INIZIO = "GiornoDataEmissioneInizio";
	public static final String CAMPO_MESE_DATA_EMISSIONE_INIZIO = "MeseDataEmissioneInizio";
	public static final String CAMPO_ANNO_DATA_EMISSIONE_INIZIO = "AnnoDataEmissioneInizio";

	public static final String CAMPO_GIORNO_DATA_EMISSIONE_FINE = "GiornoDataEmissioneFine";
	public static final String CAMPO_MESE_DATA_EMISSIONE_FINE = "MeseDataEmissioneFine";
	public static final String CAMPO_ANNO_DATA_EMISSIONE_FINE = "AnnoDataEmissioneFine";

	public static final String CAMPO_GIORNO_DATA_DEPOSITO_INI = "GiornoDataDepositoIni";
	public static final String CAMPO_MESE_DATA_DEPOSITO_INI = "MeseDataDepositoIni";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_INI = "AnnoDataDepositoIni";

	public static final String CAMPO_GIORNO_DATA_DEPOSITO_FINE = "GiornoDataDepositoFin";
	public static final String CAMPO_MESE_DATA_DEPOSITO_FINE = "MeseDataDepositoFin";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_FINE = "AnnoDataDepositoFin";

	public static final String CAMPO_ANNO_INI = "AnnoIni";
	public static final String CAMPO_NUM_INI = "NumIni";
	public static final String CAMPO_ANNO_FINE = "AnnoS3Fine";
	public static final String CAMPO_NUM_FINE = "NumS3Fine";

	public static final String CHECK_ALL_STATISTICS = "Tutti";
	public static final String CHECK_FOGLI_MANUALI = "manuali";
	public static final String CHECK_FOGLI_TRASMESSI = "trasmessi";
	public static final String CHECK_PROVV_PRIVI_FC = "privifc";
	public static final String CHECK_FOGLI_NON_TRASMESSI = "fcnontrasmessi";
	public static final String CHECK_FOGLI_ERRORE = "conerrore";
	public static final String CHECK_FOGLI_ANNULLATI = "annullati";
	public static final String CAMPO_MODALITA_RICERCA = "ModalitaRicerca";

	public static final String CAMPO_ANNO_INIZIALE = "AnnoIniziale";
	public static final String CAMPO_ANNO_FINALE = "AnnoFinale";

	public static final String TUTTI = "T";
	public static final String ANNULLATI = "A";
	public static final String NON_VALIDATI = "NV";
	public static final String NON_ANNULLATI = "NA";
	public static final String DATA_EMISSIONE_INTERVALLO = "EI";
	public static final String VALIDATI = "V";
	public static final String DATA_DEPOSITO_INTERVALLO = "DI";
	public static final String ESTREMI_PROVVEDIMENTO_INTERVALLO = "PI";

	public static final String RICERCA_FC = "RFC";

	public static final String CAMPO_DESC_CALCOLI = "ConteggiStatisticheSige";

	// MEV_39: aggiunte costanti
	public static final String PG_LOAD_STATISTICHE_ESTRAZIONE_DATI = "files/siap/siep/statistiche/StatisticheEstrazioneDati.jsp";
	public static final String PG_LOAD_RICERCA_PROCEDIMENTI_CLASSE_IV_VII = "files/siap/siep/statistiche/LoadRicercaProcedimentiClasseIVeVII.jsp";

}