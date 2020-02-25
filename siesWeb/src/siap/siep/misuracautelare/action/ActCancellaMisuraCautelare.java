package siap.siep.misuracautelare.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActCancellaMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE);

		MisuraCautelareModel lMod = new MisuraCautelareModel();

		lMod.setIdMisuraCautelare(lId);

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		lCtrl.ExCancellaMisuraCautelare(lMod);
		// Effettuo la cancellazione logica della misura dalla tabella misura_cautelare_bdmc in
		// in modo che la store procedure notifichi a BDMC l'avvenuta cancellazione
		MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
		lModBdmc.setIdMisuraCautelare(lId);
		lModBdmc.setFlagStato("I");
		IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
		if (misCautBdmc != null && misCautBdmc.size() != 0) {
			lModBdmc = new MisuraCautelareBdmcModel();
			lModBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(0);
			lModBdmc.setFlagStato("C");
			if (lModBdmc.getStatoTrasmissioneIsc().compareTo("S") == 0)
				lModBdmc.setStatoTrasmissioneIsc("N");
			else
				lModBdmc.setStatoTrasmissioneIsc("S");
			lCtrlBdmc.ExModificaMisuraCautelareBdmc(lModBdmc, null);
		}
		/*
		 * RedirectTo lRedirigi = new RedirectTo();
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente");
		 * lRedirigi.setAction("siap.siep.misuracautelare.action.ActRicercaMisuraCautelare");
		 * setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
		 * 
		 * return IWebConstants.PG_MESSAGE;
		 */

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuracautelare.action.ActRicercaMisuraCautelare&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		return lPage; // restituisce la jsp di VIEW
	}

}