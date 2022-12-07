package siap.sius.depositoordinanzapc.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiDepositoOrdinanzaPc
 * </p>
 * <p>
 * Description: Classe di costanti di DepositoOrdinanzaPc
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
public interface ICostantiDepositoOrdinanzaPc {

	public static final String CAMPO_ID_DEPOSITO_ORDINANZA_PC = "IdDepositoOrdinanzaPc";
	public static final String CAMPO_ANNO_S3 = "AnnoS3";
	public static final String CAMPO_NUM_S3 = "NumS3";
	public static final String CAMPO_OGGETTO_PROCEDIMENTO = "OggettoProcedimento";
	public static final String CAMPO_GIORNO_DATA_UDIENZA = "GiornoDataUdienza";
	public static final String CAMPO_MESE_DATA_UDIENZA = "MeseDataUdienza";
	public static final String CAMPO_ANNO_DATA_UDIENZA = "AnnoDataUdienza";
	public static final String CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO = "GiornoDataCameraConsiglio";
	public static final String CAMPO_MESE_DATA_CAMERA_CONSIGLIO = "MeseDataCameraConsiglio";
	public static final String CAMPO_ANNO_DATA_CAMERA_CONSIGLIO = "AnnoDataCameraConsiglio";
	public static final String CAMPO_GIORNO_DATA_DEPOSITO = "GiornoDataDeposito";
	public static final String CAMPO_MESE_DATA_DEPOSITO = "MeseDataDeposito";
	public static final String CAMPO_ANNO_DATA_DEPOSITO = "AnnoDataDeposito";
	public static final String CAMPO_COD_NATURA_PROVVEDIMENTO = "CodNaturaProvvedimento";
	public static final String CAMPO_ID_CSSA_COMP = "IdCssaComp";
	public static final String CAMPO_COMUNE_CSSA_COMP = "ComuneCssaComp";
	//
	public static final String CAMPO_COD_UFFICIO_MAGISTRATO_COMP = "CodUfficioMagistratoComp";
	public static final String CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA = "CodUfficioMagistratoCompRecla";
	public static final String CAMPO_UFFICIO_USSM = "Ussm";
	//
	public static final String CAMPO_COD_UFFICIO_TDS_COMP = "CodUfficioOrdTdSComp";
	public static final String CAMPO_LUOGO_SVOLGIMENTO_PROVA = "LuogoSvolgimentoProva";
	public static final String CAMPO_SERVIZIO_TERAPEUTICO_COMP = "ServizioTerapeuticoComp";
	public static final String CAMPO_NUM_GIORNI_DETENZIONE_DOM = "NumGiorniDetenzioneDom";
	public static final String CAMPO_NUM_MESI_DETENZIONE_DOM = "NumMesiDetenzioneDom";
	public static final String CAMPO_NUM_ANNI_DETENZIONE_DOM = "NumAnniDetenzioneDom";
	public static final String CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI = "NumGiorniPermessoAccordati";
	public static final String CAMPO_NUM_GIORNI_RIDUZIONE_PENA = "NumGiorniRiduzionePena";
	public static final String CAMPO_NUM_GIORNI_RIDUZIONE_USUFRUITI = "NumGiorniRiduzioneUsufruiti";
	public static final String CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE = "CodUffTdsConcessoRiduzione";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO = "GenPridGeneraleProcedimento";
	public static final String CAMPO_TIPO_ORDINANZA_DA_PRODURRE = "TipoOrdinanzadaProdurre";
	public static final String CAMPO_ID_DOCUMENTO_ALLEGATO = "IdDocumentoAllegato";

