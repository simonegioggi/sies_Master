package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActLoadModificaCivilmenteObbligato 
 * Description: Classe Action per la insert del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadModificaCivilmenteObbligato extends ActionSiap
		implements ICostantiPagoPA, ICostantiEvento {

	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String retPage = PG_LOAD_INSERISCI_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		gestioneRitorno();

		// Identificativo della parte
		String idFascicolSiep = getRequestStringParameter(CAMPO_ID_FASCICOLO_SIEP);
		setRequestAttribute("idFascicolSiep", "" + idFascicolSiep);

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();

		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(new BigDecimal(idFascicolSiep));

		setRequestAttribute("civilmenteObbligati", coms);
		setRequestAttribute("modalita", "M");

		Option o = new Option(DecodificheManager.getInstance().getSesso());
		setRequestAttribute("sesso", "" + o);

		o = new Option(DecodificheManager.getInstance().getRagioneSociale(), "-");
		setRequestAttribute("ragioneSociale", "" + o);

		o = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		setRequestAttribute("province", "" + o);

		o = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		setRequestAttribute("nazioni", "" + o);

		o = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		setRequestAttribute("nazioniResidenza", "" + o);

		// restituisce la jsp di VIEW
		return retPage;
	}

}