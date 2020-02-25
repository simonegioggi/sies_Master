package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadElencoCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei Provvedimenti
 * <p>
 * Sige, per cui è possibile compilare il Foglio Complementare
 * <p>
 * Copyright: 2002
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadElencoCompFoglioComp extends ActionSige
		implements ICostantiProvvedimentoSige, ICostantiFoglioComp {

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();
		BigDecimal lIdFascicolo = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		} else {
			FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
		}

		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_DECRETO_GENERICO + "','"
				+ ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA + "'";
		Vector<ProvvedimentoSigeEventoModel> lVect = mCtrl.ExRicercaProvvSigeXCFC(lIdFascicolo, lTipiProvv,
				COD_EVENTO_PROVVEDIMENTO);

		setRequestAttribute("provvedimenti", lVect);

		// Ricerca dell'eventuale prima impugnazione valida per ciascun provvedimento

		IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();

		Vector<ImpugnazioneSigeModel> impugnazioniProvvedimenti = new Vector<>();
		for (ProvvedimentoSigeEventoModel lProveEve : lVect) {
			Vector<ImpugnazioneSigeModel> impugnazioniProvvedimento = ctrIS
					.ExRicercaImpugnazioniProvvedimentoSige(
							lProveEve.getProvvedimento().getIdProvvedimentoSige());
			if (impugnazioniProvvedimento != null && impugnazioniProvvedimento.size() > 0)
				impugnazioniProvvedimenti.addElement(impugnazioniProvvedimento.firstElement());
		}
		setRequestAttribute("impugnazioni", impugnazioniProvvedimenti);

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "SI";
		setRequestAttribute("isModificabile", lModificabile);

		return PG_ELENCOPROVVEDIMENTICFC;
	}

}