	public static final String CAMPO_NUM_GIORNI_LIBANTICIPATA = "NumGiorniLibanticipata";
	// Nuova Odinanza L.A - decreto legge 146/2013
	public static final String CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE = "NumGiorniLibanticipataspe";
	public static final String CAMPO_NUM_GIORNI_LIBANTICIPATA_INT = "NumGiorniLibanticipataint";
	//
	public static final String CAMPO_FLAG_ELABORATO = "FlagElaborato";
	public static final String CAMPO_COD_TIPO_ORDINANZA = "CodTipoOrdinanza";
	public static final String CAMPO_CK_PRESCRIZIONI = "CheckPrescrizioni";
	public static final String CAMPO_FLAG_ESITO_RECLAMO_LA_PM_ACCOLTO = "EsitoReclamoLaPmAccolto";
	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public static final String CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE = "CheckTipoControlloEsecuzione";
	//
	public static final String CAMPO_FLAG_ESITO_RECLAMO_LA_SPE_PM_ACCOLTO = "EsitoReclamoLaSpePmAccolto";
	public static final String CAMPO_FLAG_ESITO_RECLAMO_LA_INT_PM_ACCOLTO = "EsitoReclamoLaIntPmAccolto";
	//
	public static final String CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO = "EsitoRevocaLaPmAccolto";
	public static final String CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO = "EsitoRevocaLaSpePmAccolto";
	public static final String CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO = "EsitoRevocaLaIntPmAccolto";
	//
	public static final String CAMPO_GIORNO_DATA_FINE_MISURA = "GiornoDataFineMisura";
	public static final String CAMPO_MESE_DATA_FINE_MISURA = "MeseDataFineMisura";
	public static final String CAMPO_ANNO_DATA_FINE_MISURA = "AnnoDataFineMisura";

	public static final String CAMPO_GIORNO_DECORRENZA_MISURA = "GiornoDecorrenzaMisura";
	public static final String CAMPO_MESE_DECORRENZA_MISURA = "MeseDecorrenzaMisura";
	public static final String CAMPO_ANNO_DECORRENZA_MISURA = "AnnoDecorrenzaMisura";

	public static final String CAMPO_GIORNO_DATA_DECORRENZA = "GiornoDataDecorrenza";
	public static final String CAMPO_MESE_DATA_DECORRENZA = "MeseDataDecorrenza";
	public static final String CAMPO_ANNO_DATA_DECORRENZA = "AnnoDataDecorrenza";
	public static final String CAMPO_GIORNO_DATA_PROROGA = "GiornoDataProroga";
	public static final String CAMPO_MESE_DATA_PROROGA = "MeseDataProroga";
	public static final String CAMPO_ANNO_DATA_PROROGA = "AnnoDataProroga";
	public static final String CAMPO_GIORNO_DATA_INIZIO_PERIODO = "GiornoDataInizioPeriodo";
	public static final String CAMPO_MESE_DATA_INIZIO_PERIODO = "MeseDataInizioPeriodo";
	public static final String CAMPO_ANNO_DATA_INIZIO_PERIODO = "AnnoDataInizioPeriodo";
	public static final String CAMPO_FLAG_ESISTENZA_REATOOSTATIVO = "FlagEsistenzaReatoostativo";
	public static final String CAMPO_FLAG_ESPIAZIONE_REATOOSTATIVO = "FlagEspiazioneReatoostativo";
	public static final String CAMPO_AUTORITA_VIGILANTE = "AutoritaVigilante";
	//
	public static final String CAMPO_GIORNO_DATA_TRASMISSIONE = "GiornoDataTrasmissione";
	public static final String CAMPO_MESE_DATA_TRASMISSIONE = "MeseDataTrasmissione";
	public static final String CAMPO_ANNO_DATA_TRASMISSIONE = "AnnoDataTrasmissione";
	public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_SPE = "GiornoDataTrasmissioneSpe";
	public static final String CAMPO_MESE_DATA_TRASMISSIONE_SPE = "MeseDataTrasmissioneSpe";
	public static final String CAMPO_ANNO_DATA_TRASMISSIONE_SPE = "AnnoDataTrasmissioneSpe";
	public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_INT = "GiornoDataTrasmissioneInt";
	public static final String CAMPO_MESE_DATA_TRASMISSIONE_INT = "MeseDataTrasmissioneInt";
	public static final String CAMPO_ANNO_DATA_TRASMISSIONE_INT = "AnnoDataTrasmissioneInt";
	//
	public static final String CAMPO_NUM_GIORNI_ARRESTO_REV = "NumGiorniArrestoRev";
	public static final String CAMPO_NUM_MESI_ARRESTO_REV = "NumMesiArrestoRev";
	public static final String CAMPO_NUM_ANNI_ARRESTO_REV = "NumAnniArrestoRev";
	public static final String CAMPO_NUM_GIORNI_PROROGA = "NumGiorniProroga";
	public static final String CAMPO_NUM_MESI_PROROGA = "NumMesiProroga";
	public static final String CAMPO_NUM_ANNI_PROROGA = "NumAnniProroga";

