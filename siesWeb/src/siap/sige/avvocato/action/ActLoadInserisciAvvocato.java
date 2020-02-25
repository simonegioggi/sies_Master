package siap.sige.avvocato.action;

/**
* <p>Title: ActLoadInserisciAvvocato</p>
* <p>Description: Classe Action per la load inserisci di Avvocato</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.SIGEException;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Selezionare il procedimento.");

		// Gestione bottone di ritorno
		this.gestioneRitorno();

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					this.getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVect = null;

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());

		// setRequestAttribute("fascicoloSigeEsteso", lAvvFascMod.getFasSigeIdFascicoloSige());
		setRequestAttribute("avvocatoFascicoloSigeModel", lAvvFascMod);

		// Ricerca Sentenze assegnate al Fascicolo (Ulteriori Titoli Esecutivi)
		BigDecimal lId = lAvvFascMod.getFasSigeIdFascicoloSige();
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(lId);
		setRequestAttribute("sentenze", lSentenze);

		try {
			lVect = new Vector();
			lVect = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Elemento trovato");
		}
		if (lVect.size() > 0) {
			setRequestAttribute("modalita", "NoPop");
			setRequestAttribute("avvocato", lVect);

			return PG_ASSEGNA_INSERISCI_AVVOCATO; // restituisce la jsp di VIEW
		} else {
			Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
			String[] lFilter = { "-", "01", "02" };
			lOption.setFilter(lFilter);
			setRequestAttribute("tipoAvvocato", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("autoritaEsterna", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
			setRequestAttribute("autoritaEsternaDif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
			setRequestAttribute("motivoDesignazione", "" + lOption);

			setRequestAttribute("modalita", "I");

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			// Vector lVect1 = lCtrl.ExRicercaForo();
			// this.setRequestAttribute("foro", lVect1);

			UfficioModel lUffUte = this.getUfficioUtenteConnesso();
			String lDescrComune = lUffUte.getDescrComune();
			this.setRequestAttribute("comune", lDescrComune);

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			lOption = new Option(DecodificheManager.getInstance().getForo(),
					lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
			setRequestAttribute("foro", "" + lOption);

			return PG_LOAD_INSERISCIAVVOCATO; // restituisce la jsp di VIEW

		}
	}

}