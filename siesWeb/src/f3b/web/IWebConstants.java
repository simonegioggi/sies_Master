package f3b.web;

public interface IWebConstants {

	public static final String ROOT_DIR = "/jsp/";
	public static final String CSS_DIR = "/css/";
	public static final String IMAGES_DIR = "/images/";
	public static final String JS_DIR = "/html/";
	public static final String PG_MAIN = ROOT_DIR + "Main.jsp";
	public static final String PG_STYLE = CSS_DIR + "style.css";
	public static final String PG_MENUSCRIPT = ROOT_DIR + "files/menuscript.js";
	public static final String PG_CHECK = ROOT_DIR + "files/check.js";
	public static final String JS_CONFIRM = JS_DIR + "conferma.js";
	public static final String JS_VALIDATOR = JS_DIR + "gen_validatorv2.js";
	public static final String JS_DATE_CONTROL = JS_DIR + "ControllaData.js";
	public static final String JS_POPCALENDAR = JS_DIR + "popcalendar.js";
	// public static final String JS_JQUERY = JS_DIR + "jquery-1.11.3.min.js";
	// public static final String JS_JQUERY = JS_DIR + "jquery-1.11.3.js"; // for debugging

	public static final String JS_JQUERY = JS_DIR + "jquery-1.6.2.min.js";
	public static final String PG_MESSAGE = ROOT_DIR + "files/message.jsp";
	public static final String PG_MESSAGE_POPUP = ROOT_DIR + "files/message_popup.jsp";
	public static final String PG_MESSAGE_CFC = ROOT_DIR + "files/messageCFC.jsp";
	public static final String PG_MESSAGE_ANNULLA_CFC = ROOT_DIR + "files/messageAnnullaCFC.jsp";

	public static final String JS_FLAYER = JS_DIR + "floatingLayer.js";
	public static final String PG_DOWNLOAD_DOCUMENT = ROOT_DIR + "files/DownloadDocument.jsp";

	// Costanti per modalità (disposition) di download del file.
	public static final String DISPOSITION_FIELD = "disposition";
	public static final String INLINE_DISPOSITION_FILE = "inline";
	public static final String ATTACHMENT_DISPOSITION_FILE = "attachment";

	// GDV 11-11-2004
	// 2010-04-12 ( parte commentata per introduzione TIKA lib )
	// Portata dalla jsp vecchia Download.jsp alla nuova versione Download2.jsp
	// public static final String PG_DOWNLOAD = ROOT_DIR + "files/Download2.jsp";
	// public static final String PG_DOWNLOAD_NEW = ROOT_DIR + "files/Download2.jsp";
	// Per il download di file Excel
	// public static final String PG_DOWNLOAD_XLS = ROOT_DIR + "files/DownloadXLS.jsp";
	// Per il download di file Pdf
	// public static final String PG_DOWNLOAD_PDF = ROOT_DIR + "files/DownloadPdf.jsp";

	// 2010-04-12 ( modificato riferimento JSP di download per introduzione TIKA Lib )
	// In caso di problemi, commentare questa parte e decommentare quella precedente,
	// vedi sopra.
	public static final String PG_DOWNLOAD = PG_DOWNLOAD_DOCUMENT;
	public static final String PG_DOWNLOAD_NEW = PG_DOWNLOAD_DOCUMENT;
	// Per il download di file Excel
	public static final String PG_DOWNLOAD_XLS = PG_DOWNLOAD_DOCUMENT;
	// Per il download di file Pdf
	public static final String PG_DOWNLOAD_PDF = PG_DOWNLOAD_DOCUMENT;

	public static final String PG_MESSAGE_LOGIN = ROOT_DIR + "files/message_login.jsp";
	public static final String PG_ERROR = ROOT_DIR + "ErrorPage.jsp";
	public static final String PG_SEND_TO = ROOT_DIR + "files/RedirectTo.jsp";
	public static final String PG_LOGIN = "/login.jsp";
	public static final String PG_FRAMESET = "/frame.htm";
	public static final String PG_TOOLBAR_HEADER = ROOT_DIR + "files/siap/sico/security/toolbar_header.jsp";

	// Toolbar Header senza il controllo sul Fascicolo SIEP in sessione per le abilitazioni delle funzioni
	public static final String PG_TOOLBAR_HEADER_NOSIEP = ROOT_DIR
			+ "files/siap/sico/security/toolbar_header_nosiep.jsp";
	public static final String PG_TOOLBAR_HEADER_FIX = ROOT_DIR
			+ "files/siap/sico/security/toolbar_header_fix.jsp";