	public static final String CAMPO_ULTERIORE_DESCRIZIONE = "UlterioreDescrizione";
	public static final String CAMPO_MOTIVAZIONI = "Motivazioni";
	public static final String CAMPO_ALTRO_RIMESSIONI = "AltroRimessioni";
	public static final String FILTRO_RIMESSIONE = "filtroRimessione";

	public static final String CAMPO_PRESIDENZA_CONSIGLIO = "PresidenzaConsiglioDeiMinistri";
	public static final String CAMPO_CORTE_GIUSTIZIA_EUROPEA = "CorteGiustiziaEuropea";
	public static final String CAMPO_CORTE_COSTITUZIONALE = "CorteCostituzionale";
	public static final String CAMPO_PRESIDENZA_GIUNTA = "PresidenteGiuntaRegionale";
	public static final String CAMPO_PRESIDENTE_SENATO = "PresidenteSenato";
	public static final String CAMPO_PRESIDENTE_CAMERA = "PresidenteCamera";
	public static final String CAMPO_PRESIDENTE_CONSIGLIO = "PresidenteConsigliMinistri";

	// Costanti per form Modelli Ordinanza .
	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "giornoDataEmissione";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "meseDataEmissione";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "annoDataEmissione";
	public static final String CAMPO_DATA_EMISSIONE = "DataEmissione";

	public static final String CAMPO_ID_NOTIFICA = "id_notifica";

	// 201311229 - Paolo : Magistrato Ordinanza.
	public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";
	public static final String CAMPO_ID_EVENTO_GENERATO = "IdEventoGenerato";

	// 20140605 - d.f. Aggiunti campi per RICHIESTA OTTEMPERANZA
	public static final String CAMPO_FLAG_NOMINA_COMM_ACTA = "FlagNominaCommActa";
	public static final String CAMPO_DESCR_COMM_ACTA = "DescCommActa";

	// DL 92/2014 - Ordinanza Rimedi Risarcitori per Violazione ART. 3 CEDU
	public static final String CAMPO_INTERO_IMPORTO_CEDU = "interoImportoCedu";
	public static final String CAMPO_DECIMALE_IMPORTO_CEDU = "decimaleImportoCedu";
	public static final String CAMPO_NUM_GIORNI_CEDU = "NumGiorniCedu";

	public static final String CAMPO_SALVA_INTERO_CEDU = "SalvaInteroImportoCedu";
	public static final String CAMPO_SALVA_DECIMALE_CEDU = "SalvaDecimaleImportoCedu";
	public static final String CAMPO_SALVA_GIORNI_CEDU = "SalvaNumGiorniCedu";

	public static final String CAMPO_CHECK_PERIODI_CONCESSI_CEDU = "ggConcessiCedu";
	public static final String CAMPO_CHECK_EURO_CONCESSI_CEDU = "EuroConcessiCedu";
	public static final String CAMPO_CHECK_RIGETTATI_CEDU = "RigettatiCedu";
	public static final String CAMPO_CHECK_INAMMISSIBILI_CEDU = "InammissibiliCedu";
	public static final String CAMPO_CHECK_NLP_CEDU = "NLPCedu";
	public static final String CAMPO_FORMA_MISURA = "RadioFormaMisura";
	public static final String CAMPO_NOME_COMUNITA = "NomeComunita";

	public static final String COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA = "C001";
	public static final String COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE = "C009";

	public static final String COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA = "U002";
	public static final String COD_OGGETTO_APPLICAZIONE_MISURA_SICUREZZA = "U023";
	public static final String COD_OGGETTO_RIESAME_PERICOLOSITA_SOCIALE = "U067";
	public static final String COD_OGGETTO_LIBERAZIONE_CONDIZIONALE = "U016";
	// MEV63: aggiunta costante
	public static final String COD_OGGETTO_ESECUZIONE_PRESSO_DOMICILIO_PENA_DETENTIVA = "U084";
	
