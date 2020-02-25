package siap.sius.depositosentenza.action;

import f3b.web.IWebConstants;

public interface ICostantiDepositoSentenza {

	public static final String CAMPO_ANNO = "Anno";
	public static final String CAMPO_NUM = "Num";

	public static final String CAMPO_ID_DEPOSITO_SENTENZA = "IdDepositoSentenza";
	public static final String CAMPO_COD_TIPO_SENTENZA = "CodTipoSentenza";
	public static final String CAMPO_ID_DOCUMENTO_ALLEGATO = "IdDocumentoAllegato";
	public static final String CAMPO_GIORNO_DATA_DEPOSITO = "GiornoDataDeposito";
	public static final String CAMPO_MESE_DATA_DEPOSITO = "MeseDataDeposito";
	public static final String CAMPO_ANNO_DATA_DEPOSITO = "AnnoDataDeposito";
	public static final String CAMPO_TIPO_SENTENZA_DA_PRODURRE = "TipoSentenzaDaProdurre";

	public static final String GENERICA = "GE";
	// public static final String MISURA_SICUREZZA = "MS";

	public static final String RIABILITAZIONE_SPECIALE = "01";
	public static final String REVOCA_RIABILITAZIONE_SPECIALE = "02";
	public static final String CORREZIONE_ERRORE_MATERIALE = "03";
	public static final String RINVIO_UDIENZA = "04";
	public static final String RIMESSIONE_ATTI = "05";

	public static final String REVOCA_SENTENZA = "02";

	public static final String PG_LOAD_INSERISCI_SENTENZA_GENERICA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/InserisciSentenzaGenerica.jsp";
	public static final String PG_LOAD_DETTAGLIO_EMISSIONE_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/DettaglioEmissioneSentenza.jsp";

	// Modifica Sentenza
	public static final String PG_MODIFICA_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/ModificaSentenza.jsp";

	// Rimessione Atti
	public static final String PG_MODIFICA_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/ModificaRimessioneAtti.jsp";
	public static final String PG_LOAD_DETTAGLIO_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/DettaglioRimessioneAtti.jsp";

	public static final String PG_LOAD_INSERISCIDATADEPOSITO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/LoadInserisciDataDeposito.jsp";
	public static final String PG_LOAD_DETTAGLIO_DEPOSITO_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/DettaglioDepositoSentenza.jsp";
	public static final String PG_ELENCO_DEPOSITO_SENTENZE = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/ElencoSentenze.jsp";
	public static final String PG_LOAD_MODIFICA_DATA_DEPOSITO_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/LoadModificaDataDepositoSentenza.jsp";
	public static final String PG_LOAD_TRASFERISCI_SENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/LoadTrasferisciSentenza.jsp";
	public static final String PG_SINTESI_SENTENZA_REVOCA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/SintesiSentenzaDiRevoca.jsp";

	public static final String PG_LOAD_RIMESSIONE_ATTI = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/LoadInserisciRimessioneAtti.jsp";

	public static final String PG_ELENCOSTAMPEMODELLISENTENZA = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/ElencoStampeModelliSentenza.jsp";

	public static final String PG_LOAD_MODIFICA_MAGISTRATO_SENTENZA = "files/siap/sius/depositosentenza/LoadModificaMagistratoSentenza.jsp";

	public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/DettaglioSentenzaRicevuta.jsp";
	public static final String PG_TOOLBAR_HEADER = IWebConstants.ROOT_DIR
			+ "files/siap/sius/depositosentenza/toolbar_header.jsp";

	public static final String TEMPLATE_MOD_SENTENZA_RIMESSIONE_ATTI = "SIUS_SE_013";
	public static final String TEMPLATE_MOD_SENTENZA_MODGENERICO = "SIUS_SE_014";
	public static final String TEMPLATE_MOD_SENTENZA_RIGETTOGENERICO = "SIUS_SE_015";
	// TODO completare e modificare il nome del template
	public static final String TEMPLATE_MOD_SENTENZA_RIABILITAZIONE = "SIUS_OM_015";

}