	public static final String PG_TOOLBAR_COMBO = ROOT_DIR + "files/siap/sico/security/toolbar_combo.jsp";
	public static final String PG_COMBO_STAMPA = ROOT_DIR + "files/siap/sico/template/ComboTemplateNew.jsp";
	public static final String PG_COMBO_STAMPA2P = ROOT_DIR
			+ "files/siap/sico/template/ComboTemplateNew2P.jsp";
	public static final String PG_TOOLBAR_BOTTOM = ROOT_DIR + "files/siap/sico/security/toolbar_bottom.jsp";
	public static final String PG_BUTTONS = ROOT_DIR + "files/siap/sico/security/buttons.jsp";
	public static final String PG_BUTTON_INSERT = ROOT_DIR + "files/siap/sico/security/buttonInsert.jsp";
	public static final String PG_BUTTONS_AVVOCATO = ROOT_DIR
			+ "files/siap/sico/security/buttons_avvocato.jsp";
	public static final String PG_BUTTONS_MORE_PARAMETERS = ROOT_DIR
			+ "files/siap/sico/security/buttons_more_parameters.jsp";

	// 17-12-2014
	public static final String PG_BUTTONS_RICERCA_MISURE_SICUREZZA_SIEP = ROOT_DIR
			+ "files/siap/sico/security/ButtonsRicercaMisureSicurezza.jsp";
	public static final String PG_TOOLBAR_HEADER_MISURA_SICUREZZA_SIEP = ROOT_DIR
			+ "files/siap/sico/security/toolbar_header_MisureSicurezza.jsp";
	public static final String PG_CALL_BUTTONS = ROOT_DIR + "files/siap/sico/security/call_buttons.jsp";
	public static final String PG_RETURN_BUTTON = ROOT_DIR + "files/siap/sico/security/return_button.jsp";
	public static final String PG_ORIZONTAL_MENU = ROOT_DIR + "files/orizontal_menu.jsp";
	public static final String PG_WARNING = ROOT_DIR + "files/warning.jsp";
	public static final String PG_WARNING2 = ROOT_DIR + "files/warning2.jsp";

	public static final String PG_VISUALIZZA_CERTIFICATO_PENALE = ROOT_DIR
			+ "files/siap/sico/webservice/VisualizzaCertificatoPenale.jsp";

	public static final String MESSAGE_TEXT = "message_text";
	public static final String GOTO_PAGE = "jump_page";
	public static final String ACTION_FIELD = "Action";
	public static final String NO_PAGE = "NO_PAGE";
	public static final String NUM_PAGE = "pag";
	public static final String REQUEST_FOR_PAGING = "RequestForPaging";
	public static final int RESULT_PER_PAGE = 20;
	public static final int RESULT_PER_PAGE_ESITO = 10;
	public static final String UTIL_DATA = "onFocus=\"javascript:textboxSelect(this)\" onkeypress=\"return TicTabNumField(this,event)\" onBlur=\"javascript:value=FillDM(value)\"";
	public static final String UTIL_DATA_ANNO = "onFocus=\"javascript:textboxSelect(this)\" onkeypress=\"return TicTabNumField(this,event)\" onBlur=\"javascript:value=FillYear(value)\"";

	public static final String PAGINAZIONE_RICERCA = IWebConstants.ROOT_DIR + "files/paginazioneRicerca.jsp";
	public static final String PAGINAZIONE_RICERCA_ESITO = IWebConstants.ROOT_DIR
			+ "files/paginazioneRicercaEsito.jsp";
	public static final String ACT_FIND = "azionericerca";
	public static final String PAGER = "/jsp/files/Pager.jsp";
	public static final String PAGE_LOGOUT = ROOT_DIR + "files/Logout.jsp";
	public static final String PAGE_EXIT_TO_FRAMESET = ROOT_DIR + "files/to_frameset.jsp";
	public static final String PAGE_OPEN_FRAMESET = ROOT_DIR + "files/OpenFrame.jsp";
	public static final String PAGE_CLOSE_FRAMESET = ROOT_DIR + "files/CloseFrame.jsp";

	// Bottone di ritorno
	public static final String LINK_RITORNO = "TornaQui";
	public static final String FLAG_RITORNO = "StoTornando";
	public static final String STACK_RITORNO = "StackDiRitorno";
	public static final String ACTION_DOPO_CANCELLAZIONE = "ActDopoCanc";

	public static final String POPUP_PAGE = "PopUpPage";

	// 20190611 [SG]: aggiunta costante
	public static final String PG_MAIN_ATTESA = ROOT_DIR + "MainAttesa.jsp";

}