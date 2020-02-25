package siap.regesies.regesentenza.action;

import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.html.Option;
import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.sico.decodifiche.controller.DecodificheManager;

/**
 * <p>
 * Title: ActLoadModificaRegeSentenza
 * </p>
 * <p>
 * Description: Modifica la Rege Sentenza
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActLoadModificaRegeSentenza extends ActionRegeSiap implements ICostantiRegeSentenza {

	public String processRequest() throws F3BException {

		// UtenteModel lUtenteConnesso = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		ProvvedimentoModel lProvvedimento = (ProvvedimentoModel) this
				.getSessionAttribute("provvedimentoRege");
		this.setRequestAttribute("regesentenza", lProvvedimento.getRegeSentenza());

		RegeSentenzaModel lSen = lProvvedimento.getRegeSentenza();

		Option lOption = new Option(DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
		// Imposta la decisione cassazione
		setRequestAttribute("tipoDecisioneCassazione", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");
		setRequestAttribute("tipoProvvedimentiRif", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
				lProvvedimento.getRegeSentenza().getCodTipoAutoritaEmittente());
		// Imposta i provvedimenti Rif.
		setRequestAttribute("autoritaEmi", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaProvRif", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getFlagSN(), "N");
		setRequestAttribute("flagSN", "" + lOption);

		String selected1 = new String("-");
		String selected2 = new String("-");
		if (lSen.getCodTipoAutoritaEmittente().equals("DIB")
				|| lSen.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
			selected1 = StringUtils.toStringJSP(lSen.getCodTipoRito());
		}
		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), selected1);
		setRequestAttribute("tipoRito1", "" + lOption);

		if (lSen.getCodTipoAutoritaProvvRif().equals("DIB")
				|| lSen.getCodTipoAutoritaProvvRif().equals("TRIBSD")) {
			selected2 = StringUtils.toStringJSP(lSen.getCodTipoRito());
		}
		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), selected2);
		setRequestAttribute("tipoRito2", "" + lOption);

		String lPage = "";
		if (lProvvedimento.getRegeSentenza().getCodTipoProvvedimento().equals("01"))
			lPage = PG_LOAD_MODIFICAREGESENTENZA;
		else
			lPage = PG_LOAD_MODIFICAREGEDECRETO;

		return lPage;
	}

}