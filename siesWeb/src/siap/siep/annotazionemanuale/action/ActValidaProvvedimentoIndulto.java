package siap.siep.annotazionemanuale.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IIndulto;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActValidaProvvedimentoIndulto
 * </p>
 * <p>
 * Description: Valida il provvedimento di tipo Indulto
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActValidaProvvedimentoIndulto extends ActionSiap implements ICostantiAnnotazioneManuale {

	/**
	 * Processa la richiesta di validazione indulto
	 * 
	 * @return
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Fascicolo corrente in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Ricerco l'ultimo (data ins) Provvedimento Indulto
		// n.b. se non si impone la condizione setFlagDocumentoRegistrato recupera
		// l'ultimo in assoluto anche se validato
		// ==========================================================================
		EventoModel lEveDaRicercare = new EventoModel();
		lEveDaRicercare.setCodTipoEvento("01");
		// lEveDaRicercare.setCodMotivo("0284"); // n.b. questa condizione NON viene utilizzata nella ricerca.
		// Il cod motivo va massato come parametro (String[])
		lEveDaRicercare.setCodTipoProvvedimento("04");
		lEveDaRicercare.setFasSieIdFascicoloSiep(lIdFascicolo);

		// lEveDaRicercare.setFlagDocumentoRegistrato(); attenzione

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveOrdinanzaModel = lCtrlEvento
				.ExRicercaEventoPerMotivo(new String[] { "0284", "0285", "0286" }, lEveDaRicercare);
		// lEveOrdinanzaModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		PenaResiduaModel lPenRes = null;
		if (lEveOrdinanzaModel != null) {
			// Cerco la pena residua legata all'evento
			IPenaResidua lPenCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenRes = lPenCtrl.ExRicercaPenaResiduaByIdEvento(lEveOrdinanzaModel.getIdEvento());

			// n.b. dalla 3.1upd02 è possibile procedere alla validazione diretta
			// anche se non è stato effettuato il calcolo della pena in fase di
			// scarico dell'ordinanza. In questo caso verrà duplicata l'ultima
			// pena validata a sistema e agganciata al provvedimento di scarico
			// if(lPenRes==null)
			// throw new F3BException(F3BException.USER_MESSAGE,"Non esiste alcuna pena residua legata
			// all'ordinanza del GE da Validare.");

			if (lPenRes != null && lPenRes.getFlagValidato() != null && lPenRes.getFlagValidato().equals("S"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Pena Residua legata all'ordinanza del GE già validata.");
		} else {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non esiste alcuna ordinanza del GE da Validare.");
		}

		lEveOrdinanzaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveOrdinanzaModel.setCodUfficioAggiornamento(this.getUfficioUtenteConnesso().getCodUfficio());
		lEveOrdinanzaModel.setDataAggiornamento(DateUtils.getSysDate());

		if (lPenRes != null) {
			lPenRes.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lPenRes.setCodUfficioAggiornamento(this.getUfficioUtenteConnesso().getCodUfficio());
			lPenRes.setDataAggiornamento(DateUtils.getSysDate());
		}

		IIndulto lIndulto = SIEPLookupRemote.getIndultoRemote();
		/* EventoModel lEve = */lIndulto.ExValidaProvvedimentoIndulto(lEveOrdinanzaModel, lPenRes);

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {// Deve ritornare al
																					// dettaglio delle stampe
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}