package siap.sige.provvedimento.action;

import f3b.web.IWebConstants;

/**
 * ICostantiProvvedimentoSige - Classe di costanti di ProvvedimentoSige
 *
* @version 1.0
*/
public interface ICostantiProvvedimentoSige {

	public static final String CAMPO_ID_PROVVEDIMENTO_SIGE = "IdProvvedimentoSige";
	public static final String CAMPO_FAS_ID_FASCICOLO_SIGE = "FasIdFascicoloSige";
	public static final String CAMPO_ID_EVENTO_GENERATO = "IdEventoGenerato";
	public static final String CAMPO_PROVV_ID_PROVVEDIMENTO_SIGE = "ProvvIdProvvedimentoSige";
	public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
	public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
	public static final String CAMPO_DATA_EMISSIONE = "DataEmissione";
	public static final String CAMPO_DATA_DEPOSITO = "DataDeposito";
	public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
	public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
	public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
	public static final String CAMPO_ID_TENORE = "IdTenore";
	public static final String CAMPO_ID_DOCUMENTO_ALLEGATO = "IdDocumentoAllegato";
	public static final String CAMPO_GIORNO_DATA_DEPOSITO = "GiornoDataDeposito";
	public static final String CAMPO_MESE_DATA_DEPOSITO = "MeseDataDeposito";
	public static final String CAMPO_ANNO_DATA_DEPOSITO = "AnnoDataDeposito";
	public static final String CAMPO_LUOGO_SVOLGIMENTO = "LuogoSvolgimento";
	public static final String CAMPO_AGGIUNTIVO = "Aggiuntivo";
	public static final String CAMPO_TRADUZIONE = "Traduzione";
	public static final String CAMPO_NOTE = "Note"; // 12/01/2010
	public static final String SEDE_MAGISTRATO_COMPETENTE = "SedeMagistratoCompetente"; // 10/01/2010
	public static final String CAMPO_COD_UFFICIO_COMPETENTE = "CodUfficioCompetente";
	public static final String CAMPO_COD_TIPO_PROV_SIGE = "CodTipoProvSige";
	public static final String CAMPO_ELENCO_DECRETI= "ElencoDecreti";

	public static final String PG_LOAD_EMISSIONE_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadEmissioneOrdinanza.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioOrdinanza.jsp";
	// public static final String PG_BUTTONS_RICERCA = IWebConstants.ROOT_DIR +
	// "files/siap/sius/provvedimento/buttonsProvvedimento.jsp";
	public static final String PG_BUTTONS_PROVVEDIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/buttonsProvvedimento.jsp";
	public static final String PG_NOTIFICHE_EVENTO_SIGE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/NotificheEventoSige.jsp";
	public static final String PG_BUTTONS_NOTIFICHE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/provvedimento/buttonsNotifiche.jsp";
	// public static final String PG_BUTTONS_NOTIFICHE = IWebConstants.ROOT_DIR +
	// "files/siap/sige/provvedimento/buttonsNotifiche.jsp";
	public static final String PG_DETTAGLIO_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioDecretoInammissibilita.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_NDPNLP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioOrdinanzaNDPNLP.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_INCOMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioOrdinanzaIncompetenza.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_SOSPENSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioOrdinanzaSospensione.jsp";
	public static final String PG_DETTAGLIO_ORDINANZA_CONFLITTO_COMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvInterlocutori/DettaglioOrdinanzaConflittoCompetenza.jsp";
	public static final String PG_LOAD_RICERCAPROVVEDIMENTOSIGE = IWebConstants.ROOT_DIR
			+ "jsp/files/siap/sige/provvedimento/LoadRicercaProvvedimentoSige.jsp";
	public static final String PG_LOAD_DETTAGLIOPROVVEDIMENTOSIGE = IWebConstants.ROOT_DIR
			+ "jsp/files/siap/sige/provvedimento/LoadRicercaProvvedimentoSige.jsp";
	public static final String PG_RICERCAPROVVEDIMENTOSIGE = IWebConstants.ROOT_DIR
			+ "jsp/files/siap/sige/provvedimento/RicercaProvvedimentoSige.jsp";
	public static final String PG_LOAD_INSERISCIPROVVEDIMENTOSIGE = IWebConstants.ROOT_DIR
			+ "jsp/files/siap/sige/provvedimento/LoadInserisciProvvedimentoSige.jsp";
	public static final String PG_ELENCO_DEPOSITO_ORDINANZE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ElencoOrdinanze.jsp";
	public static final String PG_LOAD_INSERISCIDATADEPOSITO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadInserisciDataDeposito.jsp";
	public static final String PG_LOAD_INSERISCIDATADEPOSITO_FISSAZIONE_UDIENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadInserisciDataDepositoFissazioneUdienza.jsp";
	public static final String PG_LOAD_DETTAGLIO_DEPOSITO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DettaglioDepositoProvvedimentoSige.jsp";
	public static final String PG_ELENCOPROVVEDIMENTI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ElencoProvvedimentiSige.jsp";
	public static final String PG_LOAD_CANCELLA_PROVVEDIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadCancellaProvvedimento.jsp";
	public static final String PG_LOAD_EMISSIONE_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadEmissioneDecretoInammissibilita.jsp";
	public static final String PG_LOAD_MODIFICA_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadModificaDecretoInammissibilita.jsp";

	public static final String PG_ELENCO_MOTIVI_PROVV = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ElencoProvvedimentiSige.jsp";
	public static final String PG_LOAD_DESTINATARI_ORDINANZA_SOSPENSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/DestinatariOrdinanzaSospensione.jsp";
	public static final String PG_ELENCO_DEPOSITO_DECRETI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ElencoDecreti.jsp";
	public static final String PG_LOAD_MODIFICADATADEPOSITO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadModificaDataDeposito.jsp";
	public static final String PG_LOAD_EMISSIONE_ORDINANZA_NDPNLP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadEmissioneOrdinanzaNDPNLP.jsp"; // 05/01/2010
	public static final String PG_LOAD_EMISSIONE_ORDINANZA_INCOMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadEmissioneOrdinanzaIncompetenza.jsp"; // 29/01/2010
	public static final String PG_LOAD_EMISSIONE_ORDINANZA_SOSPENSIONE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/LoadEmissioneOrdinanzaSospensione.jsp"; // 29/01/2010

	// Inserimento Nuovi destinatari
	public static final String PG_INSERIMENTO_DESTINATARI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/InserimentoDestinatari.jsp";
	// Inserimento Notifica al Soggetto
	public static final String PG_INSERIMENTO_NOTIFICA_SOGGETTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/InserisciNotificaSoggetto.jsp";
	// Inserimento Notifiche Avvocati ed Altro Destinatario
	public static final String PG_INSERIMENTO_NOTIFICA_AVVOCATI_ALTRO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/InserisciAvvocati.jsp";
	// Modifica Notifiche a Destinatari
	public static final String PG_MODIFICA_DESTINATARI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/ModificaDestinatari.jsp";
	// Inserimento Notifica ad altre autorita giudiziarie.
	public static final String PG_INSERIMENTO_ALTRE_AUTORITA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositodecreto/InserisciNotificaAltreAutoritaGiudiziarie.jsp";
	// Inserimento Notifica al Soggetto presso il Difensore
	public static final String PG_INSERIMENTO_NOTIFICA_SOGGETTO_PRESSO_DIFENSORE = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/InserisciNotificaSoggettoPressoDifensore.jsp";

	public static final String COD_ORDINANZA_GENERICA = "03";
	public static final String COD_RINVIO_UDIENZA_VERBALE = "50";
	public static final String COD_DECRETO_GENERICO = "02";
	public static final String COD_FISSAZIONE_UDIENZA = "01";
	public static final String COD_EVENTO_PROVVEDIMENTO = "01";
	public static final String COD_ORDINANZA_RINVIO_UDIENZA = "04";
	public static final String COD_DECRETO_INAMMISSIBILITA = "05";
	public static final String COD_ORDINANZA_NDPNLP = "06"; // 05/01/2010
	public static final String COD_ORDINANZA_INCOMPETENZA = "07"; // 05/01/2010
	public static final String COD_DECRETO_LATITANZA = "09";
	public static final String COD_ORDINANZA_SOSPENSIONE = "10";
	public static final String COD_DECRETO_IRREPERIBILITA = "12";
	public static final String COD_NOMINA_PERITI = "13";
	public static final String COD_CITAZIONE_TESTI = "17";
	public static final String COD_ORDINANZA_CONFLITTO_COMPETENZA = "18";
	public static final String COD_UNEP = "22"; // Codice tipo Autorità Esterna UNEP
	public static final String NOTIFICA_SIGE_ESEGUITA = "01";
	public static final String MOTIVO_FISSAZIONE_UDIENZA = "0601";
	public static final String DEFINIZIONE_MANUALE = "62";
	
	public static final String 	COD_DECRETO_CITAZIONE_TESTI="17";
	public static final String 	COD_DECRETO_FISSAZIONE_UDIENZA="01";
	public static final String 	COD_DECRETO_INAMISSIBILITA="05";
	
	public static final String 	COD_ORDINANZA_CONFLITTO_COMPETENZA1="14";	
	public static final String 	COD_ORDINANZA_CONFLITTO_COMPETENZA2="18";
	public static final String COD_ORDINANZA_INCOPETENZA="07";
	public static final String COD_ORIDINANZA_SOSPENSIONE_PRECEDENTE_ORDINANZA="10";

