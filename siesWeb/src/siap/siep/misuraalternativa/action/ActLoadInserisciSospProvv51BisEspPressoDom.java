package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

/**
 * <p>
 * Title: ActLoadInserisciSospProvv51BisEspPressoDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Sospensione Provvisoria 51 bis Esp Presso Dom
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciSospProvv51BisEspPressoDom extends ActSospensioneProvvisoria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getSospensioni();
		if (!lRitorno.equals(""))
			return lRitorno;

		// setto il campo codice motivo perchè nella maschera nn deve vedere la tendina
		// ma un campo singolo non modificabile
		Collection lmotivo = DecodificheManager.getInstance()
				.getMotivoProvvedimentoProsecProvvMAEspPressoDom();

		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);
		setRequestAttribute("tipoSospensione", ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS);

		return PG_LOAD_INSERISCI_MA_DECRETO_SOSP;
	}
}