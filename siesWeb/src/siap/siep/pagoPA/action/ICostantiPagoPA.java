package siap.siep.pagoPA.action;

import f3b.web.IWebConstants;

/**
 * MEV_2023-13: aggiunta classe interfaccia per pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public interface ICostantiPagoPA {

	public static final String PG_VISUALIZZA_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/VisualizzaAvvisoPagoPA.jsp";

	public static final String PG_LOAD_GENERA_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/LoadGeneraAvvisoPagoPA.jsp";

	public static final String PG_DOWNLOAD_AVVISO_PAGOPA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DownloadAvvisoPagoPA.jsp";

	public static final String PG_LOAD_CIVILMENTE_OBBLIGATO = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/LoadCivilmenteObbligato.jsp";

	public static final String RADIO_COD_PERSONA = "CodPersona";

	public static final String DIV_PERSONA_FISICA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DivPersonaFisica.jsp";

	public static final String DIV_PERSONA_GIURIDICA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/DivPersonaGiuridica.jsp";

	public static final String JS_TIPO_PERSONA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/pagoPA/JsTipoPersona.js";

}