	// INIZIO: MEV_9 (D.lgs. 123/2018)
	public static final String COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVA_678 = "C050";
	public static final String COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVA_678_MINORI = "C051";
	
	public static final String CAMPO_CK_ATTI_AL_PRESIDENTE = "AttiAlPresidente";
	public static final String CAMPO_PROCURA_COMPETENTE= "ProcuraCompetente";
	
	// FINE: MEV_9
	

	public static final String PG_LOAD_RICERCADEPOSITOORDINANZAPC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadRicercaDepositoOrdinanzaPc.jsp";
	public static final String PG_LOAD_DETTAGLIODEPOSITOORDINANZAPC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadRicercaDepositoOrdinanzaPc.jsp";
	public static final String PG_LOAD_DETTAGLIO_DEPOSITO_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioDepositoOrdinanza.jsp";
	public static final String PG_RICERCADEPOSITOORDINANZAPC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/RicercaDepositoOrdinanzaPc.jsp";
	public static final String PG_LOAD_INSERISCIDEPOSITOORDINANZAPC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadInserisciDepositoOrdinanzaPc.jsp";
	public static final String PG_LOAD_EMISSIONE_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadInserisciEmissioneOrdinanza.jsp";
	public static final String PG_INSERISCI_EMISSIONE_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciEmissioneOrdinanza.jsp";
	public static final String PG_INSERISCI_PRESCRIZIONI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciPrescrizioni.jsp";
	public static final String PG_LOAD_DETTAGLIO_EMISSIONE_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioEmissioneOrdinanza.jsp";
	public static final String PG_LOAD_INSERISCIDATADEPOSITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadInserisciDataDeposito.jsp";
	public static final String PG_LOAD_DEPOSITO_ORDINANZA = IWebConstants.ROOT_DIR + "???????"; // STUB : ???
	public static final String PG_ELENCOSTAMPEMODELLIORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ElencoStampeModelliOrdinanza.jsp";
	public static final String PG_ELENCO_DEPOSITO_ORDINANZE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ElencoOrdinanze.jsp";

	public static final String PG_LOAD_TRASFERISCI_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadTrasferisciOrdinanza.jsp";
	public static final String PG_DETTAGLIO_TRASFERISCI_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioTrasferisciOrdinanza.jsp";
	public static final String PG_DETTAGLIO_MESSAGGIO_TRASMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaSpedita.jsp";
	public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRicevuta.jsp";
	public static final String PG_LOAD_MODIFICADATADEPOSITOORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadModificaDataDepositoOrdinanza.jsp";
	public static final String PG_TOOLBAR_HEADER = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/toolbar_header.jsp";

	public static final String TEMPLATE_ORDINANZA_GENERICO = "SIUS_OR_018";
	public static final String TEMPLATE_ORDINANZA_NLP_GENERICO = "SIUS_OR_019";
	public static final String TEMPLATE_ORDINANZA_RIGETTO_GENERICO = "SIUS_OR_003";

	// Umberto 17/9/2004 - modello Ordinanza Tipo
	public static final String TEMPLATE_MOD_ORDINANZA_TIPO = "SIUS_OM_000";

