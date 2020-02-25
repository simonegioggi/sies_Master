package siap.sius.provvedimento.action;

import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActLoadModificaProvvedimento
 * </p>
 * <p>
 * Azione di switch. Richiamata come Modifica del Provvedimento generico, in base al tipo di provvedimento
 * richiama la "Modifica dell'Ordinanza", la "Modifica Decreto" o la "Modifica Sentenza".
 * </p>
 * *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eunics
 * </p>
 * 
 * @version 2.2
 */
public class ActLoadModificaProvvedimento extends ActionSius {

	public String processRequest() throws F3BException {

		String lRetPage = "";

		// lettura dalla request dell' ID Evento ed il codice Tipo Provvedimento
		String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lCodTipoProvvedimento = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO);

		// Costruzione della pagina di redirect
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento);

		// Passaggio dei parametri inerenti il bottone di ritorno
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			lRedirectTo.setParameter(IWebConstants.LINK_RITORNO,
					getRequestStringParameter(IWebConstants.LINK_RITORNO));
		else if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			lRedirectTo.setParameter(IWebConstants.FLAG_RITORNO,
					getRequestStringParameter(IWebConstants.FLAG_RITORNO));

		// Selezione della Action in base al tipo di provvedimento
		if (lCodTipoProvvedimento.equalsIgnoreCase("03")) {
			// Modifica Ordinanza
			lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadModificaOrdinanza");
		} else if (lCodTipoProvvedimento.equalsIgnoreCase("sentenza")
				|| lCodTipoProvvedimento.equalsIgnoreCase("01")) {
			// Modifica Sentenza
			lRedirectTo.setAction("siap.sius.depositosentenza.action.ActLoadModificaSentenza");
		} else {
			// Modifica Decreto
			lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadModificaDecreto");
		}

		lRetPage = lRedirectTo.toString();
		return lRetPage;
	}

}