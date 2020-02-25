package siap.sico.evento.action;

import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ICostantiEvento
 * </p>
 * <p>
 * Description: Classe di costanti di Evento
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
public interface ICostantiEvento {
	public static final String CAMPO_ID_EVENTO = "IdEvento";
	public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
	public static final String CAMPO_COD_TIPO_EVENTO = "CodTipoEvento";
	public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
	public static final String CAMPO_COD_MOTIVO = "CodMotivo";
	public static final String CAMPO_COD_MOTIVO_TRIBUNALE = "CodMotivoTribunale";
	public static final String CAMPO_COD_MOTIVO_MAGISTRATO = "CodMotivoMagidtrato";
	public static final String CAMPO_COD_UFFICIO_EMITTENTE = "CodUfficioEmittente";
	public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
	public static final String CAMPO_SOGGETTO_PRESENTANTE = "SoggettoPresentante";
	public static final String CAMPO_NOME_SOGGETTO_PRESENTANTE = "NomeSoggettoPresentante";
	public static final String CAMPO_COGNOME_SOGGETTO_PRESENTANTE = "CognomeSoggettoPresentante";

	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
	public static final String CAMPO_DATA_EMISSIONE = "DataEmissione";
	public static final String CAMPO_COD_ESITO = "CodEsito";
	public static final String CAMPO_COD_MAGISTRATO = "CodMagistrato";
	public static final String CAMPO_MAGISTRATO = "Magistrato";
	public static final String CAMPO_FLAG_PIU_MENO = "FlagPiuMeno";
	public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI = "GiornoDataTrasmissioneAtti";
	public static final String CAMPO_MESE_DATA_TRASMISSIONE_ATTI = "MeseDataTrasmissioneAtti";
	public static final String CAMPO_ANNO_DATA_TRASMISSIONE_ATTI = "AnnoDataTrasmissioneAtti";
	public static final String CAMPO_GIORNO_DATA_RICEZIONE_ATTI = "GiornoDataRicezioneAtti";
	public static final String CAMPO_MESE_DATA_RICEZIONE_ATTI = "MeseDataRicezioneAtti";
	public static final String CAMPO_ANNO_DATA_RICEZIONE_ATTI = "AnnoDataRicezioneAtti";
	public static final String CAMPO_COD_UFFICIO_DESTINATARIO = "CodUfficioDestinatario";
	public static final String CAMPO_CODICE_LUOGO_DESTINATARIO = "CodiceLuogoDestinatario";
	public static final String CAMPO_ANNO_PROTOCOLLO = "AnnoProtocollo";
	public static final String CAMPO_PROGR_PROTOCOLLO = "ProgrProtocollo";
	public static final String CAMPO_DOC_BLOB = "DocBlob";
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
	public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
	public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
	public static final String CAMPO_FAS_SIU_SOG_ID_SOGGETTO = "FasSiuSogIdSoggetto";
	public static final String CAMPO_BLOB = "CampoBlob";
	public static final String CAMPO_VALIDA = "CampoValida";
	public static final String CAMPO_AZIONE_DETTAGLIO = "AzioneDettaglio";
	public static final String CAMPO_FLAG_STAMPA_SIEP = "FlagStampaSiep";
	public static final String CAMPO_FLAG_STAMPA_SIUS = "FlagStampaSius";
	public static final String CAMPO_FLAG_VIDEO_SIEP = "FlagVideoSiep";
	public static final String CAMPO_FLAG_VIDEO_SIUS = "FlagVideoSius";
	public static final String CAMPO_DEC_ID_DECRETO_ORDINANZA_SIEP = "DecIdDecretoOrdinanzaSiep";
	public static final String CAMPO_ANN_ID_ANNOTAZIONE_MANUALE = "AnnIdAnnotazioneManuale";
	public static final String CAMPO_PEN_ID_PENA_RESIDUA = "PenIdPenaResidua";

	public static final String CAMPO_COD_TIPO_UFFICIO_DESTINATARIO = "CodTipoUfficioDestinatario";
	public static final String CAMPO_COD_LUOGO_DESTINATARIO = "CodLuogoDestinatario";

	public static final String FOGLIO_COMPLEMENTARE = "foglio";

	public static final String CAMPO_GIORNO_DATA_ESPULSIONE_SANZ_SOST = "GiornoDataEspulsioneSanzSost";
	public static final String CAMPO_MESE_DATA_ESPULSIONE_SANZ_SOST = "MeseDataEspulsioneSanzSost";
	public static final String CAMPO_ANNO_DATA_ESPULSIONE_SANZ_SOST = "AnnoDataEspulsioneSanzSost";

	public static final String TEMPLATE_ORDINE_ESECUZIONE_CONDANNATO_LIBERO = "OE1";
	public static final String TEMPLATE_ORDINE_ESECUZIONE_CONDANNATO_DETENUTO_QC = "OE2";

	public static final String PG_LOAD_DETTAGLIOEVENTO = "FasSiuSogIdSoggetto";
	public static final String PG_LOAD_RICERCAEVENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/LoadRicercaEvento.jsp";

	public static final String PG_RICERCAEVENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/RicercaEvento.jsp";
	public static final String PG_BUTTONS_RICERCA = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/buttonsEvento.jsp";
	public static final String PG_BUTTONS_RICERCA_OMESSE_NOTIFICHE = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/buttonsEventoOmesseNotifiche.jsp";

	public static final String PG_TOOLBAR_HEADER = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/toolbar_header.jsp";
	public static final String PG_LOAD_DETTAGLIO_DOCUMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/DettaglioDocumento.jsp";
	public static final String PG_LOAD_TRASFERISCI_DOCUMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/LoadTrasferisciDocumento.jsp";

	public static final String PG_LOAD_DETTAGLIO_DOCUMENTO_ORDINANZE = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/DettaglioDocumentoOrdinanza.jsp";

	// WARNING
	public static final String CAMPO_CK_WARNING = "WarningUpload";
	public static final String PG_WARNING = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/WarningUploadDoc.jsp";
	public static final String PG_RICERCA_EVENTI_NON_REG = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/RicercaEventiNonValidati.jsp";

	public static final String PG_RICERCA_EVENTI_LS = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/RicercaEventiOmesseNotifiche.jsp";
	public static final String PG_RICERCA_NOTIFICHE_LS = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/RicercaNotificheEventoLS.jsp";

	public static final String PG_LISTA_ORDINI_ESECUZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/ListaOrdiniEsecuzione.jsp";

	// Modifica Magistrato Firmatario in Validazione
	public static final String PG_MODIFICA_MAGISTRATO_FIRMATARIO_VALIDAZIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sico/evento/ModificaMagistratoFirmatarioValidazione.jsp";

	// TEMPLATE VUOTO
	public static final String TEMPLATE_VUOTO = "SIEP_VUOTO";

	// MEV 16: aggiunte costanti per cui deve essere visibile l'icona del FC
	public static final String[] CODICI_SOSPENSIONE_DELLA_PENA = new String[] { "0061", "0063", "0078",
			"0117", "0364", "0365", "0495" };
	public static final String[] CODICI_AVVENUTA_ESECUZIONE_PENA = new String[] { "0006", "0096", "0097",
			"0098", "0099", "0473", "0587", "0588" };
	// codici.avvenutaesecuzionepena=0006|0081|0083|0096|0097|0098|0099|0424|0473|0587|0588|2245|2630|UTE1

}