	public static final String TEMPLATE_MOD_ORDINANZA_AFFIDAMENTO = "SIUS_OM_001";
	public static final String TEMPLATE_MOD_ORDINANZA_AFFIDAMENTOTD = "SIUS_OM_002";
	public static final String TEMPLATE_MOD_ORDINANZA_RIGETTOGENERICO = "SIUS_OM_003";
	public static final String TEMPLATE_MOD_ORDINANZA_DETDOMICILIARE = "SIUS_OM_004";
	public static final String TEMPLATE_MOD_ORDINANZA_DETDOMICILIARE2 = "SIUS_OM_005";
	public static final String TEMPLATE_MOD_ORDINANZA_DETDOMICILIARE3 = "SIUS_OM_006";
	public static final String TEMPLATE_MOD_ORDINANZA_DETDOMRINPENA = "SIUS_OM_007";
	public static final String TEMPLATE_MOD_ORDINANZA_SEMILIBERTA = "SIUS_OM_008";
	public static final String TEMPLATE_MOD_ORDINANZA_SEMILIBERTAALTRI = "SIUS_OM_009";
	public static final String TEMPLATE_MOD_ORDINANZA_REVOCAMISALT = "SIUS_OM_010";
	public static final String TEMPLATE_MOD_ORDINANZA_ESTENSIONEMISALT = "SIUS_OM_011";
	public static final String TEMPLATE_MOD_ORDINANZA_DECLARESTPENA = "SIUS_OM_012";
	public static final String TEMPLATE_MOD_ORDINANZA_DECLFPLIBERCONDIZ = "SIUS_OM_013";
	public static final String TEMPLATE_MOD_ORDINANZA_RIGETTODIFPENA = "SIUS_OM_014";
	public static final String TEMPLATE_MOD_ORDINANZA_RIABILITAZIONE = "SIUS_OM_015";
	public static final String TEMPLATE_MOD_ORDINANZA_ACCRECLAMOPER = "SIUS_OM_016";
	public static final String TEMPLATE_MOD_ORDINANZA_RIGRECLAMO41B = "SIUS_OM_017";
	public static final String TEMPLATE_MOD_ORDINANZA_MODGENERICO = "SIUS_OM_018";
	public static final String TEMPLATE_MOD_ORDINANZA_NLPGENERICO = "SIUS_OM_019";
	public static final String TEMPLATE_MOD_ORDINANZA_RIMESSIONE_ATTI = "SIUS_OR_605";
	public static final String TEMPLATE_MOD_ORDINANZA_TIPO_UDS = "SIUS_OM_020";
	// AFFIDAMENTO in PROVA
	public static final String TEMPLATE_MOD_ORDINANZA_MA_AFFIDAMENTO_IN_PROVA = "SIUS_OR_2008";

	// Tipi di ordinanza
	public static final String LIBERAZIONE_ANTICIPATA = "LA";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaLibAnt.jsp";
	public static final String PG_LOAD_MODIFICA_ORDINANZA_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaLibAnt.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaLibAnt.jsp";

	public static final String LICENZA = "LC";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_LICENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaLicenza.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_LICENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaLicenza.jsp";

	public static final String MISURA_ALTERNATIVA = "MA";
	// INIZIO: MEV_9 (D.lgs. 123/2018)
	public static final String MISURA_ALTERNATIVA_AMMISSIONE_PROVVISORIA = "AM";
	// FINE: MEV_9 (D.lgs. 123/2018)
	public static final String PG_LOAD_INSERISCI_ORDINANZA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaMA.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaMA.jsp";

	public static final String INDULTINO = "IN";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_INDULTINO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaIndultino.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_INDULTINO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaIndultino.jsp";

	public static final String ESEC_PRESSO_DOMICILIO = "ED";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_ESEC_PRESSO_DOMICILIO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaEsecPressoDomicilio.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_ESEC_PRESSO_DOMICILIO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaEsecPressoDomicilio.jsp";

	public static final String REVOCA_MA = "RM";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_REVOCA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRevocaMA.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRevocaMA.jsp";

	public static final String GENERICA = "GE";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_GENERICA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaGenerica.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_GENERICA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaGenerica.jsp";

	public static final String REMISSIONE_DEBITO = "RD";
	public static final String PG_LOAD_INSERISCI_REMISSIONE_DEBITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRemissione.jsp";
	public static final String PG_LOAD_DETTAGLIO_REMISSIONE_DEBITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRemissione.jsp";

	public static final String MISURA_SICUREZZA = "MS";
	public static final String PG_LOAD_INSERISCI_MISURA_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaMisuraSicurezza.jsp";
	public static final String PG_LOAD_DETTAGLIO_MISURA_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaMisuraSicurezza.jsp";
	public static final String TRASFORMA_MISURA_SICUREZZA = "TM";
	public static final String PG_LOAD_INSERISCI_RIESAME_MISURA_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRiesameMisuraSicurezza.jsp";
	public static final String PG_LOAD_DETTAGLIO_RIESAME_MISURA_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRiesameMisuraSicurezza.jsp";

