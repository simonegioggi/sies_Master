package siap.sius.statistiche.action;

import java.util.Collection;

import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadRicercaStatisticaComparataMagistrati extends ActionSiap implements ICostantiStatistiche {

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// Lock
		// LockModel lck =
		// LockController.lockIfNotLocked(getServletContext(),"STATISTICHE_OGGETTI","1",getCodUtenteConnesso(),getSession().getId());
		// mod. michele 5/12/2008
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICHE",
				getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti! <BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		Collection<DecodificheModel> lOggettiProcedimento = null;
		Collection<MagistratoModel> lMagistrati = null;

		// Imposta Contenuto.
		Option lOption = new Option();
		/*
		 * if(getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("TDS")) { lOggettiProcedimento
		 * = DecodificheManager.getInstance().getOggettoProcedimentoTDS(); } else if
		 * (getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("UDS")) { lOggettiProcedimento =
		 * DecodificheManager.getInstance().getOggettoProcedimentoUDS(); } else { lOggettiProcedimento =
		 * DecodificheManager.getInstance().getOggettoProcedimento(); } if (lOggettiProcedimento.size() > 1) {
		 * lOggettiProcedimento.remove(lOggettiProcedimento.iterator().next()); }
		 */

		// MEV10-s3: aggiunte or condition per gestire uffici minorenni
		if ("TDS".equals(getUtenteConnesso().getUfficioUtente().getCodTipoUfficio())
				|| "TDSM".equals(getUtenteConnesso().getUfficioUtente().getCodTipoUfficio())) {
			lOggettiProcedimento = // DecodificheManager.getInstance().getMotivoProvvedimento();
					DecodificheUtils.getFilteredByCodAlt(
							DecodificheManager.getInstance().getMotivoProvvedimento(), "^[C]\\d{3}");
		} else if ("UDS".equals(getUtenteConnesso().getUfficioUtente().getCodTipoUfficio())
				|| "UDSM".equals(getUtenteConnesso().getUfficioUtente().getCodTipoUfficio())) {
			lOggettiProcedimento = // DecodificheManager.getInstance().getMotivoProvvedimento();
					DecodificheUtils.getFilteredByCodAlt(
							DecodificheManager.getInstance().getMotivoProvvedimento(), "^[U]\\d{3}");
		}

		// lOption = new Option(lOggettiProcedimento, 100);
		lOption = new Option(lOggettiProcedimento);
		setRequestAttribute("oggettoProcedimento", "" + lOption);

		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();

		lMagistrati = lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio);
		if (lMagistrati.size() > 1) {
			lMagistrati.remove(lMagistrati.iterator().next());
		}
		lOption = new Option(lMagistrati);
		setRequestAttribute("magistrato", "" + lOption);

		return PG_LOAD_RICERCA_STATISTICA_COMPARATA_MAGISTRATI; // restituisce la jsp di VIEW
	}

}