package siap.siep.pagoPA.action;

import f3b.web.IWebConstants;

/**
 * MEV_2023-13: aggiunta classe interfaccia per pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public interface ICostantiPagoPA {

	public static final String RADIO_COD_PERSONA = "CodPersona";
	public static final String CAMPO_ID_CIVILMENTE_OBBLIGATO = "IdCivilmenteObbligato";
	public static final String CAMPO_ID_FASCICOLO_SIEP = "IdFascicoloSiep";
	public static final String CAMPO_COGNOME = "Cognome";
	public static final String CAMPO_NOME = "Nome";
	public static final String CAMPO_SESSO = "Sesso";
	public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
	public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
	public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
	public static final String CAMPO_COD_COMUNE_NASCITA = "CodComuneNascita";
	public static final String CAMPO_COD_STATO_NASCITA = "CodStatoNascita";
	public static final String CAMPO_DESC_COMUNE_NASCITA_ESTERO = "DescComuneNascitaEstero";
	public static final String CAMPO_COD_FISCALE = "CodFiscale";
	public static final String CAMPO_ID_RESIDENZA = "IdResidenza";
	public static final String CAMPO_INDIRIZZO = "Indirizzo";
	public static final String CAMPO_COD_COMUNE_RESIDENZA = "CodComuneResidenza";
	public static final String CAMPO_CAP_RESIDENZA = "CapResidenza";
	public static final String CAMPO_DESC_COMUNE_ESTERO_RESIDENZA = "DescComuneEsteroResidenza";
	public static final String CAMPO_COD_STATO_RESIDENZA = "CodStatoResidenza";
	public static final String CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE = "FlagDomicilioPressoDifensore";
	public static final String CAMPO_COD_AVVOCATO = "CodAvvocato";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_SEDE = "Sede";
	public static final String CAMPO_COD_DESTINATARIO = "CodDestinatario";
	public static final String CAMPO_DENOMINAZIONE = "Denominazione";
	public static final String CAMPO_RAG_SOCIALE = "RagSociale";
	public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
	public static final String CAMPO_IND_SEDE_LEGALE = "IndSedeLegale";
	public static final String CAMPO_IND_SEDE_OPERATIVA = "IndSedeOperativa";
	public static final String CAMPO_COD_FISCALE_RAP = "CodFiscaleRap";
	public static final String CAMPO_COD_IST_DETENZIONE = "CodIstDetenzione";
	public static final String CAMPO_COD_LUOGO_DETENZIONE = "CodLuogoDetenzione";
	public static final String CAMPO_INDIRIZZO_DETENZIONE = "IndirizzoDetenzione";
	public static final String CAMPO_ID_AVVOCATO_CIVILMENTE_OBBLIGATO = "IdAvvocatoCivilmenteObbligato";
	public static final String CAMPO_FLAG_SNT = "FlagSNT";
	public static final String CAMPO_PEC = "Pec";
	public static final String CAMPO_EMAIL = "Email";
	public static final String CAMPO_TIPO_TUTORE = "TipoTutore";

    public static final String PAGOPA_STATO_CARRELLO    = "CARRELLO";
    public static final String PAGOPA_STATO_DISPONIBILE = "DISPONIBILE";
    public static final String PAGOPA_STATO_ERRORE      = "ERRORE";
    public static final String PAGOPA_STATO_GENERATA    = "GENERATA";
    
    public static final String SIES_STATO_NON_PAGATO = "PN";
    public static final String SIES_STATO_PAGATO     = "PA";
    public static final String SIES_STATO_PAGATO_PARZIALMENTE = "PP";
    
	public static final String PG_LOAD_GENERA_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/LoadGeneraAvvisoPagoPA.jsp";

	public static final String PG_DOWNLOAD_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DownloadAvvisoPagoPA.jsp";

	public static final String PG_LOAD_INSERISCI_CIVILMENTE_OBBLIGATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/LoadInserisciCivilmenteObbligato.jsp";

	public static final String DIV_PERSONA_FISICA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DivPersonaFisica.jsp";

	public static final String DIV_PERSONA_GIURIDICA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DivPersonaGiuridica.jsp";

	public static final String JS_TIPO_PERSONA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/JsTipoPersona.js";
	
	public static final String PG_DETTAGLIO_CIVILMENTE_OBBLIGATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DettaglioCivilmenteObbligato.jsp";

	public static final String PG_INOLTRA_DOWNLOAD_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/InoltraDownloadAvvisoPagoPA.jsp";

	// MEV_2023-33: aggiunta pagina, costante
	public static final String PG_AVVISO_BATCH_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/AvvisoBatchPagoPA.jsp";
	public static final String RADIO_NUMERO_BOLLETTINI = "NumBollettini";
	// FINE MEV_2023-33
}