	public static final String RICOVERO_OPG = "RO";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RICOVERO_OPG = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRicoveroOPG.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RICOVERO_OPG = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRicoveroOPG.jsp";

	public static final String ESTINZIONE_PENA = "EP";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_ESTINZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaEstinzionePena.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_ESTINZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaEstinzionePena.jsp";

	public static final String REVOCA_LC = "RL";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_REVOCA_LC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRevocaLC.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA_LC = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRevocaLC.jsp";

	public static final String EST_PENA_LIB_CONDIZIONALE = "EL";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_EST_PENA_LIB_CONDIZIONALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaEPLC.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_EST_PENA_LIB_CONDIZIONALE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaEPLC.jsp";

	public static final String CONC_RINVIO_EP = "RE";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_CONC_RINVIO_EP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaConcessioneRinvioEP.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_CONC_RINVIO_EP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaConcessioneRinvioEP.jsp";

	public static final String PROROGA_DETENZIONE_SPECIALE = "PS";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_PROROGA_DETENZIONE_SPE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaProrogaDS.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_PROROGA_DETENZIONE_SPE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaProrogaDS.jsp";

	public static final String PROROGA_DETENZIONE_DOMICILIARE = "PD";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_PROROGA_DETENZIONE_DOM = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaProrogaDD.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_PROROGA_DETENZIONE_DOM = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaProrogaDD.jsp";

	public static final String SOSPENSIONE_ESECUTIVA_ORDINANZA = "SE";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_SOSP_ESEC_ORD = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaSospEsecOr.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_SOSP_ESEC_ORD = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaSospEsecOr.jsp";

	public static final String RECLAMO_PERMESSO = "RP";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaReclamoPermesso.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMO_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaReclamoPermesso.jsp";

	public static final String RECLAMO_SCOMPUTO = "RS";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_SCOMPUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaReclamoScomputo.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMO_SCOMPUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaReclamoScomputo.jsp";

	public static final String RECLAMO_LIBERAZIONE_ANTICIPATA = "RA";
	// 15/04/2014 - Gestione Nuova ordinanza L.A. Decreto 2013/146 : - //Nuova gestione del RECLAMO
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaReclamoLA.jsp";
	public static final String PG_LOAD_MODIFICA_ORDINANZA_RECLAMO_LIBERAZIONE_ANTICIPATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaReclamoLA.jsp";
	//
	public static final String PG_RIFERIMENTO_ORDINANZA_RECLAMO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/RifOrdinanzaReclamo.jsp";
	// public static final String PG_RIFERIMENTO_ORDINANZA_REVOCA = IWebConstants.ROOT_DIR +
	// "files/siap/sius/depositoordinanzapc/RifOrdinanzaRevoca.jsp";
	// public static final String PG_RIFERIMENTO_ORDINANZA_INT_RECLAMO = IWebConstants.ROOT_DIR +
	// "files/siap/sius/depositoordinanzapc/RifOrdinanzaIntegrazioneReclamo.jsp";
	//
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RECLAMATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaReclamata.jsp";

	// public static final String SOPRAVVENIENZA_NT = "07";
	public static final String PG_LOAD_DETTAGLIO_SOPRAVVENIENZA_NT = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaSopravvenienzaNT.jsp";
	public static final String PG_LOAD_DETTAGLIO_RICOVERI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRicoveri.jsp";

	// Revoca Provvedimento : Esiste sia il decreto che l'Ordinanza
	public static final String REVOCA_ORDINANZA = "RV";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_REVOCA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRevoca.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_REVOCA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRevoca.jsp";

	public static final String PG_SINTESI_ORDINANZA_REVOCA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/SintesiOrdinanzaDiRevoca.jsp";
	public static final String PG_LOAD_DETTAGLIO_RICOVERO_OPG_OSS_PSICHE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRicoveroOPGOssPsiche.jsp";
	// Modifica Ordinanza
	public static final String PG_MODIFICA_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanza.jsp";

	// Applicazione Sanzioni Sostitutive
	public static final String APPLICAZIONE_SANZIONI_SOSTITUTIVE = "SS";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_APPLICAZIONE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaApplicazioneSS.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_APPLICAZIONE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaApplicazioneSS.jsp";

