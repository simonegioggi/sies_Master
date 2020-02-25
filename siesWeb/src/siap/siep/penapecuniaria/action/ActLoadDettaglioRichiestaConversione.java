package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaConversione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Richiesta Conversione Pena Pecuniaria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
public class ActLoadDettaglioRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria {

	public String processRequest() throws F3BException {

		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();

		// Paolo Cherubini 22/04/2011 se non esiste l'id della conversione significa che sono su un classe I
		// quindi recupero l'id conversione dal fascicolo di classe VII
		int lChiaveProgr = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getChiaveProgr()
				.intValue();
		// Ticket#20191202018 - Conversione Pena Pecuniaria
		// 20200108 [SG]: modificato 20000 con 30000, cioè anche per la classe II
		if (lChiaveProgr < 30000) { // classe I + classe II
			// BigDecimal lIdFascicoloSiepClasseVII = ( (FascicoloSiepModel) getSessionAttribute("fascicolo")
			// ).getFasSieIdFascicoloSiep();
			// lRicMod.setFasSieIdFascicoloSiep(lIdFascicoloSiepClasseVII);
			// Vector lRichieste =lCtrl.ExRicercaRichiestaConversione(lRicMod );
			// lRicMod = (RichiestaConversioneModel)lRichieste.get(0);
			BigDecimal lidFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep();
			// se sono in classe I vado a cercare la richiesta di conversione legata al procedimento collegato
			// ossia di classe VII
			lRicMod = lCtrl.ExRicercaRichiestaConversioneByIdFascicoloSiepClasseI(lidFascicoloSiep);
		} else {
			if (isRequestParameterNullObj(CAMPO_ID_RICHIESTA_CONVERSIONE)) {
				lRicMod.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
				lRicMod = lCtrl.ExRicercaRichiestaConversioneByIdEvento(lRicMod.getEveIdEvento());
			} else {
				lRicMod.setIdRichiestaConversione(
						getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_CONVERSIONE));
				lRicMod = lCtrl.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
			}
		}

		setRequestAttribute("richiestaconversione", lRicMod);

		return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE;
	}

}