package siap.sius.depositosentenza.action;

import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaDepositoSentenza
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca delle Sentenze Emesse per un Procedimento SIUS.
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaDepositoSentenza extends ActRicercaFSPuntuale implements ICostantiDepositoSentenza,
		ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lRetPage = null;

		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			super.processRequest();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Predisposizione ritorno
		setLinkRitorno();

		// Per uno stesso Procedimento ora ci possono essere + sentenze
		// Si cercano tutti gli Eventi di tipo SENTENZA e VALIDATI
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento(COD_EVENTO_PROVVEDIMENTO);
		lEvento.setCodTipoProvvedimento(COD_SENTENZA);
		lEvento.setFlagDocumentoRegistrato("S");
		lEvento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		try {
			// Ricerca
			IEvento mCtrl = SICOLookupRemote.getEventoRemote();
			Vector lVect = mCtrl.ExRicercaEvento(lEvento);

			if (lVect.size() > 1) {
				// Più sentenza
				setRequestAttribute("sentenze", lVect);
				lRetPage = PG_ELENCO_DEPOSITO_SENTENZE;
			} else if (lVect.size() == 1) {
				// Unica sentenza
				lEvento = (EventoModel) lVect.get(0);
				// Si passa al deposito della Sentenza
				RedirectTo lPage = new RedirectTo();
				lPage.setPage(IWebConstants.PG_MAIN);
				lPage.setAction("siap.sius.depositosentenza.action.ActLoadInserisciDataDeposito");
				lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lEvento.getIdEvento().toString());
				lRetPage = lPage.toString();
			} else {
				// Nessun elemento trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessuna Sentenza emessa per il procedimento.");
			}
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è stata trovata nessuna Sentenza validata !");
			else
				throw (fex);
		} catch (Exception ex) {
			throw (ex);
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

}