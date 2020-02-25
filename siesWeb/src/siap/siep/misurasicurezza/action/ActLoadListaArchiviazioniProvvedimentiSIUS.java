package siap.siep.misurasicurezza.action;

import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActLoadListaArchiviazioniProvvedimentiSIUS
 * </p>
 * <p>
 * Description: Classe la load della POPUP contenebte la lista dei provvedimenti delle
 * </p>
 * <p>
 * varie tipologie di Archiviazioni della Sorveglianza
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROSINO
 *
 */
public class ActLoadListaArchiviazioniProvvedimentiSIUS extends ActionSiap implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo") || this.isSessionAttributeNullObj("soggetto")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		SoggettoModel lSog = (SoggettoModel) getSessionAttribute("soggetto");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// Dati ORDINANZA SIUS
		Vector DepoOrdinanze;
		IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepoOrdinanze = lDepoCtrl.ExRicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("lSogg", lSog);
		setRequestAttribute("ListaOrd", DepoOrdinanze);

		return PG_LOAD_LISTA_ARCHIVIAZIONE_PROVV_SORVE; // restituisce la jsp di VIEW
	}

}