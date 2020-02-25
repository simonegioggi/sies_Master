package siap.sius.depositodecreto.action;

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
 * Title: ActRicercaDepositoDecreto
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca dei Decreti Emessi per un pProcedimento SIUS.
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaDepositoDecreto extends ActRicercaFSPuntuale implements ICostantiDepositoDecreto,
		ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lRetPage = PG_LOAD_INSERISCIDATADEPOSITODECRETO;

		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			super.processRequest();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Predisposizione ritorno
		// gestioneRitorno();
		setLinkRitorno();

		// Luigi 3-3-04
		// Per uno stesso Procedimento ora ci possono essere + decreti
		// Si cercano tutti gli Eventi di tipo DECRETO e VALIDATI
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento(COD_EVENTO_PROVVEDIMENTO);
		lEvento.setCodTipoProvvedimento(COD_DECRETO);
		lEvento.setFlagDocumentoRegistrato("S");
		lEvento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		try {
			// Ricerca
			IEvento mCtrl = SICOLookupRemote.getEventoRemote();
			Vector lVect = mCtrl.ExRicercaEvento(lEvento);

			if (lVect.size() > 1) {
				// Più decreti
				setRequestAttribute("decreti", lVect);
				// Predisposizione ritorno
				// setRequestAttribute("TornaQui", getRetRequestURL());
				lRetPage = PG_ELENCO_DEPOSITO_DECRETI;
			} else if (lVect.size() == 1) {
				// Unico decreto
				lEvento = (EventoModel) lVect.get(0);
				// Si passa al deposito del decreto
				RedirectTo lPage = new RedirectTo();
				lPage.setPage(IWebConstants.PG_MAIN);
				lPage.setAction("siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto");
				lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lEvento.getIdEvento().toString());
				lRetPage = lPage.toString();
			} else {
				// Nessun elemento trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Decreto è stato emesso per il procedimento ");
			}
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è stato trovato nessun decreto validato !");
			else
				throw (fex);
		} catch (Exception ex) {
			throw (ex);
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

}