	// Declaratoria Estinzione Sanzioni Sostitutive
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_DECLARATORIA_ESTINZIONE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaDeclaratoriaEstinsioneSS.jsp";

	// Modifica Permanente Sanzioni Sostitutive
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_MODIFICA_PERMANENTE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaModificaPermanenteSS.jsp";

	// Sospensione Esecuzione Sanzioni Sostitutive
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaSospensioneEsecuzioneSanzioniSostitutive.jsp";

	// Revoca Ordinanza Sanzione Sostitutiva
	public static final String REVOCA_SANZIONE_SOSTITUTIVA = "RZ";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_REVOCA_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRevocaSS.jsp";

	// Conversione Sanzioni Sostitutive
	public static final String CONVERSIONE_SANZIONI_SOSTITUTIVE = "CS";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_CONVERSIONE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaConversioneSS.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_CONVERSIONE_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaConversioneSS.jsp";

	// Rinvio Sanzioni Sostitutive
	public static final String RINVIO_SANZIONI_SOSTITUTIVE = "RR";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RINVIO_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRinvioSS.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RINVIO_SS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRinvioSS.jsp";

	// Sospensione Misure Sicurezza
	public static final String ORD_SOSPENSIONE_ESECUZIONE_MS = "38";
	public static final String PG_INSERISCI_ORD_SOSPENSIONE_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoSospensioneEsecuzioneMisureSicurezza.jsp";
	// public static final String PG_DETTAGLIO_ORD_SOSPENSIONE_EMS = IWebConstants.ROOT_DIR +
	// "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaSospensioneEsecuzioneMisureSicurezza.jsp";

	// Inosservanza Obblighi Misure Sicurezza
	public static final String ORD_INOSSERVANZA_OBBLIGHI_MS = "40";
	public static final String PG_INSERISCI_INOSSERVANZA_OBBLIGHI_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaInosservanzaObblighiMS.jsp";
	public static final String PG_DETTAGLIO_INOSSERVANZA_OBBLIGHI_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaInosservanzaObblighiMS.jsp";

	// Cessazione Misure Sicurezza
	public static final String CESSAZIONE_MS = "41";
	public static final String PG_INSERISCI_CESSAZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaCessazioneMisureSicurezza.jsp";
	public static final String PG_DETTAGLIO_ESSAZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaMisureSicurezza.jsp";

	// Rinvio Esecuzione Misure Sicurezza
	// MEV_39: usato il 42 sia per uds che per tds oppure MS
	public static final String RINVIO_ESECUZIONE_MS = "42";
	public static final String RINVIO_ESECUZIONE_MSIC = "MS";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_RINVIO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRinvioMS.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_RINVIO_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRinvioMS.jsp";

	// Richiesta Ottemperanza since 06/2014
	public static final String RICHIESTA_OTTEMPERANZA = "OT";
	public static final String PG_LOAD_INSERISCI_RICHIESTA_OTTEMPERANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRichiestaOttemperanza.jsp";
	// public static final String PG_LOAD_DETTAGLIO_RICHIESTA_OTTEMPERANZA = IWebConstants.ROOT_DIR +
	// "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaRichiestaOttemperanza.jsp";

	// Rinvio Udienza
	public static final String RINVIO_UDIENZA = "RU";

	// Reclamo per Revoca licenza/permesso
	public static final String RECLAMO_REVOCA_LICENZA_PERMESSO = "OR";

	// Reclamo in materia di licenze
	public static final String RECLAMO_LICENZA = "OL";

	// Conversione Pene Pecuniarie
	public static final String CONVERSIONE_PENE_PECUNIARIE = "CP";
	public static final String DICHIARAZIONE_ESTINZIONE_LIB_CONTROLLATA = "32"; // 30/09/2015
	public static final String TIPO_CONV_CONVERSIONE = "2470";
	public static final String TIPO_CONV_RATEIZZAZIONE = "2471";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_CONVERSIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaConversionePP.jsp";
	public static final String PG_LOAD_DETTAGLIO_ORDINANZA_CONVERSIONE_PP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaConversionePP.jsp";

	// Rimessione Atti
	public static final String RIMESSIONE_ATTI = "SO";
	public static final String PG_LOAD_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/LoadInserisciRimessioneAtti.jsp";
	public static final String PG_LOAD_DETTAGLIO_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioRimessioneAtti.jsp";
	public static final String PG_MODIFICA_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaRimessioneAtti.jsp";

	// Decodifiche Oggetti
	public static final String OGG_APPL_SANZ_SOSTITUTIVE = "U017";
	public static final String OGG_AUTO_SANZ_SOSTITUTIVE = "U059";
	public static final String OGG_CONV_PENE_PECUNIARIE = "U070";
	public static final String OGG_APPL_MIS_SICUREZZA = "U023";
	public static final String OGG_DICH_DELINQ_ABITUALE = "U086";
	public static final String OGG_DISP_DECOR_PER_MINIM = "0307";
	public static final String OGG_ORD_RECLAMO_LICENZA = "C043";
	public static final String OGG_SOSTITUISCE_LA_MISURA = "0133";

	public static final String CAMPO_GIORNI_NUOVA_MISURA = "giorniNuovaMisura";
	public static final String CAMPO_MESI_NUOVA_MISURA = "mesiNuovaMisura";
	public static final String CAMPO_ANNI_NUOVA_MISURA = "anniNuovaMisura";

	// Ordinanza - Gennaoio 2014
	public static final String AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP = "06";
	public static final String PG_INS_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanza_AmmProvv_AffiInProva_ServSoc.jsp";
	public static final String PG_DETT_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanza_AmmProvv_AffiInProva_ServSoc.jsp";

	public static final String PG_LOAD_MODIFICA_MAGISTRATO_ORDINANZA = "files/siap/sius/depositoordinanzapc/LoadModificaMagistratoOrdinanza.jsp";

	// Revoca Liberazione Anticipata - Luglio 2014
	public static final String REVOCA_LIBERAZIONE_ANTICIPATA = "43";
	public static final String PG_LOAD_INSERISCI_ORDINANZA_REVOCA_LA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaRevocaLA.jsp";
	public static final String PG_LOAD_MODIFICA_ORDINANZA_REVOCA_LA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaRevocaLA.jsp";
	// Il Dettaglio è in /siap/sico/ICostantiLibertaAnticipata : PG_DETTAGLIO_REVOCA_LIBANTICIPATA

	// Ordinanza Rimedi Risarcitori per Violazione ART. 3 CEDU
	public static final String VIOLAZIONE_CEDU = "VC";
	public static final String PG_LOAD_INS_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaViolazioneCEDU.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaViolazioneCEDU.jsp";
	public static final String PG_MODIFICA_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaViolazioneCEDU.jsp";

	// Ordinanza Rimedi Reclami per Violazione ART. 3 CEDU
	public static final String RECLAMI_CEDU = "VD";
	public static final String PG_LOAD_INS_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaReclamiCEDU.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaReclamiCEDU.jsp";
	public static final String PG_MODIFICA_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaReclamiCEDU.jsp";

	// MEV_39
	// Appello Contro Provvedimento su Misura di Sicurezza (OGGETTO_PROCEDIMENTO = C029)
	public static final String OGG_ORD_APPELLO_CONTRO_PROVV_MS = "C029";
	public static final String APPELLO_MS = "AP";
	public static final String PG_LOAD_INS_ORDINANZA_APPELLO_CONTRO_PROVV_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/InserisciOrdinanzaAppelloControProvvMS.jsp";
	public static final String PG_LOAD_DET_ORDINANZA_APPELLO_CONTRO_PROVV_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioOrdinanzaAppelloControProvvMS.jsp";
	public static final String PG_LOAD_MOD_ORDINANZA_APPELLO_CONTRO_PROVV_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/ModificaOrdinanzaAppelloControProvvMS.jsp";

	//@emma: 28/08/2018 : intervento post-collaudo
	public static final String PG_LOAD_DETTAGLIO_EMISSIONE_ORDINANZA_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositoordinanzapc/DettaglioEmissioneOrdinanzaMS.jsp";

}