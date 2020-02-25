package siap.sius.depositoordinanzapc.action;

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
 * Title: ActRicercaDepositoOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca delle Ordinanze Emesse per un pProcedimento SIUS.
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaDepositoOrdinanza extends ActRicercaFSPuntuale implements
		ICostantiDepositoOrdinanzaPc, ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lRetPage = null;

		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			super.processRequest();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Predisposizione ritorno
		setLinkRitorno();

		// Luigi 3-3-04
		// Per uno stesso Procedimento ora ci possono essere + ordinanze
		// Si cercano tutti gli Eventi di tipo ORDINANZA e VALIDATI
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento(COD_EVENTO_PROVVEDIMENTO);
		lEvento.setCodTipoProvvedimento(COD_ORDINANZA);
		lEvento.setFlagDocumentoRegistrato("S");
		lEvento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		try {
			// Ricerca
			IEvento mCtrl = SICOLookupRemote.getEventoRemote();
			Vector lVect = mCtrl.ExRicercaEvento(lEvento);

			if (lVect.size() > 1) {
				// Più ordinanze
				setRequestAttribute("ordinanze", lVect);
				lRetPage = PG_ELENCO_DEPOSITO_ORDINANZE;
			} else if (lVect.size() == 1) {
				// Unica ordinanza
				lEvento = (EventoModel) lVect.get(0);
				// Si passa al deposito del decreto
				RedirectTo lPage = new RedirectTo();
				lPage.setPage(IWebConstants.PG_MAIN);
				lPage.setAction("siap.sius.depositoordinanzapc.action.ActLoadInserisciDataDeposito");
				lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lEvento.getIdEvento().toString());
				lRetPage = lPage.toString();
			} else {
				// Nessun elemento trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessuna Ordinanza emessa per il procedimento ");
			}
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è stata trovata nessuna ordinanza validata !");
			else
				throw (fex);
		} catch (Exception ex) {
			throw (ex);
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

}