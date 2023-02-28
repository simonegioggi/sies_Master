package siap.siep.pagoPA.action;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;

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
		String lPage = PG_LOAD_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		gestioneRitorno();

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
		setRequestAttribute("civilmenteObbligato", new CivilmenteObbligatoModel());

		return lPage; // restituisce la jsp di VIEW
	}

}