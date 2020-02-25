package siap.siep.reato.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActCancellaPenaReato extends ActionSiap implements ICostantiReato {

	public String processRequest() throws Exception {

		// Si legge il reato in sessione
		ReatoModel lMod = (ReatoModel) getSessionAttribute("reato");

		ReatoModel lReato = new ReatoModel();

		lReato.setCodTipoPenaDetentiva("-");
		lReato.setCodTipoSanzione("-");

		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();

		////// MODIFICATO PER TENERE ALLINEATI I DATI DI TUTTE LE NORME
		// Carico tutte le norme
		lReato.setProgrReato(lMod.getProgrReato());
		lReato.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));

		Vector lNorme = lReaCtrl.ExRicercaReato(lReato);

		/* ReatoModel llReaModRet = */lReaCtrl.ExModificaPenaReato(lReato, lNorme);
		////// FINE

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		return lPage; // restituisce la jsp di VIEW
	}

}