	public static final String 	COD_DECRETO_UNIFICAZIONE="55";
	public static final String 	COD_VERBALE_UNIFICAZIONE="56";

	public static final String TIPI_PROVVEDIMENTI = "'" + COD_DECRETO_GENERICO + "'," + "'"
			+ COD_ORDINANZA_GENERICA + "'," + "'" + COD_ORDINANZA_RINVIO_UDIENZA + "'," + "'"
			+ COD_RINVIO_UDIENZA_VERBALE + "','" + COD_ORDINANZA_NDPNLP + "'";

	// Contiene il Tipo di Provvedimento "Definizione Manuale"
	public static final String TIPI_PROVVEDIMENTI_DM = "'" + COD_DECRETO_GENERICO + "'," + "'"
			+ COD_ORDINANZA_GENERICA + "'," + "'" + COD_ORDINANZA_RINVIO_UDIENZA + "'," + "'"
			+ COD_RINVIO_UDIENZA_VERBALE + "'," + "'" + DEFINIZIONE_MANUALE + "'";
	
	public static final String TIPI_PROVVEDIMENTI_DECRETI = "'" + COD_DECRETO_GENERICO + "','"
			+ COD_DECRETO_IRREPERIBILITA + "','" + COD_DECRETO_LATITANZA + "','" + COD_DECRETO_INAMISSIBILITA
			+ "','" + COD_DECRETO_INAMISSIBILITA + "','" + COD_FISSAZIONE_UDIENZA + "','"
			+ COD_DECRETO_CITAZIONE_TESTI + "'";
	public static final String TIPI_PROVVEDIMENTI_ORDINANZE = "'" + COD_ORDINANZA_GENERICA + "'," + "'"
			+ COD_ORDINANZA_RINVIO_UDIENZA + "'," + "'" + COD_RINVIO_UDIENZA_VERBALE + "','"
			+ DEFINIZIONE_MANUALE + "','" + COD_ORDINANZA_CONFLITTO_COMPETENZA1 + "','"
			+ COD_ORDINANZA_CONFLITTO_COMPETENZA2 + "','" + COD_ORDINANZA_INCOPETENZA + "','"
			+ COD_ORIDINANZA_SOSPENSIONE_PRECEDENTE_ORDINANZA + "'";
	public static final String TIPI_PROVVEDIMENTI_UNIFICAZIONI = "'" + COD_DECRETO_UNIFICAZIONE + "'," + "'"
			+ COD_VERBALE_UNIFICAZIONE + "'";
	public static final String TIPI_PROVVEDIMENTI_UDIENZE = "'" + COD_ORDINANZA_RINVIO_UDIENZA + "'," + "'"
			+ COD_RINVIO_UDIENZA_VERBALE + "','" + COD_FISSAZIONE_UDIENZA + "','"
			+ COD_DECRETO_FISSAZIONE_UDIENZA + "'";
	
	public static final String COD_ISTRUTTORIE = "52";
	public static final String FASCICOLO_ARCHIVIATO = "01";
	public static final String SCADENZARIO_SIGE_IRREVOCABILITA = "90";
	public static final String COD_ANNOTAZIONE = "25";

	public static final String INC_TIPO_GIUDIZIO_COLLEGIO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegio.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_FIX = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioFix.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_CONFLITTO_COMP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioConflittoComp.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_SOSP_PREC_ORDINANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioSospPrecOrdinanza.jsp";
	public static final String INC_MOTIVI_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncludeMotiviInammissibilita.jsp";
	public static final String PG_INCLUDE_DATI_PROVVEDIMENTO = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncludeDatiProvvedimento.jsp";
	// Modifica del 08/03/2017
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_CITAZIONE_TESTI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioCitazioneTesti.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_NOMINA_PERITI = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioNominaPeriti.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_INCOMPETENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioIncompetenza.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_ORDINANZA_NDP_NLP = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioOrdinanzaNDPNLP.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_DECRETO_LATITANZA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioDecretoLatitanza.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_DECRETO_IRREPERIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioDecretoIrreperibilita.jsp";
	public static final String INC_TIPO_GIUDIZIO_COLLEGIO_DECRETO_INAMMISSIBILITA = IWebConstants.ROOT_DIR
			+ "files/siap/sige/provvedimento/IncTipoGiudizioCollegioDecretoInammissibilita.jsp";
	
	public static final String COD_EVENTO_RICH_ISTRUTTORIA = "05";
	public static final String COD_EVENTO_PROVV_RICH_ISTRUTTORIA = "'" + COD_EVENTO_PROVVEDIMENTO + "'," + "'"
			+ COD_EVENTO_RICH_ISTRUTTORIA + "'";
	
}