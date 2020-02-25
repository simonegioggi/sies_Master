package siap.sius.cancassfascsius.action;

import java.math.BigDecimal;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActCancellaCancAssFascSius
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione della assegnazione di un Fascicolo SIUS ad una Cancelleria.
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
public class ActCancellaCancAssFascSius extends ActionSiap implements ICostantiCancAssFascSius {

	public String processRequest() throws Exception {

		// Si ricava il Fascicolo dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Lock per evitare inserimento contemporaneo per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "CancelleriaAssegnataria",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new F3BException(F3BException.USER_MESSAGE, "La definizione della  " + lck.getEntity()
					+ " per il Procedimento è in gestione ad un altro utente!");
		}

		// valorizzazione del model
		CancAssFascSiusModel lCancAssFasc = new CancAssFascSiusModel();
		lCancAssFasc.setFasSiusIdFascicoloSius(lIdFascicolo);
		lCancAssFasc.setDataAggiornamento(DateUtils.getSysDate());
		lCancAssFasc.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lCancAssFasc.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		// chiama il controller per la cancellazione
		ICancAssFascSius lCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		lCtrl.ExCancellaCancAssFascSius(lCancAssFasc);

		// Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.cancassfascsius.action.ActLoadDettaglioCancAssFascSius");
		lPage.setParameter(CAMPO_FAS_SIUS_ID_FASCICOLO_SIUS, lCancAssFasc.getFasSiusIdFascicoloSius()
				.toString());

		return lPage.toString();
	}

}