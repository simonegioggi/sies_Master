package siap.siep.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notefascicolo.controller.NoteFascicoloController;
import siap.siep.notefascicolo.model.NoteFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

@SuppressWarnings("rawtypes")
public class ActLoadInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// paramentro passato solo nel caso di iscrizione guidata
		if (!isRequestParameterNullObj("lTipoFunzione")) {
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));
		}
		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		BigDecimal lKeyFascicolo = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")))
				.getIdFascicoloSiep();
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = null;

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());
		try {
			lVect = new Vector();
			lVect = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFascMod);
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
			setRequestAttribute("tipoAvvocato", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("autoritaEsterna", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
			setRequestAttribute("autoritaEsternaDif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
			setRequestAttribute("motivoDesignazione", "" + lOption);

			setRequestAttribute("modalita", "I");

			// Cerca le note dei difensori provenienti da Rege
			NoteFascicoloController lNotFascCtrl = new NoteFascicoloController();
			NoteFascicoloModel lnotfascicolo = lNotFascCtrl.ExRicercaNoteFascicolo(lKeyFascicolo);

			if (lnotfascicolo != null)
				setRequestAttribute("NoteAvvocati", lnotfascicolo.getNotaAvvocati());

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			// Vector lVect1 = lCtrl.ExRicercaForiCaricati();
			// setRequestAttribute("foro", lVect1);

			UfficioModel lUffUte = getUfficioUtenteConnesso();
			String lDescrComune = lUffUte.getDescrComune();
			setRequestAttribute("comune", lDescrComune);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" load INSERISCI2 AVVOCATO PAOLO");

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			lOption = new Option(DecodificheManager.getInstance().getForo(),
					lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
			setRequestAttribute("foro", "" + lOption);

			// 20210620 MEV_21 Nuova gestione Combo per Stato di Nascita
			lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
			setRequestAttribute("nazione", "" + lOption);

			// 20210626 MEV_21 Nuova gestione Combo per Stato Difensore
			lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
			setRequestAttribute("statoAvv", "" + lOption);

			return PG_ASSEGNA_INSERISCI_DIFENSORE; // restituisce la jsp di VIEW
		}
	}

}