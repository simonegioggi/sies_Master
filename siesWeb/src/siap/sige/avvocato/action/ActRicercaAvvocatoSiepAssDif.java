package siap.sige.avvocato.action;

/**
* <p>Title: ActRicercaAvvocato</p>
* <p>Description: Classe Action per la ricerca di Avvocato</p>
* <p>Created: A.S.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;

public class ActRicercaAvvocatoSiepAssDif extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		// Ricerca Sentenze assegnate al Fascicolo (Ulteriori Titoli Esecutivi)
		BigDecimal lId = lAvvFascMod.getFasSigeIdFascicoloSige();
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector<SentenzaSigeModel> lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(lId);
		setRequestAttribute("sentenze", lSentenze);
		SentenzaSigeModel sentenza = getFascicoloSiep(lSentenze);
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ricercaDifensoreDallaListaSiep(lAvvMod, sentenza);
		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);
		return PG_RICERCAAVVOCATOSIEP;
	}

	private SentenzaSigeModel getFascicoloSiep(Vector<SentenzaSigeModel> sentenze) {

		SentenzaSigeModel retSentenza = new SentenzaSigeModel();
		for (SentenzaSigeModel sentenza : sentenze) {
			// if (sentenza.getFasSieIdFascicoloSiep() != null) {
			// Modifica del 04/11/2015
			// Risolta anomalia segnalata da Nunzia: in presenza di più sentenze
			// bisogna recuperare la sentenza di competenza
			if (sentenza.getFasSieIdFascicoloSiep() != null && sentenza.getFlagCompetenza() != null
					&& sentenza.getFlagCompetenza().equals("S")) {
				retSentenza = sentenza;
				break;
			}
		}
		return retSentenza;
	}

}