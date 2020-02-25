package siap.sius.statistiche.action;

import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiStatistiche
 * </p>
 * <p>
 * Description: Classe di costanti per le funzioni di Ricerche Statistiche e di Monitoraggio Procedimenti SIUS.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public interface ICostantiStatistiche {

	public static final String PG_LOAD_GRIGLIA_RICERCHE = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/GrigliaRicercheSius.jsp";
	public static final String PG_LOAD_RICERCHE_ORDINANZA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcXOrdinanza.jsp";
	public static final String DIV_RICERCHE_ORDINANZE_BASE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaOrdinanzeBase.jsp";
	public static final String DIV_RICERCHE_ORDINANZE_AVANZATA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaOrdinanzeAvanzata.jsp";
	public static final String PG_LOAD_RICERCHE_ESTREMI_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcXEstremiSentenza.jsp";
	public static final String DIV_RICERCHE_ESTREMI_SENTENZA_BASE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaEstremiSentenzaBase.jsp";
	public static final String DIV_RICERCHE_ESTREMI_SENTENZA_AVANZATA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaEstremiSentenzaAvanzata.jsp";
	
	public static final String PG_ELENCO_ORDINANZE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/ElencoOrdinanze.jsp";

	public static final String RICERCA_ORDINANZA_JS = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaOrdinanze.js";
	// MEV10-s3: aggiunte costanti
	public static final String PG_ELENCO_PROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/ElencoProvvedimenti.jsp";
	public static final String RICERCA_PROVVEDIMENTO_JS = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProvvedimenti.js";

	public static final String CAMPO_TIPO_RICERCA = "TipoRicerca"; // Ricerca Avanzata o Base
	public static final String CAMPO_TIPO_INTERVALLO = "TipoIntervallo";
	public static final String CAMPO_TIPO_RELATORE = "TipoRelatore";
	public static final String ESTREMI_PROVVEDIMENTO_INTERVALLO = "PI";
	public static final String DATA_DEPOSITO_INTERVALLO = "DI";
	public static final String DATA_ARRIVO_CANCELLERIA_INTERVALLO = "CI";
	public static final String DATA_EMISSIONE_INTERVALLO = "EI";

	public static final String CAMPO_STATO_VALIDAZIONE = "StatoValidazione";
	public static final String TUTTI = "T";
	public static final String VALIDATI = "V";
	public static final String NON_VALIDATI = "NV";
	public static final String ANNULLATI = "A";
	public static final String NON_ANNULLATI = "NA";
	public static final String CAMPO_TIPO_RICORSO = "TipoRicorso";
	public static final String CAMPO_TIPO_DECRETO = "TipoDecreto";
	public static final String INAMMISSIBILITA = "Inammissibilita";
	public static final String INCOMPETENZA = "Incompetenza";
	public static final String NDPNLP = "NDP/NLP";
	public static final String REVOCA = "Revoca";
	public static final String TUTTI_DECRETI = "TuttiDecreti";
	public static final String ALTRI_DECRETI = "AltriDecreti";

	// Valori per la modalità di ricerca: Per Ordinanze o Per Decreti
	public static final String RICERCA_ORDINANZA = "RO";
	public static final String RICERCA_DECRETO = "RD";
	public static final String RICERCA_IMPUGNAZIONE = "RI";
	public static final String RICERCA_FOGLIO_COMPLEMENTARE = "RF";
	public static final String RICERCA_ESTREMI_SENTENZA = "RE";
	// MEV10-s3: aggiunta costante
	public static final String RICERCA_PROVVEDIMENTO = "RP";

	public static final String CAMPO_MODALITA_RICERCA = "ModalitaRicerca";

	public static final String CAMPO_ANNO_INI = "AnnoIni";
	public static final String CAMPO_NUM_INI = "NumIni";

	public static final String CAMPO_ANNO_FINE = "AnnoS3Fine";
	public static final String CAMPO_NUM_FINE = "NumS3Fine";

	public static final String CAMPO_GIORNO_DATA_DEPOSITO_INI = "GiornoDataDepositoIni";
	public static final String CAMPO_MESE_DATA_DEPOSITO_INI = "MeseDataDepositoIni";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_INI = "AnnoDataDepositoIni";

	public static final String CAMPO_GIORNO_DATA_DEPOSITO_FINE = "GiornoDataDepositoFin";
	public static final String CAMPO_MESE_DATA_DEPOSITO_FINE = "MeseDataDepositoFin";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_FINE = "AnnoDataDepositoFin";
	public static final String CAMPO_DESC_CALCOLI = "ConteggiStatisticheSius";

	public static final String CAMPO_GIORNO_INIZIALE = "GiornoIni";
	public static final String CAMPO_MESE_INIZIALE = "MeseIni";
	public static final String CAMPO_ANNO_INIZIALE = "AnnoIni";

	public static final String CAMPO_GIORNO_FINALE = "GiornoFine";
	public static final String CAMPO_MESE_FINALE = "MeseFine";
	public static final String CAMPO_ANNO_FINALE = "AnnoFine";

	public static final String PG_LOAD_ESTRAZIONE_OGGETTI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadEstrazioneOggetti.jsp";

	public static final String CB_LISTA_MAGISTRATI = "ListaMagistrati";
	public static final String CB_LISTA_ESPERTI = "ListaEsperti";
	public static final String CB_TIPO_ESTRAZIONE_OGGETTI = "TipoEstrOgg";
	public static final String CB_LISTA_OGGETTI = "ListaOggetti";
	public static final String CHK_DETTAGLIO = "ChkDettaglio";

	public static final String PG_LOAD_ESTRAZIONE_TEMPI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadEstrazioneTempi.jsp";
	public static final String PG_INCLUDE_COMBO_OGGETTI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/ListaOggettiCombo.jsp";
	public static final String PG_INCLUDE_INTERVALLO_DATE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/IntervalloDate.jsp";
	public static final String PG_INCLUDE_COLLABORATORE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/Collaboratore.jsp";
	public static final String PG_INCLUDE_POSIZIONE_GIURIDICA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/PosizioneGiuridica.jsp";
	public static final String FILTRO_COLLABORATORE = "filtroCollaboratore";
	public static final String FILTRO_POSIZIONE_GIURIDICA = "filtroPosizioneGiuridica";

	public static final String TABELLA_ESTRAZIONE_OGGETTI = "ISP_ESTRAZIONE_OGGETTI_TRIB";
	public static final String TABELLA_ESTRAZIONE_INTERVALLI = "ISP_PROC_INTERVALLI";

	public static final String CHK_PENDENTI = "ChkPendenti";

	// FR012 - Ricerca Procedimenti con Data Udienza Fissata ma non Definiti ( set di costanti )
	public static final String CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO = "GiornoDataCameraConsiglioInizio";
	public static final String CAMPO_MESE_DATA_CAMERA_CONSIGLIO_INIZIO = "MeseDataCameraConsiglioInizio";
	public static final String CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO = "AnnoDataCameraConsiglioInizio";

	public static final String CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_FINE = "GiornoDataCameraConsiglioFine";
	public static final String CAMPO_MESE_DATA_CAMERA_CONSIGLIO_FINE = "MeseDataCameraConsiglioFine";
	public static final String CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE = "AnnoDataCameraConsiglioFine";

	public static final String CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO = "GiornoDataIscrizioneInizio";
	public static final String CAMPO_MESE_DATA_ISCRIZIONE_INIZIO = "MeseDataIscrizioneInizio";
	public static final String CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO = "AnnoDataIscrizioneInizio";

	public static final String CAMPO_GIORNO_DATA_ISCRIZIONE_FINE = "GiornoDataIscrizioneFine";
	public static final String CAMPO_MESE_DATA_ISCRIZIONE_FINE = "MeseDataIscrizioneFine";
	public static final String CAMPO_ANNO_DATA_ISCRIZIONE_FINE = "AnnoDataIscrizioneFine";

	public static final String CAMPO_COD_OGGETTO_PROCEDIMENTO = ICostantiFascicoloSius.CAMPO_COD_OGGETTO;
	public static final String CAMPO_COD_POSIZIONE_GIURIDICA = ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA;

	public static final String PG_LOAD_RICERCA_PROC_DATA_UDI_FISS_NO_DEF = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcDataUdienzaFissataNoDefiniti.jsp";

	public static final String PG_RICERCA_PROC_DATA_UDI_FISS_NO_DEF = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcDataUdienzaFissataNoDefiniti.jsp";

	public static final String PG_LOAD_RICERCA_PROC_PROVV_NO_VALIDATI_NO_DEP = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.jsp";

	public static final String PG_RICERCA_PROC_PROVV_NO_VALIDATI_NO_DEP = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcedimentiPerProvvNoValidatiNoDepositati.jsp";

	// FR015, FA016 - Ricerca procedimenti con provvedimento emesso
	public static final String RADIO_RICERCA_PER_STATO_PROVVEDIMENTO = "RicercaPerStatoProvvedimento";
	public static final String VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_NO = "0";
	public static final String VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_EMESSI_NOVAL = "1";
	public static final String VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_NODEP = "2";
	public static final String VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_DEP_NOVAL = "3";

	// FR025-FA026
	public static final String PG_LOAD_RICERCHE_PROC_AGGREGATI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcAggregati.jsp";
	public static final String PG_RICERCA_PROC_AGGREGATI_INIZIALE_COGNOME = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcInizialeCognome.jsp";

	public static final String CAMPO_NUMERO_GIORNI = "CampoNumeroGiorni";

	// FR017 - FA018
	public static final String PG_LOAD_RICERCA_PROC_DATA_UDIENZA_FISSATA_NO_DEF_NUMGG = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcDataUdienzaFissataNoDefinitiNumGG.jsp";
	public static final String PG_RICERCA_PROC_DATA_UDIENZA_FISSATA_NO_DEF_NUMGG = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcDataUdienzaFissataNoDefinitiNumGG.jsp";

	// FR019 - FA020
	public static final String CAMPO_GIORNO_DATA_EMISSIONE_INIZIO = "GiornoDataEmissioneInizio";
	public static final String CAMPO_MESE_DATA_EMISSIONE_INIZIO = "MeseDataEmissioneInizio";
	public static final String CAMPO_ANNO_DATA_EMISSIONE_INIZIO = "AnnoDataEmissioneInizio";
	public static final String CAMPO_GIORNO_DATA_EMISSIONE_FINE = "GiornoDataEmissioneFine";
	public static final String CAMPO_MESE_DATA_EMISSIONE_FINE = "MeseDataEmissioneFine";
	public static final String CAMPO_ANNO_DATA_EMISSIONE_FINE = "AnnoDataEmissioneFine";
	public static final String PG_LOAD_RICERCA_PROC_PROVV_EMESSI_NO_DEPOSITO_NUMGG = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcProvvEmessiNoDepositoNumGG.jsp";
	public static final String PG_RICERCA_PROC_PROVV_EMESSI_NO_DEPOSITO_NUMGG = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcProvvEmessiNoDepositoNumGG.jsp";

	// FR034 - FA035
	public static final String CAMPO_CHIAVE_UFFICIO = ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO;
	public static final String CAMPO_DESCR_COMUNE_UFFICIO = ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO;
	public static final String RADIO_STATO_PROCEDIMENTO = ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO;
	public static final String CAMPO_GIORNO_FINE_PENDENZA = ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA;
	public static final String CAMPO_MESE_FINE_PENDENZA = ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA;
	public static final String CAMPO_ANNO_FINE_PENDENZA = ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA;
	public static final String CAMPO_COD_OGGETTO = ICostantiFascicoloSius.CAMPO_COD_OGGETTO;
	public static final String CAMPO_COD_MAGISTRATO = ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO;
	public static final String CAMPO_COD_CANCELLERIA = "CancelleriaAssegnataria";
	public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE;
	public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE = ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE;
	public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE = ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE;
	public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE = ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE;
	public static final String CAMPO_MESE_ISCRIZIONE_FINALE = ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE;
	public static final String CAMPO_ANNO_ISCRIZIONE_FINALE = ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE;

	public static final String PG_LOAD_RICERCA_PROCPOSZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcPosizioneGiuridica.jsp";
	public static final String PG_RICERCA_PROCPOSZIONEGIURIDICA = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaProcPosizioneGiuridica.jsp";

	// FI021, FR022, FA023, FA024
	public static final String PG_LOAD_RICERCA_STATISTICA_COMPARATA_MAGISTRATI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaStatisticaComparataMagistrati.jsp";

	public static final String PG_RICERCA_STATISTICA_COMPARATA_MAGISTRATI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/RicercaStatisticaComparataMagistrati.jsp";

	// Valori per la modalità di ricerca dei Fogli Complementari
	public static final String RICERCA_FC_TRASMESSI = "FCT";
	public static final String RICERCA_FC_DA_TRASMETTERE = "FCNT";
	public static final String RICERCA_FC_TRASMESSI_CON_ERRORE = "FCTE";
	public static final String RICERCA_FC_ISCRITTI_MANUALMENTE = "FCIM";
	public static final String RICERCA_ORDINANZE_PRIVE_DI_FC = "OPFC";
	// MEV10-s3: aggiunta costante
	public static final String RICERCA_PROVVEDIMENTI_PRIVI_DI_FC = "PPFC";

	public static final String PG_LOAD_GRIGLIA_RICERCHE_STAT = IWebConstants.ROOT_DIR + "files/siap/sius/provvedimento/GrigliaRicercheSiusStatistiche.jsp";
	public static final String PG_LOAD_RICERCA_FC_TRASMESSI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaFCTrasmessi.jsp";
	public static final String PG_LOAD_RICERCA_FC_DA_TRASMETTERE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaFCDaTrasmettere.jsp";
	public static final String DIV_RICERCA_FC_TRASMESSI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaFCTrasmessi.jsp";
	public static final String DIV_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaOrdinanzePriveDiFC.jsp";
	public static final String PG_ELENCO_FOGLI_COMPLEMENTARI = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/ElencoFogliComplementari.jsp";
	public static final String PG_ELENCO_FC_DA_TRASMETTERE = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/ElencoFCDaTrasmettere.jsp";
	public static final String PG_LOAD_RICERCA_ORD_PRIVE_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaOrdinanzePriveDiFC.jsp";
	// MEV10-s3: aggiunte costanti
	public static final String PG_LOAD_RICERCA_PROVV_PRIVI_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProvvedimentiPriviDiFC.jsp";
	public static final String DIV_RICERCA_PROVV_PRIVI_DI_FC = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/DivRicercaProvvedimentiPriviDiFC.jsp";
	
    //20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public static final String CAMPO_CK_MEZZI_ELETTRONICI = "CheckMezziElettronici";
	public static final String CAMPO_CK_STRUMENTI_TECNICI = "CheckStrumentiTecnici";
	// test
	public static final String CAMPO_TIPI_CONTROLLI_ESECUZIONE = "CheckTipiControlliEsecuzione";

	// MEV_55 Modifica del 11/12/2017
	public static final String MESSAGGIO_ERRORE_GENERICO = "Si è verificato un errore durante l'elaborazione.";

	// MEV 73
	public static final String PG_LOAD_RICERCHE_DLGS_123_2018 = IWebConstants.ROOT_DIR + "files/siap/sius/statistiche/LoadRicercaProcDlgs123_2018.jsp";

 }