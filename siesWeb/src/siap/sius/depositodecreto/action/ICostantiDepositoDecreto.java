package siap.sius.depositodecreto.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiDepositoDecreto
 * </p>
 * <p>
 * Description: Classe di costanti di DepositoDecreto
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
public interface ICostantiDepositoDecreto {

	public static final String CAMPO_ID_DEPOSITO_DECRETO = "IdDepositoDecreto";
	public static final String CAMPO_ANNO_S72 = "AnnoS72";
	public static final String CAMPO_NUM_S72 = "NumS72";
	public static final String CAMPO_COD_TIPO_DECRETO = "CodTipoDecreto";
	public static final String CAMPO_DATA_EMISSIONE = "DataEmissione";
	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
	public static final String CAMPO_GIORNO_DATA_DEPOSITO = "GiornoDataDeposito";
	public static final String CAMPO_MESE_DATA_DEPOSITO = "MeseDataDeposito";
	public static final String CAMPO_ANNO_DATA_DEPOSITO = "AnnoDataDeposito";
	public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";
	public static final String CAMPO_ALTRI_DESTINATARI = "AltriDestinatari";
	public static final String CAMPO_GIORNO_DATA_PARERE_PG = "GiornoDataParerePg";
	public static final String CAMPO_MESE_DATA_PARERE_PG = "MeseDataParerePg";
	public static final String CAMPO_ANNO_DATA_PARERE_PG = "AnnoDataParerePg";
	public static final String CAMPO_COD_TIPO_PARERE_PG = "CodTipoParerePg";
	public static final String CAMPO_GIORNO_DATA_RICORSO_IMPUGNAZIONE = "GiornoDataRicorsoImpugnazione";
	public static final String CAMPO_MESE_DATA_RICORSO_IMPUGNAZIONE = "MeseDataRicorsoImpugnazione";
	public static final String CAMPO_ANNO_DATA_RICORSO_IMPUGNAZIONE = "AnnoDataRicorsoImpugnazione";
	public static final String CAMPO_GIORNO_DATA_INVIO_ATTI_IMPUGNAZIONE = "GiornoDataInvioAttiImpugnazione";
	public static final String CAMPO_MESE_DATA_INVIO_ATTI_IMPUGNAZIONE = "MeseDataInvioAttiImpugnazione";
	public static final String CAMPO_ANNO_DATA_INVIO_ATTI_IMPUGNAZIONE = "AnnoDataInvioAttiImpugnazione";
	public static final String CAMPO_GIORNO_DATA_SENTENZA_IMPUGNAZIONE = "GiornoDataSentenzaImpugnazione";
	public static final String CAMPO_MESE_DATA_SENTENZA_IMPUGNAZIONE = "MeseDataSentenzaImpugnazione";
	public static final String CAMPO_ANNO_DATA_SENTENZA_IMPUGNAZIONE = "AnnoDataSentenzaImpugnazione";
	public static final String CAMPO_TENORE_SENTENZA_IMPUGNAZIONE = "TenoreSentenzaImpugnazione";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO = "GenPridGeneraleProcedimento";
	public static final String CAMPO_SENTENZA_RIFERIMENTO = "SentenzaRiferimento";
	public static final String CAMPO_ID_DOCUMENTO_ALLEGATO = "IdDocumentoAllegato";
	public static final String ACTION_DOPO_CANCELLAZIONE = "ActDopoCanc";
	// 20131201 - paolo ( modifica magistrato per decreto )
	public static final String CAMPO_ID_EVENTO_GENERATO = "IdEventoGenerato";

	// Emissione Decreto
	public static final String CAMPO_TIPO_DECRETO_DA_PRODURRE = "TipoDecretodaProdurre";
	public static final String CAMPO_COD_UFFICIO_MAGISTRATO_COMP = "CodUfficioComp";
	public static final String CAMPO_COD_UFFICIO_COMP = "CodUfficioComp";
	public static final String CAMPO_COD_UFFICIO_TDS_COMP = "CodUfficioTdSComp";
	public static final String CAMPO_COD_UFFICIO_PROCURA_COMP = "CodUfficioProcuraComp";
	public static final String CAMPO_LUOGO_SVOLGIMENTO_PROVA = "LuogoSvolgimentoProva";

	public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
	public static final String CAMPO_STATUS_PERSONA = "StatusPersona";
	public static final String CAMPO_TOT_ORE_RAGGIUNGIMENTO = "TotOreRaggiungimento";
	public static final String CAMPO_ANNO_PROC_REVOCATO = "AnnoProcRevocato";
	public static final String CAMPO_PROGR_PROC_REVOCATO = "ProgrProcRevocato";
	public static final String CAMPO_UFFICIO_PROC_REVOCATO = "UfficioProcRevocato";
	public static final String CAMPO_TIPO_SEDE_REVOCA = "TipoSede";
	public static final String CAMPO_CK_PRESCRIZIONI = "CheckPrescrizioni";
	public static final String CAMPO_CK_WARNING = "WarningDecreto";
	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public static final String CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE = "CheckTipoControlloEsecuzione";
	//
	// New Fields 20061116
	public static final String CAMPO_NUOVA_SCADENZA_LIM_CTRL = "NuovaScadenzaLimCtrl";
	public static final String CAMPO_DURATA_PROROGA = "DurataProroga";
	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public static final String CAMPO_GIORNO_SOSPENSIONE_SS = "GiornoSospensioneSS";
	public static final String CAMPO_MESE_SOSPENSIONE_SS = "MeseSospensioneSS";
	public static final String CAMPO_ANNO_SOSPENSIONE_SS = "AnnoSospensioneSS";
	public static final String CAMPO_GIORNI_RECUPERO_SS = "GiorniRecuperoSS";
	public static final String CAMPO_FLAG_RECUPERO_SS = "FlagRecuperoSS";
	public static final String CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS = "GiornoScadenzaSospensioneSS";
	public static final String CAMPO_MESE_SCADENZA_SOSPENSIONE_SS = "MeseScadenzaSospensioneSS";
	public static final String CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS = "AnnoScadenzaSospensioneSS";
	public static final String CAMPO_SOSPENSIONE_GG_SS = "GiorniSospensioneSS";
	public static final String CAMPO_SOSPENSIONE_MM_SS = "MesiSospensioneSS";
	public static final String CAMPO_SOSPENSIONE_AA_SS = "AnniSospensioneSS";

	// Gestione Ricerca Permesso/Licenze
	public static final String CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE = "GiornoDataDepositoIniziale";
	public static final String CAMPO_MESE_DATA_DEPOSITO_INIZIALE = "MeseDataDepositoIniziale";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_INIZIALE = "AnnoDataDepositoIniziale";
	public static final String CAMPO_GIORNO_DATA_DEPOSITO_FINALE = "GiornoDataDepositoFinale";
	public static final String CAMPO_MESE_DATA_DEPOSITO_FINALE = "MeseDataDepositoFinale";
	public static final String CAMPO_ANNO_DATA_DEPOSITO_FINALE = "AnnoDataDepositoFinale";

	// 20140605 - d.f. Aggiunti campi per RICHIESTA OTTEMPERANZA
	public static final String CAMPO_FLAG_NOMINA_COMM_ACTA = "FlagNominaCommActa";
	public static final String CAMPO_DESCR_COMM_ACTA = "DescCommActa";

	// TRASMISSIONE.
	public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRicevuto.jsp";

	// TEMPLATE.
	public static final String TEMPLATE_DECRETO_INAMMISSIBILITA = "SIUS_DE_002";
	public static final String TEMPLATE_DECRETO_CITAZIONE = "SIUS_DE_003";
	public static final String TEMPLATE_DECRETO_INCOMPETENZA = "SIUS_DE_004";
	public static final String TEMPLATE_DECRETO_IRREPERIBILITA = "SIUS_DE_005";
	public static final String TEMPLATE_DECRETO_NDPNLP = "SIUS_DE_007";
	public static final String TEMPLATE_MOD_DECRETO_TIPO = "SIUS_OM_021";
	public static final String TEMPLATE_MOD_DECRETO_TIPO_UDS = "SIUS_OM_022";

	public static final String PG_LOAD_DETTAGLIO_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoInammissibilita.jsp";
	public static final String PG_LOAD_INSERISCI_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDecretoInammissibilita.jsp";

	// genny 02/02/2004
	public static final String PG_LOAD_DETTAGLIO_DECRETO_IRREPERIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoIrreperibilità.jsp";
	public static final String PG_LOAD_INSERISCI_DECRETO_IRREPERIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDecretoIrreperibilità.jsp";

	public static final String PG_LOAD_INSERISCI_DECRETO_INCOMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDecretoIncompetenza.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_INCOMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoIncompetenza.jsp";

	public static final String PG_LOAD_INSERISCI_DECRETO_NDP_NLP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDecretoNDPNLP.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_NDP_NLP = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoNDPNLP.jsp";

	public static final String PG_LOAD_INSERISCIDATADEPOSITODECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDataDepositoDecreto.jsp";
	public static final String PG_LOAD_DETTAGLIO_DEPOSITO_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDepositoDecreto.jsp";
	public static final String PG_LOAD_TRASFERISCI_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadTrasferisciDecreto.jsp";
	public static final String PG_LOAD_EMISSIONE_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciEmissioneDecreto.jsp";

	public static final String PG_INSERISCI_APPLICAZIONE_PROVVISORIA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoSospensione.jsp";
	public static final String PG_DETTAGLIO_APPLICAZIONE_PROVVISORIA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoSospensione.jsp";

	public static final String PG_INSERISCI_DECRETO_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoEspulsione.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_ESPULSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoEspulsione.jsp";
	public static final String PG_ELENCO_DEPOSITO_DECRETI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ElencoDecreti.jsp";
	public static final String PG_INSERISCI_DECRETO_MODIFICA_ATT_LAVORATIVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoModAttLavoro.jsp";
	public static final String PG_INSERISCI_DECRETO_RICOVERI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoRicoveri.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_ATT_LAVORATIVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoModAttLavoro.jsp";
	public static final String PG_INSERISCI_DECRETO_MODIFICA_PRESCRIZIONI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoModPrescrizioni.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_PRESCRIZIONI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoModPrescrizioni.jsp";
	public static final String PG_LOAD_DETTAGLIO_DECRETO_RICOVERI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRicoveri.jsp";
	public static final String PG_LOAD_MODIFICADATADEPOSITODECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadModificaDataDepositoDecreto.jsp";
	public static final String PG_DETTAGLIO_AUTORIZ_CORRIS_TELEFONICA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoAutorizTelefono.jsp";
	public static final String PG_INSERISCI_AUTORIZ_CORRIS_TELEFONICA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoAutorizTelefono.jsp";
	public static final String PG_DETTAGLIO_RINVIO_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRinvioEsecuzionePena.jsp";
	public static final String PG_INSERISCI_RINVIO_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRinvioEsecuzionePena.jsp";
	public static final String PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoSospEsecuzionePena.jsp";
	public static final String PG_INSERISCI_SOSPENSIONE_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoSospEsecuzionePena.jsp";
	public static final String PG_DETTAGLIO_REVOCA_SOSPENSIONE_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevSospEsecuzionePena.jsp";
	public static final String PG_INSERISCI_REVOCA_SOSPENSIONE_ESECUZIONE_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRevSospEsecuzionePena.jsp";
	public static final String PG_DETTAGLIO_REVOCA_APPLICAZIONE_PROVVISORIA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevAppProvMA.jsp";
	public static final String PG_INSERISCI_REVOCA_APPLICAZIONE_PROVVISORIA_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRevAppProvMA.jsp";
	public static final String PG_DETTAGLIO_RICOVERO_OPG_OSS_PSICHE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRicoveroOPGOssPsiche.jsp";
	public static final String PG_INSERISCI_RICOVERO_OPG_OSS_PSICHE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRicoveroOPGOssPsiche.jsp";
	public static final String PG_INSERISCI_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoPermesso.jsp";
	public static final String PG_DETTAGLIO_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoPermesso.jsp";
	public static final String PG_INSERISCI_MODIFICAATTLUOGODET = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoModificaAttLuogoDet.jsp";
	public static final String PG_DETTAGLIO_MODIFICAATTLUOGODET = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoModificaAttLuogoDet.jsp";
	public static final String PG_INSERISCI_INOSSERVANZAOBBLIGHI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoInosservanzaObblighi.jsp";
	public static final String PG_DETTAGLIO_INOSSERVANZAOBBLIGHI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoInosservanzaObblighi.jsp";
	public static final String PG_INSERISCI_SOPRAVVENIENZA_NT = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoSopravvenienzaNT.jsp";
	public static final String PG_DETTAGLIO_SOPRAVVENIENZA_NT = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoSopravvenienzaNT.jsp";

	public static final String PG_INSERISCI_PROPOSTA_DECLAR_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoPropostaDeclarEsito.jsp";
	public static final String PG_DETTAGLIO_PROPOSTA_DECLAR_ESITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoPropostaDeclarEsito.jsp";
	public static final String PG_INSERISCI_GENERICO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoGenerico.jsp";
	public static final String PG_DETTAGLIO_GENERICO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoGenerico.jsp";

	// Add 20061116
	public static final String PG_INSERISCI_LIMITAZIONE_CONTROLLI_CORRISPONDENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoLimCtrlCorrispondenza.jsp";
	public static final String PG_DETTAGLIO_LIMITAZIONE_CONTROLLI_CORRISPONDENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoLimCtrlCorrispondenza.jsp";

	public static final String PG_INSERISCI_LICENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoLicenza.jsp";
	public static final String PG_DETTAGLIO_LICENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoLicenza.jsp";

	public static final String PG_INSERISCI_REVOCA_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRevocaPermesso.jsp";
	public static final String PG_DETTAGLIO_REVOCA_PERMESSO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevocaPermesso.jsp";
	public static final String PG_INSERISCI_ESCLUSIONE_COMPUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRevocaPermesso.jsp";
	// public static final String PG_DETTAGLIO_ESCLUSIONE_COMPUTO = IWebConstants.ROOT_DIR +
	// "files/siap/sius/depositodecreto/DettaglioDecretoRevocaPermesso.jsp";
	public static final String PG_INSERISCI_AUTORIZZAZIONE_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoAutorizzaMA.jsp";
	public static final String PG_DETTAGLIO_AUTORIZZAZIONE_MA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoAutorizzaMA.jsp";
	public static final String PG_DETTAGLIO_RICOVERO_OPG_ESP_PENA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRicoveroOPGEspiazione.jsp";

	public static final String BOTTONI_DETTAGLIO_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/BottoniDettaglioDecreto.jsp";

	public static final String PG_DETTAGLIO_DATA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDataDecreto.jsp";
	public static final String PG_SINTESI_DEC_PER_RIF = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DecretoPermessoRiferimento.jsp";
	public static final String PG_SINTESI_DEC_LIC_RIF = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DecretoLicenzaRiferimento.jsp";
	public static final String PG_SINTESI_MULTI_DEC_PER_LIC_RIF = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DecretiPermessoLicenzaRiferimento.jsp";
	public static final String PG_TOOLBAR_HEADER = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/toolbar_header.jsp";

	// Inserimento Nuovi destinatari
	public static final String PG_INSERIMENTO_DESTINATARI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserimentoDestinatari.jsp";
	// Inserimento Notifica al Soggetto
	public static final String PG_INSERIMENTO_NOTIFICA_SOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciNotificaSog.jsp";
	// Inserimento Notifiche Avvocati ed Altro DEstinatario
	public static final String PG_INSERIMENTO_NOTIFICA_AVVOCATI_ALTRO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciAvvocati.jsp";
	// Modifica Notifiche a Destinatari
	public static final String PG_MODIFICA_DESTINATARI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ModificaDestinatari.jsp";
	// Inserimento Notifica ad altre autorita giudiziarie.
	public static final String PG_INSERIMENTO_ALTRE_AUTORITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciNotificaAltreAutoritaGiudiziarie.jsp";
	// Inserisci Autorizzazione su Sanzioni Sostitutive
	public static final String PG_INSERISCI_AUTORIZZAZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoAutorizzaSanzioniSostitutive.jsp";
	// Dettaglio Autorizzazione su Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_AUTORIZZAZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoAutorizzaSanzioniSostitutive.jsp";
	// Inserisci Declaratoria Estinzione Sanzioni Sostitutive
	public static final String PG_INSERISCI_DECLARATORIA_ESTINZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoDeclaratoriaEstinzioneSanzioniSostitutive.jsp";
	// Dettaglio Declaratoria Estinzione Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_DECLARATORIA_ESTINZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoDeclaratoriaEstinzioneSanzioniSostitutive.jsp";
	// Inserisci Deposito/Diffida Sanzioni Sostitutive
	public static final String PG_INSERISCI_CONVOCAZIONE_DIFFIDA_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoConvocazioneDiffidaSanzioniSostitutive.jsp";
	// Dettaglio Deposito/Diffida Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_CONVOCAZIONE_DIFFIDA_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoConvocazioneDiffidaSanzioniSostitutive.jsp";
	// Inserisci Modifica Permanente Sanzioni Sostitutive
	public static final String PG_INSERISCI_MODIFICA_PERMANENTE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoModificaPermanenteSanzioniSostitutive.jsp";
	// Dettaglio Modifica Permanente Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_MODIFICA_PERMANENTE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoModificaPermanenteSanzioniSostitutive.jsp";
	// Inserisci Sospensione Esecuzione Sanzioni Sostitutive
	public static final String PG_INSERISCI_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoSospensioneEsecuzioneSanzioniSostitutive.jsp";
	// Dettaglio Sospensione Esecuzione Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoSospensioneEsecuzioneSanzioniSostitutive.jsp";
	// Inserisci Revoca Autorizzazione Sanzione Sostitutiva
	public static final String PG_INSERISCI_REVOCA_AUTORIZZAZIONE_SANZIONE_SOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRevocaAutorizzazioneSanzioneSostitutiva.jsp";
	// Dettaglio Revoca Autorizzazione Sanzione Sostitutiva
	public static final String PG_DETTAGLIO_REVOCA_AUTORIZZAZIONE_SANZIONE_SOSTITUTIVA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevocaAutorizzazioneSanzioneSostitutiva.jsp";

	// Inserisci Autorizzazione su Misure Sicurezza
	public static final String PG_INSERISCI_AUTORIZZAZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoAutorizzaMisureSicurezza.jsp";
	// Dettaglio Autorizzazione su Misure Sicurezza
	public static final String PG_DETTAGLIO_AUTORIZZAZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoAutorizzaMisureSicurezza.jsp";
	// Inserisci Deposito/Diffida Misure Sicurezza
	public static final String PG_INSERISCI_DIFFIDA_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoDiffidaMisureSicurezza.jsp";
	// Dettaglio Deposito/Diffida Sanzioni Sostitutive
	public static final String PG_DETTAGLIO_DIFFIDA_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoDiffidaMisureSicurezza.jsp";
	// Inserisci Sospensione Esecuzione Misure Sicurezza
	public static final String PG_INSERISCI_SOSPENSIONE_ESECUZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoSospensioneEsecuzioneMisureSicurezza.jsp";
	// Dettaglio Sospensione Esecuzione Misure Sicurezza
	public static final String PG_DETTAGLIO_SOSPENSIONE_ESECUZIONE_MISURE_SICUREZZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoSospensioneEsecuzioneMisureSicurezza.jsp";
	// Inserisci Modifica Prescrizioni MS
	public static final String PG_INSERISCI_DECRETO_MODIFICA_PRESCRIZIONI_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciEmissioneDecretoModPrescrizioniMS.jsp";
	// Dettaglio Modifica Prescrizioni MS
	public static final String PG_LOAD_DETTAGLIO_DECRETO_MODIFICA_PRESCRIZIONI_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoModPrescrizioniMS.jsp";

	public static final String PG_LOAD_INSERISCI_DECRETO_RICHIESTA_OTTEMPERANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRichiestaOttemperanza.jsp";

	// Tipi di decreto
	public static final String CITAZIONE = "01";
	public static final String IRREPERIBILITA = "02";
	public static final String INAMMISSIBILITA = "03";
	public static final String INCOMPETENZA = "04";
	public static final String UNIFICAZIONE = "05";
	public static final String APPLICAZIONE_PROVVISORIA_MA = "06";
	public static final String SOPRAVVENIENZA_NT = "07";
	public static final String ESPULSIONE = "08";
	public static final String MODIFICA_ATT_LAVORATIVA = "09";
	public static final String MODIFICA_PRESCRIZIONI = "10";
	public static final String RICOVERI = "11";
	public static final String AUTORIZZAZIONE_CORRISPONDENZA_TELEFONICA = "12";
	public static final String RINVIO_ESECUZIONE_PENA = "13";
	public static final String SOSPENSIONE_ESECUZIONE_PENA = "14";
	public static final String REVOCA_SOSPENSIONE_ESECUZIONE_PENA = "15";
	public static final String REVOCA_APPLICAZIONE_PROVVISORIA_MA = "16";
	public static final String PERMESSO = "17";
	public static final String RICOVERO_OPG_OSS_PSICHE = "18";
	public static final String MODIFICA_ATTIVITA_LUOGO_DET = "19";
	public static final String INOSSERVANZA_OBBLIGHI = "20";
	public static final String PROPOSTA_DECLAR_ESITO_PROVA = "21";
	public static final String GENERICO = "22";
	public static final String LICENZA = "23";
	public static final String ESCLUSIONE_COMPUTO = "24";
	public static final String REVOCA_PERMESSO = "25";
	public static final String AUTORIZZAZIONE_MA = "26";
	public static final String ESCLUSIONE_COMPUTO_LICENZA = "27";
	public static final String REVOCA_LICENZA = "28";
	public static final String NDP_NLP = "29";
	public static final String LIMITAZIONI_CONTROLLI_CORRISPONDENZA = "30";
	// Autorizzazione su Sanzioni Sostitutive
	public static final String AUTORIZZAZIONE_SS = "31";
	// Declaratoria Estinzione Sanzioni Sostitutive
	public static final String DECLARATORIA_ESTINZIONE_SS = "32";
	// Convocazione/Diffida su Sanzioni Sostitutive
	public static final String CONVOCAZIONE_DIFFIDA_SS = "33";
	// Modifica Permanente su Sanzioni Sostitutive
	public static final String MODIFICA_PERMANENTE_SS = "34";
	// Sospensione Esecuzione Sanzioni Sostitutive
	public static final String SOSPENSIONE_ESECUZIONE_SS = "35";
	// Revoca Autorizzazione Sanzione Sostitutiva
	public static final String REVOCA_AUTORIZZAZIONE_SS = "36";
	// Autorizzazione su Misure Sicurezza
	public static final String AUTORIZZAZIONE_MS = "37";
	// Modifica Prescrizioni Misure Sicurezza
	public static final String SOSPENSIONE_ESECUZIONE_MS = "38";
	// Modifica Prescrizioni Misure Sicurezza
	public static final String MODIFICA_PRESCRIZIONI_MS = "39";
	// Inosservanza Obblighi Misure Sicurezza
	public static final String DEC_INOSSERVANZA_OBBLIGHI_MS = "40";
	// MEV_39: aggiunta costante per contenuto U082
	public static final String RINVIO_ESECUZIONE_MS = "42";

	public static final String CAMPO_ID_NOTIFICA = "id_notifica";
	public static final String PG_WARNING = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/WarningDecreto.jsp";

	// Revoca Provvedimento : Esiste sia il decreto che l'Ordinanza.
	public static final String REVOCA_DECRETO = "RV";
	public static final String PG_LOAD_DETTAGLIO_REVOCA_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevocaDecreto.jsp";
	public static final String PG_SINTESI_DECRETO_REVOCATO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DecretoRevocato.jsp";

	public static final String PG_SINTESI_DECRETO_REVOCA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/SintesiDecretoDiRevoca.jsp";

	public static final String PG_MODIFICA_DECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ModificaDecreto.jsp";

	public static final String PG_ELENCOSTAMPEMODELLIDECRETO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ElencoStampeModelliDecreto.jsp";
	public static final String COD_UNEP = "22"; // Codice tipo Autorità Esterna UNEP

	// 20131201 - Magistrato Decreto/Provvedimento
	public static final String PG_LOAD_MODIFICA_MAGISTRATO_DECRETO = "files/siap/sius/depositodecreto/LoadModificaMagistratoDecreto.jsp";
	// Decreto Revoca L.A.
	public static final String PG_DETTAGLIO_DECRETO_REVOCA_LA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRevocaLA.jsp";
	public static final String PG_MODIFICA_DECRETO_REVOCA_LA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ModificaDecretoRevocaLA.jsp";

	// DL 92 2014 Violazione CEDU
	public static final String PG_DETTAGLIO_DECRETO_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoViolazioneCEDU.jsp";
	public static final String PG_DECRETO_VIOLAZIONE_CEDU_RIEPILOGO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DecretoViolazioneCEDU_Riepilogo.jsp";
	public static final String PG_MODIFICA_DECRETO_VIOLAZIONE_CEDU = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/ModificaDecretoViolazioneCEDU.jsp";

	// MEV63
	public static final String COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA = "U002";

	// MEV_39: aggiunta pagina per contenuto U082
	public static final String PG_RINVIO_ESECUZIONE_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciDecretoRinvioMisureSicurezza.jsp";
	public static final String PG_DETTAGLIO_RINVIO_ESECUZIONE_MS = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDecretoRinvioMisureSicurezza.jsp";

	// MEV_9: create nuove pagine
	public static final String PG_LOAD_INSERISCI_DESIGNAZIONE_MAGISTRATO_RELATORE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciDesignazioneMagistratoRelatore.jsp";
	public static final String PG_LOAD_INSERISCI_CONFERMA_DECISIONE_MAGISTRATO_RELATORE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadInserisciConfermaDecisioneMagistratoRelatore.jsp";
	public static final String COD_OGGETTO_CONCESSIONE_MISURE_ALTERNATIVE_ALLA_DETENZIONE = "C050";
	public static final String COD_OGGETTO_CONCESSIONE_MISURE_PENALI_DI_COMUNITA_MISURE_ALTERNATIVE_ALLA_DETENZIONE = "C051";
	public static final String DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA = "DM";
	public static final String CAMPO_GIORNO_DATA_TERMINE_EMISSIONE = "GiornoDataTermineEmissione";
	public static final String CAMPO_MESE_DATA_TERMINE_EMISSIONE = "MeseDataTermineEmissione";
	public static final String CAMPO_ANNO_DATA_TERMINE_EMISSIONE = "AnnoDataTermineEmissione";
	public static final String PG_LOAD_DETTAGLIO_DESIGNAZIONE_MAGISTRATO_RELATORE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioDesignazioneMagistratoRelatore.jsp";
	public static final String PG_LOAD_DETTAGLIO_CONFERMA_DECISIONE_MAGISTRATO_RELATORE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/DettaglioConfermaDecisioneMagistratoRelatore.jsp";
	public static final String PG_LOAD_MODIFICA_DESIGNAZIONE_MAGISTRATO_RELATORE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/LoadModificaDesignazioneMagistratoRelatore.jsp";
	public static final String TEMPLATE_DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE = "SIUS_DE_0610";
	public static final String TEMPLATE_DECRETO_CONFERMA_DECISIONE_MAGISTRATO_RELATORE = "SIUS_DE_0271";
	public static final String CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE = "NumeroGiorniTermineEmissione";
	// FINE MEV_9

}