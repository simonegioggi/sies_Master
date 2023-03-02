package siap.siep.pagoPA.action;

import java.util.Vector;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActLoadInserisciCivilmenteObbligato 
 * Description: Classe Action per la load del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadInserisciCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String lPage = PG_LOAD_INSERISCI_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		gestioneRitorno();

		// Fascicolo siep in sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String idFascicolSiep = fsm.getIdFascicoloSiep().toString();
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		// Chiama il controller.
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		if (Utils.isPresent(coms)) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction("siap.siep.pagoPA.action.ActDettaglioCivilmenteObbligato");
			rt.setParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO,
					coms.get(0).getIdCivilmenteObbligato().toString());
			rt.setParameter(CAMPO_ID_FASCICOLO_SIEP, fsm.getIdFascicoloSiep().toString());
			return rt.toString();
		} else {
			// aggiungo model vuoto per insert
			coms.add(new CivilmenteObbligatoModel());
		}

		Option lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getRagioneSociale(), "-");
		setRequestAttribute("ragioneSociale", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		setRequestAttribute("province", "" + lOption);

		lOption = new Option(
				DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
				"039");
		setRequestAttribute("nazioni", "" + lOption);
		setRequestAttribute("nazioniResidenza", "" + lOption);

		// Imposta Modalità Inserimento.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("civilmenteObbligati", coms);
		setRequestAttribute("idFascicolSiep", "" + idFascicolSiep);

		// restituisce la jsp di VIEW
		return lPage;
	}

}