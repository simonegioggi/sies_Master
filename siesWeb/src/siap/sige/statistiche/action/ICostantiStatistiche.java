package siap.sige.statistiche.action;

import f3b.web.IWebConstants;

/**
 * Title: ICostantiStatistiche
 * Description: Classe di costanti per le funzioni di Monitoraggio/Estrazione Dati Procedimenti SIGE.
 * Company: Engineering S.p.A.
 * @version 1.0
 */
public interface ICostantiStatistiche {

	public static final String CAMPO_TIPO_INTERVALLO = "TipoIntervallo";

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

	public static final String CAMPO_DESC_CALCOLI = "ConteggiStatisticheSige";
	
	public static final String PG_LOAD_GRIGLIA_RICERCA_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/GrigliaRicercaFogliComp.jsp";
	public static final String PG_LOAD_RICERCA_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/LoadRicercaFogliComp.jsp";
	public static final String PG_LOAD_RICERCA_STATISTICHE_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/LoadRicercaStatisticheFogliComp.jsp";	
	
	public static final String DIV_RICERCA_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/DivRicercaFogliComp.jsp";
	public static final String RICERCA_JS = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/Ricerca.js";
	public static final String PG_ELENCO_FOGLI_COMP = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/ElencoFogliComplementari.jsp";
	public static final String PG_STATISTICHE_FOGLI_COMP = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/StatisticheFogliComplementari.jsp";
	public static final String PG_LOAD_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/LoadRicercaOrdinanzePriveDiFC.jsp";
	public static final String DIV_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/DivRicercaOrdinanzePriveDiFC.jsp";
	public static final String PG_ELENCO_ORDINANZE = IWebConstants.ROOT_DIR + "files/siap/sige/statistiche/ElencoOrdinanze.jsp";
	
	// Valori per la modalità di ricerca
	public static final String RICERCA_FOGLIO_COMPLEMENTARE = "RF";
	public static final String ESTREMI_PROVVEDIMENTO_INTERVALLO = "PI";
	public static final String CAMPO_STATO_VALIDAZIONE = "StatoValidazione";
	public static final String TUTTI = "T";
	public static final String ANNULLATI = "A";
	public static final String NON_VALIDATI = "NV";
	public static final String NON_ANNULLATI = "NA";
	public static final String DATA_EMISSIONE_INTERVALLO = "EI";
	public static final String VALIDATI = "V";
	public static final String DATA_DEPOSITO_INTERVALLO = "DI";
	
	public static final String RICERCA_FC = "RFC";
	
	public static final String CAMPO_MODALITA_RICERCA = "ModalitaRicerca";

	// Valori per la modalità di ricerca dei Fogli Complementari
	public static final String RICERCA_FC_TRASMESSI = "FCT";
	public static final String RICERCA_FC_DA_TRASMETTERE = "FCNT";
	public static final String RICERCA_FC_TRASMESSI_CON_ERRORE = "FCTE";
	public static final String RICERCA_FC_ISCRITTI_MANUALMENTE = "FCIM";
	public static final String RICERCA_ORDINANZE_PRIVE_DI_FC = "OPFC";

	
	public static final String CHECK_ALL_STATISTICS="Tutti";
	public static final String CHECK_FOGLI_MANUALI="manuali";
	public static final String CHECK_FOGLI_TRASMESSI="trasmessi";
	public static final String CHECK_PROVV_PRIVI_FC="privifc";
	public static final String CHECK_FOGLI_NON_TRASMESSI="fcnontrasmessi";
	public static final String CHECK_FOGLI_ERRORE="conerrore";
	public static final String CHECK_FOGLI_ANNULLATI="annullati";
	
	public static final String CAMPO_ANNO_INIZIALE="AnnoIniziale";
	public static final String CAMPO_ANNO_FINALE="AnnoFinale